package com.github.subtitles.view;

import android.app.ActionBar;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ChatMessage extends TextView {
    public ChatMessage(Context context, boolean userMessage) {
        super(context);

        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        if (userMessage) {
            setGravity(Gravity.RIGHT);
        } else {
            setGravity(Gravity.LEFT);
        }
//        setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT));
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        setLayoutParams(params);
    }
}
