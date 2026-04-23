package com.example.learningassistantapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.InterestViewHolder> {

    public interface OnInterestClickListener {
        void onInterestClick(String interest);
    }

    private List<String> interests = InterestsData.Interests;
    private Set<String> selected = new HashSet<>();
    private OnInterestClickListener listener;

    public InterestAdapter(List<String> interests, OnInterestClickListener listener) {
        this.interests = interests;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InterestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_interest, parent, false);
        return new InterestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestViewHolder holder, int position) {
        String interest = interests.get(position);

        holder.name.setText(interest);

        if (selected.contains(interest))
        {
            holder.itemView.setBackgroundResource(R.drawable.interest_selected_background);
        }
        else
        {
            holder.itemView.setBackgroundResource(R.drawable.interest_background);
        }

        holder.itemView.setOnClickListener(v -> {
            if (selected.contains(interest))
            {
                selected.remove(interest);
            }
            else
            {
                selected.add(interest);
            }
            notifyItemChanged(position);

            if (listener != null) {
                listener.onInterestClick(interest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return interests.size();
    }

    static class InterestViewHolder extends RecyclerView.ViewHolder {
        TextView name;


        public InterestViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.interestName);

        }
    }
}