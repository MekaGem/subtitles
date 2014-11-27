package com.github.subtitles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.github.subtitles.managers.TalkRecognition;
import com.github.subtitles.view.ChatMessageModel;
import com.github.subtitles.view.FullScreenTextView;
import com.github.subtitles.view.MessagesAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatScreen extends Activity {
    private MessagesAdapter adapter;
    private boolean isListening = false;
    private TalkRecognition recognition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        recognition = new TalkRecognition(this);

        final ListView listView = (ListView) findViewById(R.id.messages);
        adapter = new MessagesAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.message);
                Intent intent = new Intent(ChatScreen.this, FullScreenTextView.class);
                intent.putExtra(Intent.EXTRA_TEXT, textView.getText());
                startActivity(intent);
            }
        });

        final Button startListening = (Button) findViewById(R.id.button);
        startListening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isListening) {
                    startListening.setText(getString(R.string.start_recording));
                    isListening = false;
                    recognition.stop();
                } else {
                    startListening.setText(getString(R.string.stop_recording));
                    isListening = true;
                    recognition.start();
                }
            }
        });

        final EditText textField = (EditText) findViewById(R.id.typed_message);
        final Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = String.valueOf(textField.getText());
                if (!message.isEmpty()) {
                    ChatMessageModel model = new ChatMessageModel();
                    model.setMessage(message);
                    model.setUserMessage(true);
                    textField.setText("");

                    adapter.add(model);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    public void addMessage(String message) {
        ChatMessageModel model = new ChatMessageModel();
        model.setMessage(message);
        model.setUserMessage(false);

        adapter.add(model);
        adapter.notifyDataSetChanged();
    }

    public void rewriteLastMessage(String message) {
        ChatMessageModel lastMessage = adapter.getItem(adapter.getCount() - 1);
        lastMessage.setMessage(message);
        lastMessage.setUserMessage(false);

        adapter.notifyDataSetChanged();
    }

    public void appendToLastMessage(String message) {
        ChatMessageModel lastMessage = adapter.getItem(adapter.getCount() - 1);
        lastMessage.setMessage(lastMessage.getMessage() + message);
        lastMessage.setUserMessage(false);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        if (isListening) {
            recognition.start();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (isListening) {
            recognition.stop();
        }
        super.onPause();
    }
}
