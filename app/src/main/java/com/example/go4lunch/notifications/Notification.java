package com.example.go4lunch.notifications;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.go4lunch.AccueilActivity;
import com.example.go4lunch.R;
import com.example.go4lunch.ViewModel;
import com.example.go4lunch.models.Workmate;
import com.example.go4lunch.repository.UserDataRepository;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;

public class Notification extends FirebaseMessagingService {

    private String messageBody;
    private Workmate workmate;
    private List<Workmate> mWorkmates;
    private boolean notificationBoolean;


    public Notification(){}

    public void onNewToken(String token){
        super.onNewToken(token);
        Log.e("Refreshed token:",token);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            this.getUserUidInSharedPreferences();
            if (notificationBoolean) {
                this.retrievesWorkmateData();
            }
        }
    }

    private void getUserUidInSharedPreferences() {
        SharedPreferences mSharedPreferences = getSharedPreferences("Go4Lunch", MODE_PRIVATE);
        notificationBoolean = mSharedPreferences.getBoolean("notificationBoolean", false);
    }

    public Workmate currentWorkmate(){
        for (int i = 0; i < mWorkmates.size() ; i++) {
            if (UserDataRepository.getCurrentUser().getUid().equals(mWorkmates.get(i).getUid())) {
                workmate = mWorkmates.get(i);
            }
        }return workmate;
    }

    private void retrievesWorkmateData() {
        UserDataRepository.getWorkmate(currentWorkmate().getUid()).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                workmate = documentSnapshot.toObject(Workmate.class);
                if (workmate != null && !workmate.getInterestedBy().equals("")) {
                    retrievesOtherWorkmatesWithSameChoice();
                }
            }
        });
    }

    private void retrievesOtherWorkmatesWithSameChoice() {
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
            buildBodyMessage(listOfWorkmateWithSameChoice);
        });
    }

    private void buildBodyMessage(ArrayList<Workmate> listOfWorkmateWithSameChoice) {
        if (listOfWorkmateWithSameChoice.size() != 0) {
            StringBuilder workmatesName = new StringBuilder();
            int i = 0;
            do {
                if (i == listOfWorkmateWithSameChoice.size() -1) {
                    workmatesName.append(listOfWorkmateWithSameChoice.get(i).getUsername()).append(".");
                } else {
                    workmatesName.append(listOfWorkmateWithSameChoice.get(i).getUsername()).append(", ");
                }
                i++;
            } while (i != listOfWorkmateWithSameChoice.size());
            messageBody = workmate.getInterestedBy()+ getString(R.string.located_at) + workmatesName.toString();
        } else {
            messageBody = workmate.getInterestedBy() + getString(R.string.located_at);
        }
        this.sendVisualNotification();
    }

    private void sendVisualNotification() {
        Intent intent = new Intent(this, AccueilActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(messageBody);

        String channelId = getString(R.string.default_notification_channel_id);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_notifications_noir_24dp)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.notification_title))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(bigTextStyle);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence Name = getString(R.string.name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId,Name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        String NOTIFICATION_TAG = "FIREBASE";
        int NOTIFICATION_ID = 456;
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }

}




