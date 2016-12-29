package com.projet.fashcard;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationService extends Service {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("Noto", "onCreate: ");
        envoyerNotification();
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void envoyerNotification() {
        Intent intent = new Intent(this, ListeActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ListeActivity.class);
        stackBuilder.addNextIntent(intent);


        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification =
                new NotificationCompat.Builder(this)
                        .setContentTitle("lancer C")
                        .setContentText("Il faut lancer C immédiatement")
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.small)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.large))
                        .setTicker("Starting up!!!")
                        .setAutoCancel(true)
                        .build();
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                .notify(1, notification);

    }

}

