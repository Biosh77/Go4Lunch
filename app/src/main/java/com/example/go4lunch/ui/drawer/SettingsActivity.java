package com.example.go4lunch.ui.drawer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;


import com.example.go4lunch.AccueilActivity;
import com.example.go4lunch.R;
import com.example.go4lunch.base.BaseActivity;

import com.example.go4lunch.notification.NotificationRestaurant;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;


public class SettingsActivity extends BaseActivity {


    @BindView(R.id.notification_textview)
    TextView mText;
    @BindView(R.id.notification_switch)
    SwitchCompat mSwitch;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.accueil_toolbar)
    Toolbar toolbar;


    SharedPreferences mSharedPreferences;
    public static final String BOOLEAN = "NotificationBoolean";
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    public int getLayout() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onConfigureDesign() {

        configureToolbar();
        spinnerLanguage();

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        boolean notificationBoolean = mSharedPreferences.getBoolean(BOOLEAN, false);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, NotificationRestaurant.class);
        pendingIntent = PendingIntent.getBroadcast(this, 1, alarmIntent, 0);


        if (notificationBoolean) {
            mSwitch.setChecked(true);
        }

        mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (mSwitch.isChecked()) {
                editor.putBoolean(BOOLEAN, true);
                startAlarm();
                Toast.makeText(this, getResources().getString(R.string.Alarm_manager_start), Toast.LENGTH_SHORT).show();
            } else {
                editor.putBoolean(BOOLEAN, false);
                cancelAlarm();
                Toast.makeText(this, getResources().getString(R.string.Alarm_manager_cancel), Toast.LENGTH_SHORT).show();
            }
            editor.apply();
        });
    }

    private void startAlarm() {


        Calendar cal = Calendar.getInstance();
        // If it is after noon then we add one day to the meter of release of the alarm
        if (cal.get(Calendar.HOUR_OF_DAY) > 12) cal.add(Calendar.DATE, 1);
        // The alarm next one will thus be at 12:00 am tomorrow
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    private void cancelAlarm() {
        alarmManager.cancel(pendingIntent);
    }


    private void configureToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getMenu().removeItem(R.id.search_restaurant);
        toolbar.setNavigationOnClickListener(v -> finish());
    }


    // Language -->

    private void spinnerLanguage() {

        List<String> list = new ArrayList<>();

        list.add(getResources().getString(R.string.select));
        list.add(getResources().getString(R.string.en));
        list.add(getResources().getString(R.string.fr));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View view,
                                       int position, long l) {
                switch (position) {
                    case 0:
                        break;

                    case 1:
                        setLocale("en");
                        startActivity(new Intent(getBaseContext(), AccueilActivity.class));
                        finish();

                        break;

                    case 2:
                        setLocale("fr");
                        startActivity(new Intent(getBaseContext(), AccueilActivity.class));
                        finish();

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    private void setLocale(String localeName) {
        Locale myLocale = new Locale(localeName);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}
