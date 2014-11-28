package com.github.subtitles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.github.subtitles.managers.ListenerService;
import com.github.subtitles.view.DialogAdapter;
import com.github.subtitles.view.DialogModel;
import ru.yandex.speechkit.SpeechKit;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainScreen extends Activity {
    private boolean isListening = false;
    Menu menu;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings_button) {
            if (isListening) {
                stopService(new Intent(MainScreen.this, ListenerService.class));
                isListening = false;
                item.setTitle("Голосовая активация отключена");
            } else {
                startService(new Intent(MainScreen.this, ListenerService.class));
                isListening = true;
                item.setTitle("Голосовая активация включена");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initSpeechKit();

        final ListView listView = (ListView) findViewById(R.id.dialogs);
        final DialogAdapter adapter = new DialogAdapter(this);
        listView.setAdapter(adapter);

        DialogModel dialog = new DialogModel();
        dialog.setTitle("Вася");
        String currentDateTimeString = new SimpleDateFormat("MM-dd HH:mm").format(new Date());
        dialog.setDate(currentDateTimeString);
        dialog.setLastMessage("Привет");
        for (int index = 0; index < 15; ++index) {
            adapter.add(dialog);
        }
        adapter.notifyDataSetChanged();

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreen.this, ChatScreen.class);
                startActivity(intent);
            }
        });

    }

    private void initSpeechKit() {
        SpeechKit.getInstance().configure(getBaseContext(), "57a557e3-45f4-4533-acac-8b8907c53985", null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}