package com.LMStudy.app.structures.workitems;

import com.LMStudy.app.structures.NewCourse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Basic data blueprint for a scheduling item. Forms the basis of the WorkItem class family.
 */
public abstract class WorkItem implements Comparable<WorkItem>{
   /**
    * Helper field for compareTo, which uses the current day in comparisons.
    * Prevents excessive calls to the Date() constructor.
    */
   private static Date benchmark = new Date();

   protected NewCourse course;
   protected String id;
   protected String name;
   protected String due;
   protected Date dueDate;
   protected Integer priority;
   protected Integer hours;
   protected Integer progress;

   /**
    * "Slow" Constructor used in user-prompted creation. Used with setID().
    * @param name display name
    * @param due duedate string for display
    * @param priority user-defined priority input for sorting
    * @param hours user-defined expected work length, for sorting
    */
   public WorkItem(NewCourse course, String name, String due, Integer priority, Integer hours) {
      this.course = course;
      this.id = "";
      this.name = name;
      this.due = due;
      this.dueDate = parseDate();
      this.priority = priority;
      this.hours = hours;
      this.progress = 0;
   }

   /**
    * "Fast" Constructor for WorkItems retrieved from server.
    * @param id server-assigned id string
    * @param name display name
    * @param due duedate string for display
    * @param priority user-defined priority input for sorting
    * @param hours user-defined expected work length, for sorting
    * @param progress user-submitted progress value, for tracking
    */
   public WorkItem(NewCourse course, String id, String name, String due, Integer priority, Integer hours, Integer progress) {
      this.course = course;
      this.id = id;
      this.name = name;
      this.due = due;
      this.dueDate = parseDate();
      this.priority = priority;
      this.hours = hours;
      this.progress = progress;
   }

   /**
    * Utility method for updating the static reference date.
    */
   public static void resetBenchmark(){
      benchmark = new Date();
   }

   /**
    * Compartmental method for string-to-date conversion in construction.
    * @return a Date object for the string dueDate, or Today's date, if parsing fails.
    */
   private Date parseDate() {
      Date result = new Date();
      try {
         result = new SimpleDateFormat("yyyy-MM-dd").parse(this.due);
      } catch (ParseException e) {
         e.printStackTrace();
      }
         return result;
   }

   /**
    * Adds a server-supplied id String to this item--used after local construction.
    * @param id An id string used by the server to identify this item.
    */
   public void setIID(String id) {
      this.id = id;
   }

   /**
    * Getter for the Course reference
    * @return the Course object for this item.
    */
   public NewCourse getCourse() {
      return this.course;
   }

   /**
    * Getter for item id
    * @return the item id for this item.
    */
   public String getIID() {
      return this.id;
   }

   /**
    * Getter for item display name
    * @return the display name for this item
    */
   public String getName() {
      return this.name;
   }

   /**
    * Getter for item due date string
    * @return the display date for this item.
    */
   public String getDisplayDate() {
      return this.due;
   }

   /**
    * Getter for the item due date, used for comparisons
    * @return the due date for this item, as a Date object.
    */
   public Date getRealDate() {
      return this.dueDate;
   }

   /**
    * Getter for item priority
    * @return item priority
    */
   public Integer getPriority() { return this.priority; }

   /**
    * Getter for basic item type to be overriden in respective work items class
    * @return basic item type
    */
   public String getType() {
      return "Work Item";
   }


   /**
    * Getter for prioritization data, used for comparisons
    * @return the priority, hours, and progress values for this item, as an array.
    */
   public Integer[] getPriorityData() {
      return new Integer[] {
         this.priority,
         this.hours,
         this.progress
      };
   }

   /**
    * Classification method for due date, compartmentalized for updatability.
    * @param date A date for classification
    * @return an Integer value representing the date's priority tier.
    */
   protected Integer getPriorityTier(Date date) {
         Long timeLeft = date.getTime() - benchmark.getTime();
         Long daysLeft = TimeUnit.DAYS.convert(timeLeft,TimeUnit.MILLISECONDS);
         if (daysLeft < 3) return daysLeft.intValue();
         if (daysLeft < 6) return 3;
         else return 4;
   }

   /**
    * Calculates an expected workload for this workitem, based on hours and progress fields.
    * @return a float indicating remaining expected hours.
    */
   protected float getRealHoursLeft() {
      if (this.hours == 0) return this.hours;
      else {
         float realProgress = (this.progress / 100) * this.hours;
         return this.hours - realProgress;
      }
   }

   /**
    * Custom toString for displaying a WorkItem on the Student Menu
    * @return A stylized string containing this WorkItem's fields.
    */
   @Override
   public String toString() {
      return "(" + this.course.toString() + ") "
         + this.name + "\n"
         + "Due: " + this.due + "\n"
         + "Progress: " + this.progress + "/" + this.hours;
   }

   /**
    * Custom equals() for the WorkItem class. Uses ID or Course and Name as comparators.
    * @param obj the Object to be compared
    * @return a boolean indicating successful matching.
    */
   @Override
   public boolean equals(Object obj) {
      if (obj instanceof WorkItem) {
         WorkItem item = (WorkItem) obj;
         if(this.id.isEmpty()) {
            return this.course.equals(item.getCourse()) && this.name.equals(item.getName());
         }
         return this.id.equals(item.getIID());
      }
      else return false;
   }

   /**
    * Base Comparator for the workItem family. Provides type-agnostic date-based priority tiers.
    * @param item the input item for comparison
    * @return an integer representing scheduling priority.
    */
   @Override
   public int compareTo(WorkItem item) {
      return getPriorityTier(this.dueDate) - getPriorityTier(item.getRealDate());
   }

   /**
    * Helper method for CompareTo--returns a compareTo result based on priority, workload,
    * or item id, with descending priority.
    * @param item a WorkItem to compare against.
    * @return an integer representing scheduling priority.
    */
   protected int typeMatchedCompare(WorkItem item) {
      int comparedPriorities = this.priority - item.getPriority();
      if (comparedPriorities != 0) return comparedPriorities;
      int comparedWorkloads = this.compareWorkload(item);
      if (comparedWorkloads != 0) return comparedWorkloads;
      else return this.id.compareTo(item.getIID());
   }

   /**
    * Helper method for typeMatchedCompare()--returns a compareTo result based on expected workload
    * @param item a WorkItem to compare against
    * @return an integer representing scheduling priority
    */
   protected int compareWorkload (WorkItem item) {
      float comparedWorkLoads = this.getRealHoursLeft() - item.getRealHoursLeft();
      if (comparedWorkLoads < 0) return -1;
      else if (comparedWorkLoads > 0) return 1;
      else return 0;
   }
}
