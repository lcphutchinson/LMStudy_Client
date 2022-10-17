package com.LMStudy.app.structures;

import com.LMStudy.app.localdb.dbService;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;

/**
 * WorkQueue-like object with (not yet implemented) built-in sorting via TreeSet
 */
public class WorkFlow {
   private final dbService writer = new dbService();
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

   /**
    * Loads bulk items from local storage into the Treeset and marks expired items for deletion.
    * @param inflow : WorkItem List produced by the dbService
    */
   public void loadDBItems(ArrayList<WorkItem> inflow) {
      ArrayList<String> expiredItems = new ArrayList<>();
      Date loadTime = new Date();
      items.forEach(item -> {
         if(item.getDueDate().after(loadTime)) items.add(item);
         else expiredItems.add(item.getID());
      });
      if(!expiredItems.isEmpty()) writer.startDelete(expiredItems);
   }

   public void insert(WorkItem item) {
      if(!items.contains(item)){
         items.add(item);
         writer.startInsert(item);
      }
   }

   //there is room here for a method that marks an item complete, using an index input--if that proves useful for the UI

}
