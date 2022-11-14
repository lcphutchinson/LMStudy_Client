package com.LMStudy.app.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.LMStudy.app.R;
import com.LMStudy.app.structures.Assignment;
import com.LMStudy.app.structures.WorkQueue;

import java.util.ArrayList;
import java.util.List;

/**
 * Primary UI for Student Users (previously "Main Activity")
 */
public class StudentHome extends AppCompatActivity {

   private final WorkQueue queueLink = WorkQueue.getInstance();
//   private List<AssignmentItem> items;
   private List<Assignment> items;

   private TextView currentAssignment;

   private RecyclerAdapter studentHomeAdapter;
   private RecyclerView rcSchedule;
   private ImageView profilePicture;
   private Button refreshButton, addAssignmentBtn, confirmAssignmentBtn;
   private TextView newAssignmentName, newAssignmentType, newAssignmentCourseInfo, newAssignmentDueDate;

   private TextView assignmentNameText, assignmentNameInfo, assignmentAssigneeText, assignmentAssigneeInfo,
           assignmentTypeText, assignmentTypeInfo, assignmentCourseText, assignmentCourseInfo,
           assignmentDueDateText, assignmentDueDateInfo, assignmentNotesText, assignmentNotesInfo;
   private Button completeAssignmentBtn, removeAssignmentBtn;

   private TextView confirmRemovalText;
   private Button yesButton, noButton;

   ArrayList<Assignment> assignmentArrayList;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_student_home);


      profilePicture = findViewById(R.id.profile_icon);
      rcSchedule = findViewById(R.id.assignment_work_queue);
      refreshButton = findViewById(R.id.refresh_Btn);
      addAssignmentBtn = findViewById(R.id.addAssignment_Btn);
      currentAssignment = findViewById(R.id.current_assignment_text);

      setDisplay();
      rcSchedule.setLayoutManager(new LinearLayoutManager(this));

//      if (queueLink.getFirstAssignment() == null) {
//         currentAssignment.setText("Schedule is empty!");
//      }
//      else {
//         currentAssignment.setText(queueLink.getFirstAssignment().toString());
//      }

      rcSchedule.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
              rcSchedule, new RecyclerItemClickListener.OnItemClickListener() {

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
            assignmentTypeText = popupView.findViewById(R.id.assignmentType_txt);
            assignmentCourseText = popupView.findViewById(R.id.course_txt);
            assignmentCourseInfo = popupView.findViewById(R.id.courseName_txt);
            assignmentDueDateText = popupView.findViewById(R.id.dueDateInfo_title);
            assignmentDueDateInfo = popupView.findViewById(R.id.dueDateInfo_txt);
            completeAssignmentBtn = popupView.findViewById(R.id.completeAssignment_btn);
            removeAssignmentBtn = popupView.findViewById(R.id.rmvAssignment_btn);

            assignmentNameInfo.setText(studentHomeAdapter.getItemName(position));
            //assignmentTypeInfo.setText(studentHomeAdapter.getItemType(position));
            assignmentCourseInfo.setText(studentHomeAdapter.getItemCourse(position));
            assignmentDueDateInfo.setText(studentHomeAdapter.getItemDueDate(position));

            completeAssignmentBtn.setOnClickListener(view1 -> {
               studentHomeAdapter.removeAt(position);

               //removal logic for "COMPLETED" and update recycler view

               Toast.makeText(getBaseContext(), "Assignment completed", Toast.LENGTH_SHORT).show();
               popupWindow.dismiss();
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
                  studentHomeAdapter.removeAt(position);

//                  String assignmentName = assignmentNameInfo.getText().toString();
//                  String assignmentType = assignmentTypeInfo.getText().toString();
//                  String courseInfo = assignmentCourseInfo.getText().toString();
//                  String dueDate = assignmentDueDateInfo.getText().toString();
//
//                  String[] partitionDate = dueDate.split("-");
//                  LocalDateTime ldtDueDate = LocalDateTime.of(Integer.parseInt(partitionDate[2]),
//                          Integer.parseInt(partitionDate[0]), Integer.parseInt(partitionDate[1]), 23, 59);
//
//                  Assignment newAssignment = new Assignment(courseInfo, assignmentName, assignmentType, ldtDueDate);
//
//                  queueLink.removeFromQueue(newAssignment);
//                  setDisplay();

                  Toast.makeText(getBaseContext(), "Assignment successfully removed", Toast.LENGTH_SHORT).show();
                  popupWindow1.dismiss();
                  popupWindow.dismiss();
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

      addAssignmentBtn.setOnClickListener(view -> {

         // inflate the layout of the popup window
         LayoutInflater inflater = (LayoutInflater)
                 getSystemService(LAYOUT_INFLATER_SERVICE);
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
         newAssignmentType = popupView.findViewById(R.id.newAssignmentType_input);
         newAssignmentCourseInfo = popupView.findViewById(R.id.newAssignmentCourseInfo_input);
         newAssignmentDueDate = popupView.findViewById(R.id.newDueDate_input);
         confirmAssignmentBtn = popupView.findViewById(R.id.confirm_assignment_Btn);

         confirmAssignmentBtn.setOnClickListener(view1 -> {
            String assignmentName = newAssignmentName.getText().toString();
            String assignmentType = newAssignmentType.getText().toString();
            String courseInfo = newAssignmentCourseInfo.getText().toString();
            String dueDate = newAssignmentDueDate.getText().toString();

//            String[] partitionDate = dueDate.split("/");
//            LocalDateTime ldtDueDate = LocalDateTime.of(Integer.parseInt(partitionDate[2]),
//                    Integer.parseInt(partitionDate[0]), Integer.parseInt(partitionDate[1]), 23, 59);

            Assignment newAssignment = new Assignment(courseInfo, assignmentName, assignmentType, dueDate);

            queueLink.addToQueue(newAssignment);
            setDisplay();

            Toast.makeText(getBaseContext(), "Assignment successfully added", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
         });
      });
   }

   /**
    * Repopulates the RecyclerView based on the underlying AssignmentList.
    * Gets each item from the work queue with hardcoded priority but will change to array of
    * priority queues later.
    */
   private void setDisplay() {
      items = new ArrayList<>();
      queueLink.getWorkQueue(10).forEach(task -> {
         items.add(generateItem(task));
      });
      queueLink.getWorkQueue(9).forEach(task -> {
         items.add(generateItem(task));
      });
      queueLink.getWorkQueue(8).forEach(task -> {
         items.add(generateItem(task));
      });
      queueLink.getWorkQueue(7).forEach(task -> {
         items.add(generateItem(task));
      });
      queueLink.getWorkQueue(6).forEach(task -> {
         items.add(generateItem(task));
      });
      queueLink.getWorkQueue(5).forEach(task -> {
         items.add(generateItem(task));
      });
      queueLink.getWorkQueue(4).forEach(task -> {
         items.add(generateItem(task));
      });
      queueLink.getWorkQueue(3).forEach(task -> {
         items.add(generateItem(task));
      });
      queueLink.getWorkQueue(2).forEach(task -> {
         items.add(generateItem(task));
      });
      queueLink.getWorkQueue(1).forEach(task -> {
         items.add(generateItem(task));
      });

      studentHomeAdapter = new RecyclerAdapter(this, items);
      studentHomeAdapter.getItemCount();
      rcSchedule.setAdapter(studentHomeAdapter);
      studentHomeAdapter.notifyDataSetChanged();

      if (items.size() == 0) {
         currentAssignment.setText("Schedule is empty!");
      }
      else {
         currentAssignment.setText(items.get(0).toString());
      }
   }

   private Assignment generateItem(Assignment a) {
      Assignment item = new Assignment(
         a.getCourseInfo(),
         a.getAssignmentType(),
         a.getAssignmentName(),
         a.getDueDate()
      );
      return item;
   }
}