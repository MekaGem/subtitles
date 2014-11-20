package com.github.subtitles.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.github.subtitles.R;

public class MessagesAdapter extends ArrayAdapter<String> {
    protected LayoutInflater inflater;

    public MessagesAdapter(Context context) {
        super(context, R.layout.message, new String[]{});
        inflater = LayoutInflater.from(context);
    }

    private class MessageHolder {
        TextView textView;

        private MessageHolder(TextView textView) {
            this.textView = textView;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.message, null);

            TextView textView = (TextView) convertView.findViewById(R.id.message);

            holder = new MessageHolder(textView);
            convertView.setTag(holder);
        } else {
            holder = (MessageHolder) convertView.getTag();
        }

        String text = getItem(position);
        holder.textView.setText(text);
        return convertView;
    }
}
