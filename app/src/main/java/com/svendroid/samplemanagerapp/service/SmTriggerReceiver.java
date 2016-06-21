package com.svendroid.samplemanagerapp.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.svendroid.samplemanagerapp.MeasureActivity;
import com.svendroid.samplemanagerapp.R;
import com.svendroid.samplemanagerapp.util.Config;

import java.util.Random;

/**
 * Created by svhe on 11.04.2016.
 */
public class SmTriggerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("Svendroid", "Trigger broadcast received: " + intent.getStringExtra("measureId"));

        Intent measureIntent = new Intent(context, MeasureActivity.class);
        measureIntent.putExtra("type", intent.getStringExtra("type"));
        measureIntent.putExtra("text", intent.getStringExtra("text"));
        measureIntent.putExtra("alias", intent.getStringExtra("alias"));
        measureIntent.putExtra("values", intent.getStringArrayExtra("values"));
        measureIntent.putExtra("measureId", intent.getStringExtra("measureId"));
        measureIntent.putExtra("hasChildren", intent.getBooleanExtra("hasChildren", false));
        measureIntent.setAction(intent.getAction());
        measureIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //measureIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        boolean invasiveMode = false;

        if (invasiveMode) {
            // Show activity directly
            context.startActivity(measureIntent);
        } else {
            // Create notification
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            // Sets an ID for the notification
            int notificationId = new Random().nextInt(999999);
            measureIntent.putExtra("notificationId", notificationId);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_stat_notification)
                            .setSound(alarmSound)
                            .setContentTitle("Frage von Svendroid")
                            .setContentText(intent.getStringExtra("text"));

            PendingIntent measurePendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            measureIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            mBuilder.setContentIntent(measurePendingIntent);

            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            mNotifyMgr.notify(notificationId, mBuilder.build());
        }

    }

}
