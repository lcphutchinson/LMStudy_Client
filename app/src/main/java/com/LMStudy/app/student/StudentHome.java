package com.LMStudy.app.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.LMStudy.app.structures.NewCourse;
import com.LMStudy.app.structures.WorkFlow;
import com.LMStudy.app.structures.WorkQueue;
import com.LMStudy.app.structures.workitems.Exam;
import com.LMStudy.app.structures.workitems.Homework;
import com.LMStudy.app.structures.workitems.Project;
import com.LMStudy.app.structures.workitems.Quiz;
import com.LMStudy.app.structures.workitems.WorkItem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Primary UI for Student Users (previously "Main Activity")
 */
public class StudentHome extends AppCompatActivity {

   private final WorkQueue queueLink = WorkQueue.getInstance();
   private final WorkFlow flowLink = WorkFlow.getInstance();

   private List<Assignment> items;
   private List<WorkItem> itemsList;

   private TextView currentAssignment;

   private AccountActivity.RecyclerAdapter studentHomeAdapter;
   private RecyclerView rcSchedule;
   private ImageView profilePicture;
   private Button refreshButton, addAssignmentBtn, confirmAssignmentBtn;
   //private TextView newAssignmentName, newAssignmentType, newAssignmentCourseInfo, newAssignmentDueDate;

   private TextView assignmentNameText, assignmentNameInfo, assignmentAssigneeText, assignmentAssigneeInfo,
           assignmentTypeText, assignmentTypeInfo, assignmentCourseText, assignmentCourseInfo,
           assignmentDueDateText, assignmentDueDateInfo, assignmentPrioText, assignmentHoursText,
           assignmentHoursInfo;

   private TextView newAssignmentName, newAssignmentHour, dateView;
   private Spinner newAssignmentTypeSpinner, newAssignmentCourseSpinner, newAssignmentPrioritySpinner;

   private DatePicker datePicker;
   private Calendar calendar;
   private int year, month, day;

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

   private Button completeAssignmentBtn, removeAssignmentBtn;

   private TextView confirmRemovalText;
   private Button yesButton, noButton;

   ArrayList<NewCourse> enrolledCourseList = (ArrayList<NewCourse>) flowLink.getCourseList();
   ArrayList<String> enrolledCourseNameList = new ArrayList<String>();

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_student_home);

      profilePicture = findViewById(R.id.profile_icon);
      rcSchedule = findViewById(R.id.assignment_work_queue);
      refreshButton = findViewById(R.id.refresh_Btn);
      addAssignmentBtn = findViewById(R.id.addAssignment_Btn);
      currentAssignment = findViewById(R.id.current_assignment_text);

      /**** ARBITRARY ENROLLED TEST COURSE FOR STUDENT ****/
      NewCourse testCourse = new NewCourse("999999","Personal Time","pw");

      enrolledCourseList.add(testCourse);

      setDisplay();
      rcSchedule.setLayoutManager(new LinearLayoutManager(this));

      rcSchedule.addOnItemTouchListener(new AccountActivity.RecyclerItemClickListener(getApplicationContext(),
              rcSchedule, new AccountActivity.RecyclerItemClickListener.OnItemClickListener() {

         @Override
         public void onItemClick(View view, int position) {
            // inflate the layout of the popup window

            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.assignment_info_popup, null);

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

            assignmentNameText = popupView.findViewById(R.id.assignmentName);
            assignmentNameInfo = popupView.findViewById(R.id.assignmentName_txt);
            assignmentAssigneeText = popupView.findViewById(R.id.assigneeName);
            assignmentAssigneeInfo = popupView.findViewById(R.id.assigneeName_txt);
            assignmentTypeText = popupView.findViewById(R.id.assignmentType);
            assignmentTypeInfo = popupView.findViewById(R.id.assignmentType_txt);
            assignmentCourseText = popupView.findViewById(R.id.course_txt);
            assignmentCourseInfo = popupView.findViewById(R.id.courseName_txt);
            assignmentDueDateText = popupView.findViewById(R.id.dueDateInfo_title);
            assignmentDueDateInfo = popupView.findViewById(R.id.dueDateInfo_txt);
            assignmentPrioText = popupView.findViewById(R.id.priorityInfo);

            completeAssignmentBtn = popupView.findViewById(R.id.completeAssignment_btn);
            removeAssignmentBtn = popupView.findViewById(R.id.rmvAssignment_btn);

            assignmentNameInfo.setText(studentHomeAdapter.getItemName(position));
            assignmentTypeInfo.setText(studentHomeAdapter.getItemType(position));
            assignmentCourseInfo.setText(studentHomeAdapter.getItemCourse(position));
            assignmentDueDateInfo.setText(studentHomeAdapter.getItemDueDate(position));
            //assignmentPrioInfo.setText(studentHomeAdapter.getItemPriority(position));

            completeAssignmentBtn.setOnClickListener(view1 -> {
               String workType = studentHomeAdapter.getItemType(position);
               NewCourse courseSelection = new NewCourse(newAssignmentCourseSpinner.getSelectedItem().toString());
               WorkItem item;

               for (NewCourse c : enrolledCourseList) {
                  if (c.toString().equals(newAssignmentCourseSpinner.getSelectedItem().toString())) {
                     courseSelection = c;
                     break;
                  }
               }

               switch(workType) {
                  case "Exam":
                     item = new Exam(courseSelection, newAssignmentName.getText().toString(),
                             dateView.getText().toString(), 0,0);
                     break;
                  case "Project":
                     item = new Project(courseSelection, newAssignmentName.getText().toString(),
                             dateView.getText().toString(), 0,0);
                     break;
                  case "Quiz":
                     item = new Quiz(courseSelection, newAssignmentName.getText().toString(),
                             dateView.getText().toString(), 0,0);
                     break;
                  default: // Homework Case
                     item = new Homework(courseSelection, newAssignmentName.getText().toString(),
                             dateView.getText().toString(), 0,0);
                     break;
               }

               int removed = 0;
            // Find matching item in WorkItem list using equal
               for (WorkItem w : flowLink.getWorkItems()) {
                  if (w.equals(item)) {
                     flowLink.remove(w);
                     removed = 1;
                  }
               }

               if (removed == 1) {
                  setDisplay();
                  Toast.makeText(getBaseContext(), "Assignment completed", Toast.LENGTH_SHORT).show();
                  popupWindow.dismiss();
               }
               else {
                  Toast.makeText(getBaseContext(), "Error, assignment not completed.", Toast.LENGTH_SHORT).show();
                  popupWindow.dismiss();
               }
               // removal logic for "COMPLETED" and update recycler view
               // add assignment somewhere using triggers


            });

            removeAssignmentBtn.setOnClickListener(view1 -> {

               // inflate the layout of the popup window
               LayoutInflater inflater1 = (LayoutInflater)
                       getSystemService(LAYOUT_INFLATER_SERVICE);
               View popupView1 = inflater1.inflate(R.layout.remove_assignment_popup, null);

               // create the popup window
               int width1 = LinearLayout.LayoutParams.WRAP_CONTENT;
               int height1 = LinearLayout.LayoutParams.WRAP_CONTENT;
               boolean focusable1 = false; // lets taps outside the popup also dismiss it
               final PopupWindow popupWindow1 = new PopupWindow(popupView1, width1, height1, focusable1);

               // show the popup window
               // which view you pass in doesn't matter, it is only used for the window token
               popupWindow1.showAtLocation(view, Gravity.CENTER, 0, 0);

               // dismiss the popup window when touched
               popupView1.setOnTouchListener((v, event) -> {
                  popupWindow1.dismiss();
                  return true;
               });

               confirmRemovalText = popupView1.findViewById(R.id.confirmRemove_txt);
               yesButton = popupView1.findViewById(R.id.yes_btn);
               noButton = popupView1.findViewById(R.id.no_btn);

               yesButton.setOnClickListener(view3 -> {
                  //removal logic and update recycler view
                  //studentHomeAdapter.removeAt(position);

                  String workType = studentHomeAdapter.getItemType(position);
                  NewCourse courseSelection = new NewCourse(newAssignmentCourseSpinner.getSelectedItem().toString());
                  WorkItem item;

                  for (NewCourse c : enrolledCourseList) {
                     if (c.toString().equals(newAssignmentCourseSpinner.getSelectedItem().toString())) {
                        courseSelection = c;
                        break;
                     }
                  }

                  switch(workType) {
                     case "Exam":
                        item = new Exam(courseSelection, newAssignmentName.getText().toString(),
                                dateView.getText().toString(), 0,0);
                        break;
                     case "Project":
                        item = new Project(courseSelection, newAssignmentName.getText().toString(),
                                dateView.getText().toString(), 0,0);
                        break;
                     case "Quiz":
                        item = new Quiz(courseSelection, newAssignmentName.getText().toString(),
                                dateView.getText().toString(), 0,0);
                        break;
                     default: // Homework Case
                        item = new Homework(courseSelection, newAssignmentName.getText().toString(),
                                dateView.getText().toString(), 0,0);
                        break;
                  }

                  int removed = 0;
                  // Find matching item in WorkItem list using equal
                  for (WorkItem w : flowLink.getWorkItems()) {
                     if (w.equals(item)) {
                        flowLink.remove(w);
                        removed = 1;
                     }
                  }

                  if (removed == 1) {
                     setDisplay();
                     Toast.makeText(getBaseContext(), "Assignment completed", Toast.LENGTH_SHORT).show();
                     popupWindow1.dismiss();
                     popupWindow.dismiss();
                  }
                  else {
                     Toast.makeText(getBaseContext(), "Error, assignment not removed.", Toast.LENGTH_SHORT).show();
                     popupWindow1.dismiss();
                     popupWindow.dismiss();
                  }

//                  flowLink.remove(item);
//                  setDisplay();
//
//                  // add assignment somewhere using triggers
//
//                  Toast.makeText(getBaseContext(), "Assignment successfully removed", Toast.LENGTH_SHORT).show();
//                  popupWindow1.dismiss();
//                  popupWindow.dismiss();
               });

               noButton.setOnClickListener(view4 -> {
                  Toast.makeText(getBaseContext(), "Remove cancelled.", Toast.LENGTH_SHORT).show();
                  popupWindow1.dismiss();
               });
            });
         }

         @Override
         public void onItemLongClick(View view, int position) {
            // do whatever
         }
      }));

      /**
       * Refreshes the student home activity.
       * Resets everything so does not work exactly as intended.
       */
      refreshButton.setOnClickListener(view -> {
         finish();
         startActivity(getIntent());
      });

      addAssignmentBtn.setOnClickListener(view -> { // Adding an assignment only shows in the specific student's view

         // inflate the layout of the popup window
         LayoutInflater inflater = (LayoutInflater)
                 getSystemService(LAYOUT_INFLATER_SERVICE);
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
         newAssignmentType = popupView.findViewById(R.id.newAssignmentType_input);
         newAssignmentCourseInfo = popupView.findViewById(R.id.newAssignmentCourseInfo_input);
         newAssignmentDueDate = popupView.findViewById(R.id.newDueDate_input);
         confirmAssignmentBtn = popupView.findViewById(R.id.confirm_assignment_Btn);
*/
         // SPINNER THINGS
         newAssignmentName = popupView.findViewById(R.id.r_newAssignmentName_input);
         newAssignmentTypeSpinner = popupView.findViewById(R.id.assignment_type_spinner);
         newAssignmentCourseSpinner = popupView.findViewById(R.id.course_spinner);
         newAssignmentPrioritySpinner = popupView.findViewById(R.id.priority_spinner);
         newAssignmentHour = popupView.findViewById(R.id.hours_input);

         dateView = (TextView) popupView.findViewById(R.id.dateText);
         calendar = Calendar.getInstance();
         year = calendar.get(Calendar.YEAR);

         month = calendar.get(Calendar.MONTH);
         day = calendar.get(Calendar.DAY_OF_MONTH);
         showDate(year, month+1, day);

         confirmAssignmentBtn = popupView.findViewById(R.id.r_confirm_assignment_Btn);

         for (NewCourse c : enrolledCourseList) {
            enrolledCourseNameList.add(c.toString());
         }

         ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.assignment_types, android.R.layout.simple_spinner_item);
         ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, enrolledCourseNameList);
         ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(this, R.array.priority_levels, android.R.layout.simple_spinner_item);

         typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
         courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
         priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

         newAssignmentTypeSpinner.setAdapter(typeAdapter);
         newAssignmentCourseSpinner.setAdapter(courseAdapter);
         newAssignmentPrioritySpinner.setAdapter(priorityAdapter);

         confirmAssignmentBtn.setOnClickListener(view1 -> {
            if (newAssignmentHour.getText().toString().equals("") || Integer.valueOf(newAssignmentHour.getText().toString()) <= 0) {
               Toast.makeText(getBaseContext(), "Hours should be greater than 0", Toast.LENGTH_SHORT).show();
            }
            else {
               String workType = newAssignmentTypeSpinner.getSelectedItem().toString();
               NewCourse courseSelection = new NewCourse(newAssignmentCourseSpinner.getSelectedItem().toString());
               WorkItem item;

               for (NewCourse c : enrolledCourseList) {
                  if (c.toString().equals(newAssignmentCourseSpinner.getSelectedItem().toString())) {
                     courseSelection = c;
                     break;
                  }
               }

               switch (workType) {
                  case "Exam":
                     item = new Exam(courseSelection, newAssignmentName.getText().toString(),
                             dateView.getText().toString(), Integer.valueOf(newAssignmentPrioritySpinner.getSelectedItem().toString()),
                             Integer.valueOf(newAssignmentHour.getText().toString()));
                     break;
                  case "Project":
                     item = new Project(courseSelection, newAssignmentName.getText().toString(),
                             dateView.getText().toString(), Integer.valueOf(newAssignmentPrioritySpinner.getSelectedItem().toString()),
                             Integer.valueOf(newAssignmentHour.getText().toString()));
                     break;
                  case "Quiz":
                     item = new Quiz(courseSelection, newAssignmentName.getText().toString(),
                             dateView.getText().toString(), Integer.valueOf(newAssignmentPrioritySpinner.getSelectedItem().toString()),
                             Integer.valueOf(newAssignmentHour.getText().toString()));
                     break;
                  default: // Homework Case
                     item = new Homework(courseSelection, newAssignmentName.getText().toString(),
                             dateView.getText().toString(), Integer.valueOf(newAssignmentPrioritySpinner.getSelectedItem().toString()),
                             Integer.valueOf(newAssignmentHour.getText().toString()));
                     break;
               }

               flowLink.add(item);
               setDisplay();

               Toast.makeText(getBaseContext(), "Assignment successfully added", Toast.LENGTH_SHORT).show();
               popupWindow.dismiss();
            }
         });
      });
   }

   /**
    * Repopulates the RecyclerView based on the underlying AssignmentList.
    * Gets each item from the work queue with hardcoded priority but will change to array of
    * priority queues later.
    */
   private void setDisplay() {

      itemsList = flowLink.getWorkItems();
      studentHomeAdapter = new AccountActivity.RecyclerAdapter(this, itemsList);
      rcSchedule.setAdapter(studentHomeAdapter);

      if (!flowLink.hasItems()) {
         currentAssignment.setText("Schedule is empty!");
      }
      else {
         currentAssignment.setText(flowLink.getFirst().toString());
      }
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