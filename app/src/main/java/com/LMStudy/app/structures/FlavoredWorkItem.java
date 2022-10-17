package com.LMStudy.app.structures;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Compatibility class for Yulie's Student UI--has extra detail strings
 */

public class FlavoredWorkItem extends WorkItem implements Comparable<WorkItem>, Serializable {
   private String type;
   private String course;

   public FlavoredWorkItem(String name, String type, String course, String dateString) {
      super(name, null);
      try {
         this.dueDate = new SimpleDateFormat("mm/dd/yyyy").parse(dateString);
      }
      catch(ParseException e) {
         e.printStackTrace();
      }
      this.type = type;
      this.course = course;

   }

   public String getTypeStr() {
      return this.type;
   }

   public String getCourse() {
      return this.course;
   }

   // Example of how a later special workItem type (exam, homework, etc.) can have its own compareTo for sorting
   @Override
   public int compareTo(WorkItem workItem) {
      return 0;
   }
}
