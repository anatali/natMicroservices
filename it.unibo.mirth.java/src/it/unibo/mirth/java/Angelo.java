package it.unibo.mirth.java;

 

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Base64;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Angelo {
	private static final String BASIC_URI = "https://localhost:8443";
	private static final String USERNAME = "admin"; //TODO: change USERNAME
	private static final String PASSWORD = "nat25650"; //TODO: change PASSWORD

	public static void main(String[] args) throws Exception {
		disableSslVerification();
		
		final String channelId = "d6b7c1b0-ac5e-4993-9570-45d8947ad76d"; //TODO: update this ID
		final String msg = "Messaggio di test";

		
		String answer = doGet(BASIC_URI + "/api/channels"  );
		
		log("Result:\n"+ answer);
		
/*		
		String res = doPost(BASIC_URI + "/api/channels/"+ channelId + "/messages", "text/plain", msg);
		
		String msgNumber = res.replace("<long>", "").replace("</long>", "");

		res = doGet(BASIC_URI + "/api/channels/" + channelId + "/messages/" + msgNumber);
		
		log("Result:\n"+ res);
*/		
	}
/*
 *  il server locale che attiva Mirth è HTTPS ma non espone un certificato valido. 
 *  Le due cose non possono stare insieme, per cui disabilito la verifica dei certificati SSL.
 */
	private static void disableSslVerification() throws Exception {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}

	private static String createBasicAuth() {
		return Base64.getEncoder().encodeToString((USERNAME + ":" + PASSWORD).getBytes(StandardCharsets.UTF_8));
	}
	
	private static String doGet(final String url) throws Exception {
		HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();

		httpClient.setRequestMethod("GET");
		httpClient.setRequestProperty("Authorization", "Basic " + createBasicAuth());
		
		int responseCode = httpClient.getResponseCode();
		log("\nSending 'GET' request to URL : " + url);
		log("Response Code : " + responseCode);

		try (BufferedReader in = new BufferedReader(new InputStreamReader(httpClient.getInputStream()))) {
			final StringBuilder response = new StringBuilder();
			String line;

			while ((line = in.readLine()) != null) {
				response.append(line);
			}
			return response.toString();
		}
	}

	private static String doPost(final String url, final String contentType, final String urlParameters) throws Exception {
		final HttpsURLConnection httpClient = (HttpsURLConnection) new URL(url).openConnection();

		httpClient.setRequestMethod("POST");
		httpClient.setRequestProperty("Authorization", "Basic " + createBasicAuth());
		httpClient.setRequestProperty("Content-Type", contentType);
		httpClient.setDoOutput(true);

		try (DataOutputStream wr = new DataOutputStream(httpClient.getOutputStream())) {
			wr.writeBytes(urlParameters);
			wr.flush();
		}

		int responseCode = httpClient.getResponseCode();
		log("\nSending 'POST' request to URL : " + url);
		log("Post parameters : " + urlParameters);
		log("Response Code : " + responseCode);

		try (BufferedReader in = new BufferedReader(new InputStreamReader(httpClient.getInputStream()))) {
			final StringBuilder response = new StringBuilder();
			String line;
			
			while ((line = in.readLine()) != null) {
				response.append(line);
			}

			return response.toString();
		}
	}
	
	private static void log(final String msg) {
		System.out.println(msg);
	}
}
