package com.example.go4lunch;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


import androidx.annotation.Nullable;

import com.example.go4lunch.base.BaseActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {


    @BindView(R.id.test_button)
    Button testButton;

    @BindView(R.id.sign_in_button)
    SignInButton btn_login;


    @Override
    protected void onConfigureDesign() {
        setContentView(R.layout.activity_main);
        btn_login = findViewById(R.id.sign_in_button);


    }


    @OnClick(R.id.test_button)
    void onTestButtonClick() {
        Intent i = new Intent(getApplicationContext(), AccueilActivity.class);
        startActivity(i);
    }


}
