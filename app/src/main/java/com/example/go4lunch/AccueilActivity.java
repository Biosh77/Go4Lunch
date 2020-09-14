package com.example.go4lunch;

import android.content.Intent;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.base.BaseActivity;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;

public class AccueilActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    //FOR DESIGN
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;

    // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    // @Override
    // protected void onCreate(Bundle savedInstanceState) {
    //    super.onCreate(savedInstanceState);
    //   setContentView(R.layout.activity_accueil);

    //   mAuth = FirebaseAuth.getInstance();


    //   this.configureToolBar();

    //  this.configureDrawerLayout();

    //  this.configureNavigationView();

    //  this.OnBottomNavigation();
    //  }

    @BindView(R.id.accueil_nav_view)
    NavigationView accueilNavView;


    @Override
    public int getLayout() {
        return R.layout.activity_accueil;
    }

    @Override
    protected void onConfigureDesign() {

        mAuth = FirebaseAuth.getInstance();


        this.configureToolBar();

        this.OnBottomNavigation();

        this.configureDrawerLayout();

        this.configureNavigationView();

        this.updateUIWhenCreating();

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
                Toast.makeText(this, "LUNCH", Toast.LENGTH_SHORT).show();
                break;
            case R.id.accueil_drawer_settings:
                Toast.makeText(this, "SETTINGS", Toast.LENGTH_SHORT).show();
                break;
            case R.id.accueil_drawer_logout:
                logOut();
                break;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOut() {
        // Firebase sign out
        mAuth.signOut();
        // Google sign out
        GoogleSignOut();
        // FaceBook sign out
        LoginManager.getInstance().logOut();

        sendToLogin();
    }

    private void GoogleSignOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInClient.signOut();
    }

    private void sendToLogin() {

        Intent loginIntent = new Intent(AccueilActivity.this, MainActivity.class);
        startActivity(loginIntent);
        finish();

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

}
