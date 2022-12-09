package com.LMStudy.app.structures.workitems;


import com.LMStudy.app.structures.NewCourse;

public class Quiz extends WorkItem implements Comparable<WorkItem> {

   /**
    * "Slow" Constructor. See WorkItem
    * @param course a Course referent for this Quiz
    * @param name display name
    * @param due duedate string for display
    * @param priority user-defined priority input for sorting
    * @param hours user-defined expected work length, for sorting
    */
   public Quiz(NewCourse course, String name, String due, Integer priority, Integer hours) {
      super(course,name,due,priority,hours);
   }

   /**
    * "Fast" Constructor. See WorkItem
    * @param course a Course referent for this Quiz
    * @param id server-assigned id string
    * @param name display name
    * @param due duedate string for display
    * @param priority user-defined priority input for sorting
    * @param hours user-defined expected work length, for sorting
    * @param progress user-submitted progress value, for tracking
    */
   public Quiz(NewCourse course, String id, String name, String due, Integer priority, Integer hours, Integer progress) {
      super(course,id,name,due,priority,hours,progress);
   }

   @Override
   public String getType() {
      return "Quiz";
   }

   /**
    * Custom Comparator for Quiz items--provides more precise, type-dependent sorting.
    * @param item the input item for comparison
    * @return an Integer value used for sorting
    */
   public int compareTo(WorkItem item) {
      int parentVal = super.compareTo(item);
      if (parentVal != 0) return parentVal;
      else {
         if (item instanceof Homework) {
            return (-1) * item.compareTo(this);
         }
         if (item instanceof Quiz) {
            return typeMatchedCompare(item);
         }
         if (item instanceof Project) {
            return 1; //assignments before evaluations
         }
         if (item instanceof Exam) {
            return 1; //large items before small ones
         }
         return 0; //placeholder
      }
   }
}
