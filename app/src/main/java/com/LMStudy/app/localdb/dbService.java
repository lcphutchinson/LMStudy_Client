package com.LMStudy.app.localdb;

import android.content.Context;
import com.LMStudy.app.structures.WorkItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Database read/write manager. Handles threading for db processes.
 */
public class dbService {
   private Query query;

   public void startLoad() {
      query = new Query();
      Thread queryThread = new Thread(query);
      queryThread.start();
   }

   public ArrayList<WorkItem> getWorkFlow() {
      ArrayList<WorkItem> results = new ArrayList<>();
      this.query.getResult().forEach(record ->
         results.add(itemFromString(record.item)));
      return results;
   }

   public WorkItem itemFromString(String jsonString){
      Type itemType = new TypeToken<WorkItem>(){}.getType();
      WorkItem item = new Gson().fromJson(jsonString, itemType);
      return item;
   }
}
