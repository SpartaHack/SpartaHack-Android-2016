package com.example.spartahack.spartahack2016.Fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spartahack.spartahack2016.Model.Ticket;
import com.example.spartahack.spartahack2016.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by ryancasler on 1/9/16.
 */
public class CreateTicketDialogFragment  extends BaseFragment {

    @Bind(R.id.categorySpinner) Spinner categorySpinner;
    @Bind(R.id.subCategorySpinner) Spinner subCategorySpinner;
    @Bind(R.id.submit) Button button;
    @Bind(R.id.subjectLayout) TextInputLayout inputLayoutSub;
    @Bind(R.id.descLayout) TextInputLayout inputLayoutDesc;
    @Bind(R.id.locationLayout) TextInputLayout inputLayoutLoc;
    @Bind(R.id.description) AppCompatEditText description;
    @Bind(R.id.subject) AppCompatEditText subject;
    @Bind(R.id.location) AppCompatEditText location;

    @Bind(R.id.subCategoryLayout) LinearLayout subCategoryLayout;

    List<ParseObject> categoryList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View view =  inflater.inflate(R.layout.create_ticket_dialog_fragment, container, false);

        ButterKnife.bind(this, view);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("HelpDeskTickets");

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
                    ArrayAdapter<String> spinnerCategoryArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, categoryArray);
                    spinnerCategoryArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
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
                if (i == 0){
                    subCategoryLayout.setVisibility(View.VISIBLE);
                }
                else {
                    subCategoryLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // subCategory Spinner
        ArrayAdapter<String> spinnerRoomArrayAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_item, getResources().getStringArray(R.array.roomArray));
        spinnerRoomArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        subCategorySpinner.setAdapter(spinnerRoomArrayAdapter);

        subCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //((TextView) adapterView.getChildAt(0)).setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.accent));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return view;
    }

    @OnClick(R.id.submit)
    public void submit(){
        if (!validateSubject() || !validateDesc() || !validateLocation()) {
            return;
        }

        ParseObject data = new ParseObject("HelpDeskTickets");
        data.put("description", description.getText().toString());
        data.put("subject", subject.getText().toString());


        Ticket ticket = new Ticket(subject.getText().toString(), categorySpinner.getSelectedItem().toString(), subject.getText().toString(), "Open", null);

        //Category Relation Object
        ParseObject categoryObject = null;
        for (ParseObject object : categoryList) {
            if (object.get("category") == categorySpinner.getSelectedItem().toString()) {
                categoryObject = object;
            }
        }

        ParseUser user = ParseUser.getCurrentUser();
//        if (user == null){
//            // handle error
//            dismiss();
//        }

        data.put("user", user);
        data.put("category", categoryObject);
        data.put("status", "Open");
        data.saveInBackground();
        Toast.makeText(getActivity().getBaseContext(), "Submitted Successfully", Toast.LENGTH_SHORT).show();
        subject.setText("");
        description.setText("");
        location.setText("");
        requestFocus(subject);

        EventBus.getDefault().post(ticket);
        //dismiss();
        getActivity().onBackPressed();

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

    private boolean validateLocation() {
        if (location.getText().toString().trim().isEmpty()) {
            inputLayoutLoc.setError(getString(R.string.err_msg_subject));
            requestFocus(location);
            return false;
        } else {
            inputLayoutLoc.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

//    /** The system calls this only when creating the layout in a dialog. */
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        // The only reason you might override this method when using onCreateView() is
//        // to modify any dialog characteristics. For example, the dialog includes a
//        // title by default, but your custom layout might not need it. So here you can
//        // remove the dialog title, but you must call the superclass to get the Dialog.
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        return dialog;
//    }
}