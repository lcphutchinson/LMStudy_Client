package com.LMStudy.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

   private ListView menuOptions;
   private ArrayAdapter<String> menuAdapter;
   private Button okButton;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.shared_menu);
      this.setTitle(R.string.select_courses);
      context = this;
      userPrefs = this.getApplicationContext().getSharedPreferences("userPrefs", MODE_PRIVATE);
      menuOptions = findViewById(R.id.menu_options);

      //Stage 0: Pull the appropriate courses for this user, and allow selection.
      fillFromCall(getResponse(constructUrl(0)));
      menuAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_multiple_choice, this.items);
      menuOptions.setAdapter(menuAdapter);
      menuOptions.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
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

   private String constructUrl(int query_stage) {
      String returnUrl = CanvasCall.API_URL;
      switch(query_stage) {
         case 0:
            returnUrl += CanvasCall.COURSE_QUERY;
            String userRole = userPrefs.getString("role", "TEACHER");
            if (userRole.equals("STUDENT")) returnUrl += CanvasCall.STUDENT_QUERY;
            break;
      }
      return returnUrl;
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
