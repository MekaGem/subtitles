package com.github.subtitles.managers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;

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

        NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Resources res = context.getResources();
        Notification.Builder builder = new Notification.Builder(context);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_launcher))
                .setTicker("К вам обратились! Обращение: " + command)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("Напоминание")
                .setContentText("К вам только что обратились! Обращение: " + command);

        Notification n = builder.getNotification();
        n.vibrate = pattern;
        nm.notify(NOTIFY_ID, n);

        this.command = command;
    }

    public static String getLastCommand()
    {
        return command;
    }
}
