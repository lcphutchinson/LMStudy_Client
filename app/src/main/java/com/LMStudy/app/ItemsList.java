package com.LMStudy.app;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.LMStudy.app.io.SyncService;
import com.LMStudy.app.structures.WorkFlow;

public class ItemsList extends AppCompatActivity {

   private static String title;
   private static TextView[] options;

   private SyncService caller = SyncService.getInstance();
   private WorkFlow flowLink = WorkFlow.getInstance();

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.shared_list);

   }
}
