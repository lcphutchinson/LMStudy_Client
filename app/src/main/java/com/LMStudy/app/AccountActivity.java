package com.LMStudy.app;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.LMStudy.app.structures.Assignment;
import com.LMStudy.app.structures.workitems.WorkItem;

import java.util.List;

public class AccountActivity {
    public static class RecyclerItemClickListener  implements RecyclerView.OnItemTouchListener {
        public static interface OnItemClickListener {
            public void onItemClick(View view, int position);
            public void onItemLongClick(View view, int position);
        }

        private OnItemClickListener mListener;
        private GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
            mListener = listener;

            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());

                    if(childView != null && mListener != null) {
                        mListener.onItemLongClick(childView, recyclerView.getChildPosition(childView));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());

            if(childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildPosition(childView));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent){}

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
    }

    public static class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

        private Context context;
        //List<AssignmentItem> itemsList;
        List<WorkItem> itemsList;

        public RecyclerAdapter(Context context, List<WorkItem> itemsList) {
            this.context = context;
            this.itemsList = itemsList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.assignment_row_view, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.assignment_name.setText(itemsList.get(position).getName());
            holder.assignment_type.setText(itemsList.get(position).getType());
            holder.course_info.setText(itemsList.get(position).getCourse().toString());
            holder.due_date.setText(itemsList.get(position).getDisplayDate());
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
            return itemsList.get(position).getName();
        }

        public String getItemType(int position) {
            return itemsList.get(position).getType();
        }

        public String getItemCourse(int position) {
            return itemsList.get(position).getCourse().toString();
        }

        public String getItemDueDate(int position) {
            return itemsList.get(position).getDisplayDate();
        }

        public int getItemPriority(int position) {
            return itemsList.get(position).getPriority();
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
}
