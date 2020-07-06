package connQak;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;

/**
*
* @author Giacomo Petronio
*/
public class HTTPClient {

    private String host = "";
    private int port = 80;
    private String path = "";
    private String method = "GET";
    private String body = "";
    private Hashtable headers = new Hashtable();


    /** Creates a new instance of HTTPClient */
    public HTTPClient() {
    }

    public void setHost(String host, int port, String path) {
        this.host = host;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void addRequestHeader(String key, String value){
        headers.put(key, value);
    }

    /**
    * returns 2 strings
    * String[0] is the request
    * String[1] is the response
    */
    public String[] send() throws IOException{

        String response = "";
        String request = "";

        // NETWORK STUFFS
        Socket socket = new Socket(host,port);
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader in =  new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // crea la request
        request += method + " /" + " HTTP/1.0\r\n";

        // aggiungi headers
        Enumeration keys = headers.keys();
        while(keys.hasMoreElements()){
            String key = (String) keys.nextElement();
            String value = (String) headers.get(key);
            request += key + ": " + value + "\r\n";
        }
        // controllo content-length, indispensabile per il POST
        if(headers.get("Content-Length:") == null )
            request += "Content-Length: " + body.getBytes().length + "\r\n";

        // linea di fine headers
        request += "\r\n";

        // aggiungo il body
        request += body;

        // invio
        System.out.println(request+"\n");
        out.print(request);
        out.flush();


        String s;
        while ( (s = in.readLine()) != null ){
            response += s + "\n";
            System.out.println(s);
        }

        in.close();
        out.close();
        socket.close();

        String[] result = new String[2];
        result[0] = request;
        result[1] = response;

        return result;
    }
}