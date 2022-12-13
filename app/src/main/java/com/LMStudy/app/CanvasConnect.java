package com.LMStudy.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.LMStudy.app.io.CanvasCall;
import com.LMStudy.app.structures.NewCourse;
import com.LMStudy.app.structures.WorkFlow;
import com.LMStudy.app.student.StudentMenu;
import com.LMStudy.app.teacher.TeacherMenu;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * UI Manager for the Canvas UI Demo Features. Presents an example platform upon which
 * Canvas API interactions could be built, given a proper token infrastructure.
 */
public class CanvasConnect extends AppCompatActivity {

   /**
    * Static String id flag. Used in JSON operations
    */
   private static final String ID_FLAG = "id";

   /**
    * Static String name flag. Used in JSON operations
    */
   private static final String NAME_FLAG = "name";

   /**
    * Reference field for context passing
    */
   private Context context;

   /**
    * Reference field for passing User Preferences
    */
   private SharedPreferences userPrefs;

   /**
    * Singleton instance for the WorkFlow data structure. Used for data operations
    */
   private WorkFlow flowLink = WorkFlow.getInstance();

   /**
    * Reusable String array for storing id values from various queries.
    */
   private String[] ids;

   /**
    * Reusable String array for storing name values from various queries. Used for menu displays.
    */
   private String[] items;

   /**
    * Counter for looping operations
    */
   private int cycleCount;

   /**
    * Storage array for pulled courseIds. Persistent between queries.
    */
   private String[] courseIds;

   /**
    * Storage array for pulled course names. Persistent between queries.
    */
   private String[] courses;

   /**
    * Storage arrayList for reducing course results based on user selection.
    */
   private ArrayList<String> chosenIds;

   /**
    * Storage arrayList for reducing course results based on user selection.
    */
   private ArrayList<String> chosenCourses;

   private ListView menuOptions;
   private ArrayAdapter<String> menuAdapter;
   private Button confirm_btn;

   /**
    * OnCreate for the CanvasConnect menu. Sets initial values and preps the initial filter query.
    * @param savedInstanceState
    */
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.canvas_selector);
      context = this;
      userPrefs = this.getApplicationContext().getSharedPreferences(MainActivity.PREFS_FILE, MODE_PRIVATE);
      menuOptions = findViewById(R.id.selector);
      confirm_btn = findViewById(R.id.confirm_button);
      confirm_btn.setText(R.string.confirm);

      initialQuery();
   }

   /**
    * Launch method for the first selection menu. Performs a course query and populates a list for selection.
    */
   private void initialQuery() {
      this.setTitle(R.string.select_courses);
      String courseCall = CanvasCall.API_URL + CanvasCall.COURSE_QUERY;
      fillFromCall(getResponse(courseCall));
      courseIds = ids;
      courses = items;
      menuAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_multiple_choice, this.courses);
      menuOptions.setAdapter(menuAdapter);
      menuOptions.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
      confirm_btn.setOnClickListener(view -> {
         if(menuOptions.getCheckedItemCount() > 0) getSelection(menuOptions.getCheckedItemPositions());
      });
   }

   /**
    * Filter method for initial selection results. Isolates user-selected courses and preps followup queries.
    * @param selection a SparseBooleanArray indicating selected options from the previous selection menu.
    */
   private void getSelection(SparseBooleanArray selection) {
      int len = this.ids.length;
      chosenIds = new ArrayList<>();
      chosenCourses = new ArrayList<>();
      for (int i = 0; i < len; i++) {
         if (selection.get(i, false)) {
            chosenIds.add(courseIds[i]);
            chosenCourses.add(courses[i]);
         }
      }
      cycleCount = chosenIds.size();
      associateCourse(0);
   }

   /**
    * Handles a Teacher-specific example menu for associating incoming items with existing course objects.
    * Preps for Assignment queries to the extent that such preparation is possible.
    * @param cycle cycle count marking progress among selected courses.
    */
   private void associateCourse(int cycle) {
      if (cycle == cycleCount) {
         Intent back;
         String userRole = userPrefs.getString(MainActivity.ROLE_FLAG, "");
         if(userRole.equals(MainActivity.STUDENT_ROLE)) back = new Intent(getBaseContext(), StudentMenu.class);
         else back = new Intent(getBaseContext(), TeacherMenu.class);
         finish();
         startActivity(back);
      } else {
         String title_prefix = getString(R.string.associate_menu);
         String title = title_prefix + chosenCourses.get(cycle);
         setTitle(title);
         ArrayList<NewCourse> localCourses = new ArrayList<>(flowLink.getCourseList());
         String[] courseOptions = new String[localCourses.size()];
         for(int i=0; i<courseOptions.length; i++) {
            courseOptions[i] = localCourses.get(i).toString();
         }
         menuAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_single_choice, courseOptions);
         menuOptions.setAdapter(menuAdapter);
         menuOptions.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
         confirm_btn.setOnClickListener(view -> {
            setEmpty();
            /* Model For Future Versions:
            1. Prepare the next menu by launching a Canvas API query with the given Canvas Course as a parameter,
            and package each incoming course object that is a) published and b) not past due.
            2. For each selected option in this menu, launch the add assignment popup window, filling in the name
            and due date with data from the retrieved course object but prompting the user for Type, Priority, and
            Hours. Edits to Name and Due Date are obviously also allowed.
            3. Construct and Push each item as it is confirmed and move immediately to the next item.
            4. When all items have been pushed, advance the cycle like this. */
            confirm_btn.setOnClickListener(click -> associateCourse(cycle+1));
            });
      }
   }

   /**
    * Display method for cases where a query returns no items
    */
   private void setEmpty() {
      this.items = new String[] { getString(R.string.no_items) };
      menuAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, this.items);
      menuOptions.setAdapter(menuAdapter);
   }

   /**
    * Populates the id and item arrays from an input JSONArray
    * @param response a JSONArray retrieved as a Canvas API response.
    */
   private void fillFromCall(JSONArray response) {
      int len = response.length();
      this.ids = new String[len];
      this.items = new String[len];
      try {
         for (int i = 0; i < len; i++) {
            JSONObject item = response.getJSONObject(i);
            this.ids[i] = item.getString(ID_FLAG);
            this.items[i] = item.getString(NAME_FLAG);
         }
      } catch (JSONException j) { j.printStackTrace(); }
   }

   /**
    * Preps and Launches a CanvasCall thread to retrieve courses or items
    * @param url target Url string constructed from static CanvasCall components
    * @return a JSONArray returned by the Canvas API
    */
   private JSONArray getResponse(String url) {
      CanvasCall caller = new CanvasCall();
      caller.setUrl(url);
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
