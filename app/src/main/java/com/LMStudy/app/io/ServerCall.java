package com.LMStudy.app.io;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

   private final OkHttpClient caller = new OkHttpClient();
   private Object response = false;

   @Override
   public void run() {

      Request call = new Request.Builder()
         .url(SERVER_URL)
         .build();

      try {
         Response response = caller.newCall(call).execute();

         Headers responseHeaders = response.headers();
         for(int i=0; i < responseHeaders.size(); i++){
            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
         }

         System.out.println(response.body().string());
      }
      catch(IOException e) {
         e.printStackTrace();
      }

/*      try {
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
*/

   }

   public Object getResponse() {
      return this.response;
   }
}
