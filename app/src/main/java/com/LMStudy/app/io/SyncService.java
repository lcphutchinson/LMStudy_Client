package com.LMStudy.app.io;

import android.content.SharedPreferences;
import com.LMStudy.app.structures.NewCourse;
import com.LMStudy.app.structures.WorkFlow;
import com.LMStudy.app.structures.workitems.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Https communications service for preparing and launching ServerCall threads.
 * @author: Larson Pushard Hutchinson
 */
public class SyncService {

   /**
    * String label for the Action Flag, a JSON field used to indicate message intent to the LMStudy Server.
    */
   public static final String ACTION_FLAG = "ACTION";

   /**
    * ServerCall Object that is populated and run during method calls
    */
   private ServerCall caller;

   /**
    * JSON field for building a server request prior to call.
    */
   private JSONObject request;

   /**
    * SharedPreferences object for locating user settings, such as user role.
    */
   private SharedPreferences userPrefs;

   /**
    * Singleton Instance reference to the WorkFlow class. Used for retrieving items and courses.
    */
   private WorkFlow flowLink = WorkFlow.getInstance();

   /**
    * Singleton Instance reference for the SyncService. Passed to activities that make server calls.
    */
   public static SyncService instance = new SyncService();

   /**
    * Retrieves the Singleton Instance reference for the SyncService
    * @return a static SyncService Object
    */
   public static SyncService getInstance(){
      return instance;
   }

   /**
    * sets the userPrefs reference used by the Sync Service. Passed at program start.
    * @param userPrefs a SharedPreferences object, passed from MainActivity.
    */
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
    * Calling method for custom assignment submission. Used by both user roles
    * @param i: A WorkItem for sending
    */
   public String push(WorkItem i) {
      caller = new ServerCall();
      request = new JSONObject();

      try {
         switch(userPrefs.getString("role","")) {
            case "TEACHER": {
               request.put(ACTION_FLAG, "PUBLISH");
               request.put("token", userPrefs.getString("userToken", ""));
               request.put("course", i.getCourse().getCId());
               break;
            }
            case "STUDENT": {
               request.put(ACTION_FLAG, "PUSH");
               request.put("token", userPrefs.getString("userToken", ""));
               break;
            }
         }
         request.put("item", packItem(i));

      } catch (JSONException j) {
         j.printStackTrace();
      }

      Object response = getResponse();
      if (response instanceof String) return (String) response;
      else return "";
   }

   /**
    * Calling method for new course creation. Used by the Teacher role.
    * @param course the name of the course to be submitted for creation.
    * @return a boolean indicating successful creation.
    */
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

   /**
    * Calling method for joining a course as administrator. Used by Teacher Users.
    * @param course a string identifier for the course to be joined.
    * @param pw an administrative password for the course to be joined
    * @return a Boolean indicating successful enrollment.
    */
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

   /**
    * Calling method for course enrollment. Used by Student Users.
    * @param course a string identifier for the course to be joined
    * @return a Boolean indicating successful enrollment.
    */
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

   /**
    * Calling method for assignment dismissal. Used by Student Users
    * @param item a string identifier for the course to be completed.
    * @return a Boolean indicating successful completion.
    */
   public boolean complete(String item) {
      request = new JSONObject();
      caller = new ServerCall();
      try {
         request.put(ACTION_FLAG, "COMPLETE");
         request.put("token", userPrefs.getString("userToken", ""));
         request.put("item", item);
      } catch(JSONException j) {
         j.printStackTrace();
      }

      Object response = this.getResponse();
      if (response instanceof Boolean) return (Boolean) response;
      else return false;
   }

   /**
    * Calling method for logging progress updates. Used by Student Users.
    * @param item a string identifier for the item to be edited.
    * @param val a new value for the Progress field.
    * @return a Boolean indicating successful update.
    */
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
    * Calling method for updating attributes of an assignment. Used by Teacher users.
    * @param i a WorkItem for transmission, containing updated fields
    * @return a Boolean indicating successful update.
    */
   public boolean detail(WorkItem i) {
      request = new JSONObject();
      caller = new ServerCall();
      try {
         request.put(ACTION_FLAG, "DETAIL");
         request.put("token", userPrefs.getString("userToken",""));
         request.put("id", i.getIID());
         request.put("item", packItem(i));
      } catch (JSONException j) {
         j.printStackTrace();
      }

      Object response = this.getResponse();
      if (response instanceof Boolean) return (Boolean) response;
      else return false;
   }

   /**
    * Calling method for deleting a published assignment. Used by Teacher users.
    * @param item a string identifier for the item to be deleted
    * @return a Boolean indicating successful deletion.
    */
   public boolean delete(String item) {
      request = new JSONObject();
      caller = new ServerCall();
      try {
         request.put(ACTION_FLAG, "DELETE");
         request.put("token", userPrefs.getString("userToken", ""));
         request.put("item", item);
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
    * @return a JSONObject containing the input item.
    */
   private JSONObject packItem(WorkItem a) {
      JSONObject item = new JSONObject();
      try {
         item.put("name", a.getName());
         item.put("type", a.getType().toLowerCase());
         item.put("due", a.getDisplayDate());
         item.put("priority", a.getPriority());
         item.put("hours", a.getPriorityData()[1]);
      } catch (JSONException j) {
         j.printStackTrace();
      }
      return item;
   }

   /**
    * Launch method for the server call. Builds and runs the call thread, then retrieves a response.
    * @return the response Object generated by this server call.
    */
   private Object getResponse() {
      caller.setRequest(request);
      caller.setPreferences(userPrefs);
      Thread call = new Thread(caller);
      call.start();
      try {
         while (caller.getResponse() == null){
            Thread.sleep(10); //note: fix this later, if possible.
         }
      } catch(InterruptedException e) {
         e.printStackTrace();
      }
      return caller.getResponse();
   }

}
