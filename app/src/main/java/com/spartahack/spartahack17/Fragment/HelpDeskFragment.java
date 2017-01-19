package com.spartahack.spartahack17.Fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.spartahack.spartahack17.Activity.MainActivity;
import com.spartahack.spartahack17.Cache;
import com.spartahack.spartahack17.Model.Category;
import com.spartahack.spartahack17.R;
import com.spartahack.spartahack17.Retrofit.GSONMock;
import com.spartahack.spartahack17.Retrofit.SlackAPIService;
import com.spartahack.spartahack17.Retrofit.SpartaHackAPIService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.R.layout.simple_spinner_dropdown_item;
import static com.google.firebase.analytics.FirebaseAnalytics.Event.GENERATE_LEAD;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class HelpDeskFragment extends BaseFragment {

    @BindView(R.id.signed_in) LinearLayout signedIn;
    @BindView(R.id.signed_out) LinearLayout signedOut;
    @BindView(R.id.text_location) EditText locationText;
    @BindView(R.id.text_description) EditText descriptionText;
    @BindView(R.id.spinner_category) Spinner cateogrySpinner;

    // map display names to channel names
    HashMap<String, String> categoryHash;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAnalytics.getInstance(getActivity()).logEvent(GENERATE_LEAD, null);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryHash = new HashMap<>();

        SpartaHackAPIService.INSTANCE.getRestAdapter().getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(categories -> {
                    List<String> arraySpinner = new ArrayList<>();

                    for (Category category : categories){
                        categoryHash.put(category.getCategory(), category.getChannel());
                        arraySpinner.add(category.getCategory());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), simple_spinner_dropdown_item, arraySpinner);
                    cateogrySpinner.setAdapter(adapter);

                }, throwable -> {});
    }

    @Override public void onResume() {
        super.onResume();
        resetForm();
        setContent();
    }

    @Override int getLayout() { return R.layout.fragment_help_desk; }

    /**
     * Submit a help desk ticket
     */
    @OnClick(R.id.button_submit) void  onSubmit() {

        // verify the user set a location
        if (TextUtils.isEmpty(locationText.getText().toString())){
            locationText.setError("Must set a location!");
            return;
        }

        // verify the user set a description
        if (TextUtils.isEmpty(descriptionText.getText().toString())){
            descriptionText.setError("Must set a description!");
            return;
        }

        // create the ticket
        GSONMock.CreateMentorshipTicketRequest ticket = new GSONMock.CreateMentorshipTicketRequest(
                categoryHash.get(cateogrySpinner.getSelectedItem().toString()),
                "(" +  locationText.getText().toString() + ") " + Cache.INSTANCE.getSession().getFullName(),
                descriptionText.getText().toString());

        SlackAPIService.INSTANCE.getRestAdapter()
                .addTicket(ticket)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    String res = null;

                    try {
                        res = new String(response.bytes());
                    } catch (IOException e) {
                        Snackbar.make(signedIn, "Error Creating Ticket", Snackbar.LENGTH_SHORT).show();
                    }

                    if (res != null && res.equals("ok")) {
                        Snackbar.make(signedIn, "Ticket Created", Snackbar.LENGTH_SHORT).show();
                        resetForm();
                    } else {
                        Snackbar.make(signedIn, "Error Creating Ticket", Snackbar.LENGTH_SHORT).show();
                    }

                }, throwable -> Snackbar.make(signedIn, "Error Creating Ticket", Snackbar.LENGTH_SHORT).show());
    }

    /**
     * Redirect the user to the login screen
     */
    @OnClick(R.id.button_login) void onLogin() {
        MainActivity activity = ((MainActivity) getActivity());
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ProfileFragment.I_EXTRA_FROM, ProfileFragment.I_EXTRA_FROM);
        fragment.setArguments(bundle);
        activity.switchContent(R.id.container, fragment);
    }

    /**
     * Pick what content should be shown
     */
    private void setContent() {
        if (Cache.INSTANCE.hasSession()){
            signedOut.setVisibility(View.GONE);
            signedIn.setVisibility(View.VISIBLE);
        } else {
            signedIn.setVisibility(View.GONE);
            signedOut.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Reset all values in the form
     */
    private void resetForm() {
        locationText.setText(null);
        descriptionText.setText(null);
        cateogrySpinner.setSelection(0);
    }

}
