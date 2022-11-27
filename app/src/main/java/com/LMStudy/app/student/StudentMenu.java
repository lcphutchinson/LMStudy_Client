package com.LMStudy.app.student;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.LMStudy.app.R;
import com.LMStudy.app.ItemsList;
import com.LMStudy.app.SharedMenu;
import com.LMStudy.app.io.SyncService;
import com.LMStudy.app.structures.NewCourse;
import com.LMStudy.app.structures.WorkFlow;

import java.util.ArrayList;

public class StudentMenu extends AppCompatActivity {
   private static final int ITEM_MENU = 0;
   private static final int LMS_MENU = 1;
   private static final int COURSE_MENU = 2;
   private static final int SETTINGS_MENU = 3;

   //subMenu1 Items Menu
   private TextView item_list, item_select, item_complete;

   //subMenu2 LMS Menu
   private TextView lms_canvasLogin;

   //subMenu3 Course Menu
   private TextView course_default, course_add;

   //subMenu3 Settings Menu
   private TextView settings_forecast;

   //Activity Components
   private Context context;
   private SharedPreferences userPrefs;
   private SyncService caller = SyncService.getInstance();
   private final WorkFlow flowLink = WorkFlow.getInstance();
   private Button nextItem, canvas_btn, course_btn, settings_btn;
   private TextView forecast;
   Intent launchMenu;


   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      context = this;
      userPrefs = this.getApplicationContext().getSharedPreferences("userPrefs", MODE_PRIVATE);
      launchMenu = new Intent(this,SharedMenu.class);
      setContentView(R.layout.activity_student_menu);

      nextItem = findViewById(R.id.next_item);
      forecast = findViewById(R.id.forecast);
      canvas_btn = findViewById(R.id.canvas_btn);
      course_btn = findViewById(R.id.course_btn);
      settings_btn = findViewById(R.id.settings_btn);
      setDisplay();
      setMenuOptions();

      nextItem.setOnClickListener(getMenuLauncher(ITEM_MENU));
      canvas_btn.setOnClickListener(getMenuLauncher(LMS_MENU));
      course_btn.setOnClickListener(getMenuLauncher(COURSE_MENU));
      settings_btn.setOnClickListener(getMenuLauncher(SETTINGS_MENU));

   }

   private View.OnClickListener getMenuLauncher(Integer menuId) {
      String menuTitle;
      TextView[] options;
      switch(menuId) {
         case ITEM_MENU:
            menuTitle = nextItem.getText().toString();
            options = new TextView[]{item_list, item_select, item_complete};
            break;
         case LMS_MENU:
            menuTitle = getString(R.string.LMS_menuTitle);
            options = new TextView[]{lms_canvasLogin};
            break;
         case COURSE_MENU:
            menuTitle = getString(R.string.Course_menuTitle);
            options = buildCourseMenu();
            break;
         case SETTINGS_MENU:
            menuTitle = getString(R.string.Settings_menuTitle);
            options = new TextView[]{settings_forecast};
            break;
         default:
            menuTitle = getString(R.string.default_menuTitle);
            options = new TextView[]{};
      }
      return view -> {
         SharedMenu.setFields(menuTitle, options);
         context.startActivity(launchMenu);
      };
   }

   /**
    * Sets the nextItem and forecast displays based on the underlying Workflow
    */
   private void setDisplay() {
      if (flowLink.hasItems()) {
         nextItem.setText(flowLink.getFirst().toString());
         int forecastThreshold = userPrefs.getInt("forecastThreshold",1);
         int forecastItems = flowLink.getForecast(forecastThreshold);
         String[] prefixes = getString(R.string.forecast_prefix).split("%");
         String prefix = prefixes[0] + forecastItems + prefixes[1];
         String suffix;
         if (forecastThreshold > 1) {
            String[] suffixes = getString(R.string.forecast_suffix2).split("%");
            suffix = suffixes[0] + forecastThreshold + suffixes[1];
         } else suffix = getString(R.string.forecast_suffix1);
         String newForecast = prefix + suffix;
         forecast.setText(newForecast);
      } else {
         nextItem.setText(R.string.default_next);
         forecast.setText(R.string.default_forecast);
      }
   }

   /**
    * Helper method for getMenuLaucher, programmatically builds options based on the course list.
    * @return a Textview Array for the course options menu.
    */
   private TextView[] buildCourseMenu() {
      ArrayList<NewCourse> courses = new ArrayList<>(flowLink.getCourseList());
      if(courses.isEmpty()) return new TextView[]{course_default, course_add};
      else {
         ArrayList<TextView> courseOptions = new ArrayList<>();
         courses.forEach(course -> {
            TextView view = new TextView(context);
            view.setText(course.toString());
            view.setOnClickListener(click -> {
               AlertDialog.Builder confirmUnenroll = new AlertDialog.Builder(click.getContext());
               confirmUnenroll.setTitle(R.string.course_confirm_unenroll);
               String message = getString(R.string.course_unenroll_prefix) + course + "?";
               confirmUnenroll.setMessage(message);
               confirmUnenroll.setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                  caller.dropCourse(courses.get(i).getData()[1]);
               });
               confirmUnenroll.setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {
               });
               AlertDialog window = confirmUnenroll.create();
               window.show();
            });
            courseOptions.add(view);
         });
         courseOptions.add(course_add);
         int len = courseOptions.size();
         TextView[] result = new TextView[len];
         for (int i = 0; i < len; i++) result[i] = courseOptions.get(i);
         return result;
      }
   }

   /**
    * Sets the text fields and assigns listeners to menu options components
    */
   private void setMenuOptions() {
      //subMenu1 Items Menu
      item_list = new TextView(context);
      item_list.setText(R.string.item_list);
      item_list.setOnClickListener(view -> {
         context.startActivity(new Intent(context, ItemsList.class));
         //any special settings for the activity here
      });

      item_select = new TextView(context);
      item_select.setText(R.string.item_select);
      // this feature is cut

      item_complete = new TextView(context);
      item_complete.setText(R.string.item_complete);
      item_complete.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            //call Caller to launch Complete
            //call WorkFlow.remove
            //setDisplay();
         }
      });


      //subMenu2 Course Menu
      course_default = new TextView(context);
      course_default.setText(R.string.course_default);

      course_add = new TextView(context);
      course_add.setText(R.string.course_add);
      course_add.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            //launch addacourse dialogue
         }
      });

      //subMenu2 LMS Menu
      lms_canvasLogin = new TextView(context);
      lms_canvasLogin.setText(R.string.lms_canvasLogin);
      lms_canvasLogin.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            //launch canvas login
         }
      });

      //subMenu4 Settings Menu
      settings_forecast = new TextView(context);
      int forecastThreshold = userPrefs.getInt("forecastThreshold",1);
      String forecastSetting = getString(R.string.settings_forecast) + forecastThreshold;
      settings_forecast.setText(forecastSetting);
      settings_forecast.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            //launch input dialogue
         }
      });

   }

}