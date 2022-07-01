package com.example.visualphysics10.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualphysics10.databinding.FragmentItemBinding;
import com.example.visualphysics10.databinding.FragmentItemLabBinding;
import com.example.visualphysics10.databinding.FragmentItemLecBinding;
import com.example.visualphysics10.ph_lab.PlaceHolderContent4;
import com.example.visualphysics10.ph_lesson.PlaceholderContent;
import com.example.visualphysics10.ph_task.PlaceHolderContent2;
import com.example.visualphysics10.ph_lectures.PlaceHolderContent3;
import com.example.visualphysics10.ui.lab.LabFragmentList;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<PlaceHolderContent4.PlaceHolderItem4> valForLab;
    public List<PlaceholderContent.PlaceHolderItem> mValues;
    public List<PlaceHolderContent2.PlaceHolderItem2> valForTask;
    public List<PlaceHolderContent3.PlaceHolderItem3> valForLec;
    public final boolean mainList;
    public final boolean taskList;
    private final boolean lecList;
    private final OnLessonListener onLessonListener;

    //TODO: Our Recycler adapter for Placeholder and for PlaceHolder2, created with navigation library

    public RecyclerViewAdapter(List<PlaceholderContent.PlaceHolderItem> items, OnLessonListener onLessonListener) {
        mValues = items;
        mainList = true;
        taskList = false;
        lecList = false;
        this.onLessonListener = onLessonListener;
    }
    public RecyclerViewAdapter(List<PlaceHolderContent2.PlaceHolderItem2> items, OnLessonListener onLessonListener, String tag) {
        valForTask = items;
        taskList = true;
        mainList = false;
        lecList = false;
        this.onLessonListener = onLessonListener;
    }
    public RecyclerViewAdapter(List<PlaceHolderContent3.PlaceHolderItem3> items, OnLessonListener onLessonListener, String tag, int i) {
        valForLec = items;
        mainList = false;
        taskList = false;
        lecList = true;
        this.onLessonListener = onLessonListener;
    }

    public RecyclerViewAdapter(List<PlaceHolderContent4.PlaceHolderItem4> items, OnLessonListener onLessonListener, String for_lab_fragment, boolean b, int i) {
        valForLab = items;
        mainList = b;
        taskList = b;
        lecList = b;
        this.onLessonListener = onLessonListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (mainList) return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from
                (parent.getContext()), parent, false), onLessonListener);

        else if (taskList) return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from
                (parent.getContext()), parent, false), onLessonListener);

        else if(lecList) return new ViewHolder(FragmentItemLecBinding.inflate(LayoutInflater.from
                (parent.getContext()), parent, false), onLessonListener);

        else return new ViewHolder(FragmentItemLabBinding.inflate(LayoutInflater.from
                (parent.getContext()), parent, false), onLessonListener);
    }

    //insert fields of the ViewHolder class into the recycler
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mainList) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).title);
            holder.imageView.setImageResource(mValues.get(position).imageView);
        }else if(taskList){
            holder.taskItem = valForTask.get(position);
            holder.mIdView.setText(valForTask.get(position).title);
            holder.imageView.setImageResource(valForTask.get(position).imageView);
        }else if (lecList){
            holder.LecItem = valForLec.get(position);
            holder.mIdView.setText(valForLec.get(position).title);
            holder.imageView.setImageResource(valForLec.get(position).imageView);
        }else{
            holder.LabItem = valForLab.get(position);
            holder.mIdView.setText(valForLab.get(position).title);
            holder.textBody.setText(valForLab.get(position).body);
        }
    }

    //how amount item (lesson) in Recycler
    @Override
    public int getItemCount() {
        if (mainList) return mValues.size();
        else if (taskList) return valForTask.size();
        else if(lecList) return valForLec.size();
        else return valForLab.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mIdView;
        public PlaceholderContent.PlaceHolderItem mItem;
        public PlaceHolderContent2.PlaceHolderItem2 taskItem;
        public PlaceHolderContent3.PlaceHolderItem3 LecItem;
        public PlaceHolderContent4.PlaceHolderItem4 LabItem;
        public int position;
        OnLessonListener onLessonListener;
        public ImageView imageView;
        public Button button;
        public TextView textBody;

        //fields initialization
        public ViewHolder(FragmentItemBinding binding, OnLessonListener onLessonListener) {
            super(binding.getRoot());
            mIdView = binding.title;
            this.onLessonListener = onLessonListener;
            imageView = binding.imageOfLessons;
            itemView.setOnClickListener(this);
        }

        public ViewHolder(FragmentItemLecBinding binding, OnLessonListener onLessonListener) {
            super(binding.getRoot());
            mIdView = binding.title;
            this.onLessonListener = onLessonListener;
            imageView = binding.imageOfLessons;
            itemView.setOnClickListener(this);
        }

        public ViewHolder(FragmentItemLabBinding binding, OnLessonListener onLessonListener) {
            super(binding.getRoot());
            mIdView = binding.title;
            textBody = binding.body;
            this.onLessonListener = onLessonListener;
            itemView.setOnClickListener(this);
        }

        //position initialization using custom interface and transmits to abstractClass for work with FragmentInfo and FragmentTest
        @Override
        public void onClick(View v) {
            position = getLayoutPosition();
            onLessonListener.onLessonClick(getLayoutPosition());
        }
    }

    //interface transition from the list to the desired fragment
    public interface OnLessonListener{
        void onLessonClick(int position);
    }
}
