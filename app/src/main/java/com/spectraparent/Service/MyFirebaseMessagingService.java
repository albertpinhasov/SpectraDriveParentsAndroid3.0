package com.spectraparent.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.spectraparent.Activities.MainHomeActivity;
import com.spectraparent.android.R;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        handleNotification(remoteMessage.getData());
        Log.e("Recieved Firebase Noti", remoteMessage.getData().toString());
    }

    private void handleNotification(Map<String, String> message) {
        Intent intent = new Intent(this, MainHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.putExtra("key", messageBody);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Your Drive is arriving")
                .setContentText("Please be in Time")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.spectra_logo))
                .setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }
}