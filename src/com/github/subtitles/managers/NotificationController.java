package com.github.subtitles.managers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.PowerManager;
import com.github.subtitles.R;

/**
 * Created by Akmal on 20.11.2014.
 */

public class NotificationController
{
    private static final int NOTIFY_ID = 1;
    private Context context;
    private static String command;

    public static long[] pattern = {0, 500, 300, 300, 200,
            500, 300, 300, 200,
            500, 300, 300, 200,
            500, 300, 300, 200,
            500, 300, 300, 200};

    NotificationController(Context context)
    {
        this.context = context;
    }

    public void createNotification(String command)
    {
        Intent notificationIntent = new Intent(context, EmptyDialogScreen.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                NOTIFY_ID, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Resources res = context.getResources();
        Notification.Builder builder = new Notification.Builder(context);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.newest)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.newest))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("Напоминание")
                .setContentText("К вам только что обратились! Обращение: " + command)
                .setTicker("К вам обратились! Обращение: " + command);

        Notification notification = builder.getNotification();

        notification.vibrate = pattern;
        notification.defaults = Notification.DEFAULT_SOUND;

        notification.ledARGB = Color.RED;
        notification.ledOffMS = 0;
        notification.ledOnMS = 1;
        notification.flags = notification.flags | Notification.FLAG_SHOW_LIGHTS;

        notificationManager.notify(NOTIFY_ID, notification);
        this.command = command;

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
        wakeLock.acquire();
    }

    public static String getLastCommand()
    {
        return command;
    }
}
