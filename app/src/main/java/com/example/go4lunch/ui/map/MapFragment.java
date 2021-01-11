package com.example.go4lunch.ui.map;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.go4lunch.R;
import com.example.go4lunch.ViewModel;
import com.example.go4lunch.googlemapsretrofit.pojo.nearbyplaces.Result;
import com.example.go4lunch.injection.Injection;
import com.example.go4lunch.models.Workmate;
import com.example.go4lunch.repository.RestaurantDataRepository;
import com.example.go4lunch.repository.UserDataRepository;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;


import java.util.List;


public class MapFragment extends Fragment implements LocationListener {


    MapView mMapView;
    private GoogleMap mMap;
    private android.location.Location onlyOneLocation;
    private LocationManager locationManager;
    private final int REQUEST_FINE_LOCATION = 1234;


    private List<Workmate> mWorkmates;
    private Workmate workmate;


    private ViewModel RestaurantViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.Map_View);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        mapsInitializer();

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;


                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);

                else {
                    googleMap.setMyLocationEnabled(true);
                    try {
                        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                        locationManager.requestLocationUpdates("gps", 0, 0, MapFragment.this);
                    } catch (SecurityException ex) {
                        Log.d("gps", "Location permission did not work!");
                    }
                }
            }
        });

        return rootView;
    }


    public void mapsInitializer() {

        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    @Override
    public void onLocationChanged(android.location.Location location) {
        onlyOneLocation = location;
        locationManager.removeUpdates(this);

        if (!isDetached()) {
            RestaurantViewModel = new ViewModelProvider(this, Injection.provideViewModelFactory()).get(ViewModel.class);
            RestaurantViewModel.init(onlyOneLocation);
            RestaurantViewModel.getRestaurant().observe(this, new Observer<List<Result>>() {
                        @Override
                        public void onChanged(List<Result> results) {
                            RestaurantViewModel.getWorkmate().observe(MapFragment.this, workmates -> {
                                mWorkmates = workmates;
                          showMap(results);



                            });
                        }
                    }
            );
        }
    }


    private boolean getIfAWorkmateIsInterested(Result restaurant){
        for (int i = 0; i < mWorkmates.size() ; i++)
            if (restaurant.getName().equals(mWorkmates.get(i).getInterestedBy())) {
                return true;
            } return false;
    }

    public void showMap(List<Result> results){
        try {
            mMap.clear();
            // This loop will go through all the results and add marker on each location.
            for (int i = 0; i < results.size(); i++) {

                Double lat = results.get(i).getGeometry().getLocation().getLat();
                Double lng = results.get(i).getGeometry().getLocation().getLng();
                LatLng latLng = new LatLng(lat, lng);
                String placeName = results.get(i).getName();
                String vicinity = results.get(i).getVicinity();

                BitmapDescriptor icon;

                if (getIfAWorkmateIsInterested(results.get(i))) {
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurant_marker_green);
                }else {
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurant_marker_orange);
                }


                MarkerOptions markerOptions = new MarkerOptions().icon(icon);
                // Position of Marker on Map
                markerOptions.position(latLng);
                // Adding Title to the Marker
                markerOptions.title(placeName + " : " + vicinity);
                // Adding Marker to the Camera.
                mMap.addMarker(markerOptions);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }


        } catch (
                Exception e) {
            Log.d("onResponse", "There is an error");
            e.printStackTrace();
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("gps", "Location permission granted");
                    try {
                        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                        locationManager.requestLocationUpdates("gps", 0, 0, this);
                    } catch (SecurityException ex) {
                        Log.d("gps", "Location permission did not work!");
                    }
                }
                break;
        }
    }
}
