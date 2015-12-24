package com.example.spartahack.spartahack2016.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.spartahack.spartahack2016.Activity.MainActivity;
import com.example.spartahack.spartahack2016.Model.PushNotification;
import com.example.spartahack.spartahack2016.R;

import java.util.List;

/**
 * Created by ryancasler on 12/23/15.
 *
 * Adapter for notifications displayed in the notificaiton fragment.
 *
 */
public class NotificationAdapter extends BaseAdapter {

    /** Reference to the main activity for context to get images as well as get the layout inflater */
    private MainActivity context;

    /** The list of notifications that is being displayed */
    private List<PushNotification> notifications;

    public NotificationAdapter(MainActivity context, @SuppressWarnings("SameParameterValue") int resource, List<PushNotification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public Object getItem(int position) {
        return notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        NotificationViewHolder holder;

        if (convertView == null) {

            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.layout_notificaiton_item, parent, false);
            holder = new NotificationViewHolder();

            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.title = (TextView) convertView.findViewById(R.id.title);

            convertView.setTag(holder);

        } else {
            holder = (NotificationViewHolder) convertView.getTag();
        }

        final PushNotification pushNotification = notifications.get(position);

        holder.title.setText(pushNotification.getTitle());
        holder.message.setText(pushNotification.getMessage());

        return convertView;
    }

    /**
     * Class for the Viewholder pattern
     */
    static class NotificationViewHolder {
        TextView title;
        TextView message;
    }

}
