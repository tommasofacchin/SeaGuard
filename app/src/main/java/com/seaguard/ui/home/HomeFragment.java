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

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.seaguard.AddReportActivity;
import com.seaguard.R;
import com.seaguard.database.DbHelper;
import com.seaguard.database.ReportModel;
import com.seaguard.databinding.FragmentHomeBinding;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private MapView map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*
            System.out.println("ONCREATEFRAGMENT!!!!");
            if(savedInstanceState==null) {
                System.out.println("BBB");
         */
        HomeViewModel homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Map
        map = binding.map;
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);

        /*
        MapController mapController = (MapController) map.getController();
        mapController.setCenter(new GeoPoint(41.8902, 12.4922)); // Rome
        mapController.setZoom(16);
         */

        MapController mapController = (MapController) map.getController();
        map.setMinZoomLevel(3.5);
        mapController.setCenter(homeViewModel.getCenterPoint());
        mapController.setZoom(homeViewModel.getZoomLevel());

        homeViewModel.setCallBack(() -> {
            //provider.addLocationSource(LocationManager.NETWORK_PROVIDER);
            MyLocationNewOverlay location = new MyLocationNewOverlay(new GpsMyLocationProvider(map.getContext()), map);
            location.enableMyLocation();
            map.getOverlays().add(location);

            if(homeViewModel.isFirstRun()) location.runOnFirstFix(() -> {
                requireActivity().runOnUiThread(() -> map.getController().animateTo(location.getMyLocation()));
            });
        });

        map.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                homeViewModel.setCenterPoint((GeoPoint) map.getMapCenter());
                homeViewModel.setZoomLevel(map.getZoomLevel());
            }
            return false;
        });

        if(!homeViewModel.isFirstRun()) homeViewModel.setLocation();
        addReportIcons();

        // FAB to open an AddReportActivity
        ExtendedFloatingActionButton fab = binding.fab;
        fab.setOnClickListener(view -> {
                Intent intent = new Intent(requireActivity(), AddReportActivity.class);
                startActivity(intent);
        });

        return root;
        /*
                // FAB to open an AddReportActivity
                ExtendedFloatingActionButton fab = binding.fab;
                fab.setOnClickListener(view -> {
                    Intent intent = new Intent(requireActivity(), AddReportActivity.class);
                    startActivity(intent);
                });

                if (homeViewModel.permissionsRequested()) homeViewModel.setLocation();
                return root;
            }else {

                System.out.println("AAAAfragment");

                binding = FragmentHomeBinding.inflate(inflater, container, false);
                View root = binding.getRoot();
                map = binding.map;
                map.setTileSource(TileSourceFactory.MAPNIK);
                map.setBuiltInZoomControls(false);
                map.setMultiTouchControls(true);

                double latitude = savedInstanceState.getDouble("latitude", 41.8902);
                double longitude = savedInstanceState.getDouble("longitude", 12.4922);
                int zoom_level = savedInstanceState.getInt("zoom_level", 16);

                GeoPoint restoredCenter = new GeoPoint(latitude, longitude);
                System.out.println("A1");
                map.getController().setCenter(restoredCenter);
                map.getController().setZoom(zoom_level);
                System.out.println("A2");
                return root;

            }
         */
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        System.out.println("ONSAVEINSTANCESTATEfragment");
        super.onSaveInstanceState(outState);

        IGeoPoint center = map.getMapCenter();
        outState.putDouble("latitude", center.getLatitudeE6() / 1e6);
        outState.putDouble("longitude", center.getLongitudeE6() / 1e6);

        outState.putInt("zoom_level", map.getZoomLevel());

    }



    //Quando il fragment torna in primo piano (ad esempio, l'utente naviga indietro).
    @Override
    public void onResume() {
        System.out.println("ONRESUMEfragment");
        super.onResume();
        if (binding != null) {
            binding.map.onResume();
        }
    }

    //Quando il fragment non è più visibile mette in pausa per risparmiare risorse
    @Override
    public void onPause() {
        System.out.println("ONPAUSEfragment");
        super.onPause();
        if (binding != null) {
            binding.map.onPause();
        }
    }

    //per evitare memoryleak imposto il binding a null, altirmenti il GC non sa che può liberare la risorsa
    @Override
    public void onDestroyView() {
        System.out.println("ONDESTROYVIEWfragment");
        super.onDestroyView();
        binding = null;
    }

    private void addReportIcons () {
        DbHelper.getReports(
                reports -> {
                    for(ReportModel elem : reports) {
                        // Icon
                        Drawable icon = ContextCompat.getDrawable(requireContext(), R.drawable.location);
                        icon.setColorFilter(Color.parseColor("#FF4F378B"), PorterDuff.Mode.SRC_IN);

                        // Marker
                        Marker m = new Marker(map);
                        m.setPosition(new GeoPoint(elem.getLatitude(), elem.getLongitude()));
                        m.setTitle(elem.getCategory()); // Set the title
                        m.setSnippet(elem.getDescription()); // Set the snippet (optional)
                        m.setIcon(icon);
                        m.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_TOP);

                        /*
                        m.setOnMarkerClickListener((marker, mapView) -> {
                            // Open Report Page
                            return true;
                        });
                         */

                        // Add the marker to the map
                        map.getOverlays().add(m);
                    }
                },
                e -> {
                    // TODO
                }
        );
    }

}