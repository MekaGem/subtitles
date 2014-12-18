package com.github.subtitles.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.github.subtitles.R;

public class FullScreenTextView extends Activity {
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    String[] messages;
    boolean[] userMessage;
    int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_text_view);
        TextView text = (TextView)findViewById(R.id.full_screen_text_view);
//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        );
        Intent intent = getIntent();
        messages = intent.getStringArrayExtra(Intent.EXTRA_TEXT);
        userMessage = intent.getBooleanArrayExtra("userMessage");
        position = intent.getIntExtra("position", 0);

        text.setText(messages[position]);
        if (userMessage[position]) {
            text.setBackgroundResource(R.color.user_message);
        } else {
            text.setBackgroundResource(R.color.interlocutor_message);
        }

        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("FULL SCREEN", "Text.onTouch");
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    position = Math.min(messages.length - 1, position + 1);
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    position = Math.max(0, position - 1);
                }
                TextView text = (TextView)findViewById(R.id.full_screen_text_view);
                text.setText(messages[position]);
                if (userMessage[position]) {
                    text.setBackgroundResource(R.color.user_message);
                } else {
                    text.setBackgroundResource(R.color.interlocutor_message);
                }
                return true;
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }
}