package com.LMStudy.app.structures;

import com.LMStudy.app.structures.workitems.WorkItem;

import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkFlow {
   private static WorkFlow instance = new WorkFlow();
   private List<NewCourse> courses;
   private TreeSet<WorkItem> items;

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

   public WorkItem getFirst() { return items.first(); }

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

   public Boolean add() {
      return false;
   }

   public Boolean remove() {
      return false;
   }

}