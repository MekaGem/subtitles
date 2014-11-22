package com.github.subtitles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import com.github.subtitles.managers.ListenerService;
import com.github.subtitles.view.ChatMessageModel;
import com.github.subtitles.view.DialogAdapter;
import com.github.subtitles.view.DialogModel;
import com.github.subtitles.view.MessagesAdapter;
import ru.yandex.speechkit.SpeechKit;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class MainScreen extends Activity {
    private boolean isListening = false;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings_button) {
            if (isListening) {
                stopService(new Intent(MainScreen.this, ListenerService.class));
                isListening = false;
            } else {
                startService(new Intent(MainScreen.this, ListenerService.class));
                isListening = true;
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



//        Switch switcher = (Switch) findViewById(R.id.switcher);
//        switcher.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (isListening) {
//                    stopService(new Intent(MainScreen.this, ListenerService.class));
//                    isListening = false;
//                } else {
//                    System.out.println("SDS");
//                    startService(new Intent(MainScreen.this, ListenerService.class));
//                    isListening = true;
//                }
//            }
//        });
    }

    private void initSpeechKit() {
        SpeechKit.getInstance().configure(getBaseContext(), "57a557e3-45f4-4533-acac-8b8907c53985", null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}