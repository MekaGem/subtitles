package com.github.subtitles;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.github.subtitles.view.ChatMessageModel;
import com.github.subtitles.view.MessagesAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatScreen extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        final ListView listView = (ListView) findViewById(R.id.messages);
        final MessagesAdapter adapter = new MessagesAdapter(this);
        listView.setAdapter(adapter);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatMessageModel model = new ChatMessageModel();
                model.setMessage("some message");
                model.setUserMessage(adapter.getCount() % 2 == 0);

                adapter.add(model);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
