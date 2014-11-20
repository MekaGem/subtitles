package com.example.akmal.testingnotification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import ru.yandex.speechkit.*;
import ru.yandex.speechkit.Error;

public class ListenerService extends Service {
    PhraseSpotter spotter = new PhraseSpotter();
    NotificationController notificationController;

    public ListenerService() {
        notificationController = new NotificationController(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        //Создать спич кит
        spotter.initialize("ss", new PhraseSpotterListener() {
            @Override
            public void onPhraseSpotted(String s, int i) {
                notificationController.createNotification(s);
            }

            @Override
            public void onPhraseSpotterStarted() {

            }

            @Override
            public void onPhraseSpotterStopped() {

            }

            @Override
            public void onPhraseSpotterError(Error error) {

            }
        });

        spotter.start();

        Log.v("SS", "START");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void onDestroy()
    {
        spotter.stop();
        spotter.uninitialize();
        Log.v("SS", "STOP");
        //остановить спичкит
        super.onDestroy();
    }
}
