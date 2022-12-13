package com.LMStudy.app.structures;

/**
 * Data unit for tracking enrolled courses. Referenced by Drop Operations.
 * @author: Larson Pushard Hutchinson, Yulie Ying
 */
public class NewCourse {
   /**
    * Default course used by the system for Student user submissions.
    * Used for assignments that are not published by a course.
    */
   public static final NewCourse SELF_ASSIGNED = new NewCourse("SELF","SELF","");

   /**
    * A server-assigned identification string used as a storage key.
    * Allows for duplicate course names in the datastore.
    */
   private String id;

   /**
    * Display name for this course. Used as an identifier by the user.
    */
   private String name;

   /**
    * A server-assigned password string required by Teacher users
    * for joining this course as an administrator.
    */
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
    * Getter for course Id: used to pass a course as a string.
    * @return the unique Id string associated with this course.
    */
   public String getCId() { return this.id; }

   /**
    * Setter method for server-generated course codes.
    * Invoked following user-prompted Course creation.
    * Note: Post-hoc code insertion is not used in v1, but has been retained for future versions.
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

}
