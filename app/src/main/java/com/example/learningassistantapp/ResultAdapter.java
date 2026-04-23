package com.example.learningassistantapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Result> results;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_RESULT = 1;

    public ResultAdapter(List<Result> results) {
        this.results = results;
    }

    @Override
    public int getItemViewType(int position) {
        Result result = results.get(position);
        return result.isLoading ? VIEW_TYPE_LOADING : VIEW_TYPE_RESULT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_LOADING) {
            View view = inflater.inflate(R.layout.item_task_loading, parent, false);
            return new LoadingViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_result, parent, false);
            return new ResultViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Result result = results.get(position);

        if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder h = (LoadingViewHolder) holder;
            h.title.setText("AI Generating an explanation!");
            h.desc.setText("Fetching...");
        }

        if (holder instanceof ResultViewHolder) {
            ResultViewHolder h = (ResultViewHolder) holder;
            h.title.setText(result.questionTitle);
            h.desc.setText(result.resultText);
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    static class ResultViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;

        public ResultViewHolder(View itemView) {
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