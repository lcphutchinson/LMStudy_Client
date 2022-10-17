package com.LMStudy.app.localdb;
import androidx.room.Dao;
import androidx.room.OnConflictStrategy;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WorkDAO {
   @Query("SELECT * FROM work_records")
   List<WorkRecord> returnWorkRecords();
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   void insertRecords(List<WorkRecord> records);
   @Delete
   void deleteRecords(List<WorkRecord> records);

}
