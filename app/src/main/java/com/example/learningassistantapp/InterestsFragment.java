package com.example.learningassistantapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class InterestsFragment extends Fragment {

    private Set<String> selectedInterests = new HashSet<>();

    public InterestsFragment() {
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
        return inflater.inflate(R.layout.fragment_interests, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Elements
        List<String> interests = InterestsData.Interests;
        RecyclerView interestRecyclerView = view.findViewById(R.id.interests_recyclerview);
        Button createAccountButton = view.findViewById(R.id.interests_button_createaccount);

        // Set RecyclerView
        interestRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        interestRecyclerView.setAdapter(new InterestAdapter(interests, interest -> addInterest(interest)));

        // Get the previous bundle
        Bundle args = getArguments();
//        if (args != null && args.containsKey("username") && args.containsKey("password") && args.containsKey("email") && args.containsKey("phone_number"))
//        {
//            // Error: Toast
//            Toast.makeText(getContext(), "Error fetching details!", Toast.LENGTH_LONG).show();
//        }

        // Build DB and DAO
        AppDatabase db = Room.databaseBuilder(requireContext(),
                        AppDatabase.class, "user-db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        UserDao userDao = db.userDao();


        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If interests list has atleast one interest, we build the string to store in DB and create user
                if (!selectedInterests.isEmpty())
                {
                    // Build Interests string
                    StringBuilder sb = new StringBuilder();
                    for (String interest : selectedInterests)
                    {
                        sb.append(interest);
                        sb.append(",");
                    }
                    // Build User and Create Account
                    User newUser = new User(args.getString("username"), args.getString("password"), args.getString("email"), args.getString("phone_number"), sb.toString());
                    userDao.insertAll(newUser);
                    Toast.makeText(getContext(), "Account created successfully. Please login!", Toast.LENGTH_LONG).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("username", args.getString("username"));
                    var navController = NavHostFragment.findNavController(InterestsFragment.this);
                    navController.navigate(R.id.loginFragment, bundle);
                }
                else
                {
                    Toast.makeText(getContext(), "Please choose at least 1 interest.", Toast.LENGTH_LONG).show();
                }
            }
        });



    }

    private void addInterest(String interest)
    {
        if (selectedInterests.contains(interest))
        {
            return;
        }
        else
        {
            selectedInterests.add(interest);
        }

    }

}