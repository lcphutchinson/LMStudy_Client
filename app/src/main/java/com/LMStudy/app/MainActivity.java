package com.LMStudy.app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


/**
 * Launch activity--handles field initialization and settings retrieval, and launches the Student or Teacher UI.
 */
public class MainActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      // check for stored settings


      // if stored settings exist, load them

      // launch StudentHome or TeacherHome, depending on user settings

      // or launch Setup dialogue, where settings can be input

   }


}