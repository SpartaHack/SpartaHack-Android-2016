package com.example.spartahack.spartahack2016.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spartahack.spartahack16.R;
import com.example.spartahack.spartahack2016.Model.Ticket;
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
public class ViewTicketActivity extends BaseActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.category) TextView category;
    @Bind(R.id.subject) TextView subject;
    @Bind(R.id.description) TextView description;
    @Bind(R.id.status) TextView status;
    @Bind(R.id.location) TextView location;
    @Bind(R.id.extend_reopen) Button extendReopenButton;

    private Ticket ticket;
    public static final String I_TICKET = "ticket";

    public static final String I_TICKET_ID = "ticket id";
    public static String NOT_ID = "notid";
    public static final String EXTEND = "extend";
    public static final String ACTION = "action";
    public static final String CLOSE = "close";


    public static Intent getIntent(Activity a, Ticket ticket, int pushID) {
        Intent intent = new Intent(a, ViewTicketActivity.class);
        intent.putExtra(I_TICKET, ticket);
        intent.putExtra(NOT_ID, pushID);

        return intent;
    }

    public static PendingIntent getPendingIntent(Context context, int pushID, String ticketID, String act){
        Intent intent = new Intent(context, ViewTicketActivity.class);
        intent.putExtra(I_TICKET_ID, ticketID);
        intent.putExtra(ACTION, act);
        intent.putExtra(NOT_ID, pushID);

        return PendingIntent.getActivity(context, act.equals(EXTEND) ? pushID : pushID + 1, intent, PendingIntent.FLAG_ONE_SHOT);
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
        if (bundle != null) {

            // cancel the notification
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            int notid = getIntent().getIntExtra(NOT_ID, -1);

            manager.cancel(notid);

            ticket = (Ticket) bundle.get(I_TICKET);
            if (ticket != null) {
                category.setText(ticket.getSubcategory());
                subject.setText(ticket.getSubject());
                description.setText(ticket.getDescription());
                status.setText(ticket.getStatus());
                extendReopenButton.setText(ticket.getStatus().equals("Open") ? R.string.extend_ticket : R.string.reopen_ticket);
                location.setText(ticket.getLocation());
            }
            else if (bundle.get(I_TICKET_ID) != null){

                // clear curren ticket in
                getTicket(bundle.getString(I_TICKET_ID));
                ticket = new Ticket();
                ticket.setId(bundle.getString(I_TICKET_ID));

                String stat = bundle.getString(ACTION);
                if (TextUtils.isEmpty(stat)){

                } else if (stat.equals(EXTEND)){
                    //set status to open and extend ticket
                    ticket.setStatus("Open");
                    refreshTicket(new GSONMock.UpdateTicketStatusRequest("Open", false), "Ticket Extended", false);
                }else {
                    // set status to closed and close ticket
                    ticket.setStatus("Closed");
                    refreshTicket(new GSONMock.UpdateTicketStatusRequest("Closed", true), "Ticket Closed", false);
                }
            }

        } else {
            Toast.makeText(ViewTicketActivity.this, "Error Loading Ticket", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

    }

    @OnClick(R.id.delete)
    public void onFabClick() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete your ticket " + ticket.getSubject() + "?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        refreshTicket(new GSONMock.UpdateTicketStatusRequest("Deleted", true), "Ticket Deleted", true);
                    }})
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @OnClick(R.id.extend_reopen)
    public void onExtendReopen() {
        refreshTicket(new GSONMock.UpdateTicketStatusRequest("Open", false), ticket.getStatus().equals("Open") ? "Ticket Extended": "Ticket Reopened", true);
    }

    public void refreshTicket(GSONMock.UpdateTicketStatusRequest request, final String confirmMessage, final boolean exit) {
        ParseAPIService.INSTANCE.getRestAdapter()
                .updateTicketStatus(ticket.getId(), request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GSONMock.UpdateObj>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GSONMock.UpdateObj updateObj) {
                        Toast.makeText(ViewTicketActivity.this, confirmMessage, Toast.LENGTH_SHORT).show();
                        if (exit) onBackPressed();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(MainActivity.toHelpDesk(this));
    }


    private void getTicket(final String tid) {
        ParseAPIService.INSTANCE.getRestAdapter().getTicket(tid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GSONMock.Ticket>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GSONMock.Ticket t) {
                        ticket = new Ticket(t.subject, t.description, ticket.getStatus(), t.objectId, t.subCategory, t.location);

                        category.setText(ticket.getSubcategory());
                        subject.setText(ticket.getSubject());
                        description.setText(ticket.getDescription());
                        status.setText(ticket.getStatus());
                        extendReopenButton.setText(ticket.getStatus().equals("Open") ? R.string.extend_ticket : R.string.reopen_ticket);
                        location.setText(ticket.getLocation());
                    }
                });
    }

}
