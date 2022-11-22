package com.LMStudy.app.structures;

/**
 * Basic concrete object for task management--represents the generic assignment.
 */
public class Homework extends WorkItem implements Comparable<WorkItem> {

   /**
    * "Slow" Constructor. See WorkItem
    * @param name display name
    * @param due duedate string for display
    * @param priority user-defined priority input for sorting
    * @param hours user-defined expected work length, for sorting
    */
   public Homework(NewCourse course, String name, String due, Integer priority, Integer hours) {
      super(course,name,due,priority,hours);
   }

   /**
    * "Fast" Constructor. See WorkItem
    * @param id server-assigned id string
    * @param name display name
    * @param due duedate string for display
    * @param priority user-defined priority input for sorting
    * @param hours user-defined expected work length, for sorting
    * @param progress user-submitted progress value, for tracking
    */
   public Homework(NewCourse course, String id, String name, String due, Integer priority, Integer hours, Integer progress) {
      super(course,id,name,due,priority,hours,progress);
   }

   /**
    * Custom Comparator for Homework items--provides more precise, type-dependent sorting.
    * @param item the input item for comparison
    * @return an Integer value used for sorting
    */
   public int compareTo(WorkItem item) {
      int parentVal = super.compareTo(item);
      if (parentVal != 0) return parentVal;
      else {
         if (item instanceof Homework) {
            //conditionalLogic
         }
         if (item instanceof Quiz) {
            // more here
         }
         return 0; //placeholder
      }
   }
}
