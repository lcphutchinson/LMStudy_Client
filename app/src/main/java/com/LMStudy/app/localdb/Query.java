package com.LMStudy.app.localdb;

import java.util.List;

public class Query implements Runnable {
   private List<WorkRecord> result;

   public void run(){
      this.result = WorkDB.getInstance().workdao().returnWorkRecords();
   }

   public List<WorkRecord> getResult() {
      return this.result;
   }

}
