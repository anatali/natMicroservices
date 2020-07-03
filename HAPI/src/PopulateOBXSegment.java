/**
2    * The contents of this file are subject to the Mozilla Public License Version 1.1
3    * (the "License"); you may not use this file except in compliance with the License.
4    * You may obtain a copy of the License at http://www.mozilla.org/MPL/
5    * Software distributed under the License is distributed on an "AS IS" basis,
6    * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for the
7    * specific language governing rights and limitations under the License.
8    *
9    * The Original Code is "PopulateOBXSegment.java".  Description:
10   * "Example Code"
11   *
12   * The Initial Developer of the Original Code is University Health Network. Copyright (C)
13   * 2001.  All Rights Reserved.
14   *
15   * Contributor(s): James Agnew
16   *
17   * Alternatively, the contents of this file may be used under the terms of the
18   * GNU General Public License (the  ?GPL?), in which case the provisions of the GPL are
19   * applicable instead of those above.  If you wish to allow use of your version of this
20   * file only under the terms of the GPL and not to allow others to use your version
21   * of this file under the MPL, indicate your decision by deleting  the provisions above
22   * and replace  them with the notice and other provisions required by the GPL License.
23   * If you do not delete the provisions above, a recipient may use your version of
24   * this file under either the MPL or the GPL.
25   *
26   */
27
28  package ca.uhn.hl7v2.examples;
29
30  import java.io.IOException;
31
32  import ca.uhn.hl7v2.HL7Exception;
33  import ca.uhn.hl7v2.model.Varies;
34  import ca.uhn.hl7v2.model.v25.datatype.CE;
35  import ca.uhn.hl7v2.model.v25.datatype.ST;
36  import ca.uhn.hl7v2.model.v25.datatype.TX;
37  import ca.uhn.hl7v2.model.v25.group.ORU_R01_OBSERVATION;
38  import ca.uhn.hl7v2.model.v25.group.ORU_R01_ORDER_OBSERVATION;
39  import ca.uhn.hl7v2.model.v25.message.ORU_R01;
40  import ca.uhn.hl7v2.model.v25.segment.OBR;
41  import ca.uhn.hl7v2.model.v25.segment.OBX;
42
43  /**
44   * Example code for populating an OBX segment
45   *
46   * @author <a href="mailto:jamesagnew@sourceforge.net">James Agnew</a>
47   * @version $Revision: 1.1 $ updated on $Date: 2009-03-19 13:09:26 $ by $Author: jamesagnew $
48   */
49  public class PopulateOBXSegment
50  {
51
52      /**
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
108     public static void main(String[] args) throws HL7Exception, IOException {
109
110         // First, a message object is constructed
111         ORU_R01 message = new ORU_R01();
112
113         /*
114          * The initQuickstart method populates all of the mandatory fields in the
115          * MSH segment of the message, including the message type, the timestamp,
116          * and the control ID.
117          */
118         message.initQuickstart("ORU", "R01", "T");
119
120         /*
121          * The OBR segment is contained within a group called ORDER_OBSERVATION,
122          * which is itself in a group called PATIENT_RESULT. These groups are
123          * reached using named accessors.
124          */
125         ORU_R01_ORDER_OBSERVATION orderObservation = message.getPATIENT_RESULT().getORDER_OBSERVATION();
126
127         // Populate the OBR
128         OBR obr = orderObservation.getOBR();
129         obr.getSetIDOBR().setValue("1");
130         obr.getFillerOrderNumber().getEntityIdentifier().setValue("1234");
131         obr.getFillerOrderNumber().getNamespaceID().setValue("LAB");
132         obr.getUniversalServiceIdentifier().getIdentifier().setValue("88304");
133
134         /*
135          * The OBX segment is in a repeating group called OBSERVATION. You can
136          * use a named accessor which takes an index to access a specific
137          * repetition. You can ask for an index which is equal to the
138          * current number of repetitions,and a new repetition will be created.
139          */
140         ORU_R01_OBSERVATION observation = orderObservation.getOBSERVATION(0);
141
142         // Populate the first OBX
143 		OBX obx = observation.getOBX();
144         obx.getSetIDOBX().setValue("1");
145         obx.getObservationIdentifier().getIdentifier().setValue("88304");
146         obx.getObservationSubID().setValue("1");
147
148         // The first OBX has a value type of CE. So first, we populate OBX-2 with "CE"...
149         obx.getValueType().setValue("CE");
150
151         // ... then we create a CE instance to put in OBX-5.
152         CE ce = new CE(message);
153         ce.getIdentifier().setValue("T57000");
154         ce.getText().setValue("GALLBLADDER");
155         ce.getNameOfCodingSystem().setValue("SNM");
156         Varies value = obx.getObservationValue(0);
157         value.setData(ce);
158
159         // Now we populate the second OBX
160         obx = orderObservation.getOBSERVATION(1).getOBX();
161         obx.getSetIDOBX().setValue("2");
162         obx.getObservationSubID().setValue("1");
163
164         // The second OBX in the sample message has an extra subcomponent at
165         // OBX-3-1. This component is actually an ST, but the HL7 specification allows
166         // extra subcomponents to be tacked on to the end of a component. This is
167         // uncommon, but HAPI nontheless allows it.
168         ST observationIdentifier = obx.getObservationIdentifier().getIdentifier();
169         observationIdentifier.setValue("88304");
170         ST extraSubcomponent = new ST(message);
171         extraSubcomponent.setValue("MDT");
172         observationIdentifier.getExtraComponents().getComponent(0).setData(extraSubcomponent );
173
174         // The first OBX has a value type of TX. So first, we populate OBX-2 with "TX"...
175         obx.getValueType().setValue("TX");
176
177         // ... then we create a CE instance to put in OBX-5.
178         TX tx = new TX(message);
179         tx.setValue("MICROSCOPIC EXAM SHOWS HISTOLOGICALLY NORMAL GALLBLADDER TISSUE");
180         value = obx.getObservationValue(0);
181         value.setData(tx);
182
183         // Print the message (remember, the MSH segment was not fully or correctly populated)
184         System.out.println(message.encode());
185
186         /*
187          * MSH|^~\&|||||20111102082111.435-0500||ORU^R01^ORU_R01|305|T|2.5
188          * OBR|1||1234^LAB|88304
189          * OBX|1|CE|88304|1|T57000^GALLBLADDER^SNM
190          * OBX|2|TX|88304&MDT|1|MICROSCOPIC EXAM SHOWS HISTOLOGICALLY NORMAL GALLBLADDER TISSUE
191          */
192     }
193
194 }