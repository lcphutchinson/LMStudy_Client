package com.LMStudy.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.LMStudy.app.io.SyncService;
import com.LMStudy.app.structures.WorkQueue;
import com.LMStudy.app.student.StudentHome;
import com.LMStudy.app.teacher.TeacherHome;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.LMStudy.app.student.StudentHome;


/**
 * Launch activity--handles field initialization and settings retrieval, and launches the Student or Teacher UI.
 */
public class MainActivity extends AppCompatActivity {
   private final SyncService caller = SyncService.getInstance();
   private Intent launchTarget;


   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      //SyncService caller = SyncService.getInstance();
      //caller.isAvailable();

      // check for stored settings


      // if stored settings exist, load them

      // launch StudentHome or TeacherHome, depending on user settings

      // or launch Setup dialogue, where settings can be input

      TextView username = findViewById(R.id.username_i);
      TextView password = findViewById(R.id.password_i);
      Button loginBtn = (Button) findViewById(R.id.login_btn);

      loginBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            // arbitrary login info: username = "username1" and password = "password1"
            if (username.getText().toString().equals("username1") && password.getText().toString().equals("password1")) {
               // if LMStudy id -> database linked to canvas lms api id -> role = student, go to user's student home
               Intent toStudentHome = new Intent(view.getContext(), StudentHome.class);
               setContentView(R.layout.activity_student_home);
               Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
            }
            else if (username.getText().toString().equals("")){
               Toast.makeText(MainActivity.this, "Input a username", Toast.LENGTH_SHORT).show();
            }
            else if (password.getText().toString().equals("")) {
               Toast.makeText(MainActivity.this, "Input a password", Toast.LENGTH_SHORT).show();
            }
            else {
               Toast.makeText(MainActivity.this, "Username and/or password incorrect", Toast.LENGTH_SHORT).show();
            }
         }
      });

         //temporary: force StudentHome launch
         launchTarget = new Intent(this, StudentHome.class);
         this.startActivity(launchTarget);

      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }


}