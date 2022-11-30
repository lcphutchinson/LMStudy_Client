package com.LMStudy.app.structures;

import com.LMStudy.app.structures.workitems.WorkItem;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkFlow {
   private static WorkFlow instance = new WorkFlow();
   private List<NewCourse> courses = new ArrayList<NewCourse>();
   private TreeSet<WorkItem> items = new TreeSet<WorkItem>();

   public static WorkFlow getInstance() {
      return instance;
   }

   public void populateCourses(List<NewCourse> input) {
      this.courses = input;
   }

   public void populateItems(List<WorkItem> input) {
      this.items = new TreeSet<>(input);
   }

   public Boolean hasItems() { return !items.isEmpty(); }

   public Boolean hasCourse(NewCourse course) {
      return courses.contains(course);
   }

   public Boolean hasItem(WorkItem item) {
      return items.contains(item);
   }

   public List<NewCourse> getCourseList() { return courses; }

   public NewCourse getCourseById(String id) {
      int len = this.courses.size();
      for(int i=0;i<len;i++) if (courses.get(i).getData()[1].equals(id)) return courses.get(i);
      throw new NoSuchElementException();
   }

   public WorkItem getFirst() { return items.first(); }

   public ArrayList<WorkItem> getItemsFromCourse(String courseName) {
      final ArrayList<WorkItem> items = new ArrayList<>();
      NewCourse c = null;
      for(int i=0;i<this.courses.size();i++) {
         if(this.courses.get(i).toString().equals(courseName)) c = this.courses.get(i);
      }
      if(c == null) throw new IllegalArgumentException();
      else {
         final NewCourse target = c;
         this.items.forEach(item -> {
            if(item.getCourse() == target) items.add(item);
         });
      }
      return items;
   }

   public ArrayList<WorkItem> getWorkItems() {
      return new ArrayList<WorkItem>(items);
   }

   /**
    * Counts the number of items due within a provided number of days--used by the Forecast UI element
    * @param forecastThreshold The integer number of days beyond the current date to search
    * @return an integer number of WorkItems in the Workflow due within the forecastThreshold
    */
   public Integer getForecast(int forecastThreshold) {
      Long benchmark = new Date().getTime();
      AtomicInteger counter = new AtomicInteger();
      items.forEach(item -> {
         if(TimeUnit.DAYS.convert(
            item.getRealDate().getTime() - benchmark, TimeUnit.MILLISECONDS
         ) < forecastThreshold) counter.getAndIncrement();
      });
      return counter.get();
   }

   public Boolean add(WorkItem item) {
      return items.add(item);
   }

   public Boolean remove(WorkItem item) {
      return items.remove(item);
   }

}
