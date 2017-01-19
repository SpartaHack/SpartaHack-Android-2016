package com.spartahack.spartahack17.Fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
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

    private CountDownTimer timer;

    /** Recycler view that displays all objects */
    @BindView(android.R.id.list) RecyclerView recyclerView;
    @BindView(R.id.text_clock) TextView clock;
    @BindView(R.id.text_label) TextView label;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.error_message) TextView errorMessage;

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getMVPPresenter().updateEvents();
    }

    @Override public void onResume() {
        super.onResume();
        startClock();
    }

    @Override public void onPause() {
        super.onPause();
        stopClock();
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

        SimpleSectionedRecyclerViewAdapter adapter = new SimpleSectionedRecyclerViewAdapter(getActivity(), R.layout.section_header, R.id.section_text, eventListAdapter);

        adapter.setSections(sections.toArray(dummy));

        recyclerView.setAdapter(adapter);

        errorMessage.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override public void showLoading() {
        errorMessage.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override public void onError(String error) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorMessage.setVisibility(View.VISIBLE);
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

    /**
     * Start the countdown clock for either counting down to the start of the event
     * or to the end of the event.
     */
    private void startClock() {
        DateTime now = new DateTime();
        DateTime start = new DateTime(2017, 1, 21, 0, 0);
        DateTime end = new DateTime(2017, 1, 22, 12, 0, 0);

        long timeToStart = start.getMillis() - now.getMillis();
        long timeToFinish = end.getMillis() - now.getMillis();

        if (timeToStart > 0){
            startCountDownToStart(timeToStart);
        } else {
            startCountDownToFinish(timeToFinish);
        }
    }

    /**
     * Start a timer counting down to the start of the event.
     * When the event starts it will transition to the countdown to the end of the event.
     * @param timeLeft mills left to count down.
     */
    private void startCountDownToStart(long timeLeft) {

        label.setText(R.string.time_til_hacking_starts);
        timer = new CountDownTimer(timeLeft, 1) {
            @Override public void onTick(long millisUntilFinished) {
                clock.setText(getDurationBreakdown(millisUntilFinished));
            }

            @Override public void onFinish() {
                stopClock();
                startClock();
            }
        }.start();

    }

    /**
     * Start the timer to count down to the end of the event.
     * When the event ends it will set the clock to say hacking has ended
     * @param timeLeft mills left to count down.
     */
    private void startCountDownToFinish(long timeLeft) {
        label.setText(R.string.time_til_hacking_over);

        timer = new CountDownTimer(timeLeft, 1) {
            @Override public void onTick(long millisUntilFinished) {
                clock.setText(getDurationBreakdown(millisUntilFinished));
            }

            @Override public void onFinish() {
                clock.setText(R.string.hacking_over);
                label.setText("");
            }
        }.start();
    }

    /**
     * Stop any running clocks and cancel the timer
     */
    private void stopClock() {
        timer.cancel();
    }
}
