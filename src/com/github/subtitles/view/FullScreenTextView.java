package com.github.subtitles.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.github.subtitles.R;

public class FullScreenTextView extends Activity {
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
        text.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
    }
}