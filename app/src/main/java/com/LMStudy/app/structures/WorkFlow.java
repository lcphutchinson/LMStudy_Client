package com.LMStudy.app.structures;

import com.LMStudy.app.localdb.dbService;

import java.util.TreeSet;

/**
 * WorkQueue-like object with (not yet implemented) built-in sorting via TreeSet
 */
public class WorkFlow {

   private TreeSet<WorkItem> items;
   private static WorkFlow instance = new WorkFlow();

   private WorkFlow(){
      items = new TreeSet<WorkItem>();


   }

   public static WorkFlow getInstance() {
      return instance;
   }

   public void insert(WorkItem i) {
      if(!items.contains(i)){
         items.add(i);
         //and launch a db write call
      }
   }

   //remove function replaced by mark function


}
