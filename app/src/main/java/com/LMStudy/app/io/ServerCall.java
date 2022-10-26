package com.LMStudy.app.io;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Example Https communications object for early development. Can be used to confirm server connection.
 */
public class ServerCall implements Runnable {
   private static final String SERVER_URL = "https://cs431-12.cs.rutgers.edu:5490";
   private static final Integer TIMEOUT_VAL = 2000;
   private Object response = false;

   @Override
   public void run() {
      try {
         HttpsURLConnection serverLink = (HttpsURLConnection) new URL(SERVER_URL).openConnection();
         serverLink.setHostnameVerifier((s, sslSession) -> s.contains("cs.rutgers.edu"));
         serverLink.setConnectTimeout(TIMEOUT_VAL);

         //for now, just connect
         serverLink.connect();

         //and print out the header field (should be "OK")
         System.out.println(serverLink.getResponseMessage());
         serverLink.disconnect();

      } catch (IOException e) {
         if(e instanceof SocketTimeoutException) System.out.println("Connection Attempt Failed");
         System.out.println(e.getMessage());
      }
   }

   //Example response retrieval method
   public Object getResponse() {
      return this.response;
   }
}
