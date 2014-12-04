package com.github.subtitles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.github.subtitles.util.FileHelper;
import com.github.subtitles.view.DialogAdapter;
import com.github.subtitles.view.DialogModel;

import ru.yandex.speechkit.*;

import java.util.*;

public class MainScreen extends Activity {
    private boolean isListening = false;
    private DialogAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initSpeechKit();

        final ListView listView = (ListView) findViewById(R.id.dialogs);
        adapter = new DialogAdapter(this);
        listView.setAdapter(adapter);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreen.this, ChatScreen.class);
                startActivity(intent);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.clear();
        List<DialogModel> dialogModels = FileHelper.loadDialogs(this);
        adapter.addAll(dialogModels);
        adapter.notifyDataSetChanged();

        for (DialogModel model : dialogModels) {
            Log.i("DIALOG TITLE", model.getTitle());
            Log.i("DIALOG FILE", model.getFilename());
        }
    }
}