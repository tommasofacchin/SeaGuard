package com.seaguard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.seaguard.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.Map;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.seaguard.ui.explore.ExploreFragment;
import com.seaguard.ui.home.HomeFragment;
import com.seaguard.ui.reports.ReportsFragment;
import com.seaguard.ui.settings.SettingsFragment;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private HomeFragment homeFragment = new HomeFragment();
    private ReportsFragment reportsFragment = new ReportsFragment();
    private ExploreFragment exploreFragment = new ExploreFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //reload();
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.navigation_home) {
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, homeFragment)
                .commit();
            return true;
        } else if (itemId == R.id.navigation_reports) {
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, reportsFragment)
                .commit();
            return true;
        } else if (itemId == R.id.navigation_explore) {
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, exploreFragment)
                .commit();
            return true;
        } else if (itemId == R.id.navigation_settings) {
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, settingsFragment)
                .commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnItemSelectedListener(this::onNavigationItemSelected);

        // Default Fragment (Home)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
            .replace(R.id.flFragment, homeFragment)
            .commit();
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_reports, R.id.navigation_explore, R.id.navigation_settings)
                .build();
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //NavigationUI.setupWithNavController(binding.navView, navController);


        db = FirebaseFirestore.getInstance();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        /*
        Map<String, Object> user = new HashMap<>();
        user.put("name", "Tommaso");
        user.put("surname", "Facchin");
        user.put("age", 22);


        db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
            }
        });
         */


    }

}