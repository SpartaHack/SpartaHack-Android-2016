package com.example.spartahack.spartahack2016.Fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.spartahack.spartahack2016.Model.Ticket;
import com.example.spartahack.spartahack2016.R;
import com.example.spartahack.spartahack2016.Retrofit.GSONMock;
import com.example.spartahack.spartahack2016.Retrofit.ParseAPIService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;


public class TicketFragment extends BaseFragment {

    @Bind(R.id.category) TextView category;
    @Bind(R.id.subject) TextView subject;
    @Bind(R.id.description) TextView description;
    @Bind(R.id.status) TextView status;

    private Ticket ticket;
    public static final String I_TICKET = "ticket";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_ticket, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
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
            getActivity().onBackPressed();
        }

        return view;
    }

    @OnClick(R.id.fab)
    public void onFabClick(){
        ParseAPIService.INSTANCE.getRestAdapter()
                .deleteObject(ticket.getId(), new GSONMock.DeleteObjRequest())
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
                        getActivity().onBackPressed();
                        Snackbar.make(category, "Ticket Deleted", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }


}
