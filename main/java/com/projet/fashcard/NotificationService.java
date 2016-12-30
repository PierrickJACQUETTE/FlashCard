package com.projet.fashcard;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.CursorLoader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NotificationService extends Service {

    private static String authority = "com.project.fcContentProvider";
    CursorLoader c;

    public boolean checkDate(int longtemps, String dateBase) {
        try {
            String format = "dd MM yyyy";
            SimpleDateFormat formater = new SimpleDateFormat(format);
            Date d = new Date();
            String date = formater.format(d);
            Date dateDay = formater.parse(date);
            Date date2 = formater.parse(dateBase);
            long diff = dateDay.getTime() - date2.getTime();
            return (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)) > longtemps;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();
        Uri uri;
        Uri.Builder builder = new Uri.Builder();
        uri = builder.scheme("content").authority(authority).appendPath("jeu_table").build();
        c = new CursorLoader(this, uri, new String[]{"_id", "nom", "lastView"}, null, null, null);
        c.registerListener(0, new android.support.v4.content.Loader.OnLoadCompleteListener<Cursor>() {
            @Override
            public void onLoadComplete(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
                int longtemps = 1;

                String mssgNotification = "";
                int cmpt = 0;
                data.moveToFirst();
                for (int i = 0; i < data.getCount(); i++) {
                    String date = data.getString(2);
                    if (checkDate(longtemps, date)) {
                        mssgNotification += data.getString(1) + " ";
                        cmpt++;
                    }
                    data.moveToNext();
                }
                String notifications = "Les jeux suivants ne sont pas utilisés depuis longtemps : \n";
                String notification = "Le jeu suivant n'est pas utilisé depuis longtemps : \n";
                if (cmpt > 1) {
                    envoyerNotification(notifications, mssgNotification);
                } else if (cmpt == 1) {
                    notification += mssgNotification;
                    envoyerNotification(notification, mssgNotification);
                }
            }
        });
        c.startLoading();

    }

    @Override
    public void onDestroy() {
        if (c != null) {
            c.cancelLoad();
            c.stopLoading();
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void envoyerNotification(String titre, String texte) {
        Intent intent = new Intent(this, ListeActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ListeActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification =
                new NotificationCompat.Builder(this)
                        .setContentTitle(titre)
                        .setContentText(texte)
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

