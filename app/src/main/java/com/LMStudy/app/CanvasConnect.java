package com.LMStudy.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
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

public class CanvasConnect extends AppCompatActivity {

   private Context context;
   private SharedPreferences userPrefs;
   private WorkFlow flowLink = WorkFlow.getInstance();
   private String[] ids;
   private String[] items;
   private int cycleCount;

   private String[] courseIds;
   private String[] courses;
   private ArrayList<String> chosenIds;
   private ArrayList<String> chosenCourses;

   private ListView menuOptions;
   private ArrayAdapter<String> menuAdapter;
   private Button confirm_btn;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.canvas_selector);
      context = this;
      userPrefs = this.getApplicationContext().getSharedPreferences("userPrefs", MODE_PRIVATE);
      menuOptions = findViewById(R.id.selector);
      confirm_btn = findViewById(R.id.confirm_button);
      confirm_btn.setText(R.string.confirm);

      teacherQuery();
   }


   private void teacherQuery() {
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

   private void associateCourse(int cycle) {
      if (cycle == cycleCount) {
         Intent back;
         String userRole = userPrefs.getString("role", "");
         if(userRole.equals("STUDENT")) back = new Intent(getBaseContext(), StudentMenu.class);
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

   private void setEmpty() {
      this.items = new String[] { getString(R.string.no_items) };
      menuAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, this.items);
      menuOptions.setAdapter(menuAdapter);
   }

   private void fillFromCall(JSONArray response) {
      int len = response.length();
      this.ids = new String[len];
      this.items = new String[len];
      try {
         for (int i = 0; i < len; i++) {
            JSONObject item = response.getJSONObject(i);
            this.ids[i] = item.getString("id");
            this.items[i] = item.getString("name");
         }
      } catch (JSONException j) { j.printStackTrace(); }
   }

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
