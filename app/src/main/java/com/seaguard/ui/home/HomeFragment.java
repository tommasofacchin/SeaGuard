package com.seaguard.ui.home;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.seaguard.AddReportActivity;
import com.seaguard.databinding.FragmentHomeBinding;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private MapView map;
    private GeoPoint centerPoint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            centerPoint = new GeoPoint(35.682839, 139.759455); // Tokyo
        }
    }

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

        homeViewModel.getCenterPoint().observe(getViewLifecycleOwner(), geoPoint -> {
            if(geoPoint != null) mapController.setCenter(geoPoint);
        });

        homeViewModel.getZoomLevel().observe(getViewLifecycleOwner(), zoom -> {
            if(zoom != null) mapController.setZoom(zoom);
        });

        homeViewModel.setCallBack(() -> {
            //provider.addLocationSource(LocationManager.NETWORK_PROVIDER);
            MyLocationNewOverlay location = new MyLocationNewOverlay(new GpsMyLocationProvider(map.getContext()), map);
            location.enableMyLocation();
            map.getOverlays().add(location);

            if(homeViewModel.isFirstRun()) location.runOnFirstFix(() -> {
                requireActivity().runOnUiThread(() -> map.getController().animateTo(location.getMyLocation()));
            });
        });

        // MapListener Overlay
        map.getOverlays().add(new Overlay() {
            @Override
            public void draw(Canvas canvas, MapView mapView, boolean shadow) {
                super.draw(canvas, mapView, shadow);
                homeViewModel.setCenterPoint((GeoPoint) mapView.getMapCenter());
                homeViewModel.setZoomLevel(mapView.getZoomLevel());
            }
        });

        if(!homeViewModel.isFirstRun()) homeViewModel.setLocation();

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

}