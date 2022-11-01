package com.LMStudy.app.io;

import android.content.SharedPreferences;
import com.LMStudy.app.structures.Assignment;
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

   public static SyncService instance = new SyncService();

   public static SyncService getInstance(){
      return instance;
   }

   public void setPreferences(SharedPreferences userPrefs){
      this.userPrefs = userPrefs;
   }

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

   public List<Assignment> pullAll() {
      caller = new ServerCall();
      try {
         request.put(ACTION_FLAG, "PULL");
         request.put("userToken", userPrefs.getString("userToken", ""));
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
                  JSONObject json = new JSONObject(jsonString);
                  String name = json.getString("name");
                  String[] dateStrings = json.getString("due").split("/");
                  LocalDateTime due = LocalDateTime.of(
                     Integer.parseInt(dateStrings[1]),
                     Integer.parseInt(dateStrings[2]),
                     Integer.parseInt(dateStrings[0]),
                     23, 59
                  );
                  results.add(new Assignment("DBtest", name, "Synced", due));
               } catch (JSONException j) {
                  j.printStackTrace();
               }
            }
         }
         return results;
      }
      else return null;
   }

   public void push(Assignment a) {
      caller = new ServerCall();
      request = new JSONObject();

      try {
         request.put(ACTION_FLAG, "PUSH");
         request.put("userToken", userPrefs.getString("userToken", ""));
         request.put("name", a.getAssignmentName());
         request.put("due", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(a.getDueDate()));
      } catch (JSONException j) {
         j.printStackTrace();
      }

      Object response = getResponse();
      // later If(response instanceof Boolean) etc etc. This won't be a void function then.
   }

   private Object getResponse() {
      caller.setRequest(request);
      caller.setPreferences(userPrefs);
      Thread call = new Thread(caller);
      call.start();

      try {
         Thread.currentThread().wait();
      } catch(InterruptedException e) {
         e.printStackTrace();
      }
      return caller.getResponse();
   }

}
