package connQak;

import java.io.*;
import java.net.*;

class TCPClient {
 public static void main(String argv[]) throws Exception {
	 
String sentence ="MSH|^~\\&|NES|NINTENDO|TESTSYSTEM|TESTFACILITY|20010101000000||ADT^A04|Q123456789T123456789X123456|P|2.3\r\n" + 
		"EVN|A04|20010101000000|||^KOOPA^BOWSER\r\n" + 
		"PID|1||123456789|0123456789^AA^^JP|BROS^MARIO||19850101000000|M|||123 FAKE STREET^MARIO \\T\\ LUIGI BROS PLACE^TOADSTOOL KINGDOM^NES^A1B2C3^JP^HOME^^1234|1234|(555)555-0123^HOME^JP:1234567|||S|MSH|12345678|||||||0|||||N\r\n" + 
		"NK1|1|PEACH^PRINCESS|SO|ANOTHER CASTLE^^TOADSTOOL KINGDOM^NES^^JP|(123)555-1234|(123)555-2345|NOK\r\n" + 
		"NK1|2|TOADSTOOL^PRINCESS|SO|YET ANOTHER CASTLE^^TOADSTOOL KINGDOM^NES^^JP|(123)555-3456|(123)555-4567|EMC\r\n" + 
		"PV1|1|O|ABCD^EFGH||||123456^DINO^YOSHI^^^^^^MSRM^CURRENT^^^NEIGHBOURHOOD DR NBR|^DOG^DUCKHUNT^^^^^^^CURRENT||CRD|||||||123456^DINO^YOSHI^^^^^^MSRM^CURRENT^^^NEIGHBOURHOOD DR NBR|AO|0123456789|1|||||||||||||||||||MSH||A|||20010101000000\r\n" + 
		"IN1|1|PAR^PARENT||||LUIGI\r\n" + 
		"IN1|2|FRI^FRIEND||||PRINCESS";
	 try {
   
//   String modifiedSentence;
//   BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
  //Socket clientSocket = new Socket("localhost", 6789);
  System.out.println("TO SERVER: " );
  Socket clientSocket = new Socket("localhost", 7001); 
  DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
//   BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//   sentence = inFromUser.readLine();
//   outToServer.writeBytes(sentence + 'n');
//  sentence = "hello again from program";
  outToServer.writeBytes(sentence + 'n');
  System.out.println("TO SERVER: " + sentence );
//   modifiedSentence = inFromServer.readLine();
//   System.out.println("FROM SERVER: " + modifiedSentence);
  clientSocket.close();
	 }catch(Exception e) {
		 System.out.println("FROM SERVER: " + e.getMessage() );
	 }
 }
}
