package com.LMStudy.app.io;

/**
 * Http(s) communications manager; launches and processes server calls. (Not currently functioning)
 */
public class SyncService {
   private ServerCall caller = new ServerCall();

   public static SyncService instance = new SyncService();

   private SyncService() {
   }

   public static SyncService getInstance(){
      return instance;
   }

   public Boolean isAvailable() {
      Thread callerThread = new Thread(caller);
      callerThread.start();

      Object call = caller.getResponse();
      if (call instanceof Boolean){
         Boolean boolAnswer = (Boolean) call;
         return boolAnswer;
      }
      else {
         return false;
      }
   }



}
