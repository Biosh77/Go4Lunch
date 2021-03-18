package com.example.go4lunch;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.base.BaseActivity;
import com.example.go4lunch.googlemapsretrofit.pojo.autocomplete.Prediction;
import com.example.go4lunch.googlemapsretrofit.pojo.nearbyplaces.Result;
import com.example.go4lunch.injection.Injection;
import com.example.go4lunch.models.Workmate;

import com.example.go4lunch.repository.UserDataRepository;
import com.example.go4lunch.ui.autoComplete.PlacesAutoCompleteAdapter;
import com.example.go4lunch.ui.detail.DetailActivity;
import com.example.go4lunch.ui.drawer.SettingsActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class AccueilActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    //FOR DESIGN
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SearchView.SearchAutoComplete mSearchAutoComplete;
    private ViewModel mViewModel;
    private Workmate workmate;
    private Result result;
    private android.location.Location onlyOneLocation;
    private LocationManager locationManager;
    private final int REQUEST_FINE_LOCATION = 1234;
    private List<Workmate> mWorkmates;
    private static final int SIGN_OUT_TASK = 10;



    @BindView(R.id.accueil_nav_view)
    NavigationView accueilNavView;


    @Override
    public int getLayout() {
        return R.layout.activity_accueil;
    }

    @Override
    protected void onConfigureDesign() {


        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates("gps", 0, 0, AccueilActivity.this);
        } catch (SecurityException ex) {
            Log.d("gps", "Location permission did not work!");
        }


        this.configureToolBar();

        this.OnBottomNavigation();

        this.configureDrawerLayout();

        this.configureNavigationView();

        this.updateUIWhenCreating();

    }



    public void initSearchView() {



        mViewModel.getPredictions().observe(this, predictions -> {
            List<Prediction> restaurantList = new ArrayList<>();
            for (Prediction prediction : predictions) {
                if (prediction.getTypes().contains("restaurant")){
                    restaurantList.add(prediction);
                }
            }
            mSearchAutoComplete.setAdapter(new PlacesAutoCompleteAdapter(getApplicationContext(), restaurantList));
            mSearchAutoComplete.showDropDown();
            mSearchAutoComplete.setOnItemClickListener((parent, view, position, id) -> {

                workmate = currentWorkmate();

                    Intent intent = new Intent(AccueilActivity.this, DetailActivity.class);
                    intent.putExtra("id", restaurantList.get(position).getPlaceId());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("workmate", workmate);
                    intent.putExtras(bundle);
                    startActivity(intent);


        });
        });

        SearchView searchView = findViewById(R.id.search_restaurant);
        mSearchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        int options = searchView.getImeOptions();
        searchView.setImeOptions(options| EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        searchView.setQueryHint(getResources().getString(R.string.search_a_restaurant));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mViewModel.setInput(newText);
                return false;
            }
        });
    }

    // BOTTOM NAV MENU

    private void OnBottomNavigation() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.Map_View_main, R.id.List_View, R.id.Workmates)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    // DRAWER

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Navigation Item Click
        int id = item.getItemId();

        switch (id) {
            case R.id.accueil_drawer_yourlunch:
                lunch();
                break;
            case R.id.accueil_drawer_settings:
                settings();
                break;
            case R.id.accueil_drawer_logout:
                signOutUserFromFireBase();
                break;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOutUserFromFireBase() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRequestCompleted(SIGN_OUT_TASK));
    }

    // Create OnCompleteListener called after tasks ended
    private OnSuccessListener<Void> updateUIAfterRequestCompleted(final int origin) {
        return aVoid -> {
            if (origin == SIGN_OUT_TASK) {
                Intent intent = new Intent(this, ConnectionActivity.class);
                startActivity(intent);
                finish();
            }
        };
    }


    public Workmate currentWorkmate() {
        for (int i = 0; i < mWorkmates.size(); i++) {
            if (UserDataRepository.getCurrentUser().getUid().equals(mWorkmates.get(i).getUid())) {
                workmate = mWorkmates.get(i);
            }
        }
        return workmate;
    }



    @Override
    public void onLocationChanged(android.location.Location location) {
        onlyOneLocation = location;

        locationManager.removeUpdates(AccueilActivity.this);

        mViewModel = new ViewModelProvider(this, Injection.provideViewModelFactory()).get(ViewModel.class);
        mViewModel.init(onlyOneLocation);
        mViewModel.getWorkmates().observe(this, workmates -> mWorkmates = workmates);

        initSearchView();
    }

    private void lunch() {

        mViewModel.getRestaurants().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {
                for (int i = 0; i < results.size(); i++) {
                    result = results.get(i);
                    workmate = currentWorkmate();

                }


                if (workmate.getInterestedBy() == null) {
                    Toast.makeText(AccueilActivity.this, getResources().getString(R.string.no_restaurant), Toast.LENGTH_SHORT).show();
                    return;
                }

                for (Result result : results) {
                    if (result.getName().equals(workmate.getInterestedBy())) {
                        Intent intent = new Intent(AccueilActivity.this, DetailActivity.class);
                        intent.putExtra("id", result.getPlaceId());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("workmate", workmate);
                        intent.putExtras(bundle);
                        AccueilActivity.this.startActivity(intent);
                    }
                    }
            }
        });

    }


    private void settings() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }


    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    // Configure Toolbar

    private void configureToolBar() {
        this.toolbar = (Toolbar) findViewById(R.id.accueil_toolbar);
        setSupportActionBar(toolbar);
    }

    // Configure Drawer Layout
    private void configureDrawerLayout() {
        this.drawerLayout = (DrawerLayout) findViewById(R.id.accueil_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Configure NavigationView
    private void configureNavigationView() {
        this.navigationView = (NavigationView) findViewById(R.id.accueil_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // --------------------
    // UI
    // --------------------

    // Arranging method that updating UI with Firestore data

    private void updateUIWhenCreating() {


        ImageView avatar = navigationView.getHeaderView(0).findViewById(R.id.avatar_user);


        TextView user = navigationView.getHeaderView(0).findViewById(R.id.user_name);


        TextView email2 = navigationView.getHeaderView(0).findViewById(R.id.user_email);


        if (this.getCurrentUser() != null) {

            if (this.getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this)
                        .load(this.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(avatar);
            }

            String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ? getString(R.string.info_no_email_found) : this.getCurrentUser().getEmail();
            email2.setText(email);

            // 7 - Get additional data from Firestore ( Username)
            user.setText(this.getCurrentUser().getDisplayName());
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
                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        locationManager.requestLocationUpdates("gps", 0, 0, this);
                    } catch (SecurityException ex) {
                        Log.d("gps", "Location permission did not work!");
                    }
                }
                break;
        }
    }
}