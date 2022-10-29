package com.LMStudy.app.student;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AssignmentItem implements Serializable {

    private String courseInfo;
    private String assignmentName;
    private String assignmentType;
    private LocalDateTime dueDate;

    /**
     * Parameterized constructor for an Item object.
     * @param courseInfo
     * @param assignmentType
     * @param assignmentName
     * @param dueDate
     */
    public AssignmentItem(String courseInfo, String assignmentType, String assignmentName, LocalDateTime dueDate) { //Added itemQuantity
        this.courseInfo = courseInfo;
        this.assignmentName = assignmentName;
        this.assignmentType = assignmentType;
        this.dueDate = dueDate;
    }

    /**
     * Getter method that returns the assignment's course info.
     * @return assignment's course info
     */
    public String getCourseInfo() {
        return courseInfo;
    }

    /**
     * Getter method that returns the name of an assignment.
     * @return the assignment's name.
     */
    public String getAssignmentName() {
        return assignmentName;
    }

    /**
     * Getter method that returns the type of assignment.
     * @return type of assignment.
     */
    public String getAssignmentType() {
        return assignmentType;
    }

    /**
     * Getter method that returns the due date of an assignment.
     * @return assignment's due date.
     */
    public String getDueDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy @ HH:mm");
        return dueDate.format(formatter);
    }


    public String toString() {
        return this.assignmentName;
    }
}
