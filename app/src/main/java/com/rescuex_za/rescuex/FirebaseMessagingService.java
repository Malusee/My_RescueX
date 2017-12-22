package com.rescuex_za.rescuex;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Asus on 12/9/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title= remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();

        String from_userId = remoteMessage.getData().get("from_user_id");

        String intentFilter = remoteMessage.getNotification().getClickAction();

        android.support.v7.app.NotificationCompat.Builder Nbuilder = new android.support.v7.app.NotificationCompat.Builder(getApplicationContext());

        Nbuilder.setContentTitle(title);
        Nbuilder.setContentText(message);
        Nbuilder.setAutoCancel(true);

        Intent intent= new Intent(intentFilter);
        intent.putExtra("user_id",from_userId);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Nbuilder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(100, Nbuilder.build());

    }
}
