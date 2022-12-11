package com.LMStudy.app;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

/**
 * Modular Menu activity pre-populated with a Title and list of TextViews. Displays as a DialogAlert
 */
public class SharedMenu extends AppCompatActivity {

   /**
    *
    */
   private static final Stack<String> TITLE = new Stack<>();
   private static final Stack<TextView[]> OPTIONS = new Stack<>();
   private static Context context;

   private ListView menuOptions;
   private ArrayAdapter<TextView> menuAdapter;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.shared_menu);
      context = this;
      this.setTitle(TITLE.peek());
      TextView[] theseOptions = OPTIONS.peek();

      menuOptions = findViewById(R.id.menu_options);

      menuAdapter = new ArrayAdapter<TextView>(this, android.R.layout.select_dialog_item, theseOptions){
         @Override
         public View getView(int position, View convertView, ViewGroup parent){
            TextView option = theseOptions[position];
            option.setLayoutParams(new ListView.LayoutParams(
               ListView.LayoutParams.WRAP_CONTENT,
               ListView.LayoutParams.WRAP_CONTENT));
            option.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            option.setPadding(0,0,0,10);
            return option;
         }
      };
      menuOptions.setAdapter(menuAdapter);
   }

   public static Context getContext() { return context; }

   public static void setFields(String inTitle, TextView[] inOptions) {
      TITLE.push(inTitle);
      OPTIONS.push(inOptions);
   }

   @Override
   protected void onDestroy() {
      super.onDestroy();
      TITLE.pop();
      OPTIONS.pop();
   }
}
