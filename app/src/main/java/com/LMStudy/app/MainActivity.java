package com.LMStudy.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.LMStudy.app.io.SyncService;
import com.LMStudy.app.structures.WorkFlow;
import com.LMStudy.app.student.StudentHome;
import com.LMStudy.app.teacher.TeacherHome;


/**
 * Launch activity--handles field initialization and settings retrieval, and launches the Student or Teacher UI.
 */
public class MainActivity extends AppCompatActivity {
   //private SharedPreferences userSettings = this.getSharedPreferences("LMStudyPrefs", MODE_PRIVATE);
   private Intent launchTarget;

   TextView banner;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      banner = findViewById(R.id.banner);

      try {
         banner.setText(R.string.ret_user_settings);

         SharedPreferences userSettings = this.getSharedPreferences("LMStudy_Prefs", MODE_PRIVATE);

         if (userSettings.getBoolean("isTeacher", false)) {
            launchTarget = new Intent(this, TeacherHome.class);
         }

         else if (userSettings.getBoolean("isStudent", false)) {
            launchTarget = new Intent(this, StudentHome.class);
         }

         Thread.sleep(500);
         banner.setText(R.string.launching);

         SyncService caller = SyncService.getInstance();
         caller.isAvailable();

         //temporary: force StudentHome launch
         launchTarget = new Intent(this, StudentHome.class);
         this.startActivity(launchTarget);

      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }


}