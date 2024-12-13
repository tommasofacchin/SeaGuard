package com.seaguard.ui.settings;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.seaguard.databinding.ActivityProfileBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private TextInputEditText name;
    private TextInputEditText email;
    private TextInputEditText password;
    private AutoCompleteTextView nationality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProfileBinding binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toobar
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        // Back in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Name
        name = binding.editTextName;
        name.setText("");

        // Email
        email = binding.editTextEmail;
        email.setText("");

        // Password
        password = binding.editTextPassword;
        password.setText("");

        // Nationalities
        nationality = binding.autoCompleteNationalities;
        List<String> items = new ArrayList<>(Arrays.asList(
                "Italia",
                "Germania",
                "Regno Unito",
                "Spagna"
        ));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, items);
        nationality.setAdapter(adapter);

        // Save
        binding.buttonSaveProfile.setOnClickListener(v -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if(currentUser != null) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name.getText().toString())
                        .build();

                currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(
                                this,
                                "Profilo aggiornato",
                                Toast.LENGTH_SHORT
                        ).show();
                        finish();
                    }
                    else {
                        Toast.makeText(
                            this,
                            "Impossibile aggiornare il profilo!",
                            Toast.LENGTH_SHORT
                        ).show();
                    }
                });
            }
        });

        setData();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setData () {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            name.setText(currentUser.getDisplayName());
            email.setText(currentUser.getEmail());
        }
    }

}