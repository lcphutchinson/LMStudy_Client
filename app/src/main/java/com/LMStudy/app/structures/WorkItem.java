package com.LMStudy.app.structures;


import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

/**
 * Minimal scheduling unit--later we'll write extending subclasses with more details
 */
public class WorkItem implements Comparable<WorkItem>,Serializable {
   private static final String USER_DEFINED_CC = "00";
   private String name;
   private Integer priority; //strictly internal, for sorting
   private Boolean complete = false;
   private Date dueDate;

   public WorkItem(String name, Date dueDate){
      this.name = name;
      this.dueDate = dueDate;
   }

   public String getName() {
      return this.name;
   }

   // for use as a db key
   public String getID() {
      return USER_DEFINED_CC + "_" + this.name;
   }

   public Boolean isComplete() {
      return this.complete;
   }

   public void switchComplete() {
      this.complete = !this.complete;
   }

   public Date getDueDate() {
      return this.dueDate;
   }

   // for display purposes, as opposed to comparison
   public String getDueDateString() {
      return DateFormat.getDateInstance().format(this.dueDate);
   }

   // We'll use this for our sorting logic later!
   @Override
   public int compareTo(WorkItem workItem) {
      return 0;
   }
}
