package com.LMStudy.app.localdb;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "work_records")
public class WorkRecord {
   @PrimaryKey
   @NonNull
   public String id;
   @ColumnInfo
   public String item;

   public WorkRecord(String id, String item){
      this.id = id;
      this.item = item;
   }
}
