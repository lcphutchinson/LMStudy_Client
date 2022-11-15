package com.LMStudy.app.io;

import android.content.SharedPreferences;
import com.LMStudy.app.structures.Assignment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Https communications object; manages a library of server calls.
 */
public class SyncService {
   public static final String ACTION_FLAG = "ACTION";
   private ServerCall caller;
   private JSONObject request;
   private SharedPreferences userPrefs;
   private final Object lock = new Object();

   public static SyncService instance = new SyncService();

   public static SyncService getInstance(){
      return instance;
   }

   public void setPreferences(SharedPreferences userPrefs){
      this.userPrefs = userPrefs;
   }

   /**
    * Calling method for login action: loads a userToken into preferences
    * @param username username input from login screen
    * @param pw password input from login screen
    * @return A boolean indicating successful login.
    */
   public Boolean login(String username, String pw) {
      caller = new ServerCall();
      request = new JSONObject();

      try {
         request.put(ACTION_FLAG, "LOGIN");
         request.put("username", username);
         request.put("password", pw);
      } catch(JSONException j) {
         j.printStackTrace();
      }

      Object response = getResponse();
      if (response instanceof Boolean) return (Boolean) response;
      else return null;
   }

   /**
    * Calling method for pulling user assignments. Used by both user roles.
    * @return A list of Assignments associated with this user.
    */
   public List<Assignment> pullAll() {
      request = new JSONObject();
      caller = new ServerCall();
      try {
         request.put(ACTION_FLAG, "PULL");
         request.put("token", userPrefs.getString("userToken", ""));
         request.put("role", userPrefs.getString("role", ""));
      } catch(JSONException j) {
         j.printStackTrace();
      }

      Object response = this.getResponse();
      if (response instanceof Object[]) {
         ArrayList<Assignment> results = new ArrayList<>();
         Object[] chunks = (Object[]) response;

         for (Object chunk : chunks) {
            if (chunk instanceof String) {
               try {
                  String jsonString = (String) chunk;
                  System.out.println("Server Payload:\n" + jsonString);
                  JSONArray json = new JSONArray(jsonString);
                  for (int i = 0; i < json.length(); i++) {
                     JSONArray j = json.getJSONArray(i);
                     String course = j.getString(0);
                     String id = j.getString(1);
                     String name = j.getString(2);
                     String type = j.getString(3);
                     String due = j.getString(4);
                     Integer priority = j.getInt(5);
                     Integer hours = j.getInt(6);
                     Integer progress = j.getInt(7);
                     results.add(new Assignment(course, name, type, due));
                  }
               } catch (JSONException j) {
                  j.printStackTrace();
               }
            }
         }
         return results;
      }
      else return null;
   }

   /**
    * Calling method for terminating course membership. Used by both roles.
    * @param courseId String code used for identifying courses.
    */
   public void dropCourse(String courseId) {

   }

   /**
    * Calling method for custom assignment submission. Used by both users
    * @param a:
    */
   public void push(Assignment a) {
      caller = new ServerCall();
      request = new JSONObject();

      try {
         switch(userPrefs.getString("role","")) {
            case "TEACHER": {
               request.put(ACTION_FLAG, "PUBLISH");
               request.put("course", a.getCourseInfo());
            }
            case "STUDENT": {
               request.put(ACTION_FLAG, "PUSH");
            }
            request.put("item", packItem(a));
         }

      } catch (JSONException j) {
         j.printStackTrace();
      }

      Object response = getResponse();
      // later If(response instanceof Boolean) etc etc. This won't be a void function then.
   }


   /**
    * Utility method for processing a single assignment for transmission
    * @param a an Assignment for packing
    * @return a JSONObject containing the input assignment.
    */
   private JSONObject packItem(Assignment a) {
      JSONObject item = new JSONObject();
      try {
         item.put("name", a.getAssignmentName());
         item.put("type", a.getAssignmentType());
         item.put("due", a.getDueDate());
         item.put("priority", a.getPriority());
         item.put("hours", 0); //replace when Assignment has an hours field.
      } catch (JSONException j) {
         j.printStackTrace();
      }
      return item;
   }

   private Object getResponse() {
      caller.setRequest(request);
      caller.setPreferences(userPrefs);
      Thread call = new Thread(caller);
      call.start();

      try {
         while (caller.getResponse() == null){
            Thread.sleep(50); //note: fix this later.
         }
      } catch(InterruptedException e) {
         e.printStackTrace();
      }
      return caller.getResponse();
   }

}
