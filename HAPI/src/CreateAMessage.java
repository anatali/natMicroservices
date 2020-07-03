View Javadoc

1   /**
2    * The contents of this file are subject to the Mozilla Public License Version 1.1
3    * (the "License"); you may not use this file except in compliance with the License.
4    * You may obtain a copy of the License at http://www.mozilla.org/MPL/
5    * Software distributed under the License is distributed on an "AS IS" basis,
6    * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for the
7    * specific language governing rights and limitations under the License.
8    *
9    * The Original Code is "CreateAMessage.java".  Description:
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
27  package ca.uhn.hl7v2.examples;
28
29  import ca.uhn.hl7v2.DefaultHapiContext;
30  import ca.uhn.hl7v2.HL7Exception;
31  import ca.uhn.hl7v2.HapiContext;
32  import ca.uhn.hl7v2.model.v24.message.ADT_A01;
33  import ca.uhn.hl7v2.model.v24.segment.MSH;
34  import ca.uhn.hl7v2.model.v24.segment.PID;
35  import ca.uhn.hl7v2.parser.Parser;
36
37  /**
38   * Example transmitting a message
39   *
40   * @author <a href="mailto:jamesagnew@sourceforge.net">James Agnew</a>
41   * @version $Revision: 1.4 $ updated on $Date: 2009-10-03 15:29:05 $ by $Author: jamesagnew $
42   */
43  public class CreateAMessage
44  {
45
46      /**
47       * @param args
48       * @throws HL7Exception
49       */
50      public static void main(String[] args) throws Exception {
51
52          ADT_A01 adt = new ADT_A01();
53          adt.initQuickstart("ADT", "A01", "P");
54
55          // Populate the MSH Segment
56          MSH mshSegment = adt.getMSH();
57          mshSegment.getSendingApplication().getNamespaceID().setValue("TestSendingSystem");
58          mshSegment.getSequenceNumber().setValue("123");
59
60          // Populate the PID Segment
61          PID pid = adt.getPID();
62          pid.getPatientName(0).getFamilyName().getSurname().setValue("Doe");
63          pid.getPatientName(0).getGivenName().setValue("John");
64          pid.getPatientIdentifierList(0).getID().setValue("123456");
65
66          /*
67           * In a real situation, of course, many more segments and fields would be populated
68           */
69
70          // Now, let's encode the message and look at the output
71          HapiContext context = new DefaultHapiContext();
72          Parser parser = context.getPipeParser();
73          String encodedMessage = parser.encode(adt);
74          System.out.println("Printing ER7 Encoded Message:");
75          System.out.println(encodedMessage);
76
77          /*
78           * Prints:
79           *
80           * MSH|^~\&|TestSendingSystem||||200701011539||ADT^A01^ADT A01||||123
81           * PID|||123456||Doe^John
82           */
83
84          // Next, let's use the XML parser to encode as XML
85          parser = context.getXMLParser();
86          encodedMessage = parser.encode(adt);
87          System.out.println("Printing XML Encoded Message:");
88          System.out.println(encodedMessage);
89
90          /*
91           * Prints:
92           *
93           * <?xml version="1.0" encoding="UTF-8"?>
94  			<ADT_A01 xmlns="urn:hl7-org:v2xml">
95  			    <MSH>
96  			        <MSH.1>|</MSH.1>
97  			        <MSH.2>^~\&amp;</MSH.2>
98  			        <MSH.3>
99  			            <HD.1>TestSendingSystem</HD.1>
100 			        </MSH.3>
101 			        <MSH.7>
102 			            <TS.1>200701011539</TS.1>
103 			        </MSH.7>
104 			        <MSH.9>
105 			            <MSG.1>ADT</MSG.1>
106 			            <MSG.2>A01</MSG.2>
107 			            <MSG.3>ADT A01</MSG.3>
108 			        </MSH.9>
109 			        <MSH.13>123</MSH.13>
110 			    </MSH>
111 			    <PID>
112 			        <PID.3>
113 			            <CX.1>123456</CX.1>
114 			        </PID.3>
115 			        <PID.5>
116 			            <XPN.1>
117 			                <FN.1>Doe</FN.1>
118 			            </XPN.1>
119 			            <XPN.2>John</XPN.2>
120 			        </PID.5>
121 			    </PID>
122 			</ADT_A01>
123          */
124
125     }
126
127 }
