package com.example.go4lunch.base;



import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.go4lunch.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.ButterKnife;



public abstract class BaseActivity extends AppCompatActivity {


     protected void onCreate (@Nullable Bundle savedInstanceState ) {
         super.onCreate(savedInstanceState);
         setContentView(getLayout());
         ButterKnife.bind(this);
         onConfigureDesign();
     }

    public abstract int getLayout();


    protected abstract void onConfigureDesign();


    // --------------------
    // ERROR HANDLER
    // --------------------

    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
            }
        };
    }

    // -------------------
    // UTILS
    // -------------------

    @Nullable
    protected FirebaseUser getCurrentUser(){return FirebaseAuth.getInstance().getCurrentUser();}

}
