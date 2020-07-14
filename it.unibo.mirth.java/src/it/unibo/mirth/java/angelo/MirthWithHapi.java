package it.unibo.mirth.java.angelo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import ca.uhn.hl7v2.HL7Exception;

public class MirthWithHapi {
	
	private static final String END_LINE = "\r\n";

	public void sendMessage() {
		String hl7Msg = generateHl7Message();

		try {
			Socket clientSocket = new Socket("localhost", 7001);

			DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
			outputStream.writeBytes(hl7Msg);
			outputStream.flush();
			
			log("---> HL7 Message Sent!");
			
			clientSocket.close();
			
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
	}
	
	public void onMessageReceived(final String msgContent) {
		log("---> HL7 Message Received!");
		
		try {
			final MyVitalSignsMessage msg = HL7Utils.processORF_R04(msgContent);
			log("---> My interpretation of the message: \n" + msg.toJsonRep());
		} catch (HL7Exception e) {
			e.printStackTrace();
		}
	}

	public void startListening() {
		new Thread() {
			public void run() {
				try {
					ServerSocket serverSocket = new ServerSocket(7002);
					Socket connectionSocket = serverSocket.accept();
					
					BufferedReader inputStream = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
					
					final StringBuilder builder = new StringBuilder();
					String line;
					
					while ((line = inputStream.readLine()) != null) {
					    builder.append(line + END_LINE);
					}
					
					onMessageReceived(builder.toString());
					
					connectionSocket.close();
					serverSocket.close();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public static void main(String argv[]) {
		final MirthWithHapi appl = new MirthWithHapi();
		appl.startListening();
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
