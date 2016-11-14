package com.spartahack.spartahack17.Fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.spartahack.spartahack17.Adapters.EventListAdapter;
import com.spartahack.spartahack17.Adapters.SimpleSectionedRecyclerViewAdapter;
import com.spartahack.spartahack17.Model.Event;
import com.spartahack.spartahack17.Presenter.SchedulePresenter;
import com.spartahack.spartahack17.R;
import com.spartahack.spartahack17.View.ScheduleView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends MVPFragment<ScheduleView, SchedulePresenter> implements ScheduleView {

    private static final String TAG = "ScheduleFragment";

    CountDownTimer timer;

    /** Recycler view that displays all objects */
    @BindView(android.R.id.list) RecyclerView recyclerView;
    @BindView(R.id.text_clock) TextView clock;

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        DateTime start = new DateTime();
        DateTime end = new DateTime(2016, 10, 27, 17, 46, 0);

        long t = end.getMillis() - start.getMillis();

        if (t > 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            timer = new CountDownTimer(t, 1) {
                @Override public void onTick(long millisUntilFinished) {
                    clock.setText(getDurationBreakdown(millisUntilFinished));
                }

                @Override public void onFinish() {
                    clock.setText("Hacking over");
                }
            }.start();
        } else {
            clock.setText("Hacking over");
        }

        getMVPPresenter().updateEvents();
    }

    @NonNull @Override public SchedulePresenter createPresenter() {
        return new SchedulePresenter();
    }

    @Override int getLayout() {
        return R.layout.fragment_schedule;
    }

    @Override boolean registerEventbus() {
        return false;
    }

    @Override public void showEvents(ArrayList<Event> events) {
        EventListAdapter eventListAdapter = new EventListAdapter(getActivity(), events);

        ArrayList<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();

        DateTime date = events.get(0).getTime();
        SimpleDateFormat formatDate = new SimpleDateFormat("EEEE", Locale.US);

        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, formatDate.format(date.toDate())));
        // location the header should go at
        int sectionLoc = 0;
        for (Event e : events){
            if (DateTimeComparator.getDateOnlyInstance().compare(e.getTime(), date) == 1){
                sections.add(new SimpleSectionedRecyclerViewAdapter.Section(sectionLoc, formatDate.format(e.getTime().toDate())));
                date = e.getTime();
            }
            sectionLoc++;
        }

        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];

        SimpleSectionedRecyclerViewAdapter adapter = new SimpleSectionedRecyclerViewAdapter(getActivity(), R.layout.date_section, R.id.section_text, eventListAdapter);

        adapter.setSections(sections.toArray(dummy));

        recyclerView.setAdapter(adapter);
    }

    @Override public void showLoading() {
    }

    @Override public void onError(String error) {
        Log.e(TAG, error);
    }


    /**
     * Convert a millisecond duration to a string format
     *
     * @param millis A duration to convert to a string form
     * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
     */
    public static String getDurationBreakdown(long millis) {
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        String str = "%02d:%02d:%02d";

        return String.format(Locale.US, str, hours, minutes, seconds);
    }
}
