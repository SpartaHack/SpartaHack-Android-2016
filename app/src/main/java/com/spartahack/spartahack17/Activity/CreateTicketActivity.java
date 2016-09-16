package com.spartahack.spartahack17.Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.spartahack.spartahack17.Model.Ticket;
import com.spartahack.spartahack17.R;
import com.spartahack.spartahack17.Utility;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by ryancasler on 2/2/16.
 */
public class CreateTicketActivity extends BaseActivity {

//    @Bind(R.id.categorySpinner) Spinner categorySpinner;
    @Bind(R.id.subCategorySpinner) Spinner subCategorySpinner;
    @Bind(R.id.subjectLayout) TextInputLayout inputLayoutSub;
    @Bind(R.id.descLayout) TextInputLayout inputLayoutDesc;
    @Bind(R.id.locationLayout) TextInputLayout inputLayoutLoc;
    @Bind(R.id.description) AppCompatEditText description;
    @Bind(R.id.subject) AppCompatEditText subject;
    @Bind(R.id.location) AppCompatEditText location;
    @Bind(R.id.rootLayout) View view;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.subCategoryLayout) LinearLayout subCategoryLayout;

    List<ParseObject> categoryList;
    List<String> subCategoryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ticket);

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



        ParseQuery<ParseObject> query = new ParseQuery<>("HelpDeskTickets");
        final List<Map<String, String>> subItem  = new ArrayList<>();
        final ArrayList<String> categoryArray = new ArrayList<>();
        query = new ParseQuery<>("HelpDesk");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> markers, ParseException e) {
                if (e == null) {
                    categoryList = markers;
                    for (ParseObject object : markers) {
                        Map<String, String> category = new HashMap<>(2);
                        category.put("text", object.get("category").toString());
                        category.put("subText", object.get("Description").toString());
                        subItem.add(category);

                        categoryArray.add(object.get("category").toString());
                        if (object.get("category").toString().equals("Mentorship")) {
                            subCategoryList = object.getList("subCategory");
                        }
                    }

                    // subCategory Spinner
                    ArrayAdapter<String> spinnerRoomArrayAdapter = new ArrayAdapter<>(CreateTicketActivity.this, R.layout.spinner_item, subCategoryList);
                    //spinnerRoomArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    subCategorySpinner.setAdapter(spinnerRoomArrayAdapter);

                }
            }
        });

    }


    @OnClick(R.id.submit)
    public void submit() {
        if (!validateSubject() || !validateDesc() || !validateLocation()) {
            return;
        }

        ParseObject data = new ParseObject("HelpDeskTickets");

        Ticket ticket = new Ticket();//Ticket(subject.getText().toString(), categorySpinner.getSelectedItem().toString(), subject.getText().toString(), "Open", null);

        data.put("description", description.getText().toString());
        ticket.setDescription(description.getText().toString());

        data.put("subject", subject.getText().toString());
        ticket.setSubject(subject.getText().toString());

        data.put("location", location.getText().toString());

        //Category Relation Object
        ParseObject categoryObject = categoryList.get(0);

        ParseUser user = ParseUser.getCurrentUser();

        data.put("user", user);
        data.put("category", categoryObject);
        data.put("subCategory", subCategorySpinner.getSelectedItem().toString());
        ticket.setSubcategory(subCategorySpinner.getSelectedItem().toString());
        ticket.setSubcategory("Mentorship");

        data.put("status", "Open");
        ticket.setStatus("Open");

        data.put("notifiedFlag", false);
        data.saveInBackground();
        
        Toast.makeText(getBaseContext(), "Submitted Successfully", Toast.LENGTH_SHORT).show();
        subject.setText("");
        description.setText("");
        location.setText("");
        requestFocus(subject);

        EventBus.getDefault().post(ticket);

        onBackPressed();

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
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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

}