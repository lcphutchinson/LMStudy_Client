package com.LMStudy.app.student;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.LMStudy.app.R;

/**
 * Primary UI for Student Users (previously "Main Activity")
 */
public class StudentHome extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_student_home);
   }
}