package com.example.learningassistantapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<HistoryItem> historyItems;
    private int expandedPosition = RecyclerView.NO_POSITION;

    public HistoryAdapter(List<Task> tasks) {
        this.historyItems = new ArrayList<>();

        for (Task task : tasks) {
            if (task.questions != null) {
                for (Question question : task.questions) {
                    historyItems.add(new HistoryItem(task, question));
                }
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HistoryItem historyItem = historyItems.get(position);

        if (holder instanceof HistoryViewHolder) {
            HistoryViewHolder h = (HistoryViewHolder) holder;

            Task task = historyItem.task;
            Question question = historyItem.question;

            h.metadata.setText(task.title);
            h.title.setText((position + 1) + ". " + question.title);
            h.question.setText(question.question);

            h.answer1Text.setText(question.answer1);
            h.answer2Text.setText(question.answer2);
            h.answer3Text.setText(question.answer3);

            h.answer1Aside.setText("");
            h.answer2Aside.setText("");
            h.answer3Aside.setText("");

            h.answer1Text.setTextColor(0xFFFEFEFE);
            h.answer2Text.setTextColor(0xFFFEFEFE);
            h.answer3Text.setTextColor(0xFFFEFEFE);

            h.answer1Circle.setColorFilter(0xFFFEFEFE);
            h.answer2Circle.setColorFilter(0xFFFEFEFE);
            h.answer3Circle.setColorFilter(0xFFFEFEFE);

            int correctAnswerId = question.correctanswerid;

            if (question.selectedanswerid == 1) {
                h.answer1Aside.setText("Your Answer");

                if (question.selectedanswerid != correctAnswerId) {
                    h.answer1Text.setTextColor(0xFFFF0000);
                    h.answer1Circle.setColorFilter(0xFFFF0000);
                }
            } else if (question.selectedanswerid == 2) {
                h.answer2Aside.setText("Your Answer");

                if (question.selectedanswerid != correctAnswerId) {
                    h.answer2Text.setTextColor(0xFFFF0000);
                    h.answer2Circle.setColorFilter(0xFFFF0000);
                }
            } else if (question.selectedanswerid == 3) {
                h.answer3Aside.setText("Your Answer");

                if (question.selectedanswerid != correctAnswerId) {
                    h.answer3Text.setTextColor(0xFFFF0000);
                    h.answer3Circle.setColorFilter(0xFFFF0000);
                }
            }

            if (correctAnswerId == 1) {
                h.answer1Aside.setText("Correct Answer");
                h.answer1Text.setTextColor(0xFF00FF00);
                h.answer1Circle.setColorFilter(0xFF00FF00);
            } else if (correctAnswerId == 2) {
                h.answer2Aside.setText("Correct Answer");
                h.answer2Text.setTextColor(0xFF00FF00);
                h.answer2Circle.setColorFilter(0xFF00FF00);
            } else if (correctAnswerId == 3) {
                h.answer3Aside.setText("Correct Answer");
                h.answer3Text.setTextColor(0xFF00FF00);
                h.answer3Circle.setColorFilter(0xFF00FF00);
            }

            if (question.selectedanswerid == correctAnswerId) {
                if (correctAnswerId == 1) {
                    h.answer1Aside.setText("Your Answer");
                } else if (correctAnswerId == 2) {
                    h.answer2Aside.setText("Your Answer");
                } else if (correctAnswerId == 3) {
                    h.answer3Aside.setText("Your Answer");
                }
            }

            boolean isExpanded = position == expandedPosition;

            h.expandedContainer.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            h.expandButton.setVisibility(isExpanded ? View.GONE : View.VISIBLE);

            h.expandButton.setOnClickListener(v -> {
                int currentPosition = h.getBindingAdapterPosition();

                if (currentPosition == RecyclerView.NO_POSITION) {
                    return;
                }

                int previousExpanded = expandedPosition;

                if (currentPosition == expandedPosition) {
                    expandedPosition = RecyclerView.NO_POSITION;
                } else {
                    expandedPosition = currentPosition;
                }

                if (previousExpanded != RecyclerView.NO_POSITION) {
                    notifyItemChanged(previousExpanded);
                }

                if (expandedPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(expandedPosition);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView metadata;
        TextView title;
        TextView question;

        TextView answer1Text;
        TextView answer2Text;
        TextView answer3Text;

        TextView answer1Aside;
        TextView answer2Aside;
        TextView answer3Aside;

        ImageView answer1Circle;
        ImageView answer2Circle;
        ImageView answer3Circle;

        ImageButton expandButton;

        View expandedContainer;

        public HistoryViewHolder(View itemView) {
            super(itemView);

            metadata = itemView.findViewById(R.id.item_history_metadata);
            title = itemView.findViewById(R.id.item_history_title);
            question = itemView.findViewById(R.id.item_history_question);

            answer1Text = itemView.findViewById(R.id.item_history_answer1_text);
            answer2Text = itemView.findViewById(R.id.item_history_answer2_text);
            answer3Text = itemView.findViewById(R.id.item_history_answer3_text);

            answer1Aside = itemView.findViewById(R.id.item_history_answer1_aside);
            answer2Aside = itemView.findViewById(R.id.item_history_answer2_aside);
            answer3Aside = itemView.findViewById(R.id.item_history_answer3_aside);

            answer1Circle = itemView.findViewById(R.id.item_history_answer1_circle);
            answer2Circle = itemView.findViewById(R.id.item_history_answer2_circle);
            answer3Circle = itemView.findViewById(R.id.item_history_answer3_circle);

            expandButton = itemView.findViewById(R.id.item_history_button_expand);

            expandedContainer = itemView.findViewById(R.id.item_history_expanded_container);
        }
    }

    static class HistoryItem {
        Task task;
        Question question;

        public HistoryItem(Task task, Question question) {
            this.task = task;
            this.question = question;
        }
    }
}