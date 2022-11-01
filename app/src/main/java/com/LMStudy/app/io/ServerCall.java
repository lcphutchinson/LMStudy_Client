package com.LMStudy.app.io;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.JsonReader;
import android.util.JsonWriter;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

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
         byte[] output = request.toString().getBytes(StandardCharsets.UTF_8);
         OutputStream dispatcher = new BufferedOutputStream(serverLink.getOutputStream());
         dispatcher.write(output);

         //Prep response for reading
         BufferedReader receiver = new BufferedReader(
            new InputStreamReader(
               new BufferedInputStream(serverLink.getInputStream())
            )
         );

         switch (request.getString(SyncService.ACTION_FLAG)) {
            case "LOGIN": {
               String userToken = receiver.readLine();
               userPrefs.edit().putString("userToken", userToken).apply();
               response = (serverLink.getResponseCode() == HttpURLConnection.HTTP_OK);
               break;
            }
            case "PULL": {
               response = receiver.lines().toArray();
               break;
            }
            case "PUSH": {
               response = (serverLink.getResponseCode() == HttpsURLConnection.HTTP_CREATED);
               break;
            }
         }

         receiver.close();
         serverLink.disconnect();

      } catch (IOException e) {
         if(e instanceof SocketTimeoutException) {
            System.out.println("Connection Attempt Failed");
            this.response = CONNECTION_ERROR;
         }
         else e.printStackTrace();
      } catch (JSONException j) {
         j.printStackTrace();
      } finally {
         Thread.currentThread().notify();
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
