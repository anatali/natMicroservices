package connQak;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import java.io.*;

public class HttpApplClient {

	private String hl7251 =
"MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r\n" + 
"PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^CDCREC|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r\n" + 
"PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r\n" + 
"NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r\n" + 
"ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r\n" + 
"RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r\n" + 
"RXR|C28161^Intramuscular^NCIT|LD^Left Arm^HL70163\r\n" + 
"OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r\n" + 
"OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r\n" + 
"OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r\n" + 
"OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r\n" + 
"";
//  private static String url = "https://www.apache.org/";
//   private static String url = "https://localhost:8443/api/users";
//  private static String url = "https://localhost:8443/api/channels";
//   private static String url = "https://localhost:8443/api/#/users/_login?username=admin&password=nat25650"; //PAGE
//   private static String url = "https://localhost:8443/api/users/_login?username=admin&password=nat25650";
//   private static String url = "https://localhost:8443/api/users/_login?username=admin&password=nat25650";
     private static String url = "http://localhost:8050";

  public static void main(String[] args) {
    // Create an instance of HttpClient.
    HttpClient client = new HttpClient();

    // Create a method instance.
    GetMethod method  = new GetMethod(url);
//    PutMethod methodp = new PutMethod(url);
//    PostMethod methodp = new PostMethod(url);
    
    // Provide custom retry handler is necessary
    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
    		new DefaultHttpMethodRetryHandler(3, false));
     
//    System.out.println("POST path :"+ methodp.getPath() );
//    System.out.println("POST query:"+ methodp.getQueryString());
    
    //methodp.getParams().
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
}