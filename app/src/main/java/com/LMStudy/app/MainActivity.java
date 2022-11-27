package com.LMStudy.app;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.LMStudy.app.io.SyncService;
import com.LMStudy.app.structures.NewCourse;
import com.LMStudy.app.structures.WorkFlow;
import com.LMStudy.app.structures.workitems.WorkItem;
import com.LMStudy.app.structures.WorkQueue;
import com.LMStudy.app.student.StudentMenu;
import com.LMStudy.app.teacher.TeacherMenu;

import java.util.ArrayList;


/**
 * Launch activity--handles field initialization and settings retrieval, and launches the Student or Teacher UI.2
 */
public class MainActivity extends AppCompatActivity {
   private static final String DEBUGROLE = "STUDENT";
   private final SyncService caller = SyncService.getInstance();
   private final WorkQueue queue = WorkQueue.getInstance();
   private Intent launchTarget;
   private Context context;

   @Override
   protected void onCreate(Bundle savedInstanceState) {

      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      context = this;

      //DEBUG Autolaunch
      ArrayList<WorkItem> exampleList = new ArrayList<>();
      ArrayList<NewCourse> exampleCourseList = new ArrayList<>();
      WorkFlow.getInstance().populateItems(exampleList);
      WorkFlow.getInstance().populateCourses(exampleCourseList);
      //launchTarget = new Intent(this, StudentMenu.class);
      launchTarget = new Intent(this, TeacherMenu.class);
      this.startActivity(launchTarget);
      // end autolaunch

      /*
      //DEBUG Login Automation
      SharedPreferences userPrefs = this.getApplicationContext().getSharedPreferences("userPrefs",MODE_PRIVATE);
      userPrefs.edit().putString("role", DEBUGROLE).commit();
      if(DEBUGROLE.equals("TEACHER")) {
         userPrefs.edit().putString("userToken", "token4").commit();
         launchTarget = new Intent(this, TeacherHome.class);
      } else if (DEBUGROLE.equals("STUDENT")){
         userPrefs.edit().putString("userToken", "token1").commit();
         launchTarget = new Intent(this, StudentHome.class);
      } else {
         userPrefs.edit().remove("userToken").commit(); //Manual Mode
      }
         caller.setPreferences(userPrefs);

      //If the user has a saved auth token for our server, skip login.
      if(userPrefs.contains("userToken")) {
         queue.populate(caller.pullAll());
      }

      // check for stored settings
      // if stored settings exist, load them
      // launch StudentHome or TeacherHome, depending on user settings
      // or launch Setup dialogue, where settings can be input

      TextView username = findViewById(R.id.username_i);
      TextView password = findViewById(R.id.password_i);
      Button loginBtn = findViewById(R.id.login_btn);

      loginBtn.setOnClickListener(view -> {
         String user = username.getText().toString();
         String pw = password.getText().toString();
         if (user.isEmpty() || pw.isEmpty()){
            Toast.makeText(MainActivity.this, "Username & Password Required", Toast.LENGTH_SHORT).show();
         }
         else {
            Boolean login = caller.login(user,pw);
            if(login == null) {
               Toast.makeText(MainActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
            }
            else if(login) {
               Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
               //force student login
               launchTarget = new Intent(this, StudentHome.class);
               this.startActivity(launchTarget);
            }
            else {
               Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
         }
      });
         this.startActivity(launchTarget);

         */


   }


}