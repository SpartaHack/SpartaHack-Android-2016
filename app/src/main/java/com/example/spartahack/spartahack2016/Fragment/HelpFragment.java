package com.example.spartahack.spartahack2016.Fragment;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spartahack.spartahack2016.Adapters.TicketAdapter;
import com.example.spartahack.spartahack2016.Model.Ticket;
import com.example.spartahack.spartahack2016.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HelpFragment extends BaseFragment {


    @Bind(R.id.categorySpinner)
    Spinner categorySpinner;
    @Bind(R.id.roomSpinner)
    Spinner roomSpinner;
    @Bind(R.id.submit)
    Button button;
    @Bind(R.id.description)
    AppCompatEditText description;
    @Bind(R.id.descLayout)
    TextInputLayout inputLayoutDesc;
    @Bind(R.id.subject)
    AppCompatEditText subject;
    @Bind(R.id.subjectLayout)
    TextInputLayout inputLayoutSub;
    @Bind(R.id.recycler)
    RecyclerView ticketView;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ParseUser user;
    List<ParseObject> categoryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        user = ParseUser.getCurrentUser();
        if (user == null) {
            return inflater.inflate(R.layout.layout_notloggedin, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_help, container, false);

            ButterKnife.bind(this, view);

            //RecyclerView
            ticketView.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(getActivity());
            ticketView.setLayoutManager(mLayoutManager);

            final ArrayList<Ticket> ticketList = new ArrayList<Ticket>();
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("HelpDeskTickets");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        String user_id = ParseUser.getCurrentUser().getObjectId();
                        for (ParseObject object : objects) {
                            String obj_id = ((ParseObject) object.get("user")).getObjectId().toString();
                            if (user_id.equals(obj_id)) {
                                // CHANGE WHEN DB IS FIXED
                                if (object.get("subject") != null) {
                                    ticketList.add(new Ticket(object.get("subject").toString(), ((ParseObject) object.get("category")).get("category").toString(), object.get("description").toString()));
                                }
                                else {
                                    ticketList.add(new Ticket("No Subject", ((ParseObject) object.get("category")).get("category").toString(), object.get("description").toString()));
                                }
                            }
                        }
                        mAdapter = new TicketAdapter(ticketList);
                        ticketView.setAdapter(mAdapter);
                    } else {
                        // handle Parse Exception here
                    }
                }
            });

            // Category Spinner
            final ArrayList<String> categoryArray = new ArrayList<String>();
            query = new ParseQuery<ParseObject>("HelpDesk");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> markers, ParseException e) {
                    if (e == null) {
                        categoryList = markers;
                        for (ParseObject object : markers) {
                            categoryArray.add(object.get("category").toString());
                        }
                        ArrayAdapter<String> spinnerCategoryArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categoryArray);
                        categorySpinner.setAdapter(spinnerCategoryArrayAdapter);

                    } else {
                        // handle Parse Exception here
                    }
                }
            });

            categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //((TextView) adapterView.getChildAt(0)).setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.accent));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            // Room Spinner
            ArrayAdapter<String> spinnerRoomArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.roomArray));
            roomSpinner.setAdapter(spinnerRoomArrayAdapter);

            roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //((TextView) adapterView.getChildAt(0)).setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.accent));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    submitForm();
                }
            });
        }

        return view;
    }

    private void submitForm() {
        if (!validateDesc() || !validateSubject()) {
            return;
        }

        ParseObject data = new ParseObject("HelpDeskTickets");
        data.put("description", description.getText().toString());
        data.put("subject", subject.getText().toString());

        //Category Relation Object
        ParseObject categoryObject = null;
        for (ParseObject object : categoryList) {
            if (object.get("category") == categorySpinner.getSelectedItem().toString()) {
                categoryObject = object;
            }
        }

        data.put("user", user);
        data.put("category", categoryObject);
        data.saveInBackground();
        Toast.makeText(getActivity().getBaseContext(), "Submitted Successfully", Toast.LENGTH_SHORT).show();
        subject.setText("");
        description.setText("");
        requestFocus(subject);
    }


    private boolean validateDesc() {
        if (description.getText().toString().trim().isEmpty()) {
            inputLayoutDesc.setError(getString(R.string.err_msg_desc));
            requestFocus(description);
            return false;
        } else {
            inputLayoutDesc.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateSubject() {
        if (subject.getText().toString().trim().isEmpty()) {
            inputLayoutSub.setError(getString(R.string.err_msg_subject));
            requestFocus(subject);
            return false;
        } else {
            inputLayoutSub.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
