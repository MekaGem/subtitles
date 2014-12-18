package com.github.subtitles.managers;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.IBinder;
import android.util.Log;
import ru.yandex.speechkit.PhraseSpotter;
import ru.yandex.speechkit.PhraseSpotterListener;

import java.io.*;

public class ListenerService extends Service {
    NotificationController notificationController;

    public ListenerService() {
        notificationController = new NotificationController(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        String dataPath = "/sdcard/data/" + getPackageName();
        String spotterModelsPath = dataPath + "/phrase-spotter";
        String modelName = "hackaton_lingware";

        PhraseSpotter.initialize(spotterModelsPath + "/" + modelName, new PhraseSpotterListener() {

            @Override
            public void onPhraseSpotted(String command, int i) {
                notificationController.createNotification(command);
            }

            @Override
            public void onPhraseSpotterStarted() {

            }

            @Override
            public void onPhraseSpotterStopped() {

            }

            @Override
            public void onPhraseSpotterError(ru.yandex.speechkit.Error error) {

            }
        });
        Log.i("Listener", "Init complete");

        PhraseSpotter.start();

        Log.v("ANALYZE", "START");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void onDestroy()
    {
        PhraseSpotter.stop();
        PhraseSpotter.uninitialize();
        Log.v("ANALYZE", "STOP");
        super.onDestroy();
    }
}
