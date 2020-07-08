package com.example.go4lunch;



import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


import com.example.go4lunch.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {



@BindView(R.id.test_button)
 Button testButton;


    @Override
    protected void onConfigureDesign() {
        setContentView(R.layout.activity_main);
    }

    @OnClick(R.id.test_button)
     void onTestButtonClick(){
        Intent i = new Intent(getApplicationContext(),AccueilActivity.class);
        startActivity(i);
    }
}