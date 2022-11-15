package com.LMStudy.app.teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.LMStudy.app.AccountActivity;
import com.LMStudy.app.R;
import com.LMStudy.app.structures.Assignment;
import com.LMStudy.app.structures.Course;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

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
   private TextView newAssignmentName;
   private Spinner newAssignmentTypeSpinner, newAssignmentCourseSpinner, newAssignmentDueDateMonthSpinner,
           newAssignmentDueDateDaySpinner, newAssignmentDueDateYearSpinner;

   ArrayList<Assignment> assignmentArrayList;

   private List<String> testListSections = new ArrayList<String>();

   public Course arbitraryCourseForTesting = new Course("Test Course", "01:198:999", "Test Department", testListSections);

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
            View popupView = inflater.inflate(R.layout.assignment_popup, null);

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

            newAssignmentName = popupView.findViewById(R.id.newAssignmentName_input);
            TextView newAssignmentType = popupView.findViewById(R.id.newAssignmentType_input);
            TextView newAssignmentCourseInfo = popupView.findViewById(R.id.newAssignmentCourseInfo_input);
            TextView newAssignmentDueDate = popupView.findViewById(R.id.newDueDate_input);
            confirmAssignmentBtn = popupView.findViewById(R.id.confirm_assignment_Btn);

            /* SPINNER THINGS
            newAssignmentName = popupView.findViewById(R.id.r_newAssignmentName_input);
            newAssignmentTypeSpinner = popupView.findViewById(R.id.assignment_type_spinner);
            newAssignmentCourseSpinner = popupView.findViewById(R.id.course_spinner);
            newAssignmentDueDateMonthSpinner = popupView.findViewById(R.id.month_spinner);
            newAssignmentDueDateDaySpinner = popupView.findViewById(R.id.day_spinner);
            newAssignmentDueDateYearSpinner = popupView.findViewById(R.id.year_spinner);
            confirmAssignmentBtn = popupView.findViewById(R.id.r_confirm_assignment_Btn);

            for (Course c : courseList) {
               courseNameList.add(c.getCourseName());
            }

            ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.assignment_types, android.R.layout.simple_spinner_item);
            ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, courseNameList);
            ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(this, R.array.assignment_types, android.R.layout.simple_spinner_item);
            ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this, R.array.assignment_types, android.R.layout.simple_spinner_item);
            ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this, R.array.assignment_types, android.R.layout.simple_spinner_item);

            typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            */

            confirmAssignmentBtn.setOnClickListener(view1 -> {
               /*
               String assignmentName = newAssignmentName.getText().toString();
               String assignmentType = newAssignmentTypeSpinner.getSelectedItem().toString();
               String courseInfo = newAssignmentCourseSpinner.getSelectedItem().toString();
               String dueDate = newAssignmentDueDateMonthSpinner.getSelectedItem().toString() +
                       newAssignmentDueDateDaySpinner.getSelectedItem().toString() +
                       newAssignmentDueDateYearSpinner.getSelectedItem().toString();*/

               String assignmentName = newAssignmentName.getText().toString();
               String assignmentType = newAssignmentType.getText().toString();
               String courseInfo = newAssignmentCourseInfo.getText().toString();
               String dueDate = newAssignmentDueDate.getText().toString();

               //            String[] partitionDate = dueDate.split("/");
               //            LocalDateTime ldtDueDate = LocalDateTime.of(Integer.parseInt(partitionDate[2]),
               //                    Integer.parseInt(partitionDate[0]), Integer.parseInt(partitionDate[1]), 23, 59);

               Assignment newAssignment = new Assignment(courseInfo, assignmentName, assignmentType, dueDate);

               int courseIndex = courseList.indexOf(newAssignmentCourseSpinner.getSelectedItem().toString());
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
/*
   private Assignment generateItem(Assignment a) {
      Assignment item = new Assignment(
              a.getCourseInfo(),
              a.getAssignmentName(),
              a.getAssignmentType(),
              a.getDueDate()
      );
      return item;
   }*/
}