package com.LMStudy.app.teacher;

import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.LMStudy.app.ItemsList;
import com.LMStudy.app.R;
import com.LMStudy.app.SharedMenu;
import com.LMStudy.app.io.SyncService;
import com.LMStudy.app.structures.NewCourse;
import com.LMStudy.app.structures.WorkFlow;

import java.util.ArrayList;

public class TeacherMenu extends AppCompatActivity {
   private static final int LMS_MENU = 1;
   private static final int COURSE_MENU = 2;
   private static final int SETTINGS_MENU = 3;

   //subMenu1 LMS Menu
   private TextView lms_canvasLogin;

   //subMenu2 Course Menu
   private TextView course_add, course_join;

   //subMenu3 Settings Menu


   //Activity Components
   private Context context;
   private SharedPreferences userPrefs;
   private ClipboardManager clipboard;
   private SyncService caller = SyncService.getInstance();
   private WorkFlow flowLink = WorkFlow.getInstance();
   private ListView courseList;
   private Button canvas_btn, course_btn, settings_btn;
   Intent launchMenu;

   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      context = this;
      userPrefs = this.getApplicationContext().getSharedPreferences("userPrefs", MODE_PRIVATE);
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
            options = new TextView[]{};
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

   private View.OnClickListener getCourseListener(NewCourse course) {
      String menuTitle = course.toString();
      TextView show_course = new TextView(context);
      show_course.setText(R.string.course_show);
      show_course.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            //prep and launch the shared list activity.
         }
      });

      TextView show_id = new TextView(context);
      show_id.setText(R.string.course_show_id);
      show_id.setOnClickListener(click -> {
         AlertDialog.Builder idDisplay = new AlertDialog.Builder(click.getContext());
         idDisplay.setTitle(R.string.course_show_id);
         idDisplay.setMessage(course.getData()[1]);
         idDisplay.setPositiveButton(android.R.string.copy, (dialogInterface, i) -> {
            ClipData clip = ClipData.newPlainText("id", course.getData()[1]);
         });
         idDisplay.setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {});
         AlertDialog window = idDisplay.create();
         window.show();
      });

      TextView show_pw = new TextView(context);
      show_pw.setText(R.string.course_show_pw);
      show_pw.setOnClickListener(click -> {
         AlertDialog.Builder pwDisplay = new AlertDialog.Builder(click.getContext());
         pwDisplay.setTitle(R.string.course_show_pw);
         pwDisplay.setMessage(course.getData()[2]);
         pwDisplay.setPositiveButton(android.R.string.copy, (dialogInterface, i) -> {
            ClipData clip = ClipData.newPlainText("id", course.getData()[2]);
            clipboard.setPrimaryClip(clip);
         });
         pwDisplay.setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {});
         AlertDialog window = pwDisplay.create();
         window.show();
      });

      TextView drop_course = new TextView(context);
      drop_course.setText(R.string.course_drop);
      drop_course.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            //tell caller to send a drop command with this course's id
            //remove the course from local course list and reset display,
            //or notify the user that changes will be present when app is reset.
         }
      });
      TextView[] options = new TextView[]{show_id, show_pw, drop_course};
      return view -> {
        SharedMenu.setFields(menuTitle, options);
        context.startActivity(launchMenu);
      };
   }


   private void setDisplay() {
      ArrayList<NewCourse> courses = new ArrayList<>(flowLink.getCourseList());
      ArrayList<TextView> views = new ArrayList<>();
      if(courses.isEmpty()) {
         TextView emptyList = new TextView(context);
         emptyList.setText(R.string.course_default);
         views.add(emptyList);
      } else {
         courses.forEach(course -> {
            TextView view = new TextView(context);
            view.setText(view.toString());
            view.setOnClickListener(getCourseListener(course));
            views.add(view);
         });
      }
      int len = views.size();
      TextView[] courseItems = new TextView[len];
      for(int i = 0; i < len; i++) courseItems[i] = views.get(i);
      ArrayAdapter<TextView> listAdapter =
         new ArrayAdapter(this, android.R.layout.select_dialog_item, courseItems) {
         @Override
         public View getView(int position, View convertView, ViewGroup parent){
            TextView item = courseItems[position];
            item.setLayoutParams(new ListView.LayoutParams(
               ListView.LayoutParams.WRAP_CONTENT,
               ListView.LayoutParams.WRAP_CONTENT));
            item.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            item.setPadding(0,0,0,8);
            return item;
         }
      };
      courseList.setAdapter(listAdapter);
   }

   private void setMenuOptions() {

      //subMenu1 LMS Menu
      lms_canvasLogin = new TextView(context);
      lms_canvasLogin.setText(R.string.lms_canvasLogin);
      lms_canvasLogin.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            //launch canvas login
         }
      });

      //subMenu2 Course Menu
      course_add = new TextView(context);
      course_add.setText(R.string.course_open);
      course_add.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            // launch a single-input dialogue view for inputting a new course name
            // tell caller to Open a course with that name
            // add that course to the list.
         }
      });

      course_join = new TextView(context);
      course_join.setText(R.string.course_add);
      course_join.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            //launch addacourse dialogue
         }
      });

      //subMenu3 Settings Menu

   }
}
