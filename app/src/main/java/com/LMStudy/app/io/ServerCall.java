package com.LMStudy.app.io;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * Https Communications thread object for launching server calls (Not currently functioning)
 */
public class ServerCall implements Runnable {
   private static final String SERVER_URL = "https://cs431-12.cs.rutgers.edu:5940";
   private static final int TIMEOUT_VAL = 6000;
   private Object response = false;

   @Override
   public void run() {
      try {
         HttpsURLConnection serverLink = (HttpsURLConnection) new URL(SERVER_URL).openConnection();

         serverLink.setConnectTimeout(TIMEOUT_VAL);
         serverLink.connect();

         System.out.println(serverLink.getResponseMessage());
         serverLink.disconnect();
      }
      catch (IOException e) {
         e.printStackTrace();
         System.out.println(e.getMessage());
      }

   }

   public Object getResponse() {
      return this.response;
   }
}
