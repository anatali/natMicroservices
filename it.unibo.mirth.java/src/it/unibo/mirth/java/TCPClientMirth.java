package it.unibo.mirth.java;

import java.io.*;
import java.net.*;


class TCPClientMirth {
	private Socket clientSocket = null ; 
//	private String sentence ="testMsg";	

	String sentence ="MSH|^~\\&|NES|NINTENDO|TESTSYSTEM|TESTFACILITY|20010101000000||ADT^A04|Q123456789T123456789X123456|P|2.3\r\n" + 
	"EVN|A04|20010101000000|||^KOOPA^BOWSER\r\n" + 
	"PID|1||123456789|0123456789^AA^^JP|BROS^MARIO||19850101000000|M|||123 FAKE STREET^MARIO \\T\\ LUIGI BROS PLACE^TOADSTOOL KINGDOM^NES^A1B2C3^JP^HOME^^1234|1234|(555)555-0123^HOME^JP:1234567|||S|MSH|12345678|||||||0|||||N\r\n" + 
	"NK1|1|PEACH^PRINCESS|SO|ANOTHER CASTLE^^TOADSTOOL KINGDOM^NES^^JP|(123)555-1234|(123)555-2345|NOK\r\n" + 
	"NK1|2|TOADSTOOL^PRINCESS|SO|YET ANOTHER CASTLE^^TOADSTOOL KINGDOM^NES^^JP|(123)555-3456|(123)555-4567|EMC\r\n" + 
	"PV1|1|O|ABCD^EFGH||||123456^DINO^YOSHI^^^^^^MSRM^CURRENT^^^NEIGHBOURHOOD DR NBR|^DOG^DUCKHUNT^^^^^^^CURRENT||CRD|||||||123456^DINO^YOSHI^^^^^^MSRM^CURRENT^^^NEIGHBOURHOOD DR NBR|AO|0123456789|1|||||||||||||||||||MSH||A|||20010101000000\r\n" + 
	"IN1|1|PAR^PARENT||||LUIGI\r\n" + 
	"IN1|2|FRI^FRIEND||||PRINCESS";
	
	String sentence1 = "MSH|^~\\&|ADT1|SHM|SHMADT|SHM|200812091126|SECURITY|ADT^A01^ADT_A01|MSG00001|P|2.5|\r\n" + 
			"EVN|A01|200812091126||\r\n" + 
			"PID|1|1001|1001^5^M11^ADT1^MR^SHM~123456789^^^USSSA^SS||OHALLAHAN^COLLEENBOLOGNA^^||19850704|F||2106-3|1200 N ELM STREET^^NEWPORT BEACH^CA^92660-1020^US^H|OC|(949) 555-1234|(949) 555-5678||S||PATID1001^2^M10^ADT1^AN^A|123456789|U1234567^CA|\r\n" + 
			"NK1|1|O'HALLAHAN^BRITTANY^M|SIS^SISTER||||N^NEXT-OF-KIN\r\n" + 
			"PV1|1|I|2000^2012^01||||001122^ZOIDBERG^JOHN^|||SUR||||1|A0|"; 
	
	public void sendAMessage() {
	try {
	   System.out.println("sendAMessage: " + sentence1 );
	   clientSocket = new Socket("localhost", 7001); 
    //OUTPUT  
	  DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
 	  outToServer.writeBytes(sentence1 + '\n');  // + "\\n" 
	  outToServer.flush();
// 	  
	}catch(Exception e) {
			 System.out.println("ERROR: " + e.getMessage() );
	  }		
	}
	
	public void receiveAMessage() {
		  new Thread() {
			  public void run() {
				    try {
				    	BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				    	System.out.println("inFromServer ready: .... " + inFromServer.ready() );
		  			    String modifiedSentence = ""+inFromServer.read(); //.readLine();
					    System.out.println("FROM SERVER: " + modifiedSentence);
					} catch (Exception e) {
						System.out.println("THREAD ERROR: " + e.getMessage() );
					}  		  
			  }
		  }.start();		
	}
	
	public void closeConnection() {
		try {
			if( clientSocket != null ) clientSocket.close();
		} catch (IOException e) {
			System.out.println("ERROR closeConnection: " + e.getMessage() );
 		}
	}
 public static void main(String argv[]) {
	 TCPClientMirth appl = new TCPClientMirth();
	 appl.sendAMessage();
 	 appl.receiveAMessage();
// 	 appl.closeConnection();
 }//main

}
