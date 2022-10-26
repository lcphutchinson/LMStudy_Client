package com.LMStudy.app.io;

/**
 * Https communications manager; launches and processes server calls. (Not currently functioning)
 */
public class SyncService {
   private ServerCall caller = new ServerCall();

   public static SyncService instance = new SyncService();

   private SyncService() {
      //Security.insertProviderAt(Conscrypt.newProvider(), 1);
   }

   public static SyncService getInstance(){
      return instance;
   }


   public void isAvailable() {
      Thread callerThread = new Thread(caller);
      callerThread.start();
   }

}
