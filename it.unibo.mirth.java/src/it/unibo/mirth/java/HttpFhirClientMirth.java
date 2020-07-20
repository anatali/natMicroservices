package it.unibo.mirth.java;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
//import ca.uhn.fhir.model.dstu2.resource.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.BundleUtil;
import it.unibo.fhir.Patients;


public class HttpFhirClientMirth {
//  private static String url = "http://localhost:9001/r4";
  private HttpClient client = new HttpClient();
  //FhirContext is an expensive (thread-safe) object
   private FhirContext ctx = FhirContext.forR4();
//   private IParser parser = ctx.newXmlParser();
   
   private Patients mypatients = new Patients();
   
  
//  private FhirContext ctx = FhirContext.forDstu2();
  private String serverBase = "http://localhost:7002/r4"; //"http://fhirtest.uhn.ca/baseDstu2";

  //From https://hapifhir.io/hapi-fhir/docs/client/generic_client.html  
  
//GET http://hapi.fhir.org/baseR4/Patient?address-city=bologna&_include=*&_pretty=true
  public void search(String serverBase, String familyName) {
	  
	  
//	  String serverBase = "http://fhirtest.uhn.ca/baseDstu2";
	  IGenericClient client = ctx.newRestfulGenericClient(serverBase);
	  System.out.println("client "+ client);
	  Bundle results = client
			   .search()
			   .forResource(Patient.class)
			   .where(Patient.FAMILY.matches().value(familyName))
			   .returnBundle(Bundle.class)
			   .execute();	
	  System.out.println("Found " + results.getEntry().size() + " patients named " + familyName);
  }
  public void readData() {
	    // Create a method instance.
	  String query = "";//"/?server=db&username=root&db=mirthdb&select=PERSON";
	    GetMethod method  = new GetMethod( serverBase + query);
	    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
	    		new DefaultHttpMethodRetryHandler(3, false));	     
	    System.out.println("get path :"+ method.getPath() );
	    System.out.println("get query:"+ method.getQueryString());	    
	    //methodp.getParams().	    
	    executeMethod(method);
  }
  
  //From https://hapifhir.io/hapi-fhir/docs/model/parsers.html
  //See https://hapifhir.io/hapi-fhir/apidocs/hapi-fhir-structures-r4/org/hl7/fhir/r4/model/Patient.html
  //https://www.hl7.org/fhir/patient.html  https://www.hl7.org/fhir/patient-examples.html FHIR Specification
  public void createPatientNat() throws IOException {
/*	  
	  String input = "{" +
			   "\"resourceType\" : \"Patient\"," +
			   "  \"name\" : [{" +
			   "    \"family\": \"Simpson\"" +
			   "  }]" +
			   "}";	  
	  
	// Instantiate a new parser
	  ca.uhn.fhir.parser.IParser parser = ctx.newJsonParser();

	  // Parse it
	  Patient parsed = parser.parseResource(Patient.class, input);
	  System.out.println(parsed.getName().get(0).getFamily());	  
*/	  
	  
	  Patient patient = mypatients.createPatient_1(ctx);
      System.out.println("Press Enter to serialise Resource to the console as XML.");
      System.in.read();

      // create a new XML parser and serialize our Patient object with it
      String encoded = ctx.newXmlParser().setPrettyPrint(true)
              .encodeResourceToString(patient);

      System.out.println(encoded);

//      System.out.println("Press Enter to end.");
//      System.in.read();
	  
  }
  
  //From https://hapifhir.io/hapi-fhir/docs/client/generic_client.html  
  public void createPatient() {
	try {
		/*
		  Patient patient = new Patient();
		// ..populate the patient object..
		patient.addIdentifier().setSystem("urn:system").setValue("12345");
 		patient.addName().setFamily("Smith").addGiven("JohnBolognaR4");			//R4
//		patient.addName().addFamily("Smith").addGiven("JohnBologna");	//DSTU2
		
 		System.out.println("createPatient: " + patient.getText());
 		*/
		Patient patient = mypatients.createPatient_1(ctx);
//		String serverBase = "http://fhirtest.uhn.ca/baseDstu2";
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		// Invoke the server create method (and send pretty-printed JSON
		// encoding to the server
		// instead of the default which is non-pretty printed XML)
		MethodOutcome outcome = client.create()
		   .resource(patient)
		   .prettyPrint()
		   .encodedJson()
		   .execute();
//var params = [type, id, versionId, lastUpdated, data, contentType, method, url];			//postgres
//var params = [sequenceId, type, id, versionId, lastUpdated, data, contentType, method, url]; //sqlserver

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
  
  /* From https://stackoverflow.com/questions/55267648/iterating-and-extracting-data-from-xml-in-javascript-on-mirth
     var myQuery = 'INSERT INTO adt.diagnosis (AcctNum, MRN, ICD10) VALUES (?, ?, ?)';

    for each (var dg1 in xml.descendants('DG1')) {
            dbConn.executeUpdate(myQuery, new java.util.ArrayList([$('AcctNum'), $('MedRecNum'), dg1['DG1.3']['DG1.3.1'].toString()]));
    }
   */
  
  public void executeMethod(HttpMethodBase method) {
	    try {
		      // Execute the method.
		      int statusCode = client.executeMethod(method);

		      if (statusCode != HttpStatus.SC_OK) {
		        System.err.println("Method failed: " + method.getStatusLine());
		      }

		      // Read the response body.
		      byte[] responseBody = method.getResponseBody();

		      // Deal with the response.
		      // Use caution: ensure correct character encoding and is not binary data
		      System.out.println("---------------------------------------");
		      System.out.println(new String(responseBody));

		    } catch (HttpException e) {
		      System.err.println("Fatal protocol violation: " + e.getMessage());
		      e.printStackTrace();
		    } catch (IOException e) {
		      System.err.println("Fatal transport error: " + e.getMessage());
		      e.printStackTrace();
		    } finally {
		      // Release the connection.
		      method.releaseConnection();
		    }	  
}
  
/*
 * USING HAPI FHIR   R4
 * From https://hapifhir.io/hapi-fhir/docs/client/examples.html
 */
  public void xxx() {
/*	  
	// Create a patient object
	  Patient patient = new Patient();
	  patient.addIdentifier()
	     .setSystem("http://acme.org/mrns")
	     .setValue("12345");
	  patient.addName()
	     .setFamily("Jameson")
	     .addGiven("J")
	     .addGiven("Jonah");
	  patient.setGender(Enumerations.AdministrativeGender.MALE);

	  // Give the patient a temporary UUID so that other resources in
	  // the transaction can refer to it
	  patient.setId(IdType.newRandomUuid());

	  // Create an observation object
	  Observation observation = new Observation();
	  observation.setStatus(Observation.ObservationStatus.FINAL);
	  observation
	     .getCode()
	        .addCoding()
	           .setSystem("http://loinc.org")
	           .setCode("789-8")
	           .setDisplay("Erythrocytes [#/volume] in Blood by Automated count");
	  observation.setValue(
	     new Quantity()
	        .setValue(4.12)
	        .setUnit("10 trillion/L")
	        .setSystem("http://unitsofmeasure.org")
	        .setCode("10*12/L"));

	  // The observation refers to the patient using the ID, which is already
	  // set to a temporary UUID  
	  observation.setSubject(new Reference(patient.getIdElement().getValue()));

	  // Create a bundle that will be used as a transaction
	  Bundle bundle = new Bundle();
	  bundle.setType(Bundle.BundleType.TRANSACTION);

	  // Add the patient as an entry. This entry is a POST with an 
	  // If-None-Exist header (conditional create) meaning that it
	  // will only be created if there isn't already a Patient with
	  // the identifier 12345
	  bundle.addEntry()
	     .setFullUrl(patient.getIdElement().getValue())
	     .setResource(patient)
	     .getRequest()
	        .setUrl("Patient")
	        .setIfNoneExist("identifier=http://acme.org/mrns|12345")
	        .setMethod(Bundle.HTTPVerb.POST);
 
	  // Add the observation. This entry is a POST with no header
	  // (normal create) meaning that it will be created even if
	  // a similar resource already exists.
	  bundle.addEntry()
	     .setResource(observation)
	     .getRequest()
	        .setUrl("Observation")
	        .setMethod(Bundle.HTTPVerb.POST);

	  // Log the request
//	  FhirContext ctx = FhirContext.forR4();
	  System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
	  System.out.println("------------------------------------------------------");
	  System.out.println("PATIENT ID="+patient.getId());
	  System.out.println("------------------------------------------------------");

	  // Create a client and post the transaction to the server
	  IGenericClient client = ctx.newRestfulGenericClient("http://localhost:9001/r4");	//"http://hapi.fhir.org/baseR4"
	  try {
		  Bundle resp = client.transaction().withBundle(bundle).execute();
	
		  // Log the response
		  System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));
	  }catch( Exception e) {
		  System.out.println("POST ERROR:" + e.getMessage());
	  }
*/	  
  }
  
  public void yyy() {
	// Create a context and a client
	  try {
		  FhirContext ctx = FhirContext.forR4();
//		  String serverBase = "http://localhost:9001/r4"  ; // "http://hapi.fhr.org/baseR4"
		  IGenericClient client = ctx.newRestfulGenericClient(serverBase);
	
		  // We'll populate this list
		  List<IBaseResource> patients = new ArrayList<>();
	
		  // We'll do a search for all Patients and extract the first page
		  Bundle bundle = client
		     .search()
		     .forResource(Patient.class)
		     .where(Patient.NAME.matches().value("smith"))
		     .returnBundle(Bundle.class)
		     .execute();
		  patients.addAll(BundleUtil.toListOfResources(ctx, bundle));
	
		  // Load the subsequent pages
		  while (bundle.getLink(IBaseBundle.LINK_NEXT) != null) {
		     bundle = client
		        .loadPage()
		        .next(bundle)
		        .execute();
		     patients.addAll(BundleUtil.toListOfResources(ctx, bundle));
		  }
	
		  System.out.println("Loaded " + patients.size() + " patients!");
	  }catch( Exception e) {
		  System.out.println("yyy ERROR:" + e.getMessage());
	  }
	  
  }
  
  public void testReadJson() throws Exception {
/*	  
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=json");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		IdentifierDt dt = ourCtx.newJsonParser().parseResource(Patient.class, responseContent).getIdentifierFirstRep();

		assertEquals("1", dt.getSystem().getValueAsString());
		assertEquals(null, dt.getValue().getValueAsString());

		Header cl = status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION_LC);
*/		
 	}  
  public static void main(String[] args) throws Exception {
	  HttpFhirClientMirth appl = new HttpFhirClientMirth();
//  	  appl.readData();
 	  appl.createPatient();
//	  appl.xxx();
//	  appl.yyy();
 
//	  appl.createPatientNat();
	  
	  //See http://hapi.fhir.org/
// 	  appl.search("http://hapi.fhir.org/baseR4","Verdi");	//Bianchi ce ne sono 10
	  
//	  appl.createPatientTutorial();
  }
}