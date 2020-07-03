package ca.uhn.hl7v2.examples;
import java.io.IOException;

import ca.uhn.hl7v2.HL7Exception;
  import ca.uhn.hl7v2.model.Varies;
  import ca.uhn.hl7v2.model.v25.datatype.CE;
  import ca.uhn.hl7v2.model.v25.datatype.ST;
  import ca.uhn.hl7v2.model.v25.datatype.TX;
  import ca.uhn.hl7v2.model.v25.group.ORU_R01_OBSERVATION;
  import ca.uhn.hl7v2.model.v25.group.ORU_R01_ORDER_OBSERVATION;
  import ca.uhn.hl7v2.model.v25.message.ORU_R01;
  import ca.uhn.hl7v2.model.v25.segment.OBR;
  import ca.uhn.hl7v2.model.v25.segment.OBX;
public class TheHapiContext {
    /**
53       * We are going to create an ORU_R01 message, for the purpose of demonstrating the creation and
54       * population of an OBX segment.
55       *
56       * The following message snippet is drawn (and modified for simplicity)
57       * from section 7.4.2.4 of the HL7 2.5 specification.
58       *
59       * <code>
60       * OBR|1||1234^LAB|88304
61       * OBX|1|CE|88304|1|T57000^GALLBLADDER^SNM
62       * OBX|2|TX|88304|1|THIS IS A NORMAL GALLBLADDER
63       * OBX|3|TX|88304&MDT|1|MICROSCOPIC EXAM SHOWS HISTOLOGICALLY NORMAL GALLBLADDER TISSUE
64       * </code>
65       *
66       * The following code attempts to generate this message structure.
67       *
68       * The HL7 spec defines, the following structure for an ORU^R01 message, represented in HAPI by
69       * the segment group:
70       *
71       * <code>
72       *                     ORDER_OBSERVATION start
73       *       {
74       *       [ ORC ]
75       *       OBR
76       *       [ { NTE } ]
77       *                     TIMING_QTY start
78       *          [{
79       *          TQ1
80       *          [ { TQ2 } ]
81       *          }]
82       *                     TIMING_QTY end
83       *       [ CTD ]
84       *                     OBSERVATION start
85       *          [{
86       *          OBX
87       *          [ { NTE } ]
88       *          }]
89       *                     OBSERVATION end
90       *       [ { FT1 } ]
91       *       [ { CTI } ]
92       *                     SPECIMEN start
93       *          [{
94       *          SPM
95       *          [ { OBX } ]
96       *          }]
97       *                     SPECIMEN end
98       *       }
99       *                     ORDER_OBSERVATION end
100      * </code>
101      *
102      * @param args
103      *            The arguments
104      * @throws HL7Exception
105      *             If any processing problem occurs
106      * @throws IOException
107      */
      public static void main(String[] args) throws HL7Exception, IOException {
 
          // First, a message object is constructed
          ORU_R01 message = new ORU_R01();
 
          /*
114          * The initQuickstart method populates all of the mandatory fields in the
115          * MSH segment of the message, including the message type, the timestamp,
116          * and the control ID.
117          */
          message.initQuickstart("ORU", "R01", "T");
 
          /*
121          * The OBR segment is contained within a group called ORDER_OBSERVATION,
122          * which is itself in a group called PATIENT_RESULT. These groups are
123          * reached using named accessors.
124          */
          ORU_R01_ORDER_OBSERVATION orderObservation = message.getPATIENT_RESULT().getORDER_OBSERVATION();
 
          // Populate the OBR
          OBR obr = orderObservation.getOBR();
          obr.getSetIDOBR().setValue("1");
          obr.getFillerOrderNumber().getEntityIdentifier().setValue("1234");
          obr.getFillerOrderNumber().getNamespaceID().setValue("LAB");
          obr.getUniversalServiceIdentifier().getIdentifier().setValue("88304");
 
          /*
135          * The OBX segment is in a repeating group called OBSERVATION. You can
136          * use a named accessor which takes an index to access a specific
137          * repetition. You can ask for an index which is equal to the
138          * current number of repetitions,and a new repetition will be created.
139          */
          ORU_R01_OBSERVATION observation = orderObservation.getOBSERVATION(0);
 
          // Populate the first OBX
  		OBX obx = observation.getOBX();
          obx.getSetIDOBX().setValue("1");
          obx.getObservationIdentifier().getIdentifier().setValue("88304");
          obx.getObservationSubID().setValue("1");
 
          // The first OBX has a value type of CE. So first, we populate OBX-2 with "CE"...
          obx.getValueType().setValue("CE");
 
          // ... then we create a CE instance to put in OBX-5.
          CE ce = new CE(message);
          ce.getIdentifier().setValue("T57000");
          ce.getText().setValue("GALLBLADDER");
          ce.getNameOfCodingSystem().setValue("SNM");
          Varies value = obx.getObservationValue(0);
          value.setData(ce);
 
          // Now we populate the second OBX
          obx = orderObservation.getOBSERVATION(1).getOBX();
          obx.getSetIDOBX().setValue("2");
          obx.getObservationSubID().setValue("1");
 
          // The second OBX in the sample message has an extra subcomponent at
          // OBX-3-1. This component is actually an ST, but the HL7 specification allows
          // extra subcomponents to be tacked on to the end of a component. This is
          // uncommon, but HAPI nontheless allows it.
          ST observationIdentifier = obx.getObservationIdentifier().getIdentifier();
          observationIdentifier.setValue("88304");
          ST extraSubcomponent = new ST(message);
          extraSubcomponent.setValue("MDT");
          observationIdentifier.getExtraComponents().getComponent(0).setData(extraSubcomponent );
 
          // The first OBX has a value type of TX. So first, we populate OBX-2 with "TX"...
          obx.getValueType().setValue("TX");
 
          // ... then we create a CE instance to put in OBX-5.
          TX tx = new TX(message);
          tx.setValue("MICROSCOPIC EXAM SHOWS HISTOLOGICALLY NORMAL GALLBLADDER TISSUE");
          value = obx.getObservationValue(0);
          value.setData(tx);
 
          // Print the message (remember, the MSH segment was not fully or correctly populated)
          System.out.println(message.encode());
 
          /*
187          * MSH|^~\&|||||20111102082111.435-0500||ORU^R01^ORU_R01|305|T|2.5
188          * OBR|1||1234^LAB|88304
189          * OBX|1|CE|88304|1|T57000^GALLBLADDER^SNM
190          * OBX|2|TX|88304&MDT|1|MICROSCOPIC EXAM SHOWS HISTOLOGICALLY NORMAL GALLBLADDER TISSUE
191          */
      }

}
