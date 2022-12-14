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
import com.LMStudy.app.io.SyncService;
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

   // TODO: WorkFlow item that stores all work item information
   private final WorkFlow flowLink = WorkFlow.getInstance();
   private final SyncService caller = SyncService.getInstance();

   private List<WorkItem> itemsList; // Used for populating recycler view

   private TextView currentAssignment;

   private AccountActivity.RecyclerAdapter studentHomeAdapter;
   private RecyclerView rcSchedule;
   private ImageView profilePicture;
   private Button refreshButton, addAssignmentBtn, confirmAssignmentBtn;

   private TextView assignmentNameText, assignmentNameInfo, assignmentAssigneeText, assignmentAssigneeInfo,
           assignmentTypeText, assignmentTypeInfo, assignmentCourseText, assignmentCourseInfo,
           assignmentDueDateText, assignmentDueDateInfo, assignmentPrioText, assignmentPrioInfo,
           assignmentHoursText, assignmentHoursInfo;

   private TextView newAssignmentName, newAssignmentHour, dateView;
   private Spinner newAssignmentTypeSpinner, newAssignmentCourseSpinner, newAssignmentPrioritySpinner;

   // CALENDAR STUFF

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

   /**
    * Creates student home screen from action
    * @param savedInstanceState
    */
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

      // enrolledCourseList gets enrolled course from WorkFlow above but courses in WorkFlow is currently null
      enrolledCourseList.add(testCourse);

      setDisplay();
      rcSchedule.setLayoutManager(new LinearLayoutManager(this));

      /**
       * Adds a listener to the recycler view of work item objects
       */
      rcSchedule.addOnItemTouchListener(new AccountActivity.RecyclerItemClickListener(getApplicationContext(),
              rcSchedule, new AccountActivity.RecyclerItemClickListener.OnItemClickListener() {

         /**
          * Expands work item from recycler view to popup with more details about work item
          * @param view
          * @param position
          */
         @Override
         public void onItemClick(View view, int position) {
            WorkItem clickedItem = itemsList.get(position);

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
            assignmentPrioInfo = popupView.findViewById(R.id.priorityInfo_txt);
            assignmentHoursText = popupView.findViewById(R.id.hoursInfo);
            assignmentHoursInfo = popupView.findViewById(R.id.hoursInfo_txt);

            completeAssignmentBtn = popupView.findViewById(R.id.completeAssignment_btn);
            removeAssignmentBtn = popupView.findViewById(R.id.rmvAssignment_btn);

            assignmentNameInfo.setText(studentHomeAdapter.getItemName(position));
            assignmentTypeInfo.setText(studentHomeAdapter.getItemType(position));
            assignmentCourseInfo.setText(studentHomeAdapter.getItemCourse(position));
            assignmentDueDateInfo.setText(studentHomeAdapter.getItemDueDate(position));
            assignmentPrioInfo.setText(Integer.toString(itemsList.get(position).getPriority()));

            /**
             * Creates a work item from recycler item information to match to a work item from
             * work flow object. Deletes from workflow and repopulates recycler view with updated
             * information
             */
            completeAssignmentBtn.setOnClickListener(view1 -> {
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
                  if (caller.complete(clickedItem.getIID())) {
                     flowLink.removeById(clickedItem.getIID());
                     itemsList.remove(position);
                     setDisplay();
                     Toast.makeText(getBaseContext(), "Assignment completed", Toast.LENGTH_SHORT).show();
                     popupWindow.dismiss();
                  } else {
                     Toast.makeText(getBaseContext(), "Error, assignment not completed.", Toast.LENGTH_SHORT).show();
                  }
               });
               noButton.setOnClickListener(view4 -> {
                  popupWindow1.dismiss();
               });
            });

            /**
             * Same logic as complete assignment
             */
            removeAssignmentBtn.setOnClickListener(view1 -> {

            });
         }

         @Override
         public void onItemLongClick(View view, int position) {
            // do whatever
         }
      }));

      /**
       * Refreshes the student home activity with refresh button.
       */
      refreshButton.setOnClickListener(view -> {
         finish();
         startActivity(getIntent());
      });

      /**
       * Opens an add assignment popup with textviews, spinners and calendars for options.
       */
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

         // Used to populate Course spinner list
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

         /**
          * Creates a new WorkItem depending on Type Spinner option chosen and adds to workflow item.
          */
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
                     item = new Exam(NewCourse.SELF_ASSIGNED, newAssignmentName.getText().toString(),
                             dateView.getText().toString(), Integer.valueOf(newAssignmentPrioritySpinner.getSelectedItem().toString()),
                             Integer.valueOf(newAssignmentHour.getText().toString()));
                     break;
                  case "Project":
                     item = new Project(NewCourse.SELF_ASSIGNED, newAssignmentName.getText().toString(),
                             dateView.getText().toString(), Integer.valueOf(newAssignmentPrioritySpinner.getSelectedItem().toString()),
                             Integer.valueOf(newAssignmentHour.getText().toString()));
                     break;
                  case "Quiz":
                     item = new Quiz(NewCourse.SELF_ASSIGNED, newAssignmentName.getText().toString(),
                             dateView.getText().toString(), Integer.valueOf(newAssignmentPrioritySpinner.getSelectedItem().toString()),
                             Integer.valueOf(newAssignmentHour.getText().toString()));
                     break;
                  default: // Homework Case
                     item = new Homework(NewCourse.SELF_ASSIGNED, newAssignmentName.getText().toString(),
                             dateView.getText().toString(), Integer.valueOf(newAssignmentPrioritySpinner.getSelectedItem().toString()),
                             Integer.valueOf(newAssignmentHour.getText().toString()));
                     break;
               }

               String iId = caller.push(item);
               item.setIID(iId);
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
      String dateString = year + "-" + month + "-" + day;
      dateView.setText(dateString);
   }

   /**
    * Calendar supporting function
    * @param view
    */
   @SuppressWarnings("deprecation")
   public void setDate(View view) {
      showDialog(999);
      Toast.makeText(getApplicationContext(), "Set Due Date.", Toast.LENGTH_SHORT).show();
   }

   /**
    * Calendar supporting function
    * @param id
    * @return
    */
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