package com.LMStudy.app.structures;

import java.time.LocalDateTime;
import java.util.TreeSet;

/**
 * Queue for managing general assignments
 */
public class WorkQueue{
   private TreeSet<Assignment> items;
   private long userId;

   //private WorkQueue instance = new WorkQueue();

   /**
    * User's work queue
    * @param userId Assigned ID of user as received from Canvas.
    */
   public WorkQueue(long userId) {
      items = new TreeSet<>();
      this.userId = userId;
   }

   public TreeSet<Assignment> getWorkQueue() {
      return items;
   }

   public boolean addToQueue(Assignment assignment) {
      if (!items.contains(assignment)) {
         return items.add(assignment);
      }
      return false;
   }

   public boolean removeFromQueue(Assignment assignment) {
      if (items.contains(assignment)) {
         return items.remove(assignment);
      }
      return false;
   }

   public Assignment getFirstAssignment() {
      if (items.isEmpty() != true) {
         return items.first();
      }

      return null; // Create a new study session assignment?
   }

   public void getAllAssignments() {
      for (Assignment a : items) {
         System.out.println(a);
      }
   }

   /**
    * Testing basic work queue functions
    * @param args Input arguments
    */
   public static void main(String[] args) {
      WorkQueue itemsList = new WorkQueue(1);
      LocalDateTime firstDate = LocalDateTime.of(2022, 11, 03, 11, 59);
      LocalDateTime secondDate = LocalDateTime.of(2022, 10, 31, 11, 59);
      Assignment assignment1 = new Assignment("English", "First_Essay", "Essay", firstDate);
      Assignment assignment2 = new Assignment("Math", "Practice_Set1", "HW", secondDate);
      Assignment assignment3 = new Assignment("CS", "Project 1", "Project", secondDate);
      assignment3.changePriority(6);
      itemsList.addToQueue(assignment1);
      itemsList.addToQueue(assignment2);
      itemsList.addToQueue(assignment3);

      itemsList.getAllAssignments();
   }
}
