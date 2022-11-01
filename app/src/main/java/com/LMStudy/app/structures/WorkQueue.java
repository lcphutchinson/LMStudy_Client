package com.LMStudy.app.structures;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Queue for managing general assignments
 */
public class WorkQueue{
   private TreeSet<Assignment> items;
   private long userId;

   private static WorkQueue instance = new WorkQueue();

   /**
    * User's work queue
    * //@param userId Assigned ID of user as received from Canvas.
    */
   private WorkQueue() {
      items = new TreeSet<Assignment>();
      //example assignment for testing
      items.add(new Assignment());
      //this.userId = userId;
   }

   public static WorkQueue getInstance() {
      return instance;
   }

   public TreeSet<Assignment> getWorkQueue() {
      return items;
   }

   public void populate(List<Assignment> input){
      input.forEach(item -> items.add(item));
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

   public int getNumElements() {
      return items.size();
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

   public ArrayList<Assignment> convertToArrayList() {
      return new ArrayList<>(items);
   }

   /**
    * Testing basic work queue functions
    * @param args Input arguments
    */
   public static void main(String[] args) {
      LocalDateTime firstDate = LocalDateTime.of(2022, 11, 03, 11, 59);
      LocalDateTime secondDate = LocalDateTime.of(2022, 10, 31, 11, 59);
      Assignment assignment1 = new Assignment("English", "First_Essay", "Essay", firstDate);
      Assignment assignment2 = new Assignment("Math", "Practice_Set1", "HW", secondDate);
      Assignment assignment3 = new Assignment("CS", "Project 1", "Project", secondDate);
      assignment3.changePriority(6);
      instance.addToQueue(assignment1);
      instance.addToQueue(assignment2);
      instance.addToQueue(assignment3);

      instance.getAllAssignments();

   }
}
