package com.LMStudy.app;


import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.LMStudy.app.io.SyncService;
import com.LMStudy.app.student.StudentHome;
import org.conscrypt.Conscrypt;

import java.security.Security;


/**
 * Launch activity--handles field initialization and settings retrieval, and launches the Student or Teacher UI.
 */
public class MainActivity extends AppCompatActivity {
   //private SharedPreferences userSettings = this.getSharedPreferences("LMStudyPrefs", MODE_PRIVATE);
   private Intent launchTarget;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      //if(userSettings.getBoolean("isTeacher",false)) {
      //   launchTarget = new Intent(this, TeacherHome.class);
      //}

      //else if(userSettings.getBoolean("isStudent",false)) {
      //   launchTarget = new Intent(this, StudentHome.class);
      //}

      //else {
         //launch first-time setup dialogue
      //}

      // temporary: force StudentHome launch

      //SyncService syncService = SyncService.getInstance();
      //syncService.isAvailable();

      launchTarget = new Intent(this, StudentHome.class);
      this.startActivity(launchTarget);
   }


}