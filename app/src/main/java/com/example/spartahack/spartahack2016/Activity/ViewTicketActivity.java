package com.example.spartahack.spartahack2016.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spartahack.spartahack2016.Model.Ticket;
import com.example.spartahack.spartahack2016.R;
import com.example.spartahack.spartahack2016.Retrofit.GSONMock;
import com.example.spartahack.spartahack2016.Retrofit.ParseAPIService;
import com.example.spartahack.spartahack2016.Utility;

import org.w3c.dom.Text;

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

    public static Intent getIntent(Activity a, Ticket ticket) {
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
        if (bundle != null) {
            ticket = (Ticket) bundle.get(I_TICKET);
            if (ticket != null) {
                category.setText(ticket.getSubcategory());
                subject.setText(ticket.getSubject());
                description.setText(ticket.getDescription());
                status.setText(ticket.getStatus());
                extendReopenButton.setText(ticket.getStatus().equals("Open") ? R.string.extend_ticket : R.string.reopen_ticket);
                location.setText(ticket.getLocation());
            }
        } else {
            Snackbar.make(category, "Error Loading Ticket", Snackbar.LENGTH_SHORT).show();
            onBackPressed();
        }

    }

    @OnClick(R.id.delete)
    public void onFabClick() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Ticket")
                .setMessage("Are you sure you want to delete your ticket for" + ticket.getSubject() + "?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        refreshTicket(new GSONMock.UpdateTicketStatusRequest("Deleted", true), "Ticket Deleted");
                    }})
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @OnClick(R.id.extend_reopen)
    public void onExtendReopen() {
        refreshTicket(new GSONMock.UpdateTicketStatusRequest("Open", false), ticket.getStatus().equals("Open") ? "Ticket Extended": "Ticket Reopened");
    }

    public void refreshTicket(GSONMock.UpdateTicketStatusRequest request, final String confirmMessage) {
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
                        finish();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
