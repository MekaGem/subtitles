package com.github.subtitles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.github.subtitles.view.ChatMessageModel;
import com.github.subtitles.view.DialogAdapter;
import com.github.subtitles.view.DialogModel;
import com.github.subtitles.view.MessagesAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainScreen extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}