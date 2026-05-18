package com.example.learningassistantapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

public class HistoryFragment extends Fragment {

    public HistoryFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Elements
        RecyclerView historyRecyclerView = view.findViewById(R.id.history_recyclerview);
        Button backButton = view.findViewById(R.id.history_button_back);
        // Build database
        AppDatabase db = Room.databaseBuilder(requireContext(),
                        AppDatabase.class, "user-db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        UserDao userDao = db.userDao();
        TaskDao taskDao = db.taskDao();
        QuestionDao questionDao = db.questionDao();

        historyRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<Task> tasks = taskDao.getAll();

        for (Task task : tasks) {
            task.questions = questionDao.getAnsweredByTaskId(task.id);
        }

        HistoryAdapter historyAdapter = new HistoryAdapter(tasks);
        historyRecyclerView.setAdapter(historyAdapter);

        // Listeners
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Navigation.findNavController(view).popBackStack();
            }
        });
    }

}