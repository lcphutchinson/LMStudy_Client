package com.LMStudy.app.structures;

import java.util.TreeSet;

/**
 * Queue for managing Student User assignments.
 */
public class WorkQueue {
   private TreeSet<Assignment> items;

   private WorkQueue instance = new WorkQueue();

   private WorkQueue() {

   }
}
