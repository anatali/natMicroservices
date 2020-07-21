package it.unibo.fhir;

import java.io.IOException;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import ca.uhn.fhir.context.FhirContext;

public class Patients {

	
	  //From https://fhir-drills.github.io/fhir-api.html
	  public Patient createPatient_1(FhirContext ctx) throws IOException {
	      Patient patient = new Patient();

	      // you can use the Fluent API to chain calls
	      // see http://hapifhir.io/doc_fhirobjects.html
	      patient.addName().setUse(HumanName.NameUse.OFFICIAL ) //  .NICKNAME
	              .addPrefix("Mr").setFamily("Unibo").addGiven("BobBologna");
	      patient.addIdentifier()
	              .setSystem("http://ns.electronichealth.net.au/id/hi/ihi/1.0")
	              .setValue("9003608177690505");
	       
	       
	      // create a new XML parser and serialize our Patient object with it
	      String encoded = ctx.newXmlParser().setPrettyPrint(true)
	              .encodeResourceToString(patient);

	      System.out.println(encoded);
 	      return patient;
	 	  
	  }

	  
}
