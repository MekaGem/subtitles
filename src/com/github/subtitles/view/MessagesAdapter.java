package com.github.subtitles.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.github.subtitles.R;

public class MessagesAdapter extends ArrayAdapter<ChatMessageModel> {
    protected LayoutInflater inflater;

    public MessagesAdapter(Context context) {
        super(context, R.layout.message);
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

        ChatMessageModel model = getItem(position);
        holder.textView.setText(model.getMessage());
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.textView.getLayoutParams();
        if (model.isUserMessage()) {
            holder.textView.setGravity(Gravity.RIGHT);
            holder.textView.setBackgroundResource(R.color.user_message);
            params.leftMargin = params.topMargin * 3;
            params.rightMargin = params.topMargin;
        } else {
            holder.textView.setGravity(Gravity.LEFT);
            holder.textView.setBackgroundResource(R.color.interlocutor_message);
            params.leftMargin = params.topMargin;
            params.rightMargin = params.topMargin * 3;
        }
        holder.textView.requestLayout();
        return convertView;
    }
}
