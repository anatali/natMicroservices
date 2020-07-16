package it.unibo.mirth.java;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import java.io.*;

public class HttpClientMirth {
  private static String url     = "http://localhost:8002/api/channels/69c079fd-1cc3-469f-8f00-b4c51638fdde/messages";
  private static String msgJson = "{\r\n" + 
  		"    \"application_sending\" : \"EPICADT\",\r\n" + 
  		"    \"sending_facility\" : \"DH\",\r\n" + 
  		"    \"receiving_application\" : \"LABADT\",\r\n" + 
  		"    \"receiving_facility\" : \"DH\",\r\n" + 
  		"    \"time\": \"201301011226\",\r\n" + 
  		"    \"message_type\" : \"ADT^A01\",\r\n" + 
  		"    \"message_control_id\" : \"HL7MSG00001\",\r\n" + 
  		"    \"process_id\" : \"P\",\r\n" + 
  		"    \"version_id\": \"2.3\"\r\n" + 
  		"}";
/* 
 * curl -H "Content-Type: application/json" --data @json_template.json http://127.0.0.1:8002/api/channels/69c079fd-1cc3-469f-8f00-b4c51638fdde/messages

'{"application_sending" : "EPICADT","sending_facility" : "DH","receiving_application" : "LABADT","receiving_facility" : "DH","time": "201301011226","message_type" : "ADT^A01","message_control_id" : "HL7MSG00001","process_id" : "P","version_id": "2.3"}'
 */
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
     
     System.out.println("get path->"+ method.getPath() );
    System.out.println("get query->"+ method.getQueryString());
    
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