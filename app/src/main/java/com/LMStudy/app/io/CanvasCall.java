package com.LMStudy.app.io;

import org.json.JSONArray;
import org.json.JSONException;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.stream.Collectors;

public class CanvasCall implements Runnable {
   public static final String MY_TOKEN = "";
   public static final String API_URL = "https://canvas.instructure.com/api/v1/";
   public static final String STUDENT_QUERY = "users/self/activity_stream?only_active_courses=true";
   public static final String COURSE_QUERY = "courses?exclude_blueprint_courses&enrollment_state=active";
   private static final Integer TIMEOUT_VAL = 3000;

   private String fullUrl;
   private JSONArray response;

   @Override
   public void run() {
      try {
         //Build the unrealized connection object, and configure it for output.
         HttpsURLConnection canvasLink = (HttpsURLConnection) new URL(fullUrl).openConnection();
         canvasLink.setHostnameVerifier((s, sslSession) -> s.contains("canvas.instructure.com"));
         canvasLink.setConnectTimeout(TIMEOUT_VAL);
         canvasLink.setChunkedStreamingMode(0);

         //Build the Request Header
         canvasLink.setRequestMethod("GET");
         canvasLink.setRequestProperty("Content-Type", "application/json");
         canvasLink.setRequestProperty("Authorization", "Bearer " + MY_TOKEN);

         //Prep response for reading
         BufferedReader receiver = new BufferedReader(
            new InputStreamReader(
               new BufferedInputStream(canvasLink.getInputStream())
            )
         );

         JSONArray response = new JSONArray(receiver.lines().collect(Collectors.joining()));
         this.response = response;
      } catch(IOException e) {
         e.printStackTrace();
      } catch(JSONException j) {
         j.getStackTrace();
      }
   }

   public void setUrl(String url) {
      this.fullUrl = url;
   }

   public JSONArray getResponse() { return this.response; }
}
