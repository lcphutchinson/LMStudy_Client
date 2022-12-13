package com.LMStudy.app.structures.workitems;

import com.LMStudy.app.structures.NewCourse;

/**
 * Basic concrete object for task management--represents the generic assignment.
 * Sorted before assessments and before other assignments.
 * @author: Larson Pushard Hutchinson
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
    * @param course a Course referent for this Homework
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
    * Display method for fetching item type
    * @return a String label for type.
    */
   @Override
   public String getType() {
      return "Homework";
   }

   /**
    * Custom Comparator for Homework items--provides more precise, type-dependent sorting.
    * @param item the input item for comparison
    * @return an Integer value used for sorting
    */
   public int compareTo(WorkItem item) {
      int parentVal = super.compareTo(item);
      if (parentVal != 0) return parentVal; //only compare items in the same priority tier
      else {
         //type compare prioritizes assignments over evaluations and large items over small items
         if (item instanceof Homework) {
            return this.typeMatchedCompare(item);
         }
         else if (item instanceof Quiz) {
            return -1; //assignments over evaluations
         }
         else if (item instanceof Project) {
            return 1; //large items over small ones
         }
         else if (item instanceof Exam) {
            return -1; //assignments over evaluations
         }
         else return -1; //if a new type is implemented but not integrated, put it last.
      }
   }
}
