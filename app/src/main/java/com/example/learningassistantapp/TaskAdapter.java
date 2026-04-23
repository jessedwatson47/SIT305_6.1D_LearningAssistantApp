package com.example.learningassistantapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }

    private List<Task> tasks;
    private OnTaskClickListener listener;
    private static int VIEW_TYPE_LOADING = 0;
    private static int VIEW_TYPE_TASK = 1;

    @Override
    public int getItemViewType(int position) {
        Task task = tasks.get(position);
        return task.isLoading ? VIEW_TYPE_LOADING : VIEW_TYPE_TASK;
    }

    public TaskAdapter(List<Task> tasks, OnTaskClickListener listener) {
        this.tasks = tasks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_LOADING) {
            View view = inflater.inflate(R.layout.item_task_loading, parent, false);
            return new LoadingViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_task, parent, false);
            return new TaskViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Task task = tasks.get(position);

        if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder h = (LoadingViewHolder) holder;
            h.title.setText("AI Generating a task!");
            h.desc.setText("Fetching...");
        }

        if (holder instanceof TaskViewHolder)
        {
            TaskViewHolder h = (TaskViewHolder) holder;
            h.title.setText(task.title);
            h.desc.setText(task.desc);
            h.itemView.setOnClickListener(v -> listener.onTaskClick(task));
        }


    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;

        public TaskViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.resultTitle);
            desc = itemView.findViewById(R.id.resultText);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        TextView desc;
        TextView title;


        public LoadingViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.taskLoadingTitle);
            desc = itemView.findViewById(R.id.taskLoadingDesc);
        }
    }


}