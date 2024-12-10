package com.seaguard;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.seaguard.database.DbHelper;
import com.seaguard.database.ReportModel;
import com.seaguard.databinding.ActivityAddReportBinding;
import com.seaguard.ui.home.HomeViewModel;

import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddReportActivity extends AppCompatActivity {
    private double latitude;
    private double longitude;
    private String time;
    private String date;
    private TextInputEditText description;
    private AutoCompleteTextView category;
    private int urgency;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityAddReportBinding binding = ActivityAddReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toobar
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        // Back in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // 1) Location Name
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if(location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    try {
                        binding.locationName.setText(getLocationDetails(latitude, longitude).get("locality"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        // 2) Time and Date
        Calendar calendar = Calendar.getInstance();
        time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.getTime());
        date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.getTime());

        binding.time.setText(time);
        binding.date.setText(date);

        // 3) Description
        description = binding.description;

        // 4) Categories
        category = binding.autoCompleteCategories;

        List<String> items = new ArrayList<>(Arrays.asList(
                "Rifiuti e Inquinamento",
                "Fauna Marina",
                "Scorie",
                "Sostanze Chimiche",
                "Altro"
        ));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, items);
        category.setAdapter(adapter);

        // 5) Stars
        urgency = 0;
        ArrayList<ImageView> stars = new ArrayList<>(Arrays.asList(
                binding.star1,
                binding.star2,
                binding.star3,
                binding.star4,
                binding.star5
        ));

        for(int i = 0; i< stars.size(); i++) {
            int current = i;
            stars.get(i).setOnClickListener(v -> {
                urgency = current;
                for(int j = 0; j < stars.size(); j++) {
                    if(j <= urgency) stars.get(j).setColorFilter(Color.YELLOW);
                    else stars.get(j).clearColorFilter();
                }
            });
        }

        // 6) Upload photo
        TextView photosText = binding.photos;
        Button uploadPhotos = binding.uploadPhotos;

        // 7) Save
        binding.save.setOnClickListener(v -> {
            DbHelper.add(
                    new ReportModel(
                    "",
                    "",
                    category.toString(),
                    date,
                    description.toString(),
                    urgency
                ),
                (docRef, e) -> {}
            );
        });
    }

    @NonNull
    private Map<String, String> getLocationDetails (double latitude, double longitude) throws IOException {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
        Map<String, String> locationDetails = new HashMap<>();

        if(addresses != null && !addresses.isEmpty()) {
            locationDetails.put("addressLine", addresses.get(0).getAddressLine(0));
            locationDetails.put("locality", addresses.get(0).getLocality());
            locationDetails.put("adminArea", addresses.get(0).getAdminArea());
            locationDetails.put("countryName", addresses.get(0).getCountryName());
            locationDetails.put("postalCode", addresses.get(0).getPostalCode());
            locationDetails.put("featureName", addresses.get(0).getFeatureName());
        }

        return locationDetails;
    }

}