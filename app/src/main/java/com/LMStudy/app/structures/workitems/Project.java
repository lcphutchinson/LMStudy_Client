package com.LMStudy.app.structures.workitems;

import com.LMStudy.app.structures.NewCourse;

public class Project extends WorkItem {

   /**
    * "Slow" Constructor. See WorkItem
    * @param course a Course referent for this Project
    * @param name display name
    * @param due duedate string for display
    * @param priority user-defined priority input for sorting
    * @param hours user-defined expected work length, for sorting
    */
   public Project(NewCourse course, String name, String due, Integer priority, Integer hours) {
      super(course, name, due, priority, hours);
   }

   /**
    * "Fast" Constructor. See WorkItem
    * @param course a Course referent for this Project
    * @param id server-assigned id string
    * @param name display name
    * @param due duedate string for display
    * @param priority user-defined priority input for sorting
    * @param hours user-defined expected work length, for sorting
    * @param progress user-submitted progress value, for tracking
    */
   public Project(NewCourse course, String id, String name, String due, Integer priority, Integer hours, Integer progress) {
      super(course,id,name,due,priority,hours,progress);
   }

   /**
    * Custom Comparator for Project items--provides more precise, type-dependent sorting.
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
            return (-1) * item.compareTo(this);
         }
         if (item instanceof Project) {
            return this.typeMatchedCompare(item);
         }
         if (item instanceof Exam) {
            return -1; //assignments before evaluations
         }
         else return -1;
      }
   }
}
