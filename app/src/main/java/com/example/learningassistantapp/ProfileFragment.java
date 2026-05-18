package com.example.learningassistantapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ProfileFragment extends Fragment {

    private int userId;
    private User user;

    public ProfileFragment() {
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get Bundle Args (userId to fetch task data)
        Bundle args = getArguments();
        if (args != null && args.containsKey("userId"))
        {
            userId = args.getInt("userId");
        }

        // Elements
        TextView totalQuestionCount = view.findViewById(R.id.profile_text_questionscount);
        TextView totalIncorrectQuestionCount = view.findViewById(R.id.profile_text_questionsincorrectcount);
        TextView totalCorrectQuestionCount = view.findViewById(R.id.profile_text_questionscorrectcount);
        TextView incorrectSummary = view.findViewById(R.id.profile_text_summary);
        // User Info
        TextView username = view.findViewById(R.id.profile_text_username);
        TextView email = view.findViewById(R.id.profile_text_email);

        Button backButton = view.findViewById(R.id.profile_button_back);
        Button shareButton = view.findViewById(R.id.profile_button_share);
        Button upgradeButton = view.findViewById(R.id.profile_button_upgrade);
        Button historyButton = view.findViewById(R.id.profile_button_history);


        // Build database
        AppDatabase db = Room.databaseBuilder(requireContext(),
                        AppDatabase.class, "user-db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        UserDao userDao = db.userDao();
        TaskDao taskDao = db.taskDao();
        QuestionDao questionDao = db.questionDao();

        // First fetch username / email
        user = userDao.getById(userId);

        // Set username/email
        username.setText(user.username);
        email.setText(user.email);


        int totalAnswered = questionDao.getTotalAnsweredQuestions();
        int correctAnswers = questionDao.getCorrectAnswerCount();
        int incorrectAnswers = totalAnswered - correctAnswers;

        totalQuestionCount.setText(String.valueOf(totalAnswered));
        totalCorrectQuestionCount.setText(String.valueOf(correctAnswers));
        totalIncorrectQuestionCount.setText(String.valueOf(incorrectAnswers));


        // Build Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        SubmissionApi api = retrofit.create(SubmissionApi.class);

        // Get incorrect answers
        List<Question> incorrectQuestions = questionDao.getIncorrectQuestions();

        // Build the query
        String query = "";

        for (Question q : incorrectQuestions) {
            query += "Question: " + q.question + "\n";
            query += "Answer 1: " + q.answer1 + "\n";
            query += "Answer 2: " + q.answer2 + "\n";
            query += "Answer 3: " + q.answer3 + "\n";
            query += "Selected Answer ID: " + q.selectedanswerid + "\n";
            query += "Correct Answer ID: " + q.correctanswerid + "\n\n";
        }

// Call API
        Call<String> call = api.getSummary(query);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    incorrectSummary.setText(response.body());

                    System.out.println(response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Example:
                // summaryText.setText("Failed to load summary.");

                System.out.println("Failed to load summary.");
            }
        });


        // Listeners

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).popBackStack();
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareMessage =
                        "Learning Assistant Profile\n\n" +
                                "Username: " + user.username + "\n" +
                                "Total answered: " + totalAnswered + "\n" +
                                "Correct answers: " + correctAnswers + "\n" +
                                "Incorrect answers: " + incorrectAnswers + "\n";

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, "Share profile");
                startActivity(shareIntent);
            }
        });

        upgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.upgradeFragment);
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.historyFragment);
            }
        });

    }

}