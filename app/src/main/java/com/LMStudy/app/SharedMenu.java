package com.LMStudy.app;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

public class SharedMenu extends AppCompatActivity {

   private static Stack<String> title = new Stack<>();
   private static Stack<TextView[]> options = new Stack<>(); {
   };

   private ListView menuOptions;
   private ArrayAdapter<TextView> menuAdapter;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.shared_menu);
      this.setTitle(title.peek());
      TextView[] theseOptions = options.peek();
      System.out.println(options.peek()[0].getText().toString());

      menuOptions = findViewById(R.id.menu_options);

      menuAdapter = new ArrayAdapter<TextView>(this, android.R.layout.select_dialog_item, theseOptions){
         @Override
         public View getView(int position, View convertView, ViewGroup parent){
            System.out.println(position);
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

   public static void setFields(String inTitle, TextView[] inOptions) {
      title.push(inTitle);
      options.push(inOptions);
   }

   @Override
   protected void onDestroy() {
      super.onDestroy();
      title.pop();
      options.pop();
   }
}
