package com.example.go4lunch.base;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.AccueilActivity;
import com.example.go4lunch.R;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.ButterKnife;



public abstract class BaseActivity extends AppCompatActivity {

    static final int GOOGLE_SIGN =123;
    SignInButton btn_login;
    FirebaseAuth mAuth;
    GoogleSignInOptions mGoogleSignInOptions;
    GoogleSignInClient mGoogleSignInClient;

     protected void onCreate (@Nullable Bundle savedInstanceState ) {
         super.onCreate(savedInstanceState);
         onConfigureDesign();
         ButterKnife.bind(this);

         btn_login = findViewById(R.id.sign_in_button);

         mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                 .requestEmail()
                 .build();

         mGoogleSignInClient = GoogleSignIn.getClient(this,mGoogleSignInOptions);

         GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
         if (signInAccount != null){
             startActivity(new Intent(this, AccueilActivity.class));
         }

         btn_login.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent sign = mGoogleSignInClient.getSignInIntent();
                 startActivityForResult(sign,GOOGLE_SIGN);
             }
         });
     }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN){
            Task<GoogleSignInAccount> signInTask  = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount signInAcc = signInTask.getResult(ApiException.class);
                Toast.makeText(this, "You are connected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, AccueilActivity.class));
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract void onConfigureDesign();


}
