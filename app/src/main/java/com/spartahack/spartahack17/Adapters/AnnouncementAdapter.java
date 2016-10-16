package com.spartahack.spartahack17.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.spartahack.spartahack17.Activity.MainActivity;
import com.spartahack.spartahack17.Model.Announcement;
import com.spartahack.spartahack17.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ryancasler on 1/9/16
 * SpartaHack2016-Android
 */
public class AnnouncementAdapter extends BaseAdapter {

    /**
     * Reference to the main activity for context to get images as well as get the layout inflater
     */
    private final MainActivity context;

    /**
     * The list of notifications that is being displayed
     */
    private final List<Announcement> announcements;

    public AnnouncementAdapter(MainActivity context, List<Announcement> announcements) {
        this.context = context;
        this.announcements = announcements;
    }

    @Override public int getCount() {
        return announcements.size();
    }

    @Override public Object getItem(int position) {
        return announcements.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View view, ViewGroup parent) {

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
<<<<<<< dfec5de82eef9ee2f7705f7d27e53012ff3f0448
        @Bind(R.id.title) TextView title;
        @Bind(R.id.message) TextView message;
        @Bind(R.id.pinned_icon) ImageView pinned;

        AnnouncementViewHolder(View view) {
=======
        @BindView(R.id.title) TextView title;
        @BindView(R.id.message) TextView message;
        @BindView(R.id.pinned_icon) ImageView pinned;

        public AnnouncementViewHolder(View view) {
>>>>>>> start to upgrade butterknife
            ButterKnife.bind(this, view);
        }
    }

}


