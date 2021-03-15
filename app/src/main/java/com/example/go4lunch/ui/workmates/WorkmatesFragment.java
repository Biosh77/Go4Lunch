package com.example.go4lunch.ui.workmates;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.AccueilActivity;
import com.example.go4lunch.R;
import com.example.go4lunch.ViewModel;
import com.example.go4lunch.googlemapsretrofit.pojo.nearbyplaces.Result;
import com.example.go4lunch.injection.Injection;
import com.example.go4lunch.models.Workmate;
import com.example.go4lunch.repository.UserDataRepository;
import com.example.go4lunch.ui.detail.DetailActivity;
import com.example.go4lunch.ui.list.ListFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

public class WorkmatesFragment extends Fragment implements WorkmatesAdapter.OnItemClickListener, LocationListener {


    //FOR DESIGN

    @BindView(R.id.workmates_recycler)
    RecyclerView recyclerView;

    // FOR DATA

    private ViewModel workmatesViewModel;
    private WorkmatesAdapter adapter;
    private Workmate workmate;
    private List<Workmate> mWorkmates;
    private List<Result> mResults;
    private Result result;
    private android.location.Location onlyOneLocation;
    private LocationManager locationManager;
    private ViewModel mViewModel;
    private final int REQUEST_FINE_LOCATION = 1234;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this, Injection.provideViewModelFactory()).get(ViewModel.class);
        View root = inflater.inflate(R.layout.fragment_workmates, container, false);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);

        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates("gps", 0, 0, WorkmatesFragment.this);
        } catch (SecurityException ex) {
            Log.d("gps", "Location permission did not work!");
        }
        ButterKnife.bind(this, root);
        return root;
    }

    private void configureRecyclerView(List<Workmate> workmates) {
        this.adapter = new WorkmatesAdapter(workmates, this);
        this.recyclerView.setAdapter(adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    @Override
    public void onLocationChanged(android.location.Location location) {
        onlyOneLocation = location;
        locationManager.removeUpdates(WorkmatesFragment.this);
        mViewModel = new ViewModelProvider(this, Injection.provideViewModelFactory()).get(ViewModel.class);
        mViewModel.init(onlyOneLocation);
        mViewModel.getRestaurants().observe(getViewLifecycleOwner(), new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {
                for (int i = 0; i < results.size(); i++) {
                    mResults = results;
                    result = results.get(i);

                }
            }
        });
        mViewModel.getWorkmates().observe(getViewLifecycleOwner(), new Observer<List<Workmate>>() {
            @Override
            public void onChanged(List<Workmate> workmates) {
                mWorkmates = workmates;

                for (int i = 0; i < workmates.size(); i++) {
                    if (UserDataRepository.getCurrentUser().getUid().equals(workmates.get(i).getUid())) {
                        workmate = workmates.get(i);
                    }
                }
                configureRecyclerView(workmates);
            }
        });
    }


    @Override
    public void onItemClick(Workmate workmate) {

        if (workmate.getInterestedBy() == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_restaurant), Toast.LENGTH_SHORT).show();
            return;
        }

        for (Result result : mResults) {
            if (result.getName().equals(workmate.getInterestedBy())) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("id", result.getPlaceId());
                Bundle bundle = new Bundle();
                bundle.putSerializable("workmate", workmate);
                intent.putExtras(bundle);
                startActivity(intent);
            }
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
}