package it.unibo.mirth.java;

//import java.io.IOException;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpException;
//import org.apache.commons.httpclient.HttpMethodBase;
//import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
//import org.hl7.fhir.r4.model.Bundle;
//import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IRead;
import it.unibo.fhir.Patients;


/*
 * Il server e' relativo al canale FHIRListener
 * http://localhost:7002/r4/ 
 * http://localhost:7002/r4/metadata 
 * http://hapi.fhir.org/baseR4/Patient?ID=216
 * 
 * // https://mvnrepository.com/artifact/ca.uhn.hapi.fhir/hapi-fhir-base
compile group: 'ca.uhn.hapi.fhir', name: 'hapi-fhir-base', version: '5.0.2'
 */
public class MirthJavaHapi {
 
//private HttpClient client   = new HttpClient();			//NO!
//FhirContext is an expensive (thread-safe) object
private FhirContext ctx       = FhirContext.forR4();
private String serverBase     = "http://localhost:7002/r4/Patient?ID=216";  
private Patients mypatients   = new Patients();
private IGenericClient client = ctx.newRestfulGenericClient(serverBase);

//From https://hapifhir.io/hapi-fhir/docs/client/generic_client.html  
	public void createPatient() {
		try {
			Patient patient = mypatients.createPatient_1(ctx);

			MethodOutcome outcome = client.create()
			   .resource(patient)
			   .prettyPrint()
			   .encodedJson()
			   .execute();
	
			IIdType id = outcome.getId();
			System.out.println("Got ID: " + id.getValue());
		  }catch( Exception e) {
			  System.out.println("createPatient ERROR:" + e.getMessage());
		  }
	}  
	
//http://hapi.fhir.org/baseR4/Patient?ID=216	
	public void readData() {
	    // Create a method instance.
	  String query = "";//"/?server=db&username=root&db=mirthdb&select=PERSON";
	    GetMethod method  = new GetMethod( serverBase + query);
	    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
	    		new DefaultHttpMethodRetryHandler(3, false));	     
	    System.out.println("get path :"+ method.getPath() );
	    System.out.println("get query:"+ method.getQueryString());	 
	    
//	    IRead v = client.read();
//	    System.out.println("readData v=" + v.resource( org.hl7.fhir.r4.model.Patient.class   ) ); // 

		  Bundle results = client
				   .search()
				   .forResource(Patient.class)
				   .where(Patient.FAMILY.matches().value("Unibo"))
				   .returnBundle(Bundle.class)
				   .execute();	
		  System.out.println("Found " + results.getEntry().size() + " patients named Unibo "  );
	    	    
	}
	 	
	public static void main(String[] args) {
		new MirthJavaHapi( ).readData();
	}
}
