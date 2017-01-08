package com.spartahack.spartahack17.Fragment;

import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.spartahack.spartahack17.Cache;
import com.spartahack.spartahack17.R;
import com.spartahack.spartahack17.Retrofit.GSONMock;
import com.spartahack.spartahack17.Retrofit.SlackAPIService;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.R.layout.simple_spinner_item;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class HelpDeskFragment extends BaseFragment {

    @BindView(R.id.signed_in) LinearLayout signedIn;
    @BindView(R.id.signed_out) LinearLayout signedOut;
    @BindView(R.id.text_location) EditText locationText;
    @BindView(R.id.text_description) EditText descriptionText;
    @BindView(R.id.spinner_category) Spinner cateogrySpinner;

    @Override public void onResume() {
        super.onResume();

        String[] arraySpinner = new String[] {"General", "Random"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), simple_spinner_item, arraySpinner);
        cateogrySpinner.setAdapter(adapter);

        locationText.setText(null);
        descriptionText.setText(null);
    }

    @Override int getLayout() { return R.layout.fragment_help_desk; }

    @OnClick(R.id.button_submit) void  onSubmit() {
        if (TextUtils.isEmpty(locationText.getText().toString())){
            locationText.setError("Must set a location!");
            return;
        }

        if (TextUtils.isEmpty(descriptionText.getText().toString())){
            descriptionText.setError("Must set a description!");
            return;
        }

        GSONMock.CreateMentorshipTicketRequest ticket = new GSONMock.CreateMentorshipTicketRequest(
                "#" + cateogrySpinner.getSelectedItem().toString(),
                String.valueOf(Cache.INSTANCE.getSession().getId()),
                "Location: " + locationText.getText().toString() + ", Description: " + descriptionText.getText().toString());

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
                    } else {
                        Snackbar.make(signedIn, "Error Creating Ticket", Snackbar.LENGTH_SHORT).show();
                    }

                }, throwable -> Snackbar.make(signedIn, "Error Creating Ticket", Snackbar.LENGTH_SHORT).show());
    }

//    @OnClick(R.id.login) void onLogin() {
//        MainActivity activity = ((MainActivity) getActivity());
//        ProfileFragment fragment = new ProfileFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString(ProfileFragment.I_EXTRA_FROM, ProfileFragment.I_EXTRA_FROM);
//        fragment.setArguments(bundle);
//        activity.switchContent(R.id.container, fragment);
//    }
}
