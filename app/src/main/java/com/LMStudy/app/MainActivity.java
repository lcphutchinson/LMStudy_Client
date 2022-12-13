package com.LMStudy.app;

import android.app.AlertDialog;
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
 * Launch activity--handles field initialization and settings retrieval and handles login/signup operations.
 * @author: Yulie Ying, Larson Pushard Hutchinson
 */
public class MainActivity extends AppCompatActivity {

   /**
    * Static String containing the Shared Preferences file name.
    */
   public static final String PREFS_FILE = "userPrefs";

   /**
    * Static String containing the user role flag, used for Shared Preferences access.
    */
   public static final String ROLE_FLAG = "role";

   /**
    * Static String containing the user token flag, used for Shared Preferences access.
    */
   public static final String TOKEN_FLAG = "userToken";

   /**
    * Static role string for Student Users. Stored in User Preferences
    */
   public static final String STUDENT_ROLE = "STUDENT";

   /**
    * Static role string for Teacher Users. Stored in User Preferences
    */
   public static final String TEACHER_ROLE = "TEACHER";

   /**
    * Singleton instance of the SyncService, used for initial pull calls
    */
   private final SyncService caller = SyncService.getInstance();

   /**
    * Singleton instance of the WorkFlow structure, used for initialization.
    */
   private final WorkFlow flowLink = WorkFlow.getInstance();

   /**
    * UserPreferences object for accessing saved key-value data, such as saved user role.
    */
   private SharedPreferences userPrefs;

   /**
    * Intent container for prepping initial launch menu.
    */
   private Intent launchTarget;

   /**
    * OnCreate for the launch activity. Populates and structures the login screen.
    * @param savedInstanceState
    */
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      userPrefs = this.getApplicationContext().getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
      caller.setPreferences(userPrefs);

      TextView username = findViewById(R.id.username_i);
      TextView password = findViewById(R.id.password_i);
      Button loginBtn = findViewById(R.id.login_btn);
      Button signupBtn = findViewById(R.id.signup_btn);
      Switch roleSwitch = findViewById(R.id.role_switch);

      loginBtn.setOnClickListener(view -> {
         String user = username.getText().toString();
         String pw = password.getText().toString();
         if (user.isEmpty() || pw.isEmpty()) Toast.makeText(
            this, R.string.missing_creds, Toast.LENGTH_SHORT).show();
         else {
            Boolean login = caller.login(user, pw);
            if (login == null) Toast.makeText(
               this, R.string.connection_err, Toast.LENGTH_SHORT).show();
            else if (login) {
               if (roleSwitch.isChecked()) userPrefs.edit().putString(ROLE_FLAG,TEACHER_ROLE).commit();
               else userPrefs.edit().putString(ROLE_FLAG, STUDENT_ROLE).commit();
               Toast.makeText(this, R.string.login_ok, Toast.LENGTH_SHORT).show();
               launch();
            } else Toast.makeText(this, R.string.login_bad, Toast.LENGTH_SHORT).show();
         }
      });

      signupBtn.setOnClickListener(view -> {
         String user = username.getText().toString();
         String pw = password.getText().toString();
         if (user.isEmpty() || pw.isEmpty()) Toast.makeText(
            this, R.string.missing_creds, Toast.LENGTH_SHORT).show();
         else {
            Boolean signup = caller.signup(user, pw);
            System.out.println(signup);
            if (signup == null) Toast.makeText(
               this, R.string.connection_err, Toast.LENGTH_SHORT).show();
            else if (signup) {
               if (roleSwitch.isChecked()) userPrefs.edit().putString(ROLE_FLAG,TEACHER_ROLE).commit();
               else userPrefs.edit().putString(ROLE_FLAG, STUDENT_ROLE).commit();
               launch();
            }
            else Toast.makeText(this, R.string.signup_bad, Toast.LENGTH_SHORT).show();
         }
      });

      if (userPrefs.contains(TOKEN_FLAG)) launch();
   }

   /**
    * Conducts final preparations for launching the Student or Teacher menus, including populating the
    * user's WorkFlow. Generates a Popup in the event of a failed server connections, facilitating app closure.
    */
   private void launch() {
      try {
         String userRole = userPrefs.getString(ROLE_FLAG, "");
         flowLink.populateCourses(caller.pullCourses());
         if (userRole.equals(STUDENT_ROLE)) {
            launchTarget = new Intent(this, StudentMenu.class);
            flowLink.addSelfCourse();
         }
         if (userRole.equals(TEACHER_ROLE)) launchTarget = new Intent(this, TeacherMenu.class);
         flowLink.populateItems(caller.pullItems());
         launchTarget.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         launchTarget.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         startActivity(launchTarget);
         finish();
      } catch ( NullPointerException e) {
         AlertDialog.Builder errorBuilder = new AlertDialog.Builder(this);
         errorBuilder.setTitle(R.string.errorTitle);
         errorBuilder.setMessage(R.string.errorMsg);
         errorBuilder.setOnDismissListener(dialogInterface -> finish());
         errorBuilder.setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
         finish();
         System.exit(0);
         });
         errorBuilder.create().show();
      }
   }
}
