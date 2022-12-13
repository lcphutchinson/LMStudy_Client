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
    * Static Stack for managing menu titles--allows the menu to be presented in layers
    */
   private static final Stack<String> TITLE = new Stack<>();

   /**
    * Static Stack for managing menu option sets--allows the menu to be presented in layers
    */
   private static final Stack<TextView[]> OPTIONS = new Stack<>();

   /**
    * Container field for passing context to methods and activities.
    */
   private static Context context;

   private ListView menuOptions;
   private ArrayAdapter<TextView> menuAdapter;

   /**
    * OnCreate for the Shared Menu. Initializes and inflates the menu UI
    * @param savedInstanceState
    */
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

   /**
    * Fetches the currently stored static context, for launching over the existing menu.
    * @return
    */
   public static Context getContext() { return context; }

   /**
    * Puts a new title and options set on the stack, determining the next available menu layout.
    * @param inTitle a String for use as a menu title
    * @param inOptions a TextView Array for generating menu options
    */
   public static void setFields(String inTitle, TextView[] inOptions) {
      TITLE.push(inTitle);
      OPTIONS.push(inOptions);
   }

   /**
    * OnDestroy for the SharedMenu. Pops the most recent title and option set from the stack before closing.
    */
   @Override
   protected void onDestroy() {
      super.onDestroy();
      TITLE.pop();
      OPTIONS.pop();
   }
}
