package com.LMStudy.app.teacher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.LMStudy.app.R;
import com.LMStudy.app.structures.Course;

import java.util.List;

public class TeacherRecyclerAdapter extends RecyclerView.Adapter<TeacherRecyclerAdapter.MyViewHolder> {

    private Context context;
    List<Course> itemsList;

    public TeacherRecyclerAdapter(Context context, List<Course> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.course_row_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.course_name.setText(itemsList.get(position).getCourseName());
        holder.course_number.setText(itemsList.get(position).getCourseNumber());
        holder.department.setText(itemsList.get(position).getDepartment());
        // holder.section_numbers.setText(itemsList.get(position).getSectionNumbers());
    }
    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public void removeAt(int position) {
        itemsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemsList.size());
    }

    public String getItemName(int position) {
        return itemsList.get(position).getCourseName();
    }

    public String getItemCourseNumber(int position) {
        return itemsList.get(position).getCourseNumber();
    }

    public String getItemDepartment(int position) {
        return itemsList.get(position).getDepartment();
    }
    /*
    public String getSectionNumber(int position) {
        return itemsList.get(position).getSectionNumber();
    }
   */

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView course_name, course_number, department;// section_number;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            course_name = itemView.findViewById(R.id.t_course_name);
            course_number = itemView.findViewById(R.id.t_course_number);
            department = itemView.findViewById(R.id.t_department);
            //section_number = itemView.findViewById(R.id.t_section_numbers_list);
        }
    }

}
