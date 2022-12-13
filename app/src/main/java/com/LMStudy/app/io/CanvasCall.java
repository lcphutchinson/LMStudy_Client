package com.LMStudy.app.io;

import org.json.JSONArray;
import org.json.JSONException;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.stream.Collectors;

/**
 * Runnable HTTPS Communications object for interfacing with the Canvas LMS REST API.
 * @author: Larson Pushard Hutchinson
 */
public class CanvasCall implements Runnable {

   /**
    * Canvas-generated user authentication token for the Canvas Features demo.
    * See LMStudy Documentation for details about this field.
    */
   public static final String MY_TOKEN = "";

   /**
    * Base URL for the Canvas API, stored as a String. Combined with other string components to form queries.
    */
   public static final String API_URL = "https://canvas.instructure.com/api/v1/";

   /**
    * URL component for launching Course Queries. Appended to API_URL
    */
   public static final String COURSE_QUERY = "courses?exclude_blueprint_courses&enrollment_state=active";

   /**
    * Timeout value for socket connection attempts
    */
   private static final Integer TIMEOUT_VAL = 3000;

   /**
    * String field for target Url, build from static URL components.
    */
   private String fullUrl;

   /**
    * JSON Array field for Canvas API responses. Populated on successful call.
    */
   private JSONArray response;

   /**
    * Run method for executing communication on a thread. Builds and operates the HTTPS connection
    * and stores the response in the appropriate field.
    */
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

   /**
    * Setter for the fullUrl field. Used to preconstruct query target.
    * @param url a String url for this call.
    */
   public void setUrl(String url) {
      this.fullUrl = url;
   }

   /**
    * Getter for the response field. Used to retrieve stored data following a successful call.
    * @return a JSONArray returned from the Canvas API.
    */
   public JSONArray getResponse() { return this.response; }
}
