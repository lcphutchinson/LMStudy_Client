package com.LMStudy.app.teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import android.app.DatePickerDialog;
import android.app.Dialog;

import com.LMStudy.app.AccountActivity;
import com.LMStudy.app.R;
import com.LMStudy.app.io.SyncService;
import com.LMStudy.app.structures.Assignment;
import com.LMStudy.app.structures.Course;
import com.LMStudy.app.structures.NewCourse;
import com.LMStudy.app.structures.WorkFlow;
import com.LMStudy.app.structures.workitems.Exam;
import com.LMStudy.app.structures.workitems.Homework;
import com.LMStudy.app.structures.workitems.Project;
import com.LMStudy.app.structures.workitems.Quiz;
import com.LMStudy.app.structures.workitems.WorkItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Primary UI for Student Users (previously "Main Activity")
 */
public class TeacherAssignmentHome extends AppCompatActivity implements Serializable {

    private final WorkFlow flowLink = WorkFlow.getInstance(); // Pass WorkFlow into TeacherHome from Menu
    private final SyncService caller = SyncService.getInstance();
    private AccountActivity.RecyclerAdapter teacherAssignmentAdapter;
    private RecyclerView rcAssignmentList;
    private ImageView profilePicture2;

    private Button addAssignmentBtn, confirmAssignmentBtn;

    private TextView newAssignmentName, newAssignmentHour, dateView;

    private Spinner newAssignmentTypeSpinner, newAssignmentCourseSpinner, newAssignmentPrioritySpinner;

    private TextView assignmentNameText, assignmentNameInfo, assignmentAssigneeText, assignmentAssigneeInfo,
            assignmentTypeText, assignmentTypeInfo, assignmentCourseText, assignmentCourseInfo,
            assignmentDueDateText, assignmentDueDateInfo, assignmentPrioText, assignmentPrioInfo,
            assignmentHoursText, assignmentHoursInfo;

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

    private Button detailAssignmentBtn, removeAssignmentBtn;

    private TextView confirmRemovalText;
    private Button yesButton, noButton;

    NewCourse courseScreen;
    private String courseName;
    private ArrayList<NewCourse> courseList = new ArrayList<NewCourse>();
    private ArrayList<String> courseNameList = new ArrayList<String>();

    // Stores course specific assignemnts into this array from SetDisplay()
    private ArrayList<WorkItem> courseAssignmentList = new ArrayList<WorkItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_assignment_list_activity);

        rcAssignmentList = findViewById(R.id.t_course_assignment_list);
        addAssignmentBtn = findViewById(R.id.t_addAssignment_Btn);

        courseScreen = flowLink.getCourseById(getIntent().getStringExtra("course"));
        setDisplay();
        rcAssignmentList.setLayoutManager(new LinearLayoutManager(this));

        rcAssignmentList.addOnItemTouchListener(new AccountActivity.RecyclerItemClickListener(getApplicationContext(),
                rcAssignmentList, new AccountActivity.RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                WorkItem clickedItem = courseAssignmentList.get(position);

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

                detailAssignmentBtn = popupView.findViewById(R.id.completeAssignment_btn);
                detailAssignmentBtn.setText("Edit Assignment");
                removeAssignmentBtn = popupView.findViewById(R.id.rmvAssignment_btn);

                assignmentNameInfo.setText(teacherAssignmentAdapter.getItemName(position));
                assignmentTypeInfo.setText(teacherAssignmentAdapter.getItemType(position));
                assignmentCourseInfo.setText(teacherAssignmentAdapter.getItemCourse(position));
                assignmentDueDateInfo.setText(teacherAssignmentAdapter.getItemDueDate(position));
                //assignmentPrioInfo.setText(studentHomeAdapter.getItemPriority(position));

                detailAssignmentBtn.setOnClickListener(view12 -> {
                    LayoutInflater inflater12 = (LayoutInflater)
                       getSystemService(LAYOUT_INFLATER_SERVICE);
                    // View popupView = inflater.inflate(R.layout.add_assignment_popup_revised, null);
                    View popupView12 = inflater12.inflate(R.layout.add_assignment_popup_revised, null);

                    // create the popup window
                    int width12 = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height12 = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable12 = true; // lets taps outside the popup also dismiss it
                    final PopupWindow popupWindow12 = new PopupWindow(popupView12, width12, height12, focusable12);

                    // show the popup window
                    // which view you pass in doesn't matter, it is only used for the window token
                    popupWindow12.showAtLocation(view, Gravity.CENTER, 0, 0);

                    // dismiss the popup window when touched
                    popupView12.setOnTouchListener((v, event) -> {
                        popupWindow12.dismiss();
                        return true;
                    });

                    // SPINNER
                    newAssignmentName = popupView12.findViewById(R.id.r_newAssignmentName_input);
                    newAssignmentName.setText(clickedItem.getName());
                    newAssignmentTypeSpinner = popupView12.findViewById(R.id.assignment_type_spinner);
                    newAssignmentPrioritySpinner = popupView12.findViewById(R.id.priority_spinner);

                    dateView = (TextView) popupView12.findViewById(R.id.dateText);
                    dateView.setText(clickedItem.getDisplayDate());

                    newAssignmentHour = popupView12.findViewById(R.id.hours_input);
                    newAssignmentHour.setText(clickedItem.getPriorityData()[1].toString());

                    confirmAssignmentBtn = popupView12.findViewById(R.id.r_confirm_assignment_Btn);

                    ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getBaseContext(), R.array.assignment_types, android.R.layout.simple_spinner_item);
                    ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(getBaseContext(), R.array.priority_levels, android.R.layout.simple_spinner_item);

                    typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

                    newAssignmentTypeSpinner.setAdapter(typeAdapter);
                    switch (clickedItem.getType()) {
                        case "Exam":
                            newAssignmentTypeSpinner.setSelection(0);
                            break;
                        case "Homework":
                            newAssignmentTypeSpinner.setSelection(1);
                            break;
                        case "Project":
                            newAssignmentTypeSpinner.setSelection(2);
                            break;
                        case "Quiz":
                            newAssignmentTypeSpinner.setSelection(3);
                            break;
                        default:
                            throw new IllegalStateException();
                    }

                    newAssignmentPrioritySpinner.setAdapter(priorityAdapter);
                    newAssignmentPrioritySpinner.setSelection(clickedItem.getPriorityData()[0]);

                    confirmAssignmentBtn.setOnClickListener(view1 -> {
                        if (newAssignmentHour.getText().toString().equals("") || Integer.valueOf(newAssignmentHour.getText().toString()) <= 0) {
                            Toast.makeText(getBaseContext(), "Hours should be greater than 0", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String workType = newAssignmentTypeSpinner.getSelectedItem().toString();
                            WorkItem item;

                            switch(workType) {
                                case "Exam":
                                    item = new Exam(courseScreen, newAssignmentName.getText().toString(),
                                       dateView.getText().toString(), Integer.valueOf(newAssignmentPrioritySpinner.getSelectedItem().toString()),
                                       Integer.valueOf(newAssignmentHour.getText().toString()));
                                    break;
                                case "Project":
                                    item = new Project(courseScreen, newAssignmentName.getText().toString(),
                                       dateView.getText().toString(), Integer.valueOf(newAssignmentPrioritySpinner.getSelectedItem().toString()),
                                       Integer.valueOf(newAssignmentHour.getText().toString()));
                                    break;
                                case "Quiz":
                                    item = new Quiz(courseScreen, newAssignmentName.getText().toString(),
                                       dateView.getText().toString(), Integer.valueOf(newAssignmentPrioritySpinner.getSelectedItem().toString()),
                                       Integer.valueOf(newAssignmentHour.getText().toString()));
                                    break;
                                default: // Homework Case
                                    item = new Homework(courseScreen, newAssignmentName.getText().toString(),
                                       dateView.getText().toString(), Integer.valueOf(newAssignmentPrioritySpinner.getSelectedItem().toString()),
                                       Integer.valueOf(newAssignmentHour.getText().toString()));
                                    break;
                            }

                            // item is the NEW work item added to the work flow
                            item.setIID(clickedItem.getIID());
                            if(caller.detail(item)) {
                                flowLink.remove(clickedItem);
                                flowLink.add(item);
                                courseAssignmentList.remove(clickedItem);
                                courseAssignmentList.add(item);
                                setDisplay();
                                Toast.makeText(getBaseContext(), "Assignment details modified", Toast.LENGTH_SHORT).show();
                                popupWindow.dismiss();
                            } else {
                                Toast.makeText(getBaseContext(), "Error: Update Failed", Toast.LENGTH_SHORT);
                            }
                        }

                    });
                });

                /**
                 * Creates a new work item to match to the specific course's workflow array
                 * (retrieved and stored in this class in SetDisplay()) and removes from work flow.
                 */
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
                           if (caller.delete(clickedItem.getIID())) {
                               System.out.println("This did happen");
                               flowLink.remove(clickedItem);
                               courseAssignmentList.remove(clickedItem);
                               setDisplay();
                               Toast.makeText(getBaseContext(), "Assignment Removed", Toast.LENGTH_SHORT);
                               popupWindow1.dismiss();
                               popupWindow.dismiss();
                           } else {
                               Toast.makeText(getBaseContext(), "Error: Remove Failed", Toast.LENGTH_SHORT);
                           }
                       });
                    noButton.setOnClickListener(view4 -> {
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

            // SPINNER
            newAssignmentName = popupView.findViewById(R.id.r_newAssignmentName_input);
            newAssignmentTypeSpinner = popupView.findViewById(R.id.assignment_type_spinner);
            newAssignmentCourseSpinner = popupView.findViewById(R.id.course_spinner);
            newAssignmentPrioritySpinner = popupView.findViewById(R.id.priority_spinner);

            dateView = (TextView) popupView.findViewById(R.id.dateText);
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);

            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            showDate(year, month+1, day);

            newAssignmentHour = popupView.findViewById(R.id.hours_input);
            confirmAssignmentBtn = popupView.findViewById(R.id.r_confirm_assignment_Btn);

            for (NewCourse c : courseList) {
                courseNameList.add(c.toString());
            }

            ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.assignment_types, android.R.layout.simple_spinner_item);
            ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(this, R.array.priority_levels, android.R.layout.simple_spinner_item);

            typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

            newAssignmentTypeSpinner.setAdapter(typeAdapter);
            newAssignmentPrioritySpinner.setAdapter(priorityAdapter);

            newAssignmentCourseSpinner.setSelection(courseNameList.indexOf(courseName));

            confirmAssignmentBtn.setOnClickListener(view1 -> {
                if (newAssignmentHour.getText().toString().equals("") || Integer.valueOf(newAssignmentHour.getText().toString()) <= 0) {
                    Toast.makeText(getBaseContext(), "Hours should be greater than 0", Toast.LENGTH_SHORT).show();
                }
                else {
                    String workType = newAssignmentTypeSpinner.getSelectedItem().toString();
                    WorkItem item;

                    switch(workType) {
                        case "Exam":
                            item = new Exam(courseScreen, newAssignmentName.getText().toString(),
                               dateView.getText().toString(), Integer.valueOf(newAssignmentPrioritySpinner.getSelectedItem().toString()),
                               Integer.valueOf(newAssignmentHour.getText().toString()));
                            break;
                        case "Project":
                            item = new Project(courseScreen, newAssignmentName.getText().toString(),
                               dateView.getText().toString(), Integer.valueOf(newAssignmentPrioritySpinner.getSelectedItem().toString()),
                               Integer.valueOf(newAssignmentHour.getText().toString()));
                            break;
                        case "Quiz":
                            item = new Quiz(courseScreen, newAssignmentName.getText().toString(),
                               dateView.getText().toString(), Integer.valueOf(newAssignmentPrioritySpinner.getSelectedItem().toString()),
                               Integer.valueOf(newAssignmentHour.getText().toString()));
                            break;
                        default: // Homework Case
                            item = new Homework(courseScreen, newAssignmentName.getText().toString(),
                               dateView.getText().toString(), Integer.valueOf(newAssignmentPrioritySpinner.getSelectedItem().toString()),
                               Integer.valueOf(newAssignmentHour.getText().toString()));
                            break;
                    }

                    // item is the NEW work item added to the work flow
                    item.setIID(caller.push(item));
                    flowLink.add(item);
                    setDisplay();

                    Toast.makeText(getBaseContext(), "Assignment successfully added", Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss();
                }

            });
        });
    }

    /**
     * Repopulates the RecyclerView based on assignments that match the course information
     * passed into this activity class from previous activity. Retrieved from general WorkFlow item.
     */
    private void setDisplay() {
        //teacherAssignmentAdapter = new AccountActivity.RecyclerAdapter(this, courseAssignmentList);

        courseAssignmentList.clear();

        for (WorkItem w : flowLink.getWorkItems()) {
            if (w.getCourse().equals(courseScreen)) {
                courseAssignmentList.add(w);
            }
        }

        teacherAssignmentAdapter = new AccountActivity.RecyclerAdapter(this, courseAssignmentList);
        rcAssignmentList.setAdapter(teacherAssignmentAdapter);
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

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "Set Due Date.", Toast.LENGTH_SHORT).show();
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
