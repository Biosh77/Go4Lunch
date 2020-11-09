package com.example.go4lunch.ui.drawer;

import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.example.go4lunch.R;
import com.example.go4lunch.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends BaseActivity {


    @BindView(R.id.notification_textview)
    TextView mText;
    @BindView(R.id.notification_switch)
    SwitchCompat mSwitch;
    @BindView(R.id.accueil_toolbar)
    Toolbar toolbar;


    @Override
    public int getLayout() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onConfigureDesign() {
    }
}
