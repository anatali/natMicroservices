package it.unibo.mirth.java.angelo;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v26.group.ORF_R04_OBSERVATION;
import ca.uhn.hl7v2.model.v26.message.ORF_R04;
import ca.uhn.hl7v2.model.v26.segment.MSA;
import ca.uhn.hl7v2.model.v26.segment.MSH;
import ca.uhn.hl7v2.model.v26.segment.OBX;
import ca.uhn.hl7v2.model.v26.segment.PID;
import ca.uhn.hl7v2.model.v26.segment.QRF;
import it.unibo.mirth.java.angelo.MyVitalSignsMessage.VitalSign;

public class HL7Utils {

	public static MyVitalSignsMessage processORF_R04(final String msgContent) throws HL7Exception {				
		final MyVitalSignsMessage.Builder msgBuilder = new MyVitalSignsMessage.Builder();
		
		final ORF_R04 orf_r04 = new ORF_R04();
		orf_r04.parse(msgContent);
		
		//MSH - MSA - QRF
		
		final MSH mshSegment = orf_r04.getMSH();
		final MSA msaSegment = orf_r04.getMSA();
		final QRF qrfSegment = orf_r04.getQRF();
		
		msgBuilder.timestamp(mshSegment.getDateTimeOfMessage().getValue());
		msgBuilder.message(msaSegment.getTextMessage().getValue());		
		msgBuilder.deviceId(qrfSegment.getOtherQRYSubjectFilter(0).getValue());
		
		//PID
		
		final PID pidSegment = orf_r04.getQUERY_RESPONSE(0).getPATIENT().getPID();
		
		msgBuilder.patientId(pidSegment.getPatientIdentifierList()[0].getIDNumber().getValue());
		msgBuilder.patientName(pidSegment.getPatientName()[0].getFamilyName().getSurname().getValue());
		
		//OBX[]
		
		for(final ORF_R04_OBSERVATION obs : orf_r04.getQUERY_RESPONSE(0).getORDER().getOBSERVATIONAll()) {
			final OBX obxSegment = obs.getOBX();
			
			msgBuilder.addObservation(new VitalSign(
					obxSegment.getObservationIdentifier().getIdentifier().getValue(), 
					obxSegment.getObservationValue()[0].getData().toString().replaceAll("[^\\.0123456789]", ""),
					obxSegment.getUnits().getIdentifier().getValue(),
					obxSegment.getDateTimeOfTheObservation().getValue()));
		}
		
		return msgBuilder.build();
	}
}

