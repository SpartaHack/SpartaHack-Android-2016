package com.example.spartahack.spartahack2016.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.spartahack.spartahack2016.Model.Ticket;
import com.example.spartahack.spartahack2016.R;

import butterknife.Bind;
import butterknife.ButterKnife;


public class TicketFragment extends BaseFragment {

    @Bind(R.id.category)
    TextView category;
    @Bind(R.id.subject)
    TextView subject;
    @Bind(R.id.description)
    TextView description;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_ticket, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();

        category.setText(((Ticket) bundle.get("ticket")).getCategory());
        subject.setText( ((Ticket) bundle.get("ticket")).getSubject());
        description.setText( ((Ticket) bundle.get("ticket")).getDescription());

        return view;
    }
}
