package com.example.learningassistantapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class QuizFragment extends Fragment {

    private int currentQuestionIndex = 0;
    Question currentQuestion;
    private List<Question> questions = new ArrayList<>();

    // Top Elements related to the task
    private TextView taskTitle;
    private TextView taskDesc;
    // Quiz Card elements
    private TextView questionTitle;
    private TextView questionContent;
    private RadioGroup questionRadioGroup;
    private RadioButton answer1Radio;
    private QuestionDao questionDao;
    private RadioButton answer2Radio;
    private RadioButton answer3Radio;
    // Next Quiz Question Elements
    private TextView nextQuestionTitle;
    private LinearLayout nextQuestionBg;
    private ImageView nextQuestionArrow;
    // Submit Button
    private Button submitButton;

    // Data to build
    private List<String> selectedAnswers = new ArrayList<>();


    public QuizFragment() {
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
        return inflater.inflate(R.layout.fragment_quiz, container, false);
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
        questionDao = db.questionDao();


        // Elements
        // Top Elements related to the task
        taskTitle = view.findViewById(R.id.quiz_text_title);
        taskDesc = view.findViewById(R.id.quiz_text_desc);
        // Quiz Card elements
        questionTitle = view.findViewById(R.id.quiz_question_title);
        questionContent = view.findViewById(R.id.quiz_question_question);
        questionRadioGroup = view.findViewById(R.id.quiz_question_radiogroup);
        answer1Radio = view.findViewById(R.id.quiz_radio1);
        answer2Radio = view.findViewById(R.id.quiz_radio2);
        answer3Radio = view.findViewById(R.id.quiz_radio3);
        // Next Quiz Question Elements
        nextQuestionTitle = view.findViewById(R.id.quiz_text_nextquestion);
        nextQuestionBg = view.findViewById(R.id.quiz_layout_nextquiz);
        nextQuestionArrow = view.findViewById(R.id.quiz_image_nextquestion);
        // Submit Button
        submitButton = view.findViewById(R.id.quiz_button_submit);

        // Get Bundle Data
        Bundle args = getArguments();
        if (args != null && args.containsKey("taskid"))
        {
            // Fetch all questions for task
            questions = questionDao.getByTaskId((long)args.getInt("taskid"));
            taskTitle.setText(args.getString("tasktitle"));
            taskDesc.setText(args.getString("taskdesc"));
        }

        // Load Initial Question
        LoadQuestion();

        // Listeners
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestionIndex != 2){
                    // Add toast potentially
                    return;
                }
                SaveAnswer();
                // Navigate to results page
                Bundle bundle = new Bundle();
                bundle.putInt("taskid", args.getInt("taskid"));
                bundle.putInt("userId", args.getInt("userId"));
                var navController = NavHostFragment.findNavController(QuizFragment.this);
                navController.navigate(R.id.resultsFragment, bundle);

            }
        });

        nextQuestionBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAnswer();
                // Load Next Question
                if (currentQuestionIndex + 1 < questions.size())
                {
                    currentQuestionIndex++;
                    LoadQuestion();
                }
            }
        });

    }

    private void LoadQuestion()
    {
        currentQuestion = questions.get(currentQuestionIndex);

        if (currentQuestionIndex + 1 < questions.size())
        {
            Question nextQuestion = questions.get(currentQuestionIndex + 1);
            nextQuestionTitle.setText("Next Question: " + nextQuestion.title);
        }

        questionTitle.setText(currentQuestion.title);
        questionContent.setText(currentQuestion.question);
        answer1Radio.setText(currentQuestion.answer1);
        answer2Radio.setText(currentQuestion.answer2);
        answer3Radio.setText(currentQuestion.answer3);

        // Hide the next question element if there is none
        if (currentQuestionIndex + 1 >= questions.size())
        {
            nextQuestionTitle.setVisibility(View.INVISIBLE);
            nextQuestionArrow.setVisibility(View.INVISIBLE);
            nextQuestionBg.setVisibility(View.INVISIBLE);
        }

    }

    private void SaveAnswer()
    {
        int checkedRadioButtonId = questionRadioGroup.getCheckedRadioButtonId();

        if (checkedRadioButtonId != -1)
        {
            if (checkedRadioButtonId == R.id.quiz_radio1) currentQuestion.selectedanswerid = 1;
            if (checkedRadioButtonId == R.id.quiz_radio2) currentQuestion.selectedanswerid = 2;
            if (checkedRadioButtonId == R.id.quiz_radio3) currentQuestion.selectedanswerid = 3;

            questionDao.update(currentQuestion);
        }
    }
}