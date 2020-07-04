package ca.uhn.hl7v2.examples;
/*
 * USING VERSION v23
 */
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v23.datatype.XPN;
import ca.uhn.hl7v2.model.v23.datatype.*;
import ca.uhn.hl7v2.model.v23.segment.*;
import ca.uhn.hl7v2.model.v23.message.*;
import ca.uhn.hl7v2.util.Hl7InputStreamMessageIterator;
import ca.uhn.hl7v2.util.Hl7InputStreamMessageStringIterator;

public class ReadMessagesFromFile {

	public static void exploreMsg(Message m) throws HL7Exception {
		String mtype = m.printStructure().substring(0, 7);
		System.out.println("exploreMsg mtype="+mtype  + " " + ( mtype.equals("ADT_A01") ));
		if( mtype.equals("ADT_A01") ) exploreA01Msg( (ADT_A01)m );
//		switch(mtype) {
//			case "ADT_A01" : exploreA01Msg( (ADT_A01)m );
//			case "ADT_A02" : exploreMsg( (ADT_A02)m );
//		}
	}
	
	public static void exploreA01Msg(ADT_A01 m) {
//        System.out.println( "exploreA01Msg " + m);
        MSH msh = m.getMSH();
        System.out.println( "msh= " + msh);
        // Retrieve some data from the MSH segment
       String msgType    = msh.getMessageType().getMessageType().getValue();
       String msgTrigger = msh.getMessageType().getTriggerEvent().getValue();

       // Prints "ADT A01"
       System.out.println("msgType=" + msgType + " trigger=" + msgTrigger);

       /* 
           * PN is an HL7 data type consisting of several components, such as 
          * family name, given name, etc. 
           */
        XPN[] patientName = m.getPID().getPatientName();
//
//       // Prints "SMITH"
        String familyName = patientName[0].getFamilyName().getValue();
        System.out.println(familyName);
		
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws FileNotFoundException, HL7Exception {

		/*
		 * This example shows how to read a series of messages from a file
		 * containing a number of HL7 messages. Assume you have a text file
		 * containing a number of messages.
		 */
		
		// Open an InputStream to read from the file
		File file = new File("hl7_messages23.txt");
		InputStream is = new FileInputStream(file);
		
		// It's generally a good idea to buffer file IO
		is = new BufferedInputStream(is);
		
		// The following class is a HAPI utility that will iterate over
		// the messages which appear over an InputStream
		Hl7InputStreamMessageIterator iter = new Hl7InputStreamMessageIterator(is);
		
		while (iter.hasNext()) {
			
			Message next = iter.next();
			
			// Do something with the message
			 
			System.out.println("TYPE="+next.printStructure().substring(0, 8) );
			System.out.println("VERSION="+ next.getVersion() );
			exploreMsg(next);
		}
		System.out.println("----------------------------------- " );
		/*
		 * If you don't want the message parsed, you can also just
		 * read them in as strings.
		 */
		
		file = new File("hl7_messages23.txt");
		is = new FileInputStream(file);
		is = new BufferedInputStream(is);
		Hl7InputStreamMessageStringIterator iter2 = new Hl7InputStreamMessageStringIterator(is); 
		
		while (iter2.hasNext()) {
			
			String next = iter2.next();
			
			// Do something with the message
			System.out.println(""+next);  
			
			
		}
		
	}

}
