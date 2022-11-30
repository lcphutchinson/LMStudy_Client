package com.LMStudy.app.teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker;

import com.LMStudy.app.AccountActivity;
import com.LMStudy.app.R;
import com.LMStudy.app.structures.Assignment;
import com.LMStudy.app.structures.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

/**
 * Primary UI for Student Users (previously "Main Activity")
 */
public class TeacherHome extends AppCompatActivity {

   private Intent launchAssignmentTarget;
   private Bundle courseBundle = new Bundle();
   private Context context;

   private ArrayList<Course> courseList = new ArrayList<Course>();
   private ArrayList<String> courseNameList = new ArrayList<String>();

   private TeacherRecyclerAdapter teacherHomeAdapter;
   private RecyclerView rcCourseList;
   private ImageView profilePicture1;

   private TextView assignmentNameText, assignmentNameInfo, assignmentAssigneeText, assignmentAssigneeInfo,
           assignmentTypeText, assignmentTypeInfo, assignmentCourseText, assignmentCourseInfo,
           assignmentDueDateText, assignmentDueDateInfo, assignmentNotesText, assignmentNotesInfo;

   private Button addAssignmentBtn, confirmAssignmentBtn;
   private TextView newAssignmentName, dateView;
   private Spinner newAssignmentTypeSpinner, newAssignmentCourseSpinner, newAssignmentDueDateMonthSpinner,
           newAssignmentDueDateDaySpinner, newAssignmentDueDateYearSpinner;

   private DatePicker datePicker;
   private Calendar calendar;
   private int year, month, day;

   ArrayList<Assignment> assignmentArrayList;

   private List<String> testListSections = new ArrayList<String>();

   public Course arbitraryCourseForTesting = new Course("Test Course", "01:198:999", "Test Department", testListSections);

   private DatePickerDialog.OnDateSetListener myDateListener = new
      DatePickerDialog.OnDateSetListener() {
         @Override
         public void onDateSet(DatePicker arg0,
                              int arg1, int arg2, int arg3) {
           // TODO Auto-generated method stub
           // arg1 = year
           // arg2 = month
           // arg3 = day
           showDate(arg1, arg2+1, arg3);
         }
      };

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_teacher_home);
      context = this;

      //profilePicture = findViewById(R.id.profile_icon);
      rcCourseList = findViewById(R.id.t_course_list);
      //refreshButton = findViewById(R.id.t_refresh_btn);
      addAssignmentBtn = findViewById(R.id.t_addAssignment_Btn);

      courseList.add(arbitraryCourseForTesting);

      setDisplay();
      rcCourseList.setLayoutManager(new LinearLayoutManager(this));

      /**
       * Selecting a course item sends the teacher to the corresponding course activity screen
       * which only displays assignments and other information for that course.
       *
       * Passes COURSE NAME as parameter to access course specific assignment list.
       * Need to create a back button.
       */
      rcCourseList.addOnItemTouchListener(new AccountActivity.RecyclerItemClickListener(getApplicationContext(),
              rcCourseList, new AccountActivity.RecyclerItemClickListener.OnItemClickListener() {

         @Override
         public void onItemClick(View view, int position) {
            launchAssignmentTarget = new Intent(view.getContext(),TeacherAssignmentHome.class);
            launchAssignmentTarget.putExtra("Course List", courseList);
            launchAssignmentTarget.putExtra("Course", courseList.get(position).toString());
            launchAssignmentTarget.putExtra("Position", position);

            view.getContext().startActivity(launchAssignmentTarget);
         }

         @Override
         public void onItemLongClick(View view, int position) {
            // do whatever
         }
      }));

      /**
       * Adds assignment from the MAIN COURSE LIST ACTIVITY screen to the Course List which has
       * their own Assignment List (See course class). There is no need to set display when adding
       * assignment here since Course List activity only displays courses.
       *
       * The teacher can choose the course to add the assignment to from this activity.
       */
      addAssignmentBtn.setOnClickListener(view -> {

         if (courseList.size() == 0) {
            Toast.makeText(getBaseContext(), "ERROR: No enrolled course", Toast.LENGTH_SHORT).show();
         }
         else {
            // inflate the layout of the popup window
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(LAYOUT_INFLATER_SERVICE);
            // View popupView = inflater.inflate(R.layout.add_assignment_popup_revised, null); // This is the revised add popup
            View popupView = inflater.inflate(R.layout.add_assignment_popup_revised, null);

            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window token
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            // dismiss the popup window when touched
            popupView.setOnTouchListener((v, event) -> {
               popupWindow.dismiss();
               return true;
            });
/*
            newAssignmentName = popupView.findViewById(R.id.newAssignmentName_input);
            TextView newAssignmentType = popupView.findViewById(R.id.newAssignmentType_input);
            TextView newAssignmentCourseInfo = popupView.findViewById(R.id.newAssignmentCourseInfo_input);
            TextView newAssignmentDueDate = popupView.findViewById(R.id.newDueDate_input);
            confirmAssignmentBtn = popupView.findViewById(R.id.confirm_assignment_Btn);
*/

            // SPINNER THINGS
            newAssignmentName = popupView.findViewById(R.id.r_newAssignmentName_input);
            newAssignmentTypeSpinner = popupView.findViewById(R.id.assignment_type_spinner);
            newAssignmentCourseSpinner = popupView.findViewById(R.id.course_spinner);

            dateView = (TextView) popupView.findViewById(R.id.dateText);
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);

            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            showDate(year, month+1, day);

            confirmAssignmentBtn = popupView.findViewById(R.id.r_confirm_assignment_Btn);

            for (Course c : courseList) {
               courseNameList.add(c.getCourseName());
            }

            ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.assignment_types, android.R.layout.simple_spinner_item);
            ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, courseNameList);

            typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

            newAssignmentTypeSpinner.setAdapter(typeAdapter);
            newAssignmentCourseSpinner.setAdapter(courseAdapter);

            confirmAssignmentBtn.setOnClickListener(view1 -> {
               String assignmentName = newAssignmentName.getText().toString();
               String assignmentType = newAssignmentTypeSpinner.getSelectedItem().toString();
               String courseInfo = newAssignmentCourseSpinner.getSelectedItem().toString();
               String dueDate = dateView.getText().toString();

               Assignment newAssignment = new Assignment(courseInfo, assignmentName, assignmentType, dueDate);

               int courseIndex = courseNameList.indexOf(courseInfo);
               courseList.get(courseIndex).getAssignmentList().add(newAssignment);

               Toast.makeText(getBaseContext(), "Assignment successfully added", Toast.LENGTH_SHORT).show();
               popupWindow.dismiss();
            });
         }
      });
   }

   /**
    * Displays the courses on teacher screen.
    */
   private void setDisplay() {
      teacherHomeAdapter = new TeacherRecyclerAdapter(this, courseList);
      rcCourseList.setAdapter(teacherHomeAdapter);
   }

   /**
    * Displays due date
    * @param year due date year
    * @param month due date month
    * @param day due date day
    */
   private void showDate(int year, int month, int day) {
      dateView.setText(new StringBuilder().append(day).append("/")
              .append(month).append("/").append(year));
   }

   @SuppressWarnings("deprecation")
   public void setDate(View view) {
      showDialog(999);
      Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT).show();
   }

   @Override
   protected Dialog onCreateDialog(int id) {
      // TODO Auto-generated method stub
      if (id == 999) {
         return new DatePickerDialog(this,
                 myDateListener, year, month, day);
      }
      return null;
   }
}