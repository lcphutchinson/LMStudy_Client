package com.LMStudy.app.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.LMStudy.app.R;
import com.LMStudy.app.structures.Assignment;
import com.LMStudy.app.structures.WorkQueue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Primary UI for Student Users (previously "Main Activity")
 */
public class StudentHome extends AppCompatActivity {

   private WorkQueue assignmentItems = new WorkQueue();
   List<Assignment> assignmentArrayList;
   List<AssignmentItem> assignmentItemsList = new ArrayList<>();
   RecyclerAdapter studentHomeAdapter;

   RecyclerView rcSchedule;
   ImageView profilePicture;
   Button refreshButton, addAssignmentBtn, confirmAssignmentBtn;
   TextView newAssignmentName, newAssignmentType, newAssignmentCourseInfo, newAssignmentDueDate;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_student_home);

      assignmentArrayList = assignmentItems.convertToArrayList();

      System.out.println(assignmentItems.getNumElements());

      profilePicture = (ImageView) findViewById(R.id.profile_icon);
      //profilePicture.setImageResource(R.drawable.my_image);

      rcSchedule = findViewById(R.id.rcView_Schedule);
      refreshButton = (Button) findViewById(R.id.refresh_Btn);
      addAssignmentBtn = (Button) findViewById(R.id.addAssignment_Btn);

//      for (int i = 0; i < assignmentArrayList.size(); i++) {
//         AssignmentItem assignmentItem = new AssignmentItem(assignmentArrayList.get(i).getCourseInfo(),
//                 assignmentArrayList.get(i).getAssignmentType(), assignmentArrayList.get(i).getAssignmentName(),
//                 assignmentArrayList.get(i).getDueDate());
//         assignmentItemsList.add(assignmentItem);
//      }

      studentHomeAdapter = new RecyclerAdapter(this, assignmentItemsList);
      rcSchedule.setAdapter(studentHomeAdapter);
      rcSchedule.setLayoutManager(new LinearLayoutManager(this));

      /**
       * Refreshes the student home activity.
       */
      refreshButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            finish();
            startActivity(getIntent());
         }
      });

      addAssignmentBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

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
            popupView.setOnTouchListener(new View.OnTouchListener() {
               @Override
               public boolean onTouch(View v, MotionEvent event) {
                  popupWindow.dismiss();
                  return true;
               }
            });

            newAssignmentName = popupView.findViewById(R.id.newAssignmentName_input);
            newAssignmentType = popupView.findViewById(R.id.newAssignmentType_input);
            newAssignmentCourseInfo = popupView.findViewById(R.id.newAssignmentCourseInfo_input);
            newAssignmentDueDate = popupView.findViewById(R.id.newDueDate_input);
            confirmAssignmentBtn = popupView.findViewById(R.id.confirm_assignment_Btn);

            confirmAssignmentBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                  String assignmentName = newAssignmentName.getText().toString();
                  String assignmentType = newAssignmentType.getText().toString();
                  String courseInfo = newAssignmentCourseInfo.getText().toString();
                  String dueDate = newAssignmentDueDate.getText().toString();

                  String[] partitionDate = dueDate.split("/");
                  LocalDateTime ldtDueDate = LocalDateTime.of(Integer.parseInt(partitionDate[2]),
                          Integer.parseInt(partitionDate[0]), Integer.parseInt(partitionDate[1]), 23, 59);

                  Assignment newAssignment = new Assignment(courseInfo, assignmentName, assignmentType, ldtDueDate);

//                  assignmentItems.addToQueue(newAssignment);
//                  assignmentItemsList.add(new AssignmentItem(newAssignment.getCourseInfo(),
//                          newAssignment.getAssignmentType(), newAssignment.getAssignmentName(),
//                          newAssignment.getDueDate()));

                  insertSingleItem(newAssignment);

                  Toast.makeText(getBaseContext(), "Assignment successfully added", Toast.LENGTH_SHORT).show();
                  popupWindow.dismiss();
               }
            });
         }
      });
   }

   private void insertSingleItem(Assignment assignment) {
      assignmentItems.addToQueue(assignment);

      AssignmentItem assignmentItem = new AssignmentItem(assignment.getCourseInfo(),
              assignment.getAssignmentType(), assignment.getAssignmentName(), assignment.getDueDate());
         assignmentItemsList.add(assignmentItem);

      int index = assignmentArrayList.indexOf(assignmentItem);
      studentHomeAdapter.notifyItemInserted(index);
   }
}