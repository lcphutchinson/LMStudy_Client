package com.LMStudy.app;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.LMStudy.app.io.SyncService;
import com.LMStudy.app.localdb.WorkDB;
import com.LMStudy.app.localdb.dbService;
import com.LMStudy.app.structures.WorkFlow;
import com.LMStudy.app.student.StudentHome;
import com.LMStudy.app.teacher.TeacherHome;
import org.conscrypt.Conscrypt;

import java.security.Security;


/**
 * Launch activity--handles field initialization and settings retrieval, and launches the Student or Teacher UI.
 */
public class MainActivity extends AppCompatActivity {
   //private SharedPreferences userSettings = this.getSharedPreferences("LMStudyPrefs", MODE_PRIVATE);
   private dbService loadService = new dbService();
   private Intent launchTarget;

   TextView banner;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      banner = findViewById(R.id.banner);

      try {
         WorkDB.getInstance(this);
         loadService.startLoad();

         Thread.sleep(500);
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

         WorkFlow.getInstance().loadDBItems(loadService.getWorkFlow());

         //temporary: force StudentHome launch
         launchTarget = new Intent(this, StudentHome.class);
         this.startActivity(launchTarget);

      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }


}