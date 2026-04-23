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
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    public SignUpFragment() {
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
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Elements
        EditText signUpUserName = view.findViewById(R.id.signup_input_username);
        EditText signUpEmail = view.findViewById(R.id.signup_input_email);
        EditText signUpEmailConfirm = view.findViewById(R.id.signup_input_confirmemail);
        EditText signUpPassword = view.findViewById(R.id.signup_input_password);
        EditText signUpPasswordConfirm = view.findViewById(R.id.signup_input_confirmpassword);
        EditText signUpPhoneNumber = view.findViewById(R.id.signup_input_phonenumber);
        Button createAccountButton = view.findViewById(R.id.signup_button_next);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = signUpUserName.getText().toString().trim();

                String email =  signUpEmail.getText().toString().trim();
                String emailConfirm = signUpEmailConfirm.getText().toString().trim();

                String password = signUpPassword.getText().toString().trim();
                String passwordConfirm = signUpPasswordConfirm.getText().toString().trim();

                String phoneNumber = signUpPhoneNumber.getText().toString().trim();


                // Build DB and DAO
                AppDatabase db = Room.databaseBuilder(requireContext(),
                                AppDatabase.class, "user-db")
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();

                UserDao userDao = db.userDao();


                if (username.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty() || email.isEmpty() || emailConfirm.isEmpty() || phoneNumber.isEmpty())
                {
                    // Error: Toast
                    Toast.makeText(getContext(), "Please fill out all the fields.", Toast.LENGTH_LONG).show();
                }
                else if (userDao.checkUsername(username))
                {
                    // Check if username already exists
                    // Error: Toast
                    Toast.makeText(getContext(), "Username already exists!", Toast.LENGTH_LONG).show();
                }
                else if (!password.equals(passwordConfirm))
                {
                    // Check if password == passwordConfirm
                    // Error: Toast
                    Toast.makeText(getContext(), "Passwords do not match.", Toast.LENGTH_LONG).show();
                }
                else if (!email.equals(emailConfirm))
                {
                    // Check if email == emailConfirm
                    // Error: Toast
                    Toast.makeText(getContext(), "Email addresses do not match.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    bundle.putString("password", password);
                    bundle.putString("email", email);
                    bundle.putString("phone_number", phoneNumber);
                    var navController = NavHostFragment.findNavController(SignUpFragment.this);
                    navController.navigate(R.id.interestsFragment, bundle);
                }

            }
        });
    }
}