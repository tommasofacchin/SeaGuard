package com.seaguard.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.seaguard.R;
import com.seaguard.database.DbHelper;
import com.seaguard.database.ReportModel;
import com.seaguard.databinding.FragmentHomeBinding;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private MapView map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Map
        map = binding.map;
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);

        MapController mapController = (MapController) map.getController();
        map.setMinZoomLevel(3.5);
        mapController.setCenter(homeViewModel.getCenterPoint());
        mapController.setZoom(homeViewModel.getZoomLevel());

        homeViewModel.setCallBack(() -> {
            MyLocationNewOverlay location = new MyLocationNewOverlay(new GpsMyLocationProvider(map.getContext()), map);
            location.enableMyLocation();
            map.getOverlays().add(location);

            if(homeViewModel.isFirstRun())
                location.runOnFirstFix(() ->
                    requireActivity().runOnUiThread(() ->
                            map.getController().animateTo(location.getMyLocation())));
        });

        map.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                homeViewModel.setCenterPoint((GeoPoint) map.getMapCenter());
                homeViewModel.setZoomLevel(map.getZoomLevel());
            }
            return false;
        });

        // FAB to open an AddReportActivity
        ExtendedFloatingActionButton fab = binding.fab;
        fab.setOnClickListener(view -> {
                Intent intent = new Intent(requireActivity(), AddReportActivity.class);
                startActivity(intent);
        });

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(!homeViewModel.isFirstRun()) homeViewModel.setLocation();
        addReportIcons();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    // Quando il fragment torna in primo piano (ad esempio, l'utente naviga indietro).
    @Override
    public void onResume() {
        super.onResume();
        if (binding != null) {
            binding.map.onResume();
        }
    }

    // Quando il fragment non è più visibile mette in pausa per risparmiare risorse
    @Override
    public void onPause() {
        super.onPause();
        if (binding != null) {
            binding.map.onPause();
        }
    }

    // Per evitare memoryleak imposto il binding a null, altirmenti il GC non sa che può liberare la risorsa
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void addReportIcons () {
        DbHelper.getReports(
                reports -> {
                    for (ReportModel elem : reports) {
                        // Icon
                        Drawable icon = ContextCompat.getDrawable(requireContext(), R.drawable.pin_reports);
                        icon.setColorFilter(Color.parseColor("#FF4F378B"), PorterDuff.Mode.SRC_IN);

                        // Marker
                        try {
                            Marker m = new Marker(map);
                            m.setPosition(new GeoPoint(elem.getLatitude(), elem.getLongitude()));
                            m.setIcon(icon);
                            m.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_TOP);

                            // --- Listener ---
                            m.setOnMarkerClickListener((marker, mapView) -> {
                                Intent intent = new Intent(getContext(), ReportActivity.class);
                                intent.putExtra("report", elem);
                                getContext().startActivity(intent);
                                return true;
                            });

                            // Add the marker to the map
                            map.getOverlays().add(m);
                        }
                        catch (Exception e) {
                            return;
                        }
                    }
                },
                e -> Toast.makeText(
                    getContext(),
                    getString(R.string.loading_error),
                    Toast.LENGTH_SHORT
                ).show()
        );
    }

}