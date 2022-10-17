package com.LMStudy.app.localdb;

import java.util.List;

public class Delete implements Runnable {
   private List<WorkRecord> targets;

   public void run(){
      WorkDB.getInstance().workdao().deleteRecords(targets);
   }

   public void setTargets(List<WorkRecord> records){
      this.targets = records;
   }
}
