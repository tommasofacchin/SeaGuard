package com.seaguard.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.seaguard.databinding.FragmentHomeBinding;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Map
        MapView map = binding.map;
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);

        MapController mapController = (MapController) map.getController();
        mapController.setZoom(6);

        // Location
        MyLocationNewOverlay location = new MyLocationNewOverlay(new GpsMyLocationProvider(map.getContext()), map);
        location.enableMyLocation();
        //aggiunta per mettere al centro della mappa la posizione appena disponibile
        /*location.runOnFirstFix(() -> {
            if (map != null && location.getMyLocation() != null) {
                map.getController().animateTo(location.getMyLocation());
            }
        });*/
        location.runOnFirstFix(() -> {
            if (map != null && location.getMyLocation() != null) {
                // La posizione è disponibile, centriamo la mappa su di essa
                map.getController().animateTo(location.getMyLocation());
            } else {
                // La posizione non è disponibile, puoi decidere cosa fare
                // Ad esempio, centrare la mappa su una posizione predefinita (Roma, ad esempio)
                GeoPoint defaultLocation = new GeoPoint(41.9028, 12.4964); // Roma
                map.getController().animateTo(defaultLocation);
            }
        });

        map.getOverlays().add(location);

        // Restore state, se esiste gia un istanza di mappa salvata dopo
        // un qualche evento riparti da quella
        if (savedInstanceState != null) {
            double latitude = savedInstanceState.getDouble("latitude", 48.8588443); // Default
            double longitude = savedInstanceState.getDouble("longitude", 2.2943506);
            int zoom = savedInstanceState.getInt("zoom", 6);
            mapController.setZoom(zoom);
            mapController.setCenter(new GeoPoint(latitude, longitude));
        }

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