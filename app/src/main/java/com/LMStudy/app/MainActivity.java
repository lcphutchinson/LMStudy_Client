package com.LMStudy.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.LMStudy.app.io.SyncService;
import com.LMStudy.app.structures.WorkFlow;
import com.LMStudy.app.student.StudentMenu;
import com.LMStudy.app.teacher.TeacherMenu;


/**
 * Launch activity--handles field initialization and settings retrieval, and launches the Student or Teacher UI.2
 */
public class MainActivity extends AppCompatActivity {
   private final SyncService caller = SyncService.getInstance();
   private final WorkFlow flowLink = WorkFlow.getInstance();
   private SharedPreferences userPrefs;
   private Intent launchTarget;
   private Context context;

   private Switch roleSwitch;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      userPrefs = this.getApplicationContext().getSharedPreferences("userPrefs", MODE_PRIVATE);
      caller.setPreferences(userPrefs);
      context = this;

      if (userPrefs.contains("userToken")) launch();

      TextView username = findViewById(R.id.username_i);
      TextView password = findViewById(R.id.password_i);
      Button loginBtn = findViewById(R.id.login_btn);
      Button signupBtn = findViewById(R.id.signup_btn);
      roleSwitch = findViewById(R.id.role_switch);

      loginBtn.setOnClickListener(view -> {
         String user = username.getText().toString();
         String pw = password.getText().toString();
         if (user.isEmpty() || pw.isEmpty()) Toast.makeText(
            this, "Username & Password Required", Toast.LENGTH_SHORT).show();
         else {
            Boolean login = caller.login(user, pw);
            if (login == null) Toast.makeText(
               this, "Connection Error", Toast.LENGTH_SHORT).show();
            else if (login) {
               if (roleSwitch.isChecked()) userPrefs.edit().putString("role","TEACHER").commit();
               else userPrefs.edit().putString("role", "STUDENT").commit();
               Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
               launch();
            } else Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
         }
      });

      signupBtn.setOnClickListener(view -> {
         String user = username.getText().toString();
         String pw = password.getText().toString();
         if (user.isEmpty() || pw.isEmpty()) Toast.makeText(
            this, "Username & Password Required", Toast.LENGTH_SHORT).show();
         else {
            Boolean signup = caller.signup(user, pw);
            System.out.println(signup);
            if (signup == null) Toast.makeText(
               this, "Connection Error", Toast.LENGTH_SHORT).show();
            else if (signup) {
               if (roleSwitch.isChecked()) userPrefs.edit().putString("role","TEACHER").commit();
               else userPrefs.edit().putString("role", "STUDENT").commit();
               launch();
            }
            else Toast.makeText(this, "Username Already in Use", Toast.LENGTH_SHORT).show();
         }
      });
   }

   private void launch() {
      flowLink.populateCourses(caller.pullCourses());
      flowLink.populateItems(caller.pullItems());
      String userRole = userPrefs.getString("role", "");
      if(userRole.equals("STUDENT")) {
         launchTarget = new Intent(this, StudentMenu.class);
         flowLink.addSelfCourse();
      }
      if(userRole.equals("TEACHER")) launchTarget = new Intent( this, TeacherMenu.class);
      launchTarget.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      launchTarget.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(launchTarget);
      finish();
   }
}
