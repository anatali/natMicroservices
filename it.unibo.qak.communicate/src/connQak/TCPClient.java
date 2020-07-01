package connQak;

import java.io.*;
import java.net.*;

class TCPClient {
 public static void main(String argv[]) throws Exception {
	 try {
  String sentence;
   String modifiedSentence;
   BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
  //Socket clientSocket = new Socket("localhost", 6789);
  System.out.println("TO SERVER: " );
  Socket clientSocket = new Socket("localhost", 7001); 
  DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
   BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//   sentence = inFromUser.readLine();
//   outToServer.writeBytes(sentence + 'n');
  sentence = "hello from program";
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
