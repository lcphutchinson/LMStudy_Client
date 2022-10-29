package com.LMStudy.app.structures;

import java.util.ArrayList;
import java.util.Date;
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

   public WorkItem[] getArray() {
      return items.toArray(new WorkItem[0]);
   }

   //there is room here for a method that marks an item complete, using an index input--if that proves useful for the UI

}
