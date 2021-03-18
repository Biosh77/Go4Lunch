package com.example.go4lunch.notification;


import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.go4lunch.R;

import com.example.go4lunch.models.Workmate;
import com.example.go4lunch.repository.UserDataRepository;
import com.google.firebase.firestore.DocumentSnapshot;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.go4lunch.repository.UserDataRepository.getCurrentUser;


public class NotificationRestaurant extends BroadcastReceiver {

    private Workmate workmate;
    private String messageBody;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent serviceIntent = new Intent(context, MyService.class);
            context.startService(serviceIntent);
        } else {
            retrievesWorkmateData(context);
        }
    }



    private void retrievesWorkmateData(Context context) {
        UserDataRepository.getWorkmate(getCurrentUser().getUid()).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                workmate = documentSnapshot.toObject(Workmate.class);
                if (workmate != null && !workmate.getInterestedBy().equals("")) {
                    retrievesOtherWorkmatesWithSameChoice(context);
                }
            }
        });
    }

    private void retrievesOtherWorkmatesWithSameChoice(Context context) {

        ArrayList<Workmate> listOfWorkmateWithSameChoice = new ArrayList<>();
        UserDataRepository.getWorkmatesWhoHaveSameChoice(workmate.getInterestedBy()).addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots != null) {
                List<DocumentSnapshot> listOfWorkmatesWithSameChoice = new ArrayList<>(queryDocumentSnapshots.getDocuments());
                if (listOfWorkmatesWithSameChoice.size() != 0) {
                    for (DocumentSnapshot documentSnapshot : listOfWorkmatesWithSameChoice) {
                        Workmate workmateTemp = documentSnapshot.toObject(Workmate.class);
                        if (workmateTemp != null && !workmate.getUid().equals(workmateTemp.getUid())) {
                            listOfWorkmateWithSameChoice.add(documentSnapshot.toObject(Workmate.class));
                        }
                    }
                }
            }
            if (listOfWorkmateWithSameChoice.size() != 0) {
                StringBuilder workmatesName = new StringBuilder();
                int i = 0;
                do {
                    if (i == listOfWorkmateWithSameChoice.size() - 1) {
                        workmatesName.append(listOfWorkmateWithSameChoice.get(i).getUsername()).append(".");
                    } else {
                        workmatesName.append(listOfWorkmateWithSameChoice.get(i).getUsername()).append(", ");
                    }
                    i++;
                } while (i != listOfWorkmateWithSameChoice.size());

                messageBody = context.getString(R.string.With) + " " + workmatesName.toString() + context.getString(R.string.located_at) + workmate.getInterestedBy() + " - " + workmate.getVicinity();
            } else {
                messageBody = context.getString(R.string.located_at_choice) + workmate.getInterestedBy() + " - " + workmate.getVicinity();
            }
            this.sendVisualNotification(context);
        });
    }

    private void sendVisualNotification(Context context) {

        String channelId = context.getString(R.string.default_notification_channel_id);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notifications_noir_24dp)
                .setContentTitle(context.getString(R.string.app_name))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(messageBody);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence Name = context.getString(R.string.name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, Name, importance);
            if (notificationManager != null)
            notificationManager.createNotificationChannel(mChannel);
        }
        if (notificationManager != null)
        notificationManager.notify(1, notificationBuilder.build());
    }
}

