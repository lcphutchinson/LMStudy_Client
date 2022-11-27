package com.LMStudy.app;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder>{
   @NonNull
   @Override
   public ListAdapter.ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      return null;
   }

   @Override
   public void onBindViewHolder(@NonNull ListAdapter.ListHolder holder, int position) {

   }

   @Override
   public int getItemCount() {
      return 0;
   }

   public static class ListHolder extends RecyclerView.ViewHolder {


      public ListHolder(@NonNull View itemView) {
         super(itemView);
      }
   }
}
