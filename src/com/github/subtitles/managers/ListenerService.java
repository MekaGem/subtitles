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

        copyAssetFolder(getAssets(), "phrase-spotter", spotterModelsPath);

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

    private static boolean copyAssetFolder(AssetManager assetManager,
                                           String fromAssetPath, String toPath) {
        Log.d("ASSETS", "copyAssetFolder: " + fromAssetPath + " -> " + toPath);
        try {
            String[] files = assetManager.list(fromAssetPath);
            new File(toPath).mkdirs();
            boolean res = true;
            for (String file : files) {
                if (file.contains("."))
                    res &= copyAsset(assetManager,
                            fromAssetPath + "/" + file,
                            toPath + "/" + file);
                else
                    res &= copyAssetFolder(assetManager,
                            fromAssetPath + "/" + file,
                            toPath + "/" + file);
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean copyAsset(AssetManager assetManager,
                                     String fromAssetPath, String toPath) {
        Log.d("ASSETS", "copyAsset: " + fromAssetPath + " -> " + toPath);
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fromAssetPath);
            new File(toPath).createNewFile();
            out = new FileOutputStream(toPath);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}
