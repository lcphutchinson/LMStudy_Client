package com.LMStudy.app.structures;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {
    ArrayList<Assignment> assignmentList;
    String courseName, courseNumber, department; // add id for course id from canvas maybe?
    ArrayList<String> sections;

    public Course (String courseName, String courseNumber, String department, List<String> sections) {
        this.courseName = courseName;
        this.courseNumber = courseNumber;
        this.department = department;
        this.sections = (ArrayList<String>) sections;
        assignmentList = new ArrayList<Assignment>();
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public String getDepartment() {
        return department;
    }

    public ArrayList<Assignment> getAssignmentList() {
        return assignmentList;
    }
}
