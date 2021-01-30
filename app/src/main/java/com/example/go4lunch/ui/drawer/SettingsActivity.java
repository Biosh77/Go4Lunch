package com.example.go4lunch.ui.drawer;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.base.BaseActivity;
import com.example.go4lunch.models.Workmate;
import com.google.android.gms.common.api.Batch;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnCheckedChanged;


public class SettingsActivity extends BaseActivity {


    @BindView(R.id.notification_textview)
    TextView mText;
    @BindView(R.id.notification_switch)
    SwitchCompat mSwitch;
    @BindView(R.id.accueil_toolbar)
    Toolbar toolbar;

    private SharedPreferences mSharedPreferences;


    @Override
    public int getLayout() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onConfigureDesign() {

        configureToolbar();
        updateUi();

    }

    private void configureToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getMenu().removeItem(R.id.search_restaurant);
        toolbar.setNavigationOnClickListener(v -> finish());
    }


    private void updateUi() {
        mSharedPreferences = getSharedPreferences("Go4Lunch", MODE_PRIVATE);
        boolean notificationBoolean = mSharedPreferences.getBoolean("notificationBoolean", false);
        if (notificationBoolean) {
            mSwitch.setChecked(true);
        }
    }


    // Notifications -->

    @OnCheckedChanged(R.id.notification_switch)
    public void onCheckedChangeListener(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            this.setNotificationsTrueInSharedPreferences();
        } else {
            this.setNotificationsFalseInSharedPreferences();
        }
    }

    private void setNotificationsTrueInSharedPreferences() {
        mSharedPreferences.edit().putBoolean("notificationBoolean", true).apply();
    }

    private void setNotificationsFalseInSharedPreferences() {
        mSharedPreferences.edit().putBoolean("notificationBoolean", false).apply();
    }


}
