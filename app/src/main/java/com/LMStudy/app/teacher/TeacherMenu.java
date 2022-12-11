package com.LMStudy.app.teacher;

import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.LMStudy.app.CanvasConnect;
import com.LMStudy.app.MainActivity;
import com.LMStudy.app.R;
import com.LMStudy.app.SharedMenu;
import com.LMStudy.app.io.SyncService;
import com.LMStudy.app.structures.NewCourse;
import com.LMStudy.app.structures.WorkFlow;

import java.util.ArrayList;

/**
 * Main Menu controller for the Teacher User. Prepares various submenus and
 * displays a list of all administered courses for this user.
 * @author: Larson Pushard Hutchinson, Yulie Ying
 */
public class TeacherMenu extends AppCompatActivity {

   /**
    * Static String flag used for the Course Extra label
    */
   private static final String COURSE_FLAG = "course";

   /**
    * Static String flag for the clipboard service, used in copy operations.
    */
   private static final String CLIP_LABEL = "lmstudyClip";

   /**
    * Static Switch Case identifier for the LMS Menu, used in menu population.
    */
   private static final int LMS_MENU = 1;

   /**
    * Static Switch Case identifier for the Course Menu, used in menu population.
    */
   private static final int COURSE_MENU = 2;

   /**
    * Static Switch Case identifier for the Settings Menu, used in menu population.
    */
   private static final int SETTINGS_MENU = 3;

   /**
    * ArrayList for maintaining and displaying registered courses for this user.
    */
   private ArrayList<NewCourse> courses;

   /**
    * String container for storing saved input values between operations.
    */
   private String savedInput;

   //subMenu1 LMS Menu
   private TextView lms_canvasLogin;

   //subMenu2 Course Menu
   private TextView course_add, course_join;

   //subMenu3 Settings Menu
   private TextView settings_logout;

   //Activity Components
   /**
    * Container field for passing Context information between methods.
    */
   private Context context;

   /**
    * Reference field for the LMStudy SharedPreferences file.
    */
   private SharedPreferences userPrefs;

   /**
    * Clipboard Manager for handling clipboard operations.
    */
   private ClipboardManager clipboard;

   /**
    * Singleton instance for the SyncService. Used for launching server calls.
    */
   private SyncService caller = SyncService.getInstance();

   /**
    * Singleton instance for the WorkFlow data structure. Used for data operations.
    */
   private WorkFlow flowLink = WorkFlow.getInstance();
   private ListView courseList;
   private Button canvas_btn, course_btn, settings_btn;

   /**
    * Container field for building and launching submenus and activities.
    */
   private Intent launchMenu;

   /**
    * OnCreate for the Teacher User Menu. Prepares and initializes submenu triggers and populates the display.
    * @param savedInstanceState
    */
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      context = this;
      userPrefs = this.getApplicationContext().getSharedPreferences(MainActivity.PREFS_FILE, MODE_PRIVATE);
      launchMenu = new Intent(this, SharedMenu.class);
      clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
      setContentView(R.layout.activity_teacher_menu);

      courseList = findViewById(R.id.course_list);
      canvas_btn = findViewById(R.id.canvas_btn);
      course_btn = findViewById(R.id.course_btn);
      settings_btn = findViewById(R.id.settings_btn);
      setMenuOptions();
      setDisplay();

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
         case LMS_MENU:
            menuTitle = getString(R.string.LMS_menuTitle);
            options = new TextView[]{lms_canvasLogin};
            break;
         case COURSE_MENU:
            menuTitle = getString(R.string.Course_menuTitle);
            options = new TextView[]{course_add, course_join};
            break;
         case SETTINGS_MENU:
            menuTitle = getString(R.string.Settings_menuTitle);
            options = new TextView[]{settings_logout};
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
    * Programmatically prepares a SharedMenu instance to be launched when the User selects the given course.
    * @param course a Course object corresponding to an item in the menu display.
    */
   private void focusCourse (NewCourse course) {
      String menuTitle = course.toString();

      TextView show_course = new TextView(context);
      show_course.setText(R.string.course_show);
      show_course.setOnClickListener(view -> {
         Intent launchTeacherList = new Intent(context, TeacherAssignmentHome.class);
         launchTeacherList.putExtra(COURSE_FLAG, course.getCId());
         startActivity(launchTeacherList);
      });

      TextView show_id = new TextView(context);
      show_id.setText(R.string.course_show_id);
      show_id.setOnClickListener(view -> {
         AlertDialog.Builder idDisplay = new AlertDialog.Builder(SharedMenu.getContext());
         idDisplay.setTitle(R.string.course_show_id);
         idDisplay.setMessage(course.getData()[1]);
         idDisplay.setPositiveButton(android.R.string.copy, (dialogInterface, i) -> {
            ClipData clip = ClipData.newPlainText(CLIP_LABEL, course.getData()[1]);
            clipboard.setPrimaryClip(clip);
         });
         idDisplay.setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {});
         idDisplay.create().show();
      });

      TextView show_pw = new TextView(context);
      show_pw.setText(R.string.course_show_pw);
      show_pw.setOnClickListener(click -> {
         AlertDialog.Builder pwDisplay = new AlertDialog.Builder(SharedMenu.getContext());
         pwDisplay.setTitle(R.string.course_show_pw);
         pwDisplay.setMessage(course.getData()[2]);
         pwDisplay.setPositiveButton(android.R.string.copy, (dialogInterface, i) -> {
            ClipData clip = ClipData.newPlainText(CLIP_LABEL, course.getData()[2]);
            clipboard.setPrimaryClip(clip);
         });
         pwDisplay.setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {});
         pwDisplay.create().show();
      });

      TextView drop_course = new TextView(context);
      drop_course.setText(R.string.course_drop);
      drop_course.setOnClickListener(view -> {
         if (caller.dropCourse(course.getData()[1])) {
            flowLink.populateCourses(caller.pullCourses());
            restart();
         }
      });

      TextView[] options = new TextView[]{ show_course, show_id, show_pw, drop_course };
        SharedMenu.setFields(menuTitle, options);
        startActivity(launchMenu);
   }

   /**
    * Populates the course list display based on the underlying WorkFlow
    */
   private void setDisplay() {
      courses = new ArrayList<>(flowLink.getCourseList());
      String[] names;
      if(courses.isEmpty()) names = new String[] { getString(R.string.course_default) };
      else {
         int len = courses.size();
         names = new String[len];
         for(int i=0;i<len;i++) names[i] = courses.get(i).toString();
      }
      ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(
         this, android.R.layout.select_dialog_item, names);
      courseList.setAdapter(listAdapter);
      courseList.setOnItemClickListener((adapterView, view, i, l) -> focusCourse(courses.get(i)));
   }

   /**
    * Sets the text fields and assigns listeners to menu options components
    */
   private void setMenuOptions() {

      //subMenu1 LMS Menu
      lms_canvasLogin = new TextView(context);
      lms_canvasLogin.setText(R.string.lms_canvasLogin);
      lms_canvasLogin.setOnClickListener(view -> {
         startActivity(new Intent(context, CanvasConnect.class));
      });

      //subMenu2 Course Menu
      course_add = new TextView(context);
      course_add.setText(R.string.course_open);
      course_add.setOnClickListener(view -> {
         AlertDialog.Builder builder = new AlertDialog.Builder(SharedMenu.getContext());
         builder.setTitle(R.string.open_prompt);
         final View singleInputWindow = getLayoutInflater().inflate(R.layout.single_input_dialogue, null);
         builder.setView(singleInputWindow);
         builder.setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
         EditText inputField = singleInputWindow.findViewById(R.id.single_input);
         savedInput = inputField.getText().toString();
         if(!savedInput.isEmpty()) {
            if(caller.open(savedInput)) {
               flowLink.populateCourses(caller.pullCourses());
               restart();
               }
            else Toast.makeText(
               view.getContext(), R.string.add_err, Toast.LENGTH_SHORT);
            }
         });
         builder.create().show();
      });

      course_join = new TextView(context);
      course_join.setText(R.string.course_add);
      course_join.setOnClickListener(view -> {
         AlertDialog.Builder builder = new AlertDialog.Builder(SharedMenu.getContext());
         builder.setTitle(R.string.join_prompt);
         final View singleInputWindow = getLayoutInflater().inflate(R.layout.single_input_dialogue, null);
         builder.setView(singleInputWindow);
         builder.setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
            EditText inputfield = singleInputWindow.findViewById(R.id.single_input);
            savedInput = inputfield.getText().toString();
            AlertDialog.Builder subBuilder = new AlertDialog.Builder(SharedMenu.getContext());
            subBuilder.setTitle(R.string.admin_prompt);
            final View innerInputWindow = getLayoutInflater().inflate(R.layout.single_input_dialogue, null);
            subBuilder.setView(innerInputWindow);
            subBuilder.setPositiveButton(android.R.string.ok, (dialogInterface1, i1) -> {
               EditText innerField = innerInputWindow.findViewById(R.id.single_input);
               String content = innerField.getText().toString();
               if(!content.isEmpty()) if (caller.join(savedInput, content)) {
                  flowLink.populateCourses(caller.pullCourses());
                  restart();
               } else Toast.makeText(
                  SharedMenu.getContext(), R.string.join_err, Toast.LENGTH_SHORT).show();
            });
            subBuilder.create().show();

         });
         builder.create().show();
      });

      //subMenu3 Settings Menu
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
      Intent restart = new Intent (this, TeacherMenu.class);
      restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      restart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(restart);
      finish();
   }
}

