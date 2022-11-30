package com.LMStudy.app.io;

import android.content.SharedPreferences;
import com.LMStudy.app.structures.Assignment;
import com.LMStudy.app.structures.NewCourse;
import com.LMStudy.app.structures.WorkFlow;
import com.LMStudy.app.structures.workitems.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
   WorkFlow flowLink = WorkFlow.getInstance();

   public static SyncService instance = new SyncService();

   public static SyncService getInstance(){
      return instance;
   }

   public void setPreferences(SharedPreferences userPrefs){
      this.userPrefs = userPrefs;
   }

   /**
    * Calling method for Signup Action: loads a userToken into preferences if successful
    * @param username username input from the login screen
    * @param pw password input from the login screen
    * @return a boolean indicating successful account creation
    */
   public Boolean signup(String username, String pw) {
      caller = new ServerCall();
      request = new JSONObject();

      try {
         request.put(ACTION_FLAG, "SIGNUP");
         request.put("username", username);
         request.put("password", pw);
      } catch (JSONException j) {
         j.printStackTrace();
      }

      Object response = getResponse();
      if (response instanceof Boolean) return (Boolean) response;
      else return null;
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
    * Calling method for pulling user courses. Used by both user roles
    * @return A List of Courses Associated with this user
    */
   public List<NewCourse> pullCourses() {
      request = new JSONObject();
      caller = new ServerCall();
      try {
         request.put(ACTION_FLAG, "CPULL");
         request.put("token", userPrefs.getString("userToken", ""));
         request.put("role", userPrefs.getString("role", ""));
      } catch (JSONException j) {
         j.printStackTrace();
      }

      Object response = this.getResponse();
      if (response instanceof Object[]) {
         ArrayList<NewCourse> results = new ArrayList<>();
         Object[] chunks = (Object[]) response;

         for (Object chunk : chunks) {
            if (chunk instanceof String) {
               try {
                  String jsonString = (String) chunk;
                  System.out.println("Server Payload: \n" + jsonString);
                  JSONArray json = new JSONArray(jsonString);
                  for (int i = 0; i < json.length(); i++) {
                     JSONArray j = json.getJSONArray(i);
                     String id = j.getString(0);
                     String name = j.getString(1);
                     String pw = j.getString(2);
                     results.add(new NewCourse(id, name, pw));
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
    * Calling method for pulling user assignments. Used by both user roles.
    * @return A list of WorkItems associated with this user.
    */
   public List<WorkItem> pullItems() {
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
         ArrayList<WorkItem> results = new ArrayList<>();
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
                     String due = j.getString(4).split("T")[0];
                     Integer priority = j.getInt(5);
                     Integer hours = j.getInt(6);
                     Integer progress = j.getInt(7);

                     switch(type) {
                        case "homework":
                           results.add(new Homework(
                              flowLink.getCourseById(course), id, name, due, priority, hours, progress));
                           break;
                        case "quiz":
                           results.add(new Quiz(
                              flowLink.getCourseById(course), id, name, due, priority, hours, progress));
                           break;
                        case "project":
                           results.add(new Project(
                              flowLink.getCourseById(course), id, name, due, priority, hours, progress));
                           break;
                        case "exam":
                           results.add(new Exam(
                              flowLink.getCourseById(course), id, name, due, priority, hours, progress));
                           break;
                     }
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
   public Boolean dropCourse(String courseId) {
      request = new JSONObject();
      caller = new ServerCall();
      try {
         request.put(ACTION_FLAG, "DROP");
         request.put("token", userPrefs.getString("userToken", ""));
         request.put("role", userPrefs.getString("role", ""));
         request.put("course", courseId);
      } catch(JSONException j) {
         j.printStackTrace();
      }

      Object response = this.getResponse();
      if (response instanceof Boolean) return (Boolean) response;
      else return false;
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

   public Boolean open(String course) {
      request = new JSONObject();
      caller = new ServerCall();
      try {
         request.put(ACTION_FLAG, "OPEN");
         request.put("token", userPrefs.getString("userToken", ""));
         request.put("course", course);
      } catch(JSONException j) {
         j.printStackTrace();
      }

      Object response = this.getResponse();
      if (response instanceof Boolean) return (Boolean) response;
      else return false;
   }

   public boolean join(String course, String pw) {
      request = new JSONObject();
      caller = new ServerCall();
      try {
         request.put(ACTION_FLAG, "JOIN");
         request.put("token", userPrefs.getString("userToken", ""));
         request.put("course", course);
         request.put("pw", pw);
      } catch(JSONException j) {
         j.printStackTrace();
      }

      Object response = this.getResponse();
      if (response instanceof Boolean) return (Boolean) response;
      else return false;
   }

   public boolean enroll(String course) {
      request = new JSONObject();
      caller = new ServerCall();
      try {
         request.put(ACTION_FLAG, "ENROLL");
         request.put("token", userPrefs.getString("userToken", ""));
         request.put("course", course);
      } catch(JSONException j) {
         j.printStackTrace();
      }

      Object response = this.getResponse();
      if (response instanceof Boolean) return (Boolean) response;
      else return false;
   }

   public boolean progress(String item, int val) {
      request = new JSONObject();
      caller = new ServerCall();
      try {
         request.put(ACTION_FLAG, "PROGRESS");
         request.put("token", userPrefs.getString("userToken", ""));
         request.put("item", item);
         request.put("val", val);
      } catch(JSONException j) {
         j.printStackTrace();
      }

      Object response = this.getResponse();
      if (response instanceof Boolean) return (Boolean) response;
      else return false;
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
            Thread.sleep(10); //note: fix this later.
         }
      } catch(InterruptedException e) {
         e.printStackTrace();
      }
      System.out.println("Busy Wait Concluded");
      return caller.getResponse();
   }

}
