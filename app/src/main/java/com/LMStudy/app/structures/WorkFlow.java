package com.LMStudy.app.structures;

import com.LMStudy.app.structures.workitems.WorkItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Principle Data Structure for the LMStudy User.
 * Maintains a sorted list of WorkItems and a list of their course references.
 * @author: Larson Pushard Hutchinson, Yulie Ying
 */
public class WorkFlow {

   /**
    * Singleton Pattern instance for shared use across classes.
    */
   private static WorkFlow instance = new WorkFlow();

   /**
    * A List of courses registered to this user, including the static SELF course for Students.
    */
   private List<NewCourse> courses = new ArrayList<NewCourse>();

   /**
    * Self-sorting set of WorkItems for the current User.
    */
   private TreeSet<WorkItem> items = new TreeSet<WorkItem>();

   /**
    * Fetch the Singleton instance of WorkFlow
    * @return a shared WorkFlow object
    */
   public static WorkFlow getInstance() {
      return instance;
   }

   /**
    * Assigns a pre-generated list of Courses to the courses field.
    * Used to set the initial course list when pulling courses from the server.
    * @param input A list of Course objects.
    */
   public void populateCourses(List<NewCourse> input) {
      try {
         this.courses = input;
      } catch (NullPointerException e) {
         e.printStackTrace();
         throw e;
      }
   }

   /**
    * Corrector method for Student users. Adds the static SELF course to courses, as
    * it is not transmitted by the server.
    */
   public void addSelfCourse() {
      this.courses.add(NewCourse.SELF_ASSIGNED);
   }

   /**
    * Assigns a pre-generated List of WorkItems to the items field.
    * Used to set the initial items list when pulling items from the server.
    * @param input A list of WorkItem objects
    */
   public void populateItems(List<WorkItem> input) {
      try {
         this.items = new TreeSet<>(input);
      } catch (NullPointerException e) {
         e.printStackTrace();
         throw e;
      }
   }

   /**
    * Verification method for the items list. Checks of items exist in the WorkFlow.
    * @return True if there are items in the WorkFlow, False otherwise.
    */
   public Boolean hasItems() { return !items.isEmpty(); }

   /**
    * Getter for the course list, for passing to UI controls.
    * @return the Course List associated with this user.
    */
   public List<NewCourse> getCourseList() { return courses; }

   /**
    * Locates a Course object in the courses list using its server-assigned identifier string.
    * Throws a NoSuchElementException if no course is stored with the given id.
    * @param id A string id referring to this course
    * @return the corresponding Course object.
    */
   public NewCourse getCourseById(String id) {
      int len = this.courses.size();
      System.out.println(len);
      System.out.println(id);
      for(int i=0;i<len;i++) if (courses.get(i).getData()[1].equals(id)) return courses.get(i);
      throw new NoSuchElementException();
   }

   /**
    * Retrieves the highest priority item from the WorkFlow, in accordance with its internal comparators.
    * Used for populating the Student Menu Next Item preview.
    * @return A single WorkItem at the front of the items TreeSet.
    */
   public WorkItem getFirst() { return items.first(); }

   /**
    * Simple getter for the items list. Used by the recyclerView UI.
    * @return all the items in the WorkFlow, as an ArrayList.
    */
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

   /**
    * Adds a single pre-constructed WorkItem object into the items list.
    * @param item a WorkItem for adding
    * @return a Boolean indicating a successful/failed add.
    */
   public Boolean add(WorkItem item) {
      return items.add(item);
   }

   /**
    * Removes a single pre-constructed WorkItem object from the items list.
    * @param item a WorkItem for removing
    * @return a Boolean indicating a successful/failed remove.
    */
   public Boolean remove(WorkItem item) {
      return items.remove(item);
   }

   /**
    * Alternative removal method for items. Removes a WorkItem with matching item id.
    * @param id the Id string for matching against remove targets.
    */
   public void removeById(String id) { this.items.removeIf(item -> item.getIID().equals(id) ); }
}
