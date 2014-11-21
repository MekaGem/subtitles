package com.github.subtitles.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.github.subtitles.R;

public class DialogAdapter extends ArrayAdapter<DialogModel> {
    private LayoutInflater inflater;

    public DialogAdapter(Context context) {
        super(context, R.layout.dialog);

        inflater = LayoutInflater.from(context);
    }

    private class DialogHolder {
        TextView title;
        TextView date;
        TextView lastMessage;

        private DialogHolder(TextView title, TextView date, TextView lastMessage) {
            this.title = title;
            this.date = date;
            this.lastMessage = lastMessage;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DialogHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.dialog, null);

            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView date = (TextView) convertView.findViewById(R.id.date);
            TextView lastMessage  = (TextView) convertView.findViewById(R.id.last_message);

            holder = new DialogHolder(title, date, lastMessage);
            convertView.setTag(holder);
        } else {
            holder = (DialogHolder) convertView.getTag();
        }

        DialogModel model = getItem(position);
        holder.title.setText(model.getTitle());
        holder.date.setText(model.getDate());
        holder.lastMessage.setText(model.getLastMessage());
        return convertView;
    }
}
