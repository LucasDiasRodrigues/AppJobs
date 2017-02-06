package com.teamappjobs.appjobs.GCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.teamappjobs.appjobs.R;

import java.util.List;

/**
 * Created by Lucas on 09/12/2015.
 */
public class MyGcmListenerService extends com.google.android.gms.gcm.GcmListenerService {


    private static final String TAG = "MyGcmListenerService";


    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("mensagem");
        String tipo = data.getString("tipo");
        String idNotificacao = data.getString("idNotificacao");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        Log.d(TAG, "idNotificacao:  " + idNotificacao);

        switch (tipo) {
            case "chat":

                break;

        }

        //   - Store message in local database.
        //    - Update UI.

    }



    private void sendNotification(String titulo, String message, String tag, PendingIntent pendingIntent) {


        Log.i(TAG, "Entrou na notific");

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_comment_multiple_outline)
                .setContentTitle(titulo)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(tag, 0, notificationBuilder.build());

    }

    private void sendNotificationNoSound(String titulo, String message, String tag, PendingIntent pendingIntent) {

        Log.i(TAG, "Entrou na notific");

        //    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_comment_multiple_outline)
                .setContentTitle(titulo)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(null)
                .setVibrate(null)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(tag, 0, notificationBuilder.build());

    }


}
