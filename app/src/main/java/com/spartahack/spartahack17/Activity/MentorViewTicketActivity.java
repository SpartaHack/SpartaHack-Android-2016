package com.spartahack.spartahack17.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.spartahack.spartahack17.Model.Ticket;
import com.spartahack.spartahack17.R;
import com.spartahack.spartahack17.Retrofit.GSONMock;
import com.spartahack.spartahack17.Retrofit.ParseAPIService;
import com.spartahack.spartahack17.Utility;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class MentorViewTicketActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.category) TextView category;
    @BindView(R.id.subject) TextView subject;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.status) TextView status;
    @BindView(R.id.location) TextView location;
    @BindView(R.id.accept) Button acceptButton;
    @BindView(R.id.name) TextView name;

    private Ticket ticket;
    public static final String I_TICKET = "ticket";
    public static final String TAG = "MentorTicketViewA";

    public static Intent getIntent(Activity a, Ticket ticket) {
        Intent intent = new Intent(a, MentorViewTicketActivity.class);
        intent.putExtra(I_TICKET, ticket);

        return intent;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_view_ticket);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_accent);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // add padding for transparent statusbar if > kitkat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (toolbar != null) toolbar.setPadding(0, Utility.getStatusBarHeight(this), 0, 0);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            toolbar.setTitleTextColor(getResources().getColor(R.color.accent, null));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ticket = (Ticket) bundle.get(I_TICKET);
            if (ticket != null) {
                category.setText(ticket.getSubcategory());
                subject.setText(ticket.getSubject());
                description.setText(ticket.getDescription());
                status.setText(ticket.getStatus());
                name.setText(ticket.getName());

                if (status.getText().equals("Accepted")){
                    acceptButton.setVisibility(View.GONE);
                }
                location.setText(ticket.getLocation());
            }
        } else {
            Snackbar.make(category, "Error Loading Ticket", Snackbar.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    @OnClick(R.id.accept) public void onAccept() {
        refreshTicket(new GSONMock.UpdateTicketStatusRequest("Accepted", false), "Ticket Accepted");
    }

    public void refreshTicket(GSONMock.UpdateTicketStatusRequest request, final String confirmMessage) {
        ParseAPIService.INSTANCE.getRestAdapter()
                .updateTicketStatus(ticket.getId(), request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GSONMock.UpdateObj>() {
                    @Override public void onCompleted() { }

                    @Override public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.toString());
                    }

                    @Override public void onNext(GSONMock.UpdateObj updateObj) {
                        Realm realm = Realm.getInstance(MentorViewTicketActivity.this);
                        realm.executeTransaction(realm1 -> {
                            ticket.setStatus("Accepted");
                            ticket.setMine(true);
                            realm1.copyToRealmOrUpdate(ticket);
                        });

                        Toast.makeText(MentorViewTicketActivity.this, confirmMessage, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(MainActivity.toHelpDesk(this));
    }

}
