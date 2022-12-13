package com.LMStudy.app.student;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.LMStudy.app.CanvasConnect;
import com.LMStudy.app.MainActivity;
import com.LMStudy.app.R;
import com.LMStudy.app.SharedMenu;
import com.LMStudy.app.io.CanvasCall;
import com.LMStudy.app.io.SyncService;
import com.LMStudy.app.structures.NewCourse;
import com.LMStudy.app.structures.WorkFlow;

import java.util.ArrayList;

/**
 * Main Menu controller for the Student User. Prepares various submenus and displays a
 * summary of the first item in the WorkFlow.
 * @author: Larson Pushard Hutchinson, Yulie Ying
 */
public class StudentMenu extends AppCompatActivity {

   /**
    * Static String containing the forecast threshold preferences flag.
    */
   public static final String FORECAST_FLAG = "forecastThreshold";

   /**
    * Static Switch Case marker for the Items Menu. Used in submenu population.
    */
   private static final int ITEM_MENU = 0;

   /**
    * Static Switch Case marker for the LMS_Menu. Used in submenu population.
    */
   private static final int LMS_MENU = 1;

   /**
    * Static Switch Case marker for the Course Menu. Used in submenu population.
    */
   private static final int COURSE_MENU = 2;

   /**
    * Static Switch Case marker for the Settings Menu. Used in submenu population.
    */
   private static final int SETTINGS_MENU = 3;

   /**
    * String container for variable Menu options text. Used in submenu population.
    */
   private String savedInput;

   //subMenu1 Items Menu
   private TextView item_list, item_log, item_complete;

   //subMenu2 LMS Menu
   private TextView lms_canvasLogin;

   //subMenu3 Course Menu
   private TextView course_default, course_add;

   //subMenu3 Settings Menu
   private TextView settings_forecast;
   private TextView settings_logout;

   /**
    * Reference field for passing context information between methods and submenus
    */
   private Context context;

   /**
    * Reference field for the User's SharedPreferences file.
    */
   private SharedPreferences userPrefs;

   /**
    * Singleton instance for the Sync Service
    */
   private SyncService caller = SyncService.getInstance();

   /**
    * Singleton instance for the WorkFlow Structure.
    */
   private final WorkFlow flowLink = WorkFlow.getInstance();
   private Button nextItem, canvas_btn, course_btn, settings_btn;
   private TextView forecast;

   /**
    * Container field for building and launching new menus.
    */
   Intent launchMenu;

   /**
    * OnCreate for the StudentMenu. Prepares the various subMenus and initializes the display.
    * @param savedInstanceState
    */
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      context = this;
      userPrefs = this.getApplicationContext().getSharedPreferences(MainActivity.PREFS_FILE, MODE_PRIVATE);
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

   /**
    * Builds an on-click listener for the submenu corresponding to the provided input flag.
    * @param menuId Input flag for designating menu path
    * @return a menu configuration packaged in an OnClickListener.
    */
   private View.OnClickListener getMenuLauncher(Integer menuId) {
      String menuTitle;
      TextView[] options;
      switch(menuId) {
         case ITEM_MENU:
            menuTitle = (flowLink).hasItems() ? flowLink.getFirst().getName() : getString(R.string.default_forecast);
            options = new TextView[]{item_list, item_log, item_complete};
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
            options = new TextView[]{settings_forecast, settings_logout};
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
         int forecastThreshold = userPrefs.getInt(FORECAST_FLAG,1);
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
      if(courses.size() < 2) return new TextView[]{course_default, course_add};
      else {
         ArrayList<TextView> courseOptions = new ArrayList<>();
         courses.forEach(course -> {
            if(!course.equals(NewCourse.SELF_ASSIGNED)) {
               TextView view = new TextView(context);
               view.setText(course.toString());
               view.setOnClickListener(click -> {
                  AlertDialog.Builder confirmUnenroll = new AlertDialog.Builder(SharedMenu.getContext());
                  confirmUnenroll.setTitle(R.string.course_confirm_unenroll);
                  String message = getString(R.string.course_unenroll_prefix) + course + "?";
                  confirmUnenroll.setMessage(message);
                  confirmUnenroll.setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                     caller.dropCourse(course.getData()[1]);
                     courses.remove(course);
                     restart();
                  });
                  confirmUnenroll.setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {
                  });
                  AlertDialog window = confirmUnenroll.create();
                  window.show();
               });
               courseOptions.add(view);
            }
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
         restart();
         context.startActivity(new Intent(context, StudentHome.class));
      });

      item_log = new TextView(context);
      item_log.setText(R.string.item_select);
      item_log.setOnClickListener(view -> {
         AlertDialog.Builder builder = new AlertDialog.Builder(SharedMenu.getContext());
         builder.setTitle(R.string.progress_prompt);
         final View singleInputWindow = getLayoutInflater().inflate(R.layout.single_input_dialogue, null);
         builder.setView(singleInputWindow);
         builder.setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
            EditText inputfield = singleInputWindow.findViewById(R.id.single_input);
            savedInput = inputfield.getText().toString();
            if(!savedInput.isEmpty()) {
               try {
                  int newProgress = Integer.parseInt(savedInput);
                  if (newProgress < 0 || newProgress > 100) {
                     Toast.makeText(
                        SharedMenu.getContext(), R.string.badprogress_prompt, Toast.LENGTH_SHORT).show();
                  } else {
                     caller.progress(flowLink.getFirst().getIID(), newProgress);
                     flowLink.populateCourses(caller.pullCourses());
                     flowLink.addSelfCourse();
                     flowLink.populateItems(caller.pullItems());
                     restart();
                  }
               } catch (NumberFormatException e) {
                  Toast.makeText(
                     SharedMenu.getContext(), R.string.badprogress_prompt, Toast.LENGTH_SHORT).show();
               }
            }
         });
         builder.create().show();
      });

      item_complete = new TextView(context);
      item_complete.setText(R.string.item_complete);
      item_complete.setOnClickListener(view -> {
         String firstId = flowLink.getFirst().getIID();
         if(caller.complete(firstId)) {
            flowLink.removeById(firstId);
            setDisplay();
            Toast.makeText(getBaseContext(), R.string.complete_success, Toast.LENGTH_SHORT).show();
         } else {
            Toast.makeText(getBaseContext(), R.string.complete_fail, Toast.LENGTH_SHORT).show();
         }
      });

      //subMenu2 LMS Menu
      lms_canvasLogin = new TextView(context);
      lms_canvasLogin.setText(R.string.lms_canvasLogin);
      lms_canvasLogin.setOnClickListener(view -> {
         if(CanvasCall.MY_TOKEN.isEmpty()) {
            Toast.makeText(this, R.string.canvas_err, Toast.LENGTH_SHORT).show();
         } else {
            startActivity(new Intent(context, CanvasConnect.class));
         }
      });

      //subMenu3 Course Menu
      course_default = new TextView(context);
      course_default.setText(R.string.course_default);

      course_add = new TextView(context);
      course_add.setText(R.string.course_add);
      course_add.setOnClickListener(view -> {
         AlertDialog.Builder builder = new AlertDialog.Builder(SharedMenu.getContext());
         builder.setTitle(R.string.join_prompt);
         final View singleInputWindow = getLayoutInflater().inflate(R.layout.single_input_dialogue, null);
         builder.setView(singleInputWindow);
         builder.setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
               EditText inputfield = singleInputWindow.findViewById(R.id.single_input);
               savedInput = inputfield.getText().toString();
               if(!savedInput.isEmpty()) {
                  caller.enroll(savedInput);
                  flowLink.populateCourses(caller.pullCourses());
                  flowLink.addSelfCourse();
                  flowLink.populateItems(caller.pullItems());
                  restart();
               } else Toast.makeText(
                  SharedMenu.getContext(), R.string.enroll_fail, Toast.LENGTH_SHORT).show();
            });
         builder.create().show();
      });

      //subMenu4 Settings Menu
      settings_forecast = new TextView(context);
      int forecastThreshold = userPrefs.getInt(FORECAST_FLAG,1);
      String forecastSetting = getString(R.string.settings_forecast) + forecastThreshold;
      settings_forecast.setText(forecastSetting);
      settings_forecast.setOnClickListener(view -> {
         AlertDialog.Builder builder = new AlertDialog.Builder(SharedMenu.getContext());
         builder.setTitle(R.string.forecast_prompt);
         final View singleInputWindow = getLayoutInflater().inflate(R.layout.single_input_dialogue, null);
         builder.setView(singleInputWindow);
         builder.setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
            EditText inputField = singleInputWindow.findViewById(R.id.single_input);
            savedInput = inputField.getText().toString();
            try {
               int newForecast = Integer.parseInt(savedInput);
               if(newForecast > 0){
                  userPrefs.edit().putInt(FORECAST_FLAG, newForecast).commit();
                  restart();
               } else {
                  Toast.makeText(
                     SharedMenu.getContext(), R.string.forecast_err, Toast.LENGTH_SHORT).show();
               }
            } catch (NumberFormatException e) {
               Toast.makeText(
                  SharedMenu.getContext(), R.string.forecast_err, Toast.LENGTH_SHORT).show();
            }
         });
         builder.create().show();
      });

      settings_logout = new TextView(context);
      settings_logout.setText(R.string.logout);
      settings_logout.setOnClickListener(view -> {
         userPrefs.edit().clear().apply();
         Intent logout = new Intent(getBaseContext().getPackageManager()
            .getLaunchIntentForPackage(getBaseContext().getPackageName()));
         logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         startActivity(logout);
         finish();
      });

   }

   /**
    * (Zealously) resets the activity, preventing back navigation.
    */
   private void restart() {
      Intent restart = new Intent (this, StudentMenu.class);
      restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      restart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(restart);
      finish();
   }
}