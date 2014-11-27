package com.github.subtitles.managers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.github.subtitles.ChatScreen;
import com.github.subtitles.MainScreen;
import com.github.subtitles.R;

/**
 * Created by Akmal on 20.11.2014.
 */
public class EmptyDialogScreen extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_empty_dialog);

        final Context context = this;

        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setTitle("Внимание!");  // заголовок
        ad.setMessage("К вам только что обратились. Обращение: " +
                NotificationController.getLastCommand());
        ad.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Intent intent = new Intent(context, MainScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        ad.setNegativeButton("Создать диалог", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                stopService(new Intent(context, ListenerService.class));
                Intent intent = new Intent(context, ChatScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        ad.setCancelable(false);

        ad.show();
    }
}
