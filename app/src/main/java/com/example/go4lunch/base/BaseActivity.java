package com.example.go4lunch.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {


     protected void onCreate (@Nullable Bundle savedInstanceState ) {
         super.onCreate(savedInstanceState);
         onConfigureDesign();
         ButterKnife.bind(this);
     }

     protected abstract void onConfigureDesign();


}
