package com.LMStudy.app.localdb;

import android.content.Context;
import com.LMStudy.app.structures.WorkItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Database read/write manager. Handles threading for db processes.
 */
public class dbService {
   private Query query;
   private Insert insert;
   private Delete delete;

   public void startLoad() {
      query = new Query();
      Thread queryThread = new Thread(query);
      queryThread.start();
   }

   public void startInsert(WorkItem item) {
      WorkRecord record = new WorkRecord(item.getID(), itemToString(item));
      insert = new Insert();
      insert.setNewRecord(record);
      Thread insertThread = new Thread(insert);
      insertThread.start();
   }

   public void startDelete(List<String> ids) {
      ArrayList<WorkRecord> targets = new ArrayList<>();
      ids.forEach(id -> {
         targets.add(new WorkRecord(id, null));
      });
      delete = new Delete();
      delete.setTargets(targets);
      Thread deleteThread = new Thread(delete);
      deleteThread.start();

   }

   public ArrayList<WorkItem> getWorkFlow() {
      ArrayList<WorkItem> results = new ArrayList<>();
      this.query.getResult().forEach(record ->
         results.add(itemFromString(record.item)));
      return results;
   }

   public WorkItem itemFromString(String jsonString){
      return new Gson().fromJson(jsonString, WorkItem.class);
   }

   public String itemToString(WorkItem item) {
      Gson gson = new Gson();
      return gson.toJson(item);
   }
}
