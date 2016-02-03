package com.example.spartahack.spartahack2016.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.spartahack.spartahack2016.Model.Ticket;
import com.example.spartahack.spartahack2016.R;
import com.example.spartahack.spartahack2016.Retrofit.GSONMock;
import com.example.spartahack.spartahack2016.Retrofit.ParseAPIService;
import com.example.spartahack.spartahack2016.Utility;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by ryancasler on 2/2/16.
 */
public class ViewTicketActivity extends BaseActivity{

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.category) TextView category;
    @Bind(R.id.subject) TextView subject;
    @Bind(R.id.description) TextView description;
    @Bind(R.id.status) TextView status;

    private Ticket ticket;
    public static final String I_TICKET = "ticket";

    public static Intent getIntent(Activity a, Ticket ticket){
        Intent intent = new Intent(a, ViewTicketActivity.class);
        intent.putExtra(I_TICKET, ticket);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ticket);

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
        if (bundle!=null) {
            ticket = (Ticket) bundle.get(I_TICKET);
            if (ticket != null) {
                category.setText(ticket.getCategory());
                subject.setText(ticket.getSubject());
                description.setText(ticket.getDescription());
                status.setText(ticket.getStatus());
            }
        }else {
            Snackbar.make(category, "Error Loading Ticket", Snackbar.LENGTH_SHORT).show();
            onBackPressed();
        }

    }

    @OnClick(R.id.fab)
    public void onFabClick(){
        refreshTicket(ticket.getId(), "Deleted", true);
    }

    public void refreshTicket(String objectID, String status, boolean not){
        ParseAPIService.INSTANCE.getRestAdapter()
                .updateTicketStatus(objectID, new GSONMock.UpdateTicketStatusRequest(status, not))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GSONMock.UpdateObj>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GSONMock.UpdateObj updateObj) {
                        onBackPressed();
                        Snackbar.make(category, "Ticket Deleted", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }


}
