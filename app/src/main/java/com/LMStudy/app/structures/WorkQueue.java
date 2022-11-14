package com.LMStudy.app.structures;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Queue for managing general assignments.
 *
 * NOTES FOR FUTURE SPRINT:
 * - Daily scheduler needs a start time to end time.
 * - Will implement things like class time, sleep time or things that need to occupy a certain block
 * so the schedule can allocate slots with respect to these occupied timeslots
 * - Implementing date will allow the implementation of preventing shadowing of assignments
 *    - Can implement a completed flag to check for successfully scheduled assignments
 * - Will optimize list of TreeSets into an array for next sprint
 */
public class WorkQueue{
   private TreeSet<Assignment> priority1, priority2, priority3, priority4, priority5, priority6,
           priority7, priority8, priority9, priority10; // one set for each priority
   private long userId;

   private static WorkQueue instance = new WorkQueue();

   /**
    * User's work queue
    * //@param userId Assigned ID of user as received from Canvas.
    */
   private WorkQueue() {
      priority1 = new TreeSet<Assignment>();
      priority2 = new TreeSet<Assignment>();
      priority3 = new TreeSet<Assignment>();
      priority4 = new TreeSet<Assignment>();
      priority5 = new TreeSet<Assignment>();
      priority6 = new TreeSet<Assignment>();
      priority7 = new TreeSet<Assignment>();
      priority8 = new TreeSet<Assignment>();
      priority9 = new TreeSet<Assignment>();
      priority10 = new TreeSet<Assignment>();

      //this.userId = userId;
   }

   public static WorkQueue getInstance() {
      return instance;
   }

   /**
    * Returns the workqueue that matches priority parameter
    * @param priorityQueue Priority of queue to be returned
    * @return priority queue
    */
   public TreeSet<Assignment> getWorkQueue(int priorityQueue) {
      switch(priorityQueue) {
         case 10: return priority10;
         case 9: return priority9;
         case 8: return priority8;
         case 7: return priority7;
         case 6: return priority6;
         case 5: return priority5;
         case 4: return priority4;
         case 3: return priority3;
         case 2: return priority2;
         case 1:
         default: return priority1;
      }
   }

   /**
    * What does this method do?
    * @param input
    */
   public void populate(List<Assignment> input){
      input.forEach(item -> priority1.add(item));
   }

   /**
    * Adds assignment to priority set (queues) with the same priority or lower.
    * Could not get switch statement to work correctly.
    * @param assignment assignment to be added to workqueue(s)
    * @return true if successfully added to any workqueue(s), false otherwise
    */
   public boolean addToQueue(Assignment assignment) {
      boolean addedSuccessfully = false;
      if (assignment.getPriority() >= 10) {
         if (!priority10.contains(assignment)) {
            addedSuccessfully = priority10.add(assignment);
         }
      }
      if (assignment.getPriority() >= 9) {
         if (!priority9.contains(assignment)) {
            addedSuccessfully = priority9.add(assignment);
         }
      }
      if (assignment.getPriority() >= 8) {
         if (!priority8.contains(assignment)) {
            addedSuccessfully = priority8.add(assignment);
         }
      }
      if (assignment.getPriority() >= 7) {
         if (!priority7.contains(assignment)) {
            addedSuccessfully = priority7.add(assignment);
         }
      }
      if (assignment.getPriority() >= 6) {
         if (!priority6.contains(assignment)) {
            addedSuccessfully = priority6.add(assignment);
         }
      }
      if (assignment.getPriority() >= 5) {
         if (!priority5.contains(assignment)) {
            addedSuccessfully = priority5.add(assignment);
         }
      }
      if (assignment.getPriority() >= 4) {
         if (!priority4.contains(assignment)) {
            addedSuccessfully = priority4.add(assignment);
         }
      }
      if (assignment.getPriority() >= 3) {
         if (!priority3.contains(assignment)) {
            addedSuccessfully = priority3.add(assignment);
         }
      }
      if (assignment.getPriority() >= 2) {
         if (!priority2.contains(assignment)) {
            addedSuccessfully = priority2.add(assignment);
         }
      }
      if (assignment.getPriority() >= 1) {
         if (!priority1.contains(assignment)) {
            addedSuccessfully = priority1.add(assignment);
         }
      }

      return addedSuccessfully;
   }

   public boolean removeFromQueue(Assignment assignment) {
      boolean removedSuccessfully = false;

      if (assignment.getPriority() >= 10) {
         if (priority10.contains(assignment)) {
            removedSuccessfully = priority10.add(assignment);
         }
      }
      if (assignment.getPriority() >= 9) {
         if (priority9.contains(assignment)) {
            removedSuccessfully = priority9.add(assignment);
         }
      }
      if (assignment.getPriority() >= 8) {
         if (priority8.contains(assignment)) {
            removedSuccessfully = priority8.add(assignment);
         }
      }
      if (assignment.getPriority() >= 7) {
         if (priority7.contains(assignment)) {
            removedSuccessfully = priority7.add(assignment);
         }
      }
      if (assignment.getPriority() >= 6) {
         if (priority6.contains(assignment)) {
            removedSuccessfully = priority6.add(assignment);
         }
      }
      if (assignment.getPriority() >= 5) {
         if (priority5.contains(assignment)) {
            removedSuccessfully = priority5.add(assignment);
         }
      }
      if (assignment.getPriority() >= 4) {
         if (priority4.contains(assignment)) {
            removedSuccessfully = priority4.add(assignment);
         }
      }
      if (assignment.getPriority() >= 3) {
         if (priority3.contains(assignment)) {
            removedSuccessfully = priority3.add(assignment);
         }
      }
      if (assignment.getPriority() >= 2) {
         if (priority2.contains(assignment)) {
            removedSuccessfully = priority2.add(assignment);
         }
      }
      if (assignment.getPriority() >= 1) {
         if (priority1.contains(assignment)) {
            removedSuccessfully = priority1.add(assignment);
         }
      }

      return removedSuccessfully;
//      if (items.contains(assignment)) {
//         return items.remove(assignment);
//      }
//      return false;
   }

   /**
    * Returns number of elements in ALL queues
    * @return number of elements
    */
   public int getNumElements() {
      int totalSize = 0;

      totalSize += priority1.size();
      totalSize += priority2.size();
      totalSize += priority3.size();
      totalSize += priority4.size();
      totalSize += priority5.size();
      totalSize += priority6.size();
      totalSize += priority7.size();
      totalSize += priority8.size();
      totalSize += priority9.size();
      totalSize += priority10.size();

      return totalSize;
      //return items.size();
   }

   /**
    * Not sure what this could be used for but leaving in just in case
    * @return
    */
   public Assignment getFirstAssignment() {
      if (!priority10.isEmpty()) {
         return priority10.first();
      }
      if (!priority9.isEmpty()) {
         return priority9.first();
      }
      if (!priority8.isEmpty()) {
         return priority8.first();
      }
      if (!priority7.isEmpty()) {
         return priority7.first();
      }
      if (!priority6.isEmpty()) {
         return priority6.first();
      }
      if (!priority5.isEmpty()) {
         return priority5.first();
      }
      if (!priority4.isEmpty()) {
         return priority4.first();
      }
      if (!priority3.isEmpty()) {
         return priority3.first();
      }
      if (!priority2.isEmpty()) {
         return priority2.first();
      }
      if (!priority1.isEmpty()) {
         return priority1.first();
      }

      return null; // Create a new study session assignment?
   }


   /**
    * Testing method to see if assignments are added to queues with the same priority and less
    */
   public void getAllAssignments() {
      System.out.println("*** Priority 10 ***");
      for (Assignment a : priority10) {
         System.out.println(a);
      }
      System.out.println("*** Priority 9 ***");
      for (Assignment a : priority9) {
         System.out.println(a);
      }
      System.out.println("*** Priority 8 ***");
      for (Assignment a : priority8) {
         System.out.println(a);
      }
      System.out.println("*** Priority 7 ***");
      for (Assignment a : priority7) {
         System.out.println(a);
      }
      System.out.println("*** Priority 6 ***");
      for (Assignment a : priority6) {
         System.out.println(a);
      }
      System.out.println("*** Priority 5 ***");
      for (Assignment a : priority5) {
         System.out.println(a);
      }
      System.out.println("*** Priority 4 ***");
      for (Assignment a : priority4) {
         System.out.println(a);
      }
      System.out.println("*** Priority 3 ***");
      for (Assignment a : priority3) {
         System.out.println(a);
      }
      System.out.println("*** Priority 2 ***");
      for (Assignment a : priority2) {
         System.out.println(a);
      }
      System.out.println("*** Priority 1 ***");
      for (Assignment a : priority1) {
         System.out.println(a);
      }
   }

   /**
    * Used to convert treeset to ArrayList which can be traversed with indexes.
    * @return ArrayList of all treesets
    */
   public ArrayList<Assignment> convertToArrayList() {
      ArrayList<Assignment> assignmentList = new ArrayList<>(priority10);
      //ArrayList<Assignment> assignmentListToAdd = new ArrayList<>(priority9);
      //assignmentList.addAll(assignmentListToAdd);
      assignmentList.addAll(priority9);
      assignmentList.addAll(priority8);
      assignmentList.addAll(priority7);
      assignmentList.addAll(priority6);
      assignmentList.addAll(priority5);
      assignmentList.addAll(priority4);
      assignmentList.addAll(priority3);
      assignmentList.addAll(priority2);
      assignmentList.addAll(priority1);

      return assignmentList;
   }

   /**
    * Testing basic work queue functions
    * @param args Input arguments
    */
   public static void main(String[] args) {
      LocalDateTime firstDate = LocalDateTime.of(2022, 11, 03, 11, 59);
      LocalDateTime secondDate = LocalDateTime.of(2022, 10, 31, 11, 59);
      Assignment assignment1 = new Assignment("English", "First_Essay", "Assignment", "11/03/2022");
      Assignment assignment2 = new Assignment("Math", "Practice_Set1", "Assignment", "10/31/2022");
      Assignment assignment3 = new Assignment("CS", "Project 1", "Project", "10/31/2022");
      Assignment assignment4 = new Assignment("Psychology", "Study for Exam", "Study Session", "11/02/2022");
      assignment3.changePriority(6);
      instance.addToQueue(assignment1);
      instance.addToQueue(assignment2);
      instance.addToQueue(assignment3);
      instance.addToQueue(assignment4);
      System.out.println(instance.getNumElements());
      System.out.println(instance.getFirstAssignment());

      instance.getAllAssignments();

   }
}
