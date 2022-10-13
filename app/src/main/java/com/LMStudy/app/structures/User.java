package com.LMStudy.app.structures;


import com.LMStudy.app.io.Form;

/**
 * Data Unit for user settings
 */
public class User {
   private static final int STUDENT = '0';
   private static final int TEACHER = '1';

   private int userType;


   private static User instance = new User();

   //takes a Form object
   private User(){

   }

   public static User getInstance() {
      return instance;
   }

}
