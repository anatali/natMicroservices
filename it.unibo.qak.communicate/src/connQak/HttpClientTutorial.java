package connQak;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import java.io.*;

public class HttpClientTutorial {
  
//  private static String url = "https://www.apache.org/";
//   private static String url = "https://localhost:8443/api/users";
//  private static String url = "https://localhost:8443/api/channels";
//   private static String url = "https://localhost:8443/api/#/users/_login?username=admin&password=nat25650"; //PAGE
//   private static String url = "https://localhost:8443/api/users/_login?username=admin&password=nat25650";
//   private static String url = "https://localhost:8443/api/users/_login?username=admin&password=nat25650";
     private static String url = "https://localhost:8443/api/channels";

  public static void main(String[] args) {
    // Create an instance of HttpClient.
    HttpClient client = new HttpClient();

    // Create a method instance.
    GetMethod method  = new GetMethod(url);
//    PutMethod methodp = new PutMethod(url);
    PostMethod methodp = new PostMethod(url);
    
    // Provide custom retry handler is necessary
    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
    		new DefaultHttpMethodRetryHandler(3, false));
     
     System.out.println("POST path :"+ methodp.getPath() );
    System.out.println("POST query:"+ methodp.getQueryString());
    
    //methodp.getParams().
    try {
      // Execute the method.
      int statusCode = client.executeMethod(methodp);

      if (statusCode != HttpStatus.SC_OK) {
        System.err.println("Method failed: " + methodp.getStatusLine());
      }

      // Read the response body.
      byte[] responseBody = methodp.getResponseBody();

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