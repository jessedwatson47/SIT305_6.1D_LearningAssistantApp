package com.example.learningassistantapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ResultsFragment extends Fragment {

    private List<Question> questions = new ArrayList<>();
    private List<Result> resultList = new ArrayList<>();

    public ResultsFragment() {
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
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Build database
        AppDatabase db = Room.databaseBuilder(requireContext(),
                        AppDatabase.class, "user-db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        UserDao userDao = db.userDao();
        TaskDao taskDao = db.taskDao();
        QuestionDao questionDao = db.questionDao();

        // Elements
        RecyclerView resultsRecyclerView = view.findViewById(R.id.results_recyclerview);
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        Button continueButton = view.findViewById(R.id.results_button_continue);

        // Get Bundle Data
        Bundle args = getArguments();
        if (args != null && args.containsKey("taskid"))
        {
            // Fetch all questions for task
            questions = questionDao.getByTaskId((long)args.getInt("taskid"));
        }

        for (Question q : questions) {
            resultList.add(new Result(q.title, "", true));
        }

        ResultAdapter resultAdapter = new ResultAdapter(resultList);
        resultsRecyclerView.setAdapter(resultAdapter);


        // Build Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        SubmissionApi api = retrofit.create(SubmissionApi.class);

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            int position = i;

            String query = buildExplanationQuery(q);
            Call<String> call = api.getExplanation(query);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        resultList.get(position).resultText = response.body();
                        resultList.get(position).isLoading = false;
                        resultAdapter.notifyItemChanged(position);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    resultList.get(position).resultText = "Failed to load explanation.";
                    resultList.get(position).isLoading = false;
                    resultAdapter.notifyItemChanged(position);
                }
            });
        }


        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("userId", args.getInt("userId"));
                var navController = NavHostFragment.findNavController(ResultsFragment.this);
                navController.navigate(R.id.homeFragment, bundle);
            }
        });



    }

    private String getAnswerText(Question q, int answerId) {
        if (answerId == 0) return q.answer1;
        if (answerId == 1) return q.answer2;
        return q.answer3;
    }

    private String buildExplanationQuery(Question q) {
        String selectedAnswer = getAnswerText(q, q.selectedanswerid);
        String correctAnswer = getAnswerText(q, q.correctanswerid);
        boolean isCorrect = q.selectedanswerid == q.correctanswerid;

        return "Question: " + q.question +
                "\nSelected answer: " + selectedAnswer +
                "\nCorrect answer: " + correctAnswer +
                "\nWas correct: " + isCorrect;
    }
}