package com.example.learningassistantapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private int userId;
    private User user;

    private String username;

    private List<Task> returnedTasks = new ArrayList<>();

    public HomeFragment() {
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get Bundle Args (userId to fetch task data)
        Bundle args = getArguments();
        if (args != null && args.containsKey("userId"))
        {
            userId = args.getInt("userId");
            username = args.getString("username");

        }


        // Elements
        TextView userName = view.findViewById(R.id.home_text_name);
        TextView notificationCount = view.findViewById(R.id.results_text_notification);
        ImageView notificationBell = view.findViewById(R.id.results_image_notification);
        LinearLayout notificationBg = view.findViewById(R.id.results_layout_notification);
        userName.setText(username);
        RecyclerView tasksRecyclerView = view.findViewById(R.id.home_task_recyclerview);
        Button profileButton = view.findViewById(R.id.home_button_profile);


        // Set RecyclerView
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        TaskAdapter taskAdapter = new TaskAdapter(returnedTasks, task -> goToTask(task));
        tasksRecyclerView.setAdapter(taskAdapter);

        // Build database
        AppDatabase db = Room.databaseBuilder(requireContext(),
                        AppDatabase.class, "user-db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        UserDao userDao = db.userDao();
        TaskDao taskDao = db.taskDao();
        QuestionDao questionDao = db.questionDao();


        if (!args.containsKey("username"))
        {
            User u = userDao.getById(userId);
            username = u.username;
            userName.setText(username);
        }

        if (userId != -1)
        {
            user = userDao.getById(userId);
        }

        // First lets check: if user has tasks associated with their ID -> display them
        // else -> AI gen some for EACH interest

        if (!taskDao.getByUserId(userId).isEmpty())
        {
            returnedTasks.clear();
            returnedTasks.addAll(taskDao.getByUserId(userId));
            taskAdapter.notifyDataSetChanged();
        }
        else
        {
            // Get user interests string in format of (x,y,z,) and split -> create a list
            // We do this to make dummy tasks that show loading state
            String interestsRaw = user.interests;
            String[] interestsSplit = user.interests.split(",");
            List<String> interestsList = new ArrayList<>();

            for (String interest : interestsSplit) {
                if (!interest.isEmpty())
                {
                    interestsList.add(interest);
                }
            }

            for (String interest : interestsList)
            {
                Task task = new Task();
                task.isLoading = true;
                returnedTasks.add(task);
            }

            // Build Retrofit
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:3000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            TaskApi api = retrofit.create(TaskApi.class);

            Call<TaskListResponse> call = api.getTasks(interestsRaw);

            call.enqueue(new Callback<TaskListResponse>()
            {
                @Override
                public void onResponse(Call<TaskListResponse> call, Response<TaskListResponse> response)
                {
                    if (response.isSuccessful() && response.body() != null)
                    {
                        returnedTasks.clear();
                        returnedTasks.addAll(response.body().tasks);
                        taskAdapter.notifyDataSetChanged();
                        // Build proper object and insert them into DB tied to userId
                        int i = 0;
                        for (Task task : returnedTasks)
                        {
                            Task t = new Task (task.title, task.desc, userId);
                            t.questions = task.questions;
                            long taskId = taskDao.insert(t);
                            task.id = (int) taskId;

                            for (Question q : task.questions)
                            {
                                q.taskid = taskId;
                                questionDao.insert(q);
                            }

                        }


                    }
                }
                @Override
                public void onFailure(Call<TaskListResponse> call, Throwable t)
                {
                    Log.e("API", "API failed", t);
                }
            });
        }

        // Set notification elements to hidden, make visible if exists
        notificationBg.setVisibility(View.INVISIBLE);
        notificationBell.setVisibility(View.INVISIBLE);
        notificationCount.setVisibility(View.INVISIBLE);
        if (!returnedTasks.isEmpty())
        {
            notificationBg.setVisibility(View.VISIBLE);
            notificationBell.setVisibility(View.VISIBLE);
            notificationCount.setVisibility(View.VISIBLE);
            notificationCount.setText("You have " + returnedTasks.size() + " task(s) due!");
        }

        // Listeners
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("userId", userId);
                var navController = NavHostFragment.findNavController(HomeFragment.this);
                navController.navigate(R.id.profileFragment, bundle);
            }
        });



    }

    public void goToTask(Task task)
    {
        Bundle bundle = new Bundle();
        bundle.putString("tasktitle", task.title);
        bundle.putString("taskdesc", task.desc);
        bundle.putInt("userId", userId);
        bundle.putInt("taskid", task.id);
        var navController = NavHostFragment.findNavController(HomeFragment.this);
        navController.navigate(R.id.quizFragment, bundle);
    }




}