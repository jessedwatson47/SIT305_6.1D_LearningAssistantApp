package com.example.learningassistantapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Elements
        EditText loginUsername = view.findViewById(R.id.login_input_username);
        EditText loginPassword = view.findViewById(R.id.login_input_password);
        Button loginButton = view.findViewById(R.id.login_button_login);
        TextView toSignUp = view.findViewById(R.id.login_text_needaccount);

        // Check for Bundle Data
        Bundle args = getArguments();
        if (args != null && args.containsKey("username"))
        {
            loginUsername.setText(args.getString("username"));
        }

        // Listeners
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Build database
                AppDatabase db = Room.databaseBuilder(requireContext(),
                                AppDatabase.class, "user-db")
                        .allowMainThreadQueries()
                        .build();

                UserDao userDao = db.userDao();

                String username = loginUsername.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                User user = userDao.login(username, password);
                if (user != null)
                {
                    // Success: Login, pass userId in bundle to fetch data
                    Bundle bundle = new Bundle();
                    bundle.putInt("userId", user.id);
                    bundle.putString("username", user.username);
                    var navController = NavHostFragment.findNavController(LoginFragment.this);
                    navController.navigate(R.id.homeFragment, bundle);
                }
                else
                {
                    // Error: Toast
                    Toast.makeText(getContext(), "Error! Invalid username or password.", Toast.LENGTH_LONG).show();
                }

            }
        });

        toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                var navController = NavHostFragment.findNavController(LoginFragment.this);
                navController.navigate(R.id.signUpFragment);
            }
        });




    }

}