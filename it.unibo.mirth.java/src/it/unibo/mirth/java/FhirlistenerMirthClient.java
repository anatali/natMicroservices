package it.unibo.mirth.java;

 
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import it.unibo.fhir.Patients;


public class FhirlistenerMirthClient {
   //FhirContext is an expensive (thread-safe) object
   private FhirContext ctx     = FhirContext.forR4();
   private Patients mypatients = new Patients();
   private String serverBase   = "http://localhost:7002/r4";  
    
  //From https://hapifhir.io/hapi-fhir/docs/client/generic_client.html  
  public void createPatient() {
	try {
		Patient patient = mypatients.createPatient_1(ctx);
 		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		// Invoke the server create method (and send pretty-printed JSON
		// encoding to the server
		// instead of the default which is non-pretty printed XML)
		MethodOutcome outcome = client.create()
		   .resource(patient)
		   .prettyPrint()
		   .encodedJson()
		   .execute();
 
		// The MethodOutcome object will contain information about the
		// response from the server, including the ID of the created
		// resource, the OperationOutcome response, etc. (assuming that
		// any of these things were provided by the server! They may not
		// always be)
		IIdType id = outcome.getId();
		System.out.println("Got ID: " + id.getValue());
	  }catch( Exception e) {
		  System.out.println("createPatient ERROR:" + e.getMessage());
	  }
  }  
  
 	  
  public static void main(String[] args) throws Exception {
	  FhirlistenerMirthClient appl = new FhirlistenerMirthClient();
//  	  appl.readData();
 	  appl.createPatient();
   }
}