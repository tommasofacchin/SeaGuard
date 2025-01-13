package com.seaguard.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.seaguard.R;
import com.seaguard.Utils;
import com.seaguard.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {
    private TextInputEditText name;
    private TextView email;

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

        // On save
        Runnable onSuccess = () -> {
            Toast.makeText(
                        this,
                        getString(R.string.profile_updated),
                        Toast.LENGTH_SHORT
                ).show();
                finish(); // Exit
        };

        Runnable onFailure = () -> Toast.makeText(
                this,
                getString(R.string.error_while_saving),
                Toast.LENGTH_SHORT
        ).show();

        // Email
        email = binding.textViewEmail;
        email.setText("");

        // Name
        name = binding.editTextName;
        name.setText("");

        // Password
        binding.buttonChangePassword.setOnClickListener(v -> updatePassword(onSuccess, onFailure));

        // Nationalities
        /*
        AutoCompleteTextView nationality = binding.autoCompleteNationalities;
        List<String> items = new ArrayList<>(Arrays.asList(
                "Italia",
                "Germania",
                "Regno Unito",
                "Spagna"
        ));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, items);
        nationality.setAdapter(adapter);
         */

        // Logout
        binding.buttonLogout.setOnClickListener(v -> logout());

        // Save
        binding.buttonSaveProfile.setOnClickListener(v -> save(onSuccess, onFailure));

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

    private void save (Runnable onSuccess, Runnable onFailure) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name.getText().toString())
                    .build();

            currentUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) onSuccess.run();
                        else onFailure.run();
                    });

        }
    }

    private void updatePassword (Runnable onSuccess, Runnable onFailure) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_change_password, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.confirm_password_change));
        builder.setView(dialogView);

        final EditText oldPasswordInput = dialogView.findViewById(R.id.edit_text_old_password);
        final EditText newPasswordInput = dialogView.findViewById(R.id.edit_text_new_password);

        builder.setPositiveButton(getString(R.string.confirm), (dialog, which) -> {
            String oldPassword = oldPasswordInput.getText().toString();
            String newPassword = newPasswordInput.getText().toString();

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if(currentUser != null && !oldPassword.isEmpty() && Utils.validatePassword(newPassword)) {
                AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), oldPassword);
                currentUser.reauthenticate(credential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUser.updatePassword(newPassword);
                        onSuccess.run();
                    } else onFailure.run();
                });
            } else onFailure.run();
        });

        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void logout () {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            mAuth.signOut();
            finish();
        }
    }

}