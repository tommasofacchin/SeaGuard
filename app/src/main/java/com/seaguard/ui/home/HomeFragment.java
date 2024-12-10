package com.seaguard.ui.home;

import android.content.Intent;
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

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private MapView map;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Map
        map = binding.map;
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);

        MapController mapController = (MapController) map.getController();
        mapController.setCenter(new GeoPoint(41.8902, 12.4922)); // Rome
        mapController.setZoom(16);

        homeViewModel.setCallBack(() -> {
            //provider.addLocationSource(LocationManager.NETWORK_PROVIDER);
            MyLocationNewOverlay location = new MyLocationNewOverlay(new GpsMyLocationProvider(map.getContext()), map);
            location.enableMyLocation();
            map.getOverlays().add(location);

            location.runOnFirstFix(() -> {
                requireActivity().runOnUiThread(() -> map.getController().animateTo(location.getMyLocation()));
            });
        });

        // FAB to open an AddReportActivity
        ExtendedFloatingActionButton fab = binding.fab;
        fab.setOnClickListener(view -> {
                Intent intent = new Intent(requireActivity(), AddReportActivity.class);
                startActivity(intent);
        });

        if(homeViewModel.permissionsRequested()) homeViewModel.setLocation();

        return root;

    }

    //metodi onAzione per il comportamento corretto del fragment dopo
    //un qualsiasi evento,
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (binding != null) {
            MapView map = binding.map;
            GeoPoint center = (GeoPoint) map.getMapCenter();
            outState.putDouble("latitude", center.getLatitude());
            outState.putDouble("longitude", center.getLongitude());
            outState.putInt("zoom", map.getZoomLevel());
        }
    }

    //Quando il fragment torna in primo piano (ad esempio, l'utente naviga indietro).
    @Override
    public void onResume() {
        super.onResume();
        if (binding != null) {
            binding.map.onResume();
        }
    }

    //Quando il fragment non è più visibile mette in pausa per risparmiare risorse
    @Override
    public void onPause() {
        super.onPause();
        if (binding != null) {
            binding.map.onPause();
        }
    }

    //per evitare memoryleak imposto il binding a null, altirmenti il GC non sa che può liberare la risorsa
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}