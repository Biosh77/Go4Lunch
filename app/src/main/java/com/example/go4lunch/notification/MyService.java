package com.example.go4lunch.notification;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Calendar;

public class MyService extends IntentService {


    public MyService() {
        super("MyService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Calendar cal = Calendar.getInstance();
        // If it is after noon then we add one day to the meter of release of the alarm
        if (cal.get(Calendar.HOUR_OF_DAY) > 12 ) cal.add(Calendar.DATE, 1);
        // The alarm next one will thus be at 12:00 am tomorrow
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 0);


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, NotificationRestaurant.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        assert alarmManager != null;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
    }


}
