package com.spartahack.spartahack17.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.spartahack.spartahack17.R;
import com.spartahack.spartahack17.Activity.MainActivity;
import com.spartahack.spartahack17.Model.Announcement;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ryancasler on 1/9/16.
 */
public class AnnouncementAdapter extends BaseAdapter {

    /**
     * Reference to the main activity for context to get images as well as get the layout inflater
     */
    private MainActivity context;

    /**
     * The list of notifications that is being displayed
     */
    private List<Announcement> announcements;

    public AnnouncementAdapter(MainActivity context, List<Announcement> announcements) {
        this.context = context;
        this.announcements = announcements;
    }

    @Override
    public int getCount() {
        return announcements.size();
    }

    @Override
    public Object getItem(int position) {
        return announcements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        AnnouncementViewHolder holder;

        if (view == null) {

            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.layout_notificaiton_item, parent, false);
            holder = new AnnouncementViewHolder(view);
            view.setTag(holder);

        } else {
            holder = (AnnouncementViewHolder) view.getTag();
        }

        final Announcement announcement = announcements.get(position);

        holder.title.setText(announcement.getTitle());
        holder.message.setText(announcement.getMessage());
        if (announcement.getPinned())
            holder.pinned.setVisibility(View.VISIBLE);
        else
            holder.pinned.setVisibility(View.GONE);

        return view;
    }

    /**
     * Class for the Viewholder pattern
     */
    static class AnnouncementViewHolder {
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.message)
        TextView message;
        @Bind(R.id.pinned_icon)
        ImageView pinned;

        public AnnouncementViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}


