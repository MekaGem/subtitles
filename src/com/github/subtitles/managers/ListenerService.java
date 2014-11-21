package com.github.subtitles.managers;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.IBinder;
import android.util.Log;
import ru.yandex.speechkit.*;
import ru.yandex.speechkit.Error;

import java.io.IOException;
import java.util.Arrays;

public class ListenerService extends Service {
    PhraseSpotter spotter = new PhraseSpotter();
    NotificationController notificationController;

    public ListenerService() {
        notificationController = new NotificationController(this);
        System.out.println("SSdffff");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("SS");
        Log.i("Listener", "Something happened");
//        notificationController = new NotificationController(this);
        notificationController.createNotification("lol");
        try {
            String[] fileList = getAssets().list("values");
            Log.i("ASSETS", Arrays.toString(fileList));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        spotter.initialize("model", new PhraseSpotterListener() {
//
//            @Override
//            public void onPhraseSpotted(String s, int i) {
//                notificationController.createNotification(s);
//            }
//
//            @Override
//            public void onPhraseSpotterStarted() {
//
//            }
//
//            @Override
//            public void onPhraseSpotterStopped() {
//
//            }
//
//            @Override
//            public void onPhraseSpotterError(Error error) {
//
//            }
//        });
//        Log.i("Listener", "Init complete");
//
//        spotter.start();
//
//        Log.v("SS", "START");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void onDestroy()
    {
//        spotter.stop();
//        spotter.uninitialize();
        Log.v("SS", "STOP");
        //остановить спичкит
        super.onDestroy();
    }
}
