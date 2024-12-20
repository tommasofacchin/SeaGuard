package com.seaguard.ui.home;

import android.Manifest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.seaguard.database.CategoryModel;
import com.seaguard.database.DbHelper;
import com.seaguard.database.ReportModel;
import com.seaguard.databinding.ActivityAddReportBinding;

import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AddReportActivity extends AppCompatActivity {
    private String userId;
    private String locationName;
    private double latitude;
    private double longitude;
    private String time;
    private String date;
    private TextInputEditText description;
    private AutoCompleteTextView category;
    private int urgency;
    private TextView uploadStatus;
    private Bitmap bitmapImage;
    private final int REQUEST_PERMISSIONS_CODE = 1;
    private ActivityResultLauncher<Intent> pickImageLauncher;

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

        // Get user ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = (currentUser != null) ? currentUser.getUid() : "";

        // 1) Location Name
        locationName = "";
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            // Got last known location. In some rare situations this can be null.
            if(location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                try {
                    locationName = getLocationDetails(latitude, longitude).get("locality");
                    binding.locationName.setText(locationName);
                } catch (IOException e) {
                    Toast.makeText(
                        this,
                        "Impossibile rilevare la posizione!",
                        Toast.LENGTH_SHORT
                    ).show();
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

        DbHelper.getCategories(
                (categories) -> {
                    List<String> items = categories.stream().map(CategoryModel::getCategory).collect(Collectors.toList());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, items);
                    category.setAdapter(adapter);
                },
                (e) -> Toast.makeText(
                        this,
                        "Impossibile caricare le categorie: " + e.getMessage(),
                        Toast.LENGTH_SHORT
                ).show()
        );

        // 5) Stars
        urgency = 0;
        ArrayList<ImageView> stars = new ArrayList<>(Arrays.asList(
                binding.star1,
                binding.star2,
                binding.star3,
                binding.star4,
                binding.star5
        ));

        for(int i = 0; i < stars.size(); i++) {
            int current = i;
            stars.get(i).setOnClickListener(v -> {
                urgency = current + 1;
                for(int j = 0; j < stars.size(); j++) {
                    if(j <= current) stars.get(j).setColorFilter(Color.BLUE);
                    else stars.get(j).clearColorFilter();
                }
            });
        }

        // 6) Upload photo
        TextView uploadStatus = binding.photos;
        Button uploadPhoto = binding.uploadPhotos;
        bitmapImage = null;

        pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri imageUri = data.getData();
                        try {
                            bitmapImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            uploadStatus.setText("Foto allegata");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        );

        uploadPhoto.setOnClickListener(v -> requestPermissions());

        // 7) Save
        binding.save.setOnClickListener(v -> {
           ReportModel elem = new ReportModel(
                    userId,
                    locationName,
                    latitude,
                    longitude,
                    category.getText().toString(),
                    time,
                    date,
                    description.getText() != null ? description.getText().toString() : "",
                    urgency,
                    ""
            );

           Consumer<DocumentReference> onSuccess = docRef -> {
                Toast.makeText(
                        this,
                        "Report salvato con successo!",
                        Toast.LENGTH_SHORT
                ).show();
                finish();
            };

           Consumer<Exception> onFailure = e -> {
               Toast.makeText(
                        this,
                        "Errore nel salvataggio: " + e.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
           };

            if(bitmapImage != null) {
                uploadImage(
                        bitmapImage,
                        imgPath -> {
                            elem.setImage(imgPath);
                            DbHelper.add(elem, onSuccess, onFailure);
                        }
                );
            }
            else DbHelper.add(elem, onSuccess, onFailure);
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void pickImage () {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
        pickImageLauncher.launch(Intent.createChooser(intent, "Seleziona un'immagine"));
    }

    private void uploadImage (Bitmap bitmap, Consumer<String> callBack) {
        // Conversion from bitmap to PNG
        byte[] data = new ByteArrayOutputStream() {{
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, this);
        }}.toByteArray();

        DbHelper.uploadImage(
                data,
                callBack,
                e -> Toast.makeText(
                    this,
                    "Errore durante il salvataggio dell'immagine!",
                    Toast.LENGTH_SHORT
                ).show()
        );
    }


    private void requestPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS_CODE
            );
        }
        else pickImage();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSIONS_CODE && grantResults.length > 0) pickImage();
    }

}