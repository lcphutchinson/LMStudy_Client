package com.LMStudy.app.structures;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.TreeSet;

import static org.junit.Assert.assertNotEquals;

public class WorkQueueTest extends TestCase {

    public void testGetInstance() {
        assertNotNull(WorkQueue.getInstance());
    }

    public void testGetWorkQueue() {
        WorkQueue workQueue = WorkQueue.getInstance();
        Assignment assignment1 = new Assignment("English", "First_Essay", "Assignment", "11/03/2022");
        workQueue.addToQueue(assignment1);

        TreeSet<Assignment> empty = new TreeSet<>();
        assertNotEquals(workQueue.getWorkQueue(4), empty);
        // shows that priority 5 queue is empty unlike priority 4 queue
        assertEquals(workQueue.getWorkQueue(5), empty);

        // cleanup workQueue for other tests
        workQueue.removeFromQueue(assignment1);
    }

    public void testAddToQueue() {
        WorkQueue workQueue = WorkQueue.getInstance();
        // counts for 4 elements
        Assignment assignment1 = new Assignment("English", "First_Essay", "Assignment", "11/03/2022");
        // counts for 6 elements
        Assignment project1 = new Assignment("CS", "Project 1", "Project", "10/31/2022");
        // counts for 2 elements
        Assignment study1 = new Assignment("Psychology", "Study for Exam", "Study Session", "11/02/2022");

        workQueue.addToQueue(assignment1);
        workQueue.addToQueue(project1);
        workQueue.addToQueue(study1);

        ArrayList<Assignment> actualList = workQueue.convertToArrayList();

        ArrayList<Assignment> expectedList = new ArrayList<>();
        expectedList.add(project1);
        expectedList.add(project1);
        expectedList.add(project1);
        expectedList.add(assignment1);
        expectedList.add(project1);
        expectedList.add(assignment1);
        expectedList.add(project1);
        expectedList.add(study1);
        expectedList.add(assignment1);
        expectedList.add(project1);
        expectedList.add(study1);
        expectedList.add(assignment1);

        assertEquals(actualList, expectedList);

        // cleanup workQueue for other tests
        workQueue.removeFromQueue(assignment1);
        workQueue.removeFromQueue(project1);
        workQueue.removeFromQueue(study1);
    }

    public void testRemoveFromQueue() {
        WorkQueue workQueue = WorkQueue.getInstance();
        // counts for 4 elements
        Assignment assignment1 = new Assignment("English", "First_Essay", "Assignment", "11/03/2022");
        // counts for 6 elements
        Assignment project1 = new Assignment("CS", "Project 1", "Project", "10/31/2022");
        // counts for 2 elements
        Assignment study1 = new Assignment("Psychology", "Study for Exam", "Study Session", "11/02/2022");

        workQueue.addToQueue(assignment1);
        workQueue.addToQueue(project1);
        workQueue.addToQueue(study1);

        workQueue.removeFromQueue(assignment1);
        workQueue.removeFromQueue(study1);

        ArrayList<Assignment> actualList = workQueue.convertToArrayList();

        ArrayList<Assignment> expectedList = new ArrayList<>();
        expectedList.add(project1);
        expectedList.add(project1);
        expectedList.add(project1);
        expectedList.add(project1);
        expectedList.add(project1);
        expectedList.add(project1);

        assertEquals(actualList, expectedList);

        // cleanup workQueue for other tests
        workQueue.removeFromQueue(project1);
    }

    public void testGetNumElements() {
        WorkQueue workQueue = WorkQueue.getInstance();
        // counts for 4 elements
        Assignment assignment1 = new Assignment("English", "First_Essay", "Assignment", "11/03/2022");
        // counts for 4 elements
        Assignment assignment2 = new Assignment("Math", "Practice_Set1", "Assignment", "10/31/2022");
        // counts for 0 elements (same due date)
        Assignment assignment3 = new Assignment("French", "Practice_Set2", "Assignment", "10/31/2022");
        // counts for 6 elements
        Assignment project1 = new Assignment("CS", "Project 1", "Project", "10/31/2022");
        // counts for 8 elements
        Assignment exam1 = new Assignment("CS", "Midterm", "Exam", "10/31/2022");
        // counts for 3 elements
        Assignment pers1 = new Assignment("CS", "Vacation", "Personal Time", "10/31/2022");
        // counts for 2 elements
        Assignment study1 = new Assignment("Psychology", "Study for Exam", "Study Session", "11/02/2022");
        // counts for 0 elements (same due date)
        Assignment study2 = new Assignment("Physics", "Study for Final Exam", "Study Session", "11/02/2022");

        workQueue.addToQueue(assignment1);
        workQueue.addToQueue(assignment2);
        workQueue.addToQueue(assignment3);
        workQueue.addToQueue(project1);
        workQueue.addToQueue(exam1);
        workQueue.addToQueue(pers1);
        workQueue.addToQueue(study1);
        workQueue.addToQueue(study2);

        assertEquals(workQueue.getNumElements(),27);

        // cleanup workQueue for other tests
        workQueue.removeFromQueue(assignment1);
        workQueue.removeFromQueue(assignment2);
        workQueue.removeFromQueue(assignment3);
        workQueue.removeFromQueue(project1);
        workQueue.removeFromQueue(exam1);
        workQueue.removeFromQueue(pers1);
        workQueue.removeFromQueue(study1);
        workQueue.removeFromQueue(study2);
    }

    public void testGetFirstAssignment() {
        WorkQueue workQueue = WorkQueue.getInstance();
        // counts for 4 elements
        Assignment assignment1 = new Assignment("English", "First_Essay", "Assignment", "11/03/2022");
        // counts for 6 elements
        Assignment project1 = new Assignment("CS", "Project 1", "Project", "10/31/2022");
        // counts for 2 elements
        Assignment study1 = new Assignment("Psychology", "Study for Exam", "Study Session", "11/02/2022");

        workQueue.addToQueue(assignment1);
        workQueue.addToQueue(project1);
        workQueue.addToQueue(study1);

        Assignment actualFirstAssignment = workQueue.getFirstAssignment();

        assertEquals(actualFirstAssignment, project1);

        // cleanup workQueue for other tests
        workQueue.removeFromQueue(assignment1);
        workQueue.removeFromQueue(project1);
        workQueue.removeFromQueue(study1);

    }
}