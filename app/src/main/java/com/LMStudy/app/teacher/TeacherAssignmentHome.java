package com.LMStudy.app.teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
import com.LMStudy.app.structures.WorkQueue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Primary UI for Student Users (previously "Main Activity")
 */
public class TeacherAssignmentHome extends AppCompatActivity implements Serializable {

    private AccountActivity.RecyclerAdapter teacherAssignmentAdapter;
    private RecyclerView rcAssignmentList;
    private ImageView profilePicture2;

    private Button addAssignmentBtn, confirmAssignmentBtn;

    private TextView newAssignmentName;

    private Spinner newAssignmentTypeSpinner, newAssignmentCourseSpinner, newAssignmentDueDateMonthSpinner,
            newAssignmentDueDateDaySpinner, newAssignmentDueDateYearSpinner;

    private TextView assignmentNameText, assignmentNameInfo, assignmentAssigneeText, assignmentAssigneeInfo,
            assignmentTypeText, assignmentTypeInfo, assignmentCourseText, assignmentCourseInfo,
            assignmentDueDateText, assignmentDueDateInfo, assignmentNotesText, assignmentNotesInfo;
    private Button removeAssignmentBtn;

    private TextView confirmRemovalText;
    private Button yesButton, noButton;

    private String courseName;
    private ArrayList<Course> courseList;
    private ArrayList<String> courseNameList = new ArrayList<String>();
    private ArrayList<Assignment> courseAssignmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_assignment_list_activity);

        Intent fromTeacherHome = getIntent();
        courseName = fromTeacherHome.getStringExtra("Course");
        courseList = (ArrayList<Course>) fromTeacherHome.getSerializableExtra("Course List");
        courseAssignmentList = courseList.get(fromTeacherHome.getIntExtra("Position", 0)).getAssignmentList();

        //profilePicture = findViewById(R.id.profile_icon);
        rcAssignmentList = findViewById(R.id.t_course_assignment_list);
        //refreshButton = findViewById(R.id.t_refresh_btn);
        addAssignmentBtn = findViewById(R.id.t_addAssignment_Btn);

        setDisplay();
        rcAssignmentList.setLayoutManager(new LinearLayoutManager(this));

        rcAssignmentList.addOnItemTouchListener(new AccountActivity.RecyclerItemClickListener(getApplicationContext(),
                rcAssignmentList, new AccountActivity.RecyclerItemClickListener.OnItemClickListener() {

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
                // completeAssignmentBtn = popupView.findViewById(R.id.completeAssignment_btn);
                removeAssignmentBtn = popupView.findViewById(R.id.rmvAssignment_btn);

                assignmentNameInfo.setText(teacherAssignmentAdapter.getItemName(position));
                assignmentTypeInfo.setText(teacherAssignmentAdapter.getItemType(position));
                assignmentCourseInfo.setText(teacherAssignmentAdapter.getItemCourse(position));
                assignmentDueDateInfo.setText(teacherAssignmentAdapter.getItemDueDate(position));

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

                        String assignmentName = assignmentNameInfo.getText().toString();
                        String assignmentType = assignmentTypeInfo.getText().toString();
                        String courseInfo = assignmentCourseInfo.getText().toString();
                        String dueDate = assignmentDueDateInfo.getText().toString();

                        Assignment newAssignment = new Assignment(courseInfo, assignmentName, assignmentType, dueDate);

                        courseAssignmentList.remove(newAssignment);
                        setDisplay();

                        // add assignment somewhere using triggers

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

        addAssignmentBtn.setOnClickListener(view -> {

            // inflate the layout of the popup window
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(LAYOUT_INFLATER_SERVICE);
            // View popupView = inflater.inflate(R.layout.add_assignment_popup_revised, null);
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

            /*
            newAssignmentName = popupView.findViewById(R.id.r_newAssignmentName_input);
            newAssignmentTypeSpinner = popupView.findViewById(R.id.assignment_type_spinner);
            newAssignmentCourseSpinner = popupView.findViewById(R.id.course_spinner);
            newAssignmentDueDateMonthSpinner = popupView.findViewById(R.id.month_spinner);
            newAssignmentDueDateDaySpinner = popupView.findViewById(R.id.day_spinner);
            newAssignmentDueDateYearSpinner = popupView.findViewById(R.id.year_spinner);
            confirmAssignmentBtn = popupView.findViewById(R.id.r_confirm_assignment_Btn);

            /**
             * Will fix this next sprint
             */

            /*
            newAssignmentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    // do something upon option selection
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {
                    // can leave this empty
                }
            });
            */
/*
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
                /*String assignmentName = newAssignmentName.getText().toString();
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

                courseAssignmentList.add(newAssignment);

/*
                int courseIndex = courseList.indexOf(newAssignmentCourseSpinner.getSelectedItem().toString());
                courseList.get(courseIndex).getAssignmentList().add(newAssignment);


                if (courseInfo.equals(courseName))
                    setDisplay();
*/
                setDisplay();

                Toast.makeText(getBaseContext(), "Assignment successfully added", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            });
        });
    }

    /**
     * Repopulates the RecyclerView based on the underlying AssignmentList.
     */
    private void setDisplay() {
        teacherAssignmentAdapter = new AccountActivity.RecyclerAdapter(this, courseAssignmentList);
        rcAssignmentList.setAdapter(teacherAssignmentAdapter);

    }
}
