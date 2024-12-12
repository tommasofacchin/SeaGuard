package com.seaguard;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

        binding.signIn.setOnClickListener(v -> login());
    }

    private void login () {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if(validateEmail(email) && !password.isEmpty()) {
            // Show progress bar
            progressBar.setVisibility(View.VISIBLE);

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    finish();
                } else {
                    // Disable progress bar
                    progressBar.setVisibility(View.GONE);

                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                            getApplicationContext(),
                            "Autenticazione fallita!",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        }
        else {
            Toast.makeText(
                getApplicationContext(),
                "Campi NON validi!",
                Toast.LENGTH_SHORT
            ).show();
        }
    }

    private boolean validateEmail (String email) {
        return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}