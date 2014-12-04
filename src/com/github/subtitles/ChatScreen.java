package com.github.subtitles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.github.subtitles.managers.TalkRecognition;
import com.github.subtitles.util.FileHelper;
import com.github.subtitles.view.ChatMessageModel;
import com.github.subtitles.view.DialogModel;
import com.github.subtitles.view.FullScreenTextView;
import com.github.subtitles.view.MessagesAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ChatScreen extends Activity {
    private MessagesAdapter adapter;
    private boolean isListening = false;
    private TalkRecognition recognition;

    private String title;
    private String date;
    private String lastMessage;

    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        if (!getIntent().hasExtra(Intent.EXTRA_TITLE)) {
            String filename = String.valueOf(new Random().nextLong());
            title = filename.substring(0, 6);
            date = new SimpleDateFormat("MM-dd HH:mm").format(new Date());
            lastMessage = "";
            getIntent().putExtra(Intent.EXTRA_TITLE, filename);

            DialogModel model = new DialogModel();
            model.setTitle(title);
            model.setLastMessage(lastMessage);
            model.setDate(date);
            model.setFilename(filename);
            FileHelper.saveDialog(this, filename, model, new ArrayList<ChatMessageModel>());
        }

        recognition = new TalkRecognition(this);

        listView = (ListView) findViewById(R.id.messages);
        adapter = new MessagesAdapter(this);
        listView.setAdapter(adapter);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

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

    @Override
    public void onResume() {
        String filename = getIntent().getStringExtra(Intent.EXTRA_TITLE);
        Log.i("LOADING FILE", filename);

        DialogModel dialogModel = FileHelper.loadDialog(this, filename);
        title = dialogModel.getTitle();
        date = dialogModel.getDate();
        lastMessage = dialogModel.getLastMessage();

        adapter.clear();
        adapter.addAll(FileHelper.loadChat(this, filename));
        adapter.notifyDataSetChanged();

        if (adapter.getCount() > 0) {
            listView.setSelection(adapter.getCount() - 1);
        }

        if (isListening) {
            recognition.start();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        String filename = getIntent().getStringExtra(Intent.EXTRA_TITLE);
        Log.i("SAVING FILE", filename);

        DialogModel dialogModel = new DialogModel();
        dialogModel.setTitle(title);
        dialogModel.setDate(date);
        dialogModel.setLastMessage(lastMessage);
        dialogModel.setFilename(filename);

        List<ChatMessageModel> messages = new ArrayList<ChatMessageModel>();
        for (int index = 0; index < adapter.getCount(); ++index) {
            messages.add(adapter.getItem(index));
        }

        FileHelper.saveDialog(this, filename, dialogModel, messages);

        if (isListening) {
            recognition.stop();
        }
        super.onPause();
    }
}
