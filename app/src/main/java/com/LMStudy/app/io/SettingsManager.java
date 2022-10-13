package com.LMStudy.app.io;


import java.io.File;

/**
 * IO class for loading internal user settings
 */
public class SettingsManager {
   private File settingsFile;

   private static SettingsManager instance = new SettingsManager();

   public SettingsManager(){

   }

   public SettingsManager getInstance() {
      return instance;
   }

}
