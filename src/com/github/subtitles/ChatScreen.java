package com.github.subtitles;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ChatScreen extends Activity {
    private List<String> messages = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        final ListView listView = (ListView) findViewById(R.id.messages);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.message, R.id.message);
//        final MessagesAdapter adapter = new MessagesAdapter(this);
        listView.setAdapter(adapter);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                messages.add("some message");
//                adapter.clear();
//                adapter.addAll(messages);
                adapter.add("some message");
                adapter.notifyDataSetChanged();
                Log.i("", String.valueOf(adapter.getCount()));
                Log.i("", adapter.getItem(adapter.getCount() - 1));
            }
        });

//        LinearLayout layout = (LinearLayout) findViewById(R.id.messages);

//        ChatMessage myMessage = new ChatMessage(this, true);
//        ChatMessage answer = new ChatMessage(this, false);

//        myMessage.setText("Привет, я - глухой чувак!");
//        answer.setText("Привет, глухой чувак!");

//        listView.addHeaderView(myMessage);
//        listView.addHeaderView(answer);
//        layout.addView(myMessage);
//        layout.addView(answer);

//        Button button = new Button(this);
//        button.setText("Начать запись");

//        listView.addFooterView(button);
    }
}
