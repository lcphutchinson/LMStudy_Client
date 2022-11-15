package com.LMStudy.app.student;

import com.LMStudy.app.structures.Assignment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.LMStudy.app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context context;
    //List<AssignmentItem> itemsList;
    List<Assignment> itemsList;

    public RecyclerAdapter(Context context, List<Assignment> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.assignment_name.setText(itemsList.get(position).getAssignmentName());
        holder.assignment_type.setText(itemsList.get(position).getAssignmentType());
        holder.course_info.setText(itemsList.get(position).getCourseInfo());
        holder.due_date.setText(itemsList.get(position).getDueDate());
    }

    @Override
    public int getItemCount() {
        System.out.println(itemsList.size());
        return itemsList.size();
    }

    public void removeAt(int position) {
        itemsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemsList.size());
    }

    public String getItemName(int position) {
        return itemsList.get(position).getAssignmentName();
    }

    public String getItemType(int position) {
        return itemsList.get(position).getAssignmentType();
    }

    public String getItemCourse(int position) {
        return itemsList.get(position).getCourseInfo();
    }

    public String getItemDueDate(int position) {
        return itemsList.get(position).getDueDate();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView assignment_name, assignment_type, course_info, due_date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            assignment_name = itemView.findViewById(R.id.assignment_name);
            assignment_type = itemView.findViewById(R.id.assignment_type);
            course_info = itemView.findViewById(R.id.course_info);
            due_date = itemView.findViewById(R.id.due_date);
        }
    }

}
