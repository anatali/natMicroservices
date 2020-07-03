package ca.uhn.hl7v2.examples;
import java.util.Map;
  
  import ca.uhn.hl7v2.DefaultHapiContext;
  import ca.uhn.hl7v2.HL7Exception;
  import ca.uhn.hl7v2.HapiContext;
  import ca.uhn.hl7v2.app.*;
  import ca.uhn.hl7v2.model.Message;
  import ca.uhn.hl7v2.parser.Parser;
  import ca.uhn.hl7v2.protocol.ReceivingApplication;
  import ca.uhn.hl7v2.protocol.ReceivingApplicationExceptionHandler;

  public class SendAndReceiveAMessage {
    /**
51      * Example for how to send messages out
52      */
     public static void main(String[] args) throws Exception {
  
        /*
56         * Before we can send, let's create a server to listen for incoming
57         * messages. The following section of code establishes a server listening
58         * on port 1011 for new connections.
59         */
        int port = 1011; // The port to listen on
        boolean useTls = false; // Should we use TLS/SSL?
        HapiContext context = new DefaultHapiContext();
        HL7Service server = context.newServer(port, useTls);
  
        /*
66         * The server may have any number of "application" objects registered to
67         * handle messages. We are going to create an application to listen to
68         * ADT^A01 messages.
69         * 
70         * You might want to look at the source of ExampleReceiverApplication
71         * (it's a nested class below) to see how it works.
72         */
//        ReceivingApplication<Message> handler = new ExampleReceiverApplication();
        ReceivingApplication  handler = new ExampleReceiverApplication();
        server.registerApplication("ADT", "A01", handler);
  
        /*
77         * We are going to register the same application to handle ADT^A02
78         * messages. Of course, we coud just as easily have specified a different
79         * handler.
80         */
        server.registerApplication("ADT", "A02", handler);
  
        /*
84         * Another option would be to specify a single application to handle all
85         * messages, like this:
86         * 
87         * server.registerApplication("*", "*", handler);
88         */
  
        /*
91         * If you want to be notified any time a new connection comes in or is
92         * lost, you might also want to register a connection listener (see the
93         * bottom of this class to see what the listener looks like). It's fine
94         * to skip this step.
95         */
        server.registerConnectionListener(new MyConnectionListener());
  
        /*
99         * If you want to be notified any processing failures when receiving,
100        * processing, or responding to messages with the server, you can 
101        * also register an exception handler. (See the bottom of this class
102        * to see what the listener looks like. ) It's also fine to skip this
103        * step, in which case exceptions will simply be logged. 
104        */
       server.setExceptionHandler(new MyExceptionHandler());
 
       // Start the server listening for messages
       server.startAndWait();
 
       /*
111        * Note: if you don't want to wait for the server to initialize itself, it
112        * can start in the background:
113        */
 
       // server.start();
 
       /*
118        * All of the code above created a listening server, which waits for
119        * connections to come in and then handles any messages that arrive on
120        * those connections.
121        * 
122        * Now, the code below creates a client, which will connect to our waiting
123        * server and send messages to it.
124        */
 
       // Create a message to send
       String msg = "MSH|^~\\&|HIS|RIH|EKG|EKG|199904140038||ADT^A01|12345|P|2.2\r"
             + "PID|0001|00009874|00001122|A00977|SMITH^JOHN^M|MOM|19581119|F|NOTREAL^LINDA^M|C|564 SPRING ST^^NEEDHAM^MA^02494^US|0002|(818)565-1551|(425)828-3344|E|S|C|0000444444|252-00-4414||||SA|||SA||||NONE|V1|0001|I|D.ER^50A^M110^01|ER|P00055|11B^M011^02|070615^BATMAN^GEORGE^L|555888^NOTREAL^BOB^K^DR^MD|777889^NOTREAL^SAM^T^DR^MD^PHD|ER|D.WT^1A^M010^01|||ER|AMB|02|070615^NOTREAL^BILL^L|ER|000001916994|D||||||||||||||||GDD|WA|NORM|02|O|02|E.IN^02D^M090^01|E.IN^01D^M080^01|199904072124|199904101200|199904101200||||5555112333|||666097^NOTREAL^MANNY^P\r"
             + "NK1|0222555|NOTREAL^JAMES^R|FA|STREET^OTHER STREET^CITY^ST^55566|(222)111-3333|(888)999-0000|||||||ORGANIZATION\r"
             + "PV1|0001|I|D.ER^1F^M950^01|ER|P000998|11B^M011^02|070615^BATMAN^GEORGE^L|555888^OKNEL^BOB^K^DR^MD|777889^NOTREAL^SAM^T^DR^MD^PHD|ER|D.WT^1A^M010^01|||ER|AMB|02|070615^VOICE^BILL^L|ER|000001916994|D||||||||||||||||GDD|WA|NORM|02|O|02|E.IN^02D^M090^01|E.IN^01D^M080^01|199904072124|199904101200|||||5555112333|||666097^DNOTREAL^MANNY^P\r"
             + "PV2|||0112^TESTING|55555^PATIENT IS NORMAL|NONE|||19990225|19990226|1|1|TESTING|555888^NOTREAL^BOB^K^DR^MD||||||||||PROD^003^099|02|ER||NONE|19990225|19990223|19990316|NONE\r"
             + "AL1||SEV|001^POLLEN\r"
             + "GT1||0222PL|NOTREAL^BOB^B||STREET^OTHER STREET^CITY^ST^77787|(444)999-3333|(222)777-5555||||MO|111-33-5555||||NOTREAL GILL N|STREET^OTHER STREET^CITY^ST^99999|(111)222-3333\r"
             + "IN1||022254P|4558PD|BLUE CROSS|STREET^OTHER STREET^CITY^ST^00990||(333)333-6666||221K|LENIX|||19980515|19990515|||PATIENT01 TEST D||||||||||||||||||02LL|022LP554";
       Parser p = context.getPipeParser();
       Message adt = p.parse(msg);
 
       // Remember, we created our HAPI Context above like so:
       // HapiContext context = new DefaultHapiContext();
 
       // A connection object represents a socket attached to an HL7 server
       Connection connection = context.newClient("localhost", port, useTls);
 
       // The initiator is used to transmit unsolicited messages
       Initiator initiator = connection.getInitiator();
       Message response = initiator.sendAndReceive(adt);
 
       String responseString = p.encode(response);
       System.out.println("Received response:\n" + responseString);
 
       /*
152        * MSH|^~\&|||||20070218200627.515-0500||ACK|54|P|2.2 MSA|AA|12345
153        */
 
       /*
156        * If you want to send another message to the same destination, it's fine
157        * to ask the context again for a client to attach to the same host/port.
158        * The context will be smart about it and return the same (already
159        * connected) client Connection instance, assuming it hasn't been closed.
160        */
       connection = context.newClient("localhost", port, useTls);
       initiator = connection.getInitiator();
       response = initiator.sendAndReceive(adt);
 
       /*
166        * Close the connection when you are done with it. If you are designing a
167        * system which will continuously send out messages, you may want to
168        * consider not closing the connection until you have no more messages to
169        * send out. This is more efficient, as most (if not all) HL7 receiving
170        * applications are capable of receiving lots of messages in a row over
171        * the same connection, even with a long delay between messages.
172        * 
173        * See
174        * http://hl7api.sourceforge.net/xref/ca/uhn/hl7v2/examples/SendLotsOfMessages.html 
175        * for an example of this.
176        */
       connection.close();
 
       // Stop the receiving server and client
       server.stopAndWait();
 
    }
 
    /**
185     * Connection listener which is notified whenever a new
186     * connection comes in or is lost
187     */
    public static class MyConnectionListener implements ConnectionListener {
 
       public void connectionReceived(Connection theC) {
          System.out.println("New connection received: " + theC.getRemoteAddress().toString());
       }
 
       public void connectionDiscarded(Connection theC) {
          System.out.println("Lost connection from: " + theC.getRemoteAddress().toString());
       }
 
    }
 
    /**
     * Exception handler which is notified any time
     */
    public static class MyExceptionHandler implements ReceivingApplicationExceptionHandler {
 
       /**
206        * Process an exception.
207        * 
208        * @param theIncomingMessage
209        *            the incoming message. This is the raw message which was
210        *            received from the external system
211        * @param theIncomingMetadata
212        *            Any metadata that accompanies the incoming message. See {@link ca.uhn.hl7v2.protocol.Transportable#getMetadata()}
213        * @param theOutgoingMessage
214        *            the outgoing message. The response NAK message generated by
215        *            HAPI.
216        * @param theE
217        *            the exception which was received
218        * @return The new outgoing message. This can be set to the value provided
219        *         by HAPI in <code>outgoingMessage</code>, or may be replaced with
220        *         another message. <b>This method may not return <code>null</code></b>.
221        */
       public String processException(String theIncomingMessage, Map<String, Object> theIncomingMetadata, String theOutgoingMessage, Exception theE) throws HL7Exception {
           
          /*
225           * Here you can do any processing you like. If you want to change
226           * the response (NAK) message which will be returned you may do
227           * so, or just return the NAK which HAPI already created (theOutgoingMessage) 
228           */
          
          return theOutgoingMessage;
       }
 
    }
}
