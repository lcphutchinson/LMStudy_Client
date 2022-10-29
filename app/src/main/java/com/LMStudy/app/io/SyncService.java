package com.LMStudy.app.io;

/**
 * Https communications manager;
 */
public class SyncService {
   private ServerCall caller = new ServerCall();

   public static SyncService instance = new SyncService();

   public static SyncService getInstance(){
      return instance;
   }


   public void isAvailable() {
      Thread callerThread = new Thread(caller);
      callerThread.start();
   }

}
