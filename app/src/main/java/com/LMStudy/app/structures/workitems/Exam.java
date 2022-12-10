package com.LMStudy.app.structures.workitems;

import com.LMStudy.app.structures.NewCourse;

/**
 * Exam WorkItem--Sorted after other examinations
 * @author: Larson Pushard Hutchinson
 */
public class Exam extends WorkItem {

   /**
    * "Slow" Constructor. See WorkItem
    * @param course a Course referent for this Exam
    * @param name display name
    * @param due duedate string for display
    * @param priority user-defined priority input for sorting
    * @param hours user-defined expected work length, for sorting
    */
   public Exam(NewCourse course, String name, String due, Integer priority, Integer hours) {
      super(course, name, due, priority, hours);
   }

   /**
    * "Fast" Constructor. See WorkItem
    * @param course a Course referent for this Exam
    * @param id server-assigned id string
    * @param name display name
    * @param due duedate string for display
    * @param priority user-defined priority input for sorting
    * @param hours user-defined expected work length, for sorting
    * @param progress user-submitted progress value, for tracking
    */
   public Exam(NewCourse course, String id, String name, String due, Integer priority, Integer hours, Integer progress) {
      super(course,id,name,due,priority,hours,progress);
   }

   /**
    * Display method for fetching item type
    * @return a String label for type.
    */
   @Override
   public String getType() {
      return "Exam";
   }

   /**
    * Custom Comparator for Exam items--provides more precise, type-dependent sorting.
    * @param item the input item for comparison
    * @return an Integer value used for sorting
    */
   public int compareTo(WorkItem item) {
      int parentVal = super.compareTo(item);
      if (parentVal != 0) return parentVal;
      else {
         if (item instanceof Homework) {
            return (-1) * item.compareTo(this); //example of how to handle written comparator
         }
         if (item instanceof Quiz) {
            return (-1) * item.compareTo(this);
         }
         if (item instanceof Project) {
            return (-1) * item.compareTo(this);
         }
         if (item instanceof Exam) {
            return this.typeMatchedCompare(item);
         } else return -1;
      }
   }
}
