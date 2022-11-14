package com.LMStudy.app.structures;

public enum AssignmentType {
    ASSIGNMENT("Assignment", 4, 30),
    PROJECT("Project", 6, 120),
    EXAM("Exam", 8, 90),
    PERSONAL_TIME("Personal Time", 3, 30),
    STUDY_SESSION("Study Session", 2, 20);

    final private String name;
    final private int priority;
    final private int timeInMinutes;

    AssignmentType(String name, int priority, int timeInMinutes) {
        this.name = name;
        this.priority = priority;
        this.timeInMinutes = timeInMinutes;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public int getTimeInMinutes() {
        return timeInMinutes;
    }
}
