package com.example.go4lunch.ui.list;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.ViewModel;
import com.example.go4lunch.googlemapsretrofit.pojo.nearbyplaces.Result;
import com.example.go4lunch.injection.Injection;
import com.example.go4lunch.ui.detail.DetailActivity;
import com.google.gson.Gson;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListFragment extends Fragment implements LocationListener, ListAdapter.OnRestaurantClickListener {


    private android.location.Location onlyOneLocation;
    private LocationManager locationManager;
    private final int REQUEST_FINE_LOCATION = 1234;


    // FOR DESIGN

    @BindView(R.id.list_recycler)
    RecyclerView recyclerView;

    // FOR DATA

    private ListAdapter adapter;


    private ViewModel mViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this, Injection.provideViewModelFactory()).get(ViewModel.class);
        View root = inflater.inflate(R.layout.fragment_list, container, false);



        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);

            try {
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates("gps", 0, 0, ListFragment.this);
            } catch (SecurityException ex) {
                Log.d("gps", "Location permission did not work!");
            }

        ButterKnife.bind(this, root);
        return root;
    }



    private void configureRecyclerView(List<Result> results) {
        this.adapter = new ListAdapter(results,this,onlyOneLocation);
        this.recyclerView.setAdapter(adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    @Override
    public void onLocationChanged(android.location.Location location) {
        onlyOneLocation = location;
        locationManager.removeUpdates(this);

        mViewModel = new ViewModelProvider(this, Injection.provideViewModelFactory()).get(ViewModel.class);
        mViewModel.init(onlyOneLocation);
        mViewModel.getRestaurant().observe(this, new Observer<List<Result>>() {
                    @Override
                    public void onChanged(List<Result> results) {
                        try {
                            configureRecyclerView(results);

                        } catch (Exception e) {
                            Log.d("onResponse", "There is an error");
                            e.printStackTrace();
                        }
                    }
                }
        );
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

    @Override
    public void onRestaurantClick(Result result) {

        Gson gson = new Gson();
         Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("obj", gson.toJson(result));
         startActivity(intent);
    }
}