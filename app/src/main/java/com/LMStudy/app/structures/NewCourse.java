package com.LMStudy.app.structures;

/**
 * Data unit for tracking enrolled courses. Referenced by Drop Operations.
 */
public class NewCourse {
   private static final NewCourse SELF_ASSIGNED = new NewCourse("SELF","SELF","");
   private String id;
   private String name;
   private String pw;

   /**
    * "Slow" Constructor for new Course creation. Used with setCodes().
    * @param name A display name for the course.
    */
   public NewCourse(String name) {
      this.id = "";
      this.name = name;
      this.pw = "";
   }

   /**
    * "Fast" Constructor for course population from server data.
    * @param id A code string for identifying this course
    * @param name A display name for this course
    * @param pw A password for joining this course as administrator
    */
   public NewCourse(String id, String name, String pw) {
      this.id = id;
      this.name = name;
      this.pw = pw;
   }

   /**
    * Setter method for server-generated course codes.
    * Invoked following user-prompted Course creation.
    * @param id A server-generated course code for this course
    * @param pw A server-generated password for this course
    */
   public void setCodes(String id, String pw) {
      this.id = id;
      this.pw = pw;
   }

   /**
    * Custom toString for the Course object; returns the Course's display name
    * @return The Display Name for this course.
    */
   @Override
   public String toString() {
      return this.name;
   }

   /**
    * Getter method for the Course data display window.
    * @return Each of the Course's fields, in their display order.
    */
   public String[] getData() {
      return new String[] { this.name, this.id, this.pw };
   }

   /**
    * Sorting method used to identify a course by its course code.
    * @param id the course id to match against this course
    * @return a Boolean indicating a successful/failed match.
    */
   public boolean hasCode(String id) {
      return this.id.equals(id);
   }

}
