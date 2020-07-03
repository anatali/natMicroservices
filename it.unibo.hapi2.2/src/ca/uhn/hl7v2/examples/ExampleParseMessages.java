package ca.uhn.hl7v2.examples;
  import ca.uhn.hl7v2.DefaultHapiContext;
  import ca.uhn.hl7v2.HL7Exception;
  import ca.uhn.hl7v2.HapiContext;
  import ca.uhn.hl7v2.model.Message;
  import ca.uhn.hl7v2.model.v22.datatype.PN;
  import ca.uhn.hl7v2.model.v22.message.ADT_A01;
  import ca.uhn.hl7v2.model.v22.segment.MSH;
  import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
  import ca.uhn.hl7v2.parser.Parser;

  public class ExampleParseMessages {
    /**
49       * A simple example of parsing a message
50       */
      public static void main(String[] args) {
          String msg = "MSH|^~\\&|HIS|RIH|EKG|EKG|199904140038||ADT^A01||P|2.2\r"
                  + "PID|0001|00009874|00001122|A00977|SMITH^JOHN^M|MOM|19581119|F|NOTREAL^LINDA^M|C|564 SPRING ST^^NEEDHAM^MA^02494^US|0002|(818)565-1551|(425)828-3344|E|S|C|0000444444|252-00-4414||||SA|||SA||||NONE|V1|0001|I|D.ER^50A^M110^01|ER|P00055|11B^M011^02|070615^BATMAN^GEORGE^L|555888^NOTREAL^BOB^K^DR^MD|777889^NOTREAL^SAM^T^DR^MD^PHD|ER|D.WT^1A^M010^01|||ER|AMB|02|070615^NOTREAL^BILL^L|ER|000001916994|D||||||||||||||||GDD|WA|NORM|02|O|02|E.IN^02D^M090^01|E.IN^01D^M080^01|199904072124|199904101200|199904101200||||5555112333|||666097^NOTREAL^MANNY^P\r"
                  + "NK1|0222555|NOTREAL^JAMES^R|FA|STREET^OTHER STREET^CITY^ST^55566|(222)111-3333|(888)999-0000|||||||ORGANIZATION\r"
                  + "PV1|0001|I|D.ER^1F^M950^01|ER|P000998|11B^M011^02|070615^BATMAN^GEORGE^L|555888^OKNEL^BOB^K^DR^MD|777889^NOTREAL^SAM^T^DR^MD^PHD|ER|D.WT^1A^M010^01|||ER|AMB|02|070615^VOICE^BILL^L|ER|000001916994|D||||||||||||||||GDD|WA|NORM|02|O|02|E.IN^02D^M090^01|E.IN^01D^M080^01|199904072124|199904101200|||||5555112333|||666097^DNOTREAL^MANNY^P\r"
                  + "PV2|||0112^TESTING|55555^PATIENT IS NORMAL|NONE|||19990225|19990226|1|1|TESTING|555888^NOTREAL^BOB^K^DR^MD||||||||||PROD^003^099|02|ER||NONE|19990225|19990223|19990316|NONE\r"
                  + "AL1||SEV|001^POLLEN\r"
                  + "GT1||0222PL|NOTREAL^BOB^B||STREET^OTHER STREET^CITY^ST^77787|(444)999-3333|(222)777-5555||||MO|111-33-5555||||NOTREAL GILL N|STREET^OTHER STREET^CITY^ST^99999|(111)222-3333\r"
                  + "IN1||022254P|4558PD|BLUE CROSS|STREET^OTHER STREET^CITY^ST^00990||(333)333-6666||221K|LENIX|||19980515|19990515|||PATIENT01 TEST D||||||||||||||||||02LL|022LP554";
  
          /*
62           * The HapiContext holds all configuration and provides factory methods for obtaining
63           * all sorts of HAPI objects, e.g. parsers. 
64           */
          HapiContext context = new DefaultHapiContext();
          
           /*
68           * A Parser is used to convert between string representations of messages and instances of
69           * HAPI's "Message" object. In this case, we are using a "GenericParser", which is able to
70           * handle both XML and ER7 (pipe & hat) encodings.
71           */
          Parser p = context.getGenericParser();
  
          Message hapiMsg;
          try {
              // The parse method performs the actual parsing
              hapiMsg = p.parse(msg);
          } catch (EncodingNotSupportedException e) {
              e.printStackTrace();
              return;
          } catch (HL7Exception e) {
              e.printStackTrace();
              return;
          }
  
          /*
87           * This message was an ADT^A01 is an HL7 data type consisting of several components1, so we
88           * will cast it as such. The ADT_A01 class extends from Message, providing specialized
89           * accessors for ADT^A01's segments.
90           * 
91           * HAPI provides several versions of the ADT_A01 class, each in a different package (note
92           * the import statement above) corresponding to the HL7 version for the message.
93           */
          ADT_A01 adtMsg = (ADT_A01)hapiMsg;
  
          MSH msh = adtMsg.getMSH();
  
          // Retrieve some data from the MSH segment
          String msgType = msh.getMessageType().getMessageType().getValue();
         String msgTrigger = msh.getMessageType().getTriggerEvent().getValue();
 
         // Prints "ADT A01"
         System.out.println(msgType + " " + msgTrigger);
 
         /* 
106          * Now let's retrieve the patient's name from the parsed message. 
107          * 
108          * PN is an HL7 data type consisting of several components, such as 
109          * family name, given name, etc. 
110          */
         PN patientName = adtMsg.getPID().getPatientName();
 
         // Prints "SMITH"
         String familyName = patientName.getFamilyName().getValue();
         System.out.println(familyName);
      }
}
