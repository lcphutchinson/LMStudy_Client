package com.LMStudy.app.localdb;

import java.util.List;

public class Insert implements Runnable {
   WorkRecord newRecord;

   public void run(){ WorkDB.getInstance().workdao().insertRecords((List<WorkRecord>) newRecord);

   }

   public void setNewRecord(WorkRecord record) {
      this.newRecord = record;
   }
}
