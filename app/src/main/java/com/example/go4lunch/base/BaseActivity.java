package com.example.go4lunch.base;



import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


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


    //@Nullable

    //protected FirebaseUser getCurrentUser(){return FirebaseAuth.getInstance().getCurrentUser();}
   // protected Boolean isCurrentUserLogged(){return (this.getCurrentUser()  != null);}

}
