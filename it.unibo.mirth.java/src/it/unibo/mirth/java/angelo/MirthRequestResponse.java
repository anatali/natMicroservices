package it.unibo.mirth.java.angelo;

import java.util.concurrent.TimeUnit;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.GenericModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;
import ca.uhn.hl7v2.parser.PipeParser;

public class MirthRequestResponse {

	private static final String END_LINE = "\r\n";
	private static final int HL7_CONNECTION_TIMEOUT = 5000;
	
	public void sendMessage() throws Exception {
		String hl7Msg = generateHl7Message();
		
		final DefaultHapiContext context = new DefaultHapiContext();

		final Connection client = context.newClient("localhost", 4321, false);//..getInitiator();
		
		client.getInitiator().setTimeout(HL7_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
		
		final ModelClassFactory modelClassFactory = new GenericModelClassFactory();
		final PipeParser p = new PipeParser(modelClassFactory);
		
		log("Sending HL7 MSG...");
		log(hl7Msg);
		
		final Message ack = client.getInitiator().sendAndReceive(p.parse(hl7Msg));
		
		log("Received ACK:");
		log(p.encode(ack));
		
		context.close();
	}
		
	public static void main(String argv[]) throws Exception {
		final MirthRequestResponse appl = new MirthRequestResponse();
		appl.sendMessage();
	}
	
	private void log(final String msg) {
		System.out.println(msg);
	}

	private String generateHl7Message() {
		return "MSH|^~\\&|Infinity||Applicazione||20190220103054||ORF^R04|19022010305401095972|P|2.3|||AL|NE" + END_LINE
				+ "MSA|AA|10901|[TraumaT,SHOCKR]Bed found" + END_LINE 
				+ "QRD|20190220102909.93+0100|R|I|6646910203467890|||||RES" + END_LINE
				+ "QRF|MONITOR||||TraumaT^^SHOCKR" + END_LINE
				+ "PID|||1234||Mario Rossi|||||||||||||000000" + END_LINE
				+ "PV1|||TraumaT^^SHOCKR" + END_LINE
				+ "OBR|||||||20190220102707" + END_LINE
				+ "OBX||SN|HR^^local^8867-4&1^^LOINC||=^65|/min^^ISO+|||||R|||20190220102707" + END_LINE
				+ "OBX||ST|SpO2^^local^2710-2&1^^LOINC||***|%^^ISO+|||||R|||20190220102707" + END_LINE
				+ "OBX||SN|NBP D^^local^8496-2&1^^LOINC||=^84|mm(hg)^^ISO+|||||R|||20190220102255" + END_LINE
				+ "OBX||SN|NBP S^^local^8508-4&1^^LOINC||=^136|mm(hg)^^ISO+|||||R|||20190220102255" + END_LINE
				+ "OBX||SN|etCO2^^local^60808-3&1^^LOINC||=^2|mm(hg)^^ISO+|||||R|||20190220102707" + END_LINE
				+ "OBX||SN|iCO2^^local^60924-8&1^^LOINC||=^2|mm(hg)^^ISO+|||||R|||20190220102707" + END_LINE 
				+ "";
	}
}

