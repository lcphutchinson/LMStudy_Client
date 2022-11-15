package com.LMStudy.app.structures;

import com.LMStudy.app.structures.AssignmentType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Data unit representing a general task.
 * Will separate different assignment tasks into individual classes (e.g. Exams, Study Sessions, etc.)
 */
public class Assignment implements Comparable<Assignment> {

    private static final int INITIAL_PRIORITY = 2;
    private static final int INITIAL_TIMEBLOCK = 60; //minutes set for assignment per session

    private String courseInfo; // go to canvas to get course number
    private String assignmentName;
    private int priority; // priority is between 1-10
    private String assignmentType; // create an assignment enum
    //private Date dueDate; // convert to Date class
    private String dueDate;
    private int assignedTimeBlocks; // for student

    /**
     * Creates an Assignment object that stores assignment information.
     * @param courseInfo Course number/Class name
     * @param assignmentName Name of assignment
     * @param assignmentType Assignment type (normal, exam, project, etc.)
     * @param dueDate Due date of assignment
     */
    public Assignment(String courseInfo, String assignmentName, String assignmentType, String dueDate) {
        this.courseInfo = courseInfo;
        this.assignmentName = assignmentName;

        switch (assignmentType) { // no default case because it'll be prevented using listview
            case "Assignment":
                priority = AssignmentType.ASSIGNMENT.getPriority();
                this.assignmentType = AssignmentType.ASSIGNMENT.getName();
                assignedTimeBlocks = AssignmentType.ASSIGNMENT.getTimeInMinutes();
                break;
            case "Project":
                priority = AssignmentType.PROJECT.getPriority();
                this.assignmentType = AssignmentType.PROJECT.getName();
                assignedTimeBlocks = AssignmentType.PROJECT.getTimeInMinutes();
                break;
            case "Exam":
                priority = AssignmentType.EXAM.getPriority();
                this.assignmentType = AssignmentType.EXAM.getName();
                assignedTimeBlocks = AssignmentType.EXAM.getTimeInMinutes();
                break;
            case "Study Session":
                priority = AssignmentType.STUDY_SESSION.getPriority();
                this.assignmentType = AssignmentType.STUDY_SESSION.getName();
                assignedTimeBlocks = AssignmentType.STUDY_SESSION.getTimeInMinutes();
                break;
            case "Personal Time":
                priority = AssignmentType.PERSONAL_TIME.getPriority();
                this.assignmentType = AssignmentType.PERSONAL_TIME.getName();
                assignedTimeBlocks = AssignmentType.PERSONAL_TIME.getTimeInMinutes();
                break;
        }

        this.dueDate = dueDate; // app prompts for date with list views instead of text view
        assignedTimeBlocks = INITIAL_TIMEBLOCK;
    }

    /**
     * Empty constructor for testing--generates a generic assignment
     */
    public Assignment(){
        this.courseInfo = "CS 431";
        this.assignmentName = "Sprint 3";
        this.priority = INITIAL_PRIORITY;
        this.assignmentType = "Sprint";
        this.dueDate = LocalDateTime.now().toString();
    }

    // May delete any unnecessary get functions
    public String getCourseInfo() {
        return courseInfo;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public int getPriority() {
        return priority;
    }

    public String getAssignmentType() {
        return assignmentType;
    }

    /**
     * Getter method that returns the due date of an assignment.
     * @return assignment's due date.
     */
    public String getDueDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy @ HH:mm");
        return dueDate;
    }

    /**
     * Changes the priority of the assignment. Might be available to students only for
     * self-assigned priority or based on schedule.
     * @param priority Assignment priority.
     */
    public void changePriority(int priority) {
        this.priority = priority;
    }

    /**
     * Changes the due date of the assignment. Available only to teachers.
     * @param dueDate Due date of assignment.
     */
    public void changeDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isEqual(Assignment assignment) {
        if (courseInfo.compareTo(assignment.courseInfo) == 0 && assignmentName.compareTo(assignment.assignmentName) == 0
                && assignmentType.compareTo(assignment.assignmentType) == 0 && dueDate.compareTo(assignment.dueDate) == 0) {
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "DUE " + dueDate + ": (" + courseInfo + ") " + assignmentName;
    }

    @Override
    public int compareTo(Assignment assignment) {
        if (dueDate.compareTo(assignment.dueDate) == 0) {
            return Integer.compare(priority, assignment.priority)*(-1);
        }
        return dueDate.compareTo(assignment.dueDate);
    }

    /**
     * Just a testing main function.
     * @param args
     */
    public static void main (String[] args) {
        System.out.println("DUE DATE:\n(COURSE NAME)");
    }
}
