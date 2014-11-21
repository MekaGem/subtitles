package com.github.subtitles;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.github.subtitles.managers.TalkRecognition;
import com.github.subtitles.view.ChatMessageModel;
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
                ChatMessageModel model = new ChatMessageModel();
                model.setMessage(String.valueOf(textField.getText()));
                model.setUserMessage(true);
                textField.setText("");

                adapter.add(model);
                adapter.notifyDataSetChanged();
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

    @Override
    public void onResume() {
        if (isListening) {
            recognition.start();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        recognition.stop();
        super.onPause();
    }
}
