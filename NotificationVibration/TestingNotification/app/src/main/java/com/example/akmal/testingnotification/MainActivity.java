package com.example.akmal.testingnotification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import ru.yandex.speechkit.SpeechKit;


public class MainActivity extends Activity {
    VibratorExecutor vibrator;
    boolean isListeting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vibrator = new VibratorExecutor(this);
        SpeechKit.getInstance().configure(getBaseContext(),
                "57a557e3-45f4-4533-acac-8b8907c53985");

        final Context context = this;
        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isListeting)
                {
                    stopService(new Intent(context, ListenerService.class));
                    isListeting = false;
                }
                else
                {
                    startService(new Intent(context, ListenerService.class));
                    isListeting = true;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        stopService(new Intent(this, ListenerService.class));
    }

}
