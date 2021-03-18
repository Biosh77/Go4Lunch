package com.example.go4lunch;


import android.Manifest;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.go4lunch.models.Workmate;
import com.example.go4lunch.repository.UserDataRepository;
import com.example.go4lunch.base.BaseActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.SignInButton;

import java.util.Collections;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class ConnectionActivity extends BaseActivity {


    private static final int RC_SIGN_IN = 123;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private String TAG = "permission";
    private String message = "Permission granted";


    @BindView(R.id.sign_in_button)
    SignInButton btn_login;

    @BindView(R.id.login__fb_button)
    Button btn_fb_login;

    @BindView(R.id.twitter_login_button)
    Button btn_twitter_login;

    @BindView(R.id.lunch)
    ImageView image_lunch;

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onConfigureDesign() {

        requestLocationPermission();

        if (isCurrentUserLogged()) {
            this.startActivityIfLogged();
        }

    }

    // --------------------
    // ACTIONS
    // --------------------

    // Google
    @OnClick(R.id.sign_in_button)
    public void onClickGoogleButton() {
        if (isCurrentUserLogged()) {
            this.startActivityIfLogged();
        } else {
            this.startSignInActivityForGoogle();
        }
    }

    // Facebook
    @OnClick(R.id.login__fb_button)
    public void onClickFacebookButton() {
        if (isCurrentUserLogged()) {
            this.startActivityIfLogged();
        } else {
            this.startSignInActivityForFacebook();
        }
    }


    // Twitter
    @OnClick(R.id.twitter_login_button)
    public void onClickTwitterButton() {
        if (isCurrentUserLogged()) {
            this.startActivityIfLogged();
        } else {
            this.startSignInActivityForTwitter();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // - Handle SignIn Activity response on activity result
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    // --------------------
    // NAVIGATION
    // --------------------

    // - Launch Sign-In Activity for Google
    private void startSignInActivityForGoogle() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build())) //Google
                        .setIsSmartLockEnabled(false, true)
                        .build(), RC_SIGN_IN);
    }

    // - Launch Sign-In Activity for Facebook
    private void startSignInActivityForFacebook() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Collections.singletonList(new AuthUI.IdpConfig.FacebookBuilder().build())) //Facebook
                        .setIsSmartLockEnabled(false, true)
                        .build(), RC_SIGN_IN);
    }

    // - Launch Sign-In Activity for Twitter
    private void startSignInActivityForTwitter() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Collections.singletonList(new AuthUI.IdpConfig.TwitterBuilder().build()))//Twitter
                        .setIsSmartLockEnabled(false, true)
                        .build(), RC_SIGN_IN);
    }

    // --------------------
    // UTILS
    // --------------------

    private void startActivityIfLogged() {
        Intent intent = new Intent(this, AccueilActivity.class);
        startActivity(intent);
        finish();
    }

    private void createUserFireStore() {
        if (this.getCurrentUser() != null) {
            String urlPicture = (this.getCurrentUser().getPhotoUrl() != null) ? this.getCurrentUser().getPhotoUrl().toString() : null;
            String username = this.getCurrentUser().getDisplayName();
            String uid = this.getCurrentUser().getUid();

            UserDataRepository.getWorkmate(this.getCurrentUser().getUid()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Workmate currentUser = task.getResult().toObject(Workmate.class);
                    if (currentUser != null && this.getCurrentUser().getUid().equals(currentUser.getUid())) {
                        UserDataRepository.updateUser(uid, username, urlPicture);
                        this.startActivityIfLogged();
                    } else {
                        UserDataRepository.createUser(uid, username, urlPicture).addOnCompleteListener(task1 -> {
                            this.startActivityIfLogged();
                        })
                                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_during_creating), Toast.LENGTH_SHORT).show());
                    }

                }
            });

        }

    }

    // - Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                this.createUserFireStore();

            } else { // ERRORS
                if (response == null) {
                    Toast.makeText(this, getResources().getString(R.string.cancel), Toast.LENGTH_SHORT).show();
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, getResources().getString(R.string.no_network), Toast.LENGTH_SHORT).show();
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, getResources().getString(R.string.Unknown_error), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // -------------------
    // PERMISSIONS
    // -------------------

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Log.i(TAG, message);
        } else {
            EasyPermissions.requestPermissions(this, getResources().getString(R.string.location_permission), REQUEST_LOCATION_PERMISSION, perms);
        }
    }

}






