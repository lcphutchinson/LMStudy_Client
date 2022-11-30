package com.LMStudy.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.LMStudy.app.io.CanvasCall;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CanvasConnect extends AppCompatActivity {

   private Context context;
   private SharedPreferences userPrefs;
   private String[] ids;
   private String[] items;

   private String[] courseIds;
   private String[] courses;

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

      String userRole = userPrefs.getString("role", "STUDENT");
      if (userRole.equals("STUDENT")) studentQuery();
      else teacherQuery();
   }

   private void studentQuery() {
      this.setTitle(R.string.select_items);
      String call = CanvasCall.API_URL + CanvasCall.STUDENT_QUERY;
      fillFromCall(getResponse(call));
      if (this.items.length == 0) setEmpty();
      else {
         menuAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_multiple_choice, this.items);
         menuOptions.setAdapter(menuAdapter);
         menuOptions.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
         menuOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               if(menuOptions.getCheckedItemCount() > 0)
                  processMenu(menuOptions.getCheckedItemPositions());
            }
         });

      }
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
         // if(menuOptions.getCheckedItemCount() > 0) nextSteps(menuOptions.getCheckedItemPositions());
      });
   }

   private void setEmpty() {
      this.items = new String[] { getString(R.string.no_items) };
      menuAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, this.items);
      menuOptions.setAdapter(menuAdapter);
   }

   private void processMenu(SparseBooleanArray selection) {
      int len = this.ids.length;
      Boolean[] selected = new Boolean[len];
      for(int i=0; i<len; i++) selected[i] = selection.get(i, false);
      for(int i=0; i<len; i++) {
         if (selected[i]) {
            //launch add menu
         }
      }

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
