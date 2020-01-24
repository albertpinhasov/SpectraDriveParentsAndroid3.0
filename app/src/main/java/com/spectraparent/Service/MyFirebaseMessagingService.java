package com.spectraparent.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.spectraparent.Activities.MainHomeActivity;
import com.spectraparent.Models.UpdatePickedUpTime;
import com.spectraparent.android.R;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("Recieved Firebase Noti", remoteMessage.getData().toString());
        if (remoteMessage.getData().size() > 0) {
            try {
                JSONObject json = new JSONObject(remoteMessage.getData());
                handleDataMessage(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void handleDataMessage(JSONObject json) {
        try {
            String title = (String) json.get("title");
            String body = (String) json.get("body");
            sendNotification(title, body);
            EventBus.getDefault().post(new UpdatePickedUpTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void sendNotification(String title , String message) {
        NotificationChannel mChannel = null;
        int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
        String CHANNEL_ID = "my_channel_02";// The id of the channel.
        CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        }
        Intent intent = new Intent(this, MainHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        /// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, iUniqueId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        int color = ContextCompat.getColor(this, R.color.colorPrimary);
        Notification notification;
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        if (useWhiteIcon) {
            notification = notificationBuilder.setSmallIcon(R.mipmap.ic_launcher).setColor(color).setTicker(getString(R.string.app_name)).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentIntent(pendingIntent)
                    .setSound(defaultSoundUri).setChannelId(CHANNEL_ID)
                   // .setStyle(new NotificationCompat.BigTextStyle().bigText(title))
                    .setContentText(message)
                    .build();
        } else {
            notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(false)
                  //  .setSound(defaultSoundUri).setSubText("fdgjdfg")
                    .setContentIntent(pendingIntent)
                    .build();
        }
        NotificationManager notificationManager1 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager1.createNotificationChannel(mChannel);
        }
        notificationManager1.notify(iUniqueId, notification);
    }
}