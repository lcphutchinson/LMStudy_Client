package com.LMStudy.app.localdb;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = WorkRecord.class, version = 1)
public abstract class WorkDB extends RoomDatabase {
   private static final String DB_Name = "Work_DB";
   private static WorkDB instance;

   public static synchronized WorkDB getInstance(){
      return instance;
   }
   public static synchronized WorkDB getInstance(Context context) {
      if (instance == null) {
         instance = Room.databaseBuilder(context.getApplicationContext(), WorkDB.class, DB_Name)
            .fallbackToDestructiveMigration()
            .build();
      }
      return instance;
   }
      public abstract WorkDAO workdao();

}
