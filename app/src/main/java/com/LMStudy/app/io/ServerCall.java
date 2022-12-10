package com.LMStudy.app.io;

import android.content.SharedPreferences;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Example Https communications object for early development. Can be used to confirm server connection.
 */
public class ServerCall implements Runnable {
   private static final String SERVER_URL = "https://cs431-12.cs.rutgers.edu:5490";
   private static final Integer TIMEOUT_VAL = 3000;
   private static final Integer CONNECTION_ERROR = -1;
   private SharedPreferences userPrefs;
   private JSONObject request;
   private Object response;

   @Override
   public void run() {
      try {
         //Build the unrealized connection object, and configure it for output.
         HttpsURLConnection serverLink = (HttpsURLConnection) new URL(SERVER_URL).openConnection();
         serverLink.setHostnameVerifier((s, sslSession) -> s.contains("cs.rutgers.edu"));
         serverLink.setConnectTimeout(TIMEOUT_VAL);
         serverLink.setDoOutput(true);
         serverLink.setChunkedStreamingMode(0);

         //Build the Request Header
         serverLink.setRequestMethod("POST");
         serverLink.setRequestProperty("Content-type", "application/json");
         serverLink.setRequestProperty("Accept", "application/json");

         //Dispatch
         System.out.println(request);
         byte[] output = request.toString().getBytes(StandardCharsets.UTF_8);
         OutputStream dispatcher = new BufferedOutputStream(serverLink.getOutputStream());
         dispatcher.write(output);
         dispatcher.flush();
         dispatcher.close();

         //Prep response for reading
         int responseCode = serverLink.getResponseCode();
         if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
            BufferedReader receiver = new BufferedReader(new InputStreamReader(
               new BufferedInputStream(serverLink.getInputStream())));

            switch (request.getString(SyncService.ACTION_FLAG)) {
               case "SIGNUP": {
                  String userToken = receiver.readLine();
                  userPrefs.edit().putString("userToken", userToken).apply();
                  response = true;
               }
               case "LOGIN": {
                  String userToken = receiver.readLine();
                  userPrefs.edit().putString("userToken", userToken).apply();
                  response = true;
                  break;
               }
               case "CPULL":
               case "PULL": {
                  response = receiver.lines().toArray();
                  break;
               }
               case "PUBLISH":
               case "PUSH": {
                  response = receiver.readLine();
                  break;
               }
               case "DROP":
               case "OPEN":
               case "JOIN":
               case "ENROLL":
               case "COMPLETE":
               case "PROGRESS":
               case "DELETE" :
               case "DETAIL" : {
                  response = true;
                  break;
               }
            }
         } else response = false;
         serverLink.disconnect();
      } catch (IOException e) {
         if(e instanceof SocketTimeoutException) {
            System.out.println("Connection Attempt Failed");
            this.response = CONNECTION_ERROR;
         }
         else e.printStackTrace();
      } catch (JSONException j) {
         j.printStackTrace();
      }
   }

   public void setRequest(JSONObject request) {
      this.request = request;
   }

   public void setPreferences(SharedPreferences userPrefs) { this.userPrefs = userPrefs; }

   //Example response retrieval method
   public Object getResponse() {
      return this.response;
   }
}
