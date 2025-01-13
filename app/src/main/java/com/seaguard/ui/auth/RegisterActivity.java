package com.seaguard.ui.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.seaguard.R;
import com.seaguard.Utils;
import com.seaguard.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText nameField;
    private TextInputEditText emailField;
    private TextInputEditText passwordField;
    private TextInputEditText confirmPasswordField;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityRegisterBinding binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        nameField = binding.name;
        emailField = binding.username;
        passwordField = binding.password;
        confirmPasswordField = binding.confirmPassword;
        progressBar = binding.loading;

        binding.signUp.setOnClickListener(v -> signUp());
    }

    private void signUp () {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();

        if(Utils.validateEmail(email) && Utils.validatePassword(password) && password.equals(confirmPassword) && !name.isEmpty()) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    mAuth.signInWithEmailAndPassword(email, password);
                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    if (currentUser != null) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();
                        currentUser.updateProfile(profileUpdates);
                    }

                    finish();
                }
                else {
                    // Disable progress bar
                    progressBar.setVisibility(View.GONE);

                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                            getApplicationContext(),
                            getString(R.string.sign_up_failed),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        }
        else {
            Toast.makeText(
                getApplicationContext(),
                    getString(R.string.invalid_credentials),
                Toast.LENGTH_SHORT
            ).show();
        }
    }

}