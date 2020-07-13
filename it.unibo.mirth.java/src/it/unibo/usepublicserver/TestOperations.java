package it.unibo.usepublicserver;
//Taken from https://github.com/FirelyTeam/fhirstarters/tree/master/java/hapi-fhirstarters-client-skeleton/ 

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.instance.model.api.IIdType;

public class TestOperations {
private String serverBase = "https://hapi.fhir.org/baseR4"; //"http://localhost:9002/r4"; //
private FhirContext ctx = FhirContext.forR4();

	public void read_a_resource() {
 		IGenericClient client = ctx.newRestfulGenericClient(serverBase);

		Patient patient;
		try { 
			// Try changing the ID from 952975 to 999999999999
//			patient = client.read().resource(Patient.class).withId("952975").execute();
			patient = client.read().resource(Patient.class).withId("123456789").execute();
		} catch ( Exception e) {	//ResourceNotFoundException
			System.out.println("Resource not found!");
			return;
		}

		String string = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		System.out.println(string);

	}

	public void search_for_patients_named_test() {
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);

		org.hl7.fhir.r4.model.Bundle results = client
			.search()
			.forResource(Patient.class)
			.where(Patient.NAME.matches().value("test")) //
			.returnBundle(org.hl7.fhir.r4.model.Bundle.class)
			.execute();

		System.out.println("First page: ");
		System.out.println(ctx.newXmlParser().encodeResourceToString(results));

		// Load the next page
		org.hl7.fhir.r4.model.Bundle nextPage = client
			.loadPage()
			.next(results)
			.execute();

		System.out.println("Next page: ");
		System.out.println(ctx.newXmlParser().encodeResourceToString(nextPage));

	}

public void create_patient() {
	try {
	// Create a patient
	Patient newPatient = new Patient();

	// Populate the patient with fake information
	newPatient
		.addName()
			.setFamily("DevDays2015")
			.addGiven("JohnBologna")
			.addGiven("Q");
	newPatient
		.addIdentifier()
			.setSystem("http://acme.org/mrn")
			.setValue("123456789");
	newPatient.setGender(Enumerations.AdministrativeGender.MALE);
	newPatient.setBirthDateElement(new DateType("2015-11-18"));

	// Create a client
 	IGenericClient client = ctx.newRestfulGenericClient(serverBase);

	// Create the resource on the server
	MethodOutcome outcome = client
		.create()
		.resource(newPatient)
		.execute();

	// Log the ID that the server assigned
	IIdType id = outcome.getId();
	System.out.println("Created patient, got ID: " + id);
} catch ( Exception e) {	//ResourceNotFoundException
	System.out.println("create_patient ERROR " + e.getMessage() );
}
}

	public static void main(String[] args) {
		TestOperations appl = new TestOperations();
//       		appl.read_a_resource();						//(1)
  		appl.search_for_patients_named_test();		//(2)
//		step2_search_for_patients_named_test();
//   		appl.create_patient();		//(4)
	}


	
	
}
