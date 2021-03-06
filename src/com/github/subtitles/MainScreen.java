package com.github.subtitles;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.github.subtitles.managers.ListenerService;
import com.github.subtitles.util.FileHelper;
import com.github.subtitles.view.DialogAdapter;
import com.github.subtitles.view.DialogModel;

import com.parse.Parse;
import com.parse.ParseObject;
import ru.yandex.speechkit.*;

import java.io.*;
import java.util.*;

public class MainScreen extends Activity {
    private DialogAdapter adapter;
    private static boolean isListening = false;
    private static boolean wasStopped = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initSpeechKit();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "5Y31eTi2hF1Cde0rqu3quWE8Z16WV1UYhIHJiRXl", "M8PtHHVnaLA5gMN3if8Eyl6HQ2flb8dhTDAllOOB");

        loadFiles();

        final ListView listView = (ListView) findViewById(R.id.dialogs);
        adapter = new DialogAdapter(this);
        listView.setAdapter(adapter);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreen.this, ChatScreen.class);
                startActivity(intent);

                if (isListening)
                {
                    stopService(new Intent(MainScreen.this, ListenerService.class));
                    isListening = false;
                    wasStopped = true;
                }
            }
        });

        final DialogAdapter _adapter = adapter;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("clicked", _adapter.getItem(position).getFilename());
                Intent intent = new Intent(MainScreen.this, ChatScreen.class);
                intent.putExtra(Intent.EXTRA_TITLE, _adapter.getItem(position).getFilename());
                startActivity(intent);
            }
        });
    }

    private void initSpeechKit() {
        SpeechKit.getInstance().configure(getBaseContext(), "57a557e3-45f4-4533-acac-8b8907c53985", null);
        SpeechKit.getInstance().setParameter(SpeechKit.Parameters.soundFormat, SpeechKit.Parameters.SoundFormats.pcmIfWifi);
        Initializer initializer = Initializer.create(new InitializerListener()  {
            @Override
            public void onInitializerBegin(Initializer initializer) {

            }

            @Override
            public void onError(Initializer initializer, ru.yandex.speechkit.Error error) {

            }

            @Override
            public void onInitializerDone(Initializer initializer) {
                // SpeechKit ready
            }
        });
        initializer.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings_button) {
            if (isListening) {
                stopService(new Intent(MainScreen.this, ListenerService.class));
                isListening = false;
                item.setTitle(getString(R.string.not_listening));
            } else {
                startService(new Intent(MainScreen.this, ListenerService.class));
                isListening = true;
                item.setTitle(getString(R.string.listening));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (wasStopped && !isListening)
        {
            startService(new Intent(MainScreen.this, ListenerService.class));
            isListening = true;
            wasStopped = false;
        }

        adapter.clear();
        List<DialogModel> dialogModels = FileHelper.loadDialogs(this);
        adapter.addAll(dialogModels);
        adapter.notifyDataSetChanged();

        for (DialogModel model : dialogModels) {
            Log.i("DIALOG TITLE", model.getTitle());
            Log.i("DIALOG FILE", model.getFilename());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!wasStopped && isListening) {
            stopService(new Intent(MainScreen.this, ListenerService.class));
        }
    }

    private void loadFiles() {
        String dataPath = "/sdcard/data/" + getPackageName();
        String spotterModelsPath = dataPath + "/phrase-spotter";

        copyAssetFolder(getAssets(), "phrase-spotter", spotterModelsPath);
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

    public static  boolean isListening() {
        return isListening;
    }

    public static void setListening(boolean bool) {
        isListening = bool;
    }

    public static boolean isWasStopped() {
        return wasStopped;
    }

    public static  void setWasStopped(boolean bool) {
        wasStopped = bool;
    }
}