package com.seaguard.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.seaguard.R;
import com.seaguard.Utils;
import com.seaguard.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText emailField;
    private TextInputEditText passwordField;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        emailField = binding.username;
        passwordField = binding.password;
        progressBar = binding.loading;

        // Register Link
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Close the activity if the user is registred
                if(FirebaseAuth.getInstance().getCurrentUser() != null) finish();
            }
        );

        binding.registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            activityResultLauncher.launch(intent);
        });

        // Login button
        binding.signIn.setOnClickListener(v -> login());
    }

    private void login () {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if(Utils.validateEmail(email) && !password.isEmpty()) {
            // Show progress bar
            progressBar.setVisibility(View.VISIBLE);

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    finish();
                } else {
                    // Disable progress bar
                    progressBar.setVisibility(View.GONE);

                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                            getApplicationContext(),
                            getString(R.string.failed_auth),
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