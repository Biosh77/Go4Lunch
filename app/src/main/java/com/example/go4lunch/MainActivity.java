package com.example.go4lunch;


import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.go4lunch.base.BaseActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import butterknife.BindView;


public class MainActivity extends BaseActivity {


    static final int GOOGLE_SIGN = 123;
    GoogleSignInOptions mGoogleSignInOptions;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;


    @BindView(R.id.sign_in_button)
    SignInButton btn_login;

    @BindView(R.id.login__fb_button)
    SignInButton btn_fb_login;

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onConfigureDesign() {

        //FACEBOOK
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        GoogleSignIn();
        signIn();
    }



    private void GoogleSignIn(){
        mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
    }

    private void signIn() {

        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);

        if (signInAccount != null || mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, AccueilActivity.class));
        }

        btn_login.setOnClickListener(v -> {
            Intent sign = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(sign, GOOGLE_SIGN);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN) {
            Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount signInAcc = signInTask.getResult(ApiException.class);
                AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAcc.getIdToken(), null);

                mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(getApplicationContext(), "You are connected", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), AccueilActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
}

