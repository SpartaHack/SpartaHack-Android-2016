package com.spartahack.spartahack17.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.spartahack.spartahack17.Activity.MainActivity;
import com.spartahack.spartahack17.Adapters.HelpDeskPagerAdapter;
import com.spartahack.spartahack17.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpDeskFragment extends BaseFragment {

    @Bind(R.id.view_pager) ViewPager viewPager;
    @Bind(R.id.no_user) LinearLayout noUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help_desk, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
    }

    private void setView(){
        if (ParseUser.getCurrentUser() == null){
            // need to log in to use the app
            noUser.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
        } else {
            noUser.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);

            // see if this person is a mentor or not
            ParseQuery<ParseObject> query = new ParseQuery<>("Mentors");
            query.whereEqualTo("mentor", ParseUser.getCurrentUser());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    // check if category array has objects or not
                    ArrayList<String> cat = null;
                    if (objects != null && objects.size()>0 && objects.get(0).get("categories") != null){
                        try {
                            cat = (ArrayList<String>) objects.get(0).get("categories");
                        } catch (ClassCastException e1){
                            Log.d("Cast", e1.toString());
                        }
                    }

                    if ( cat == null  || cat.size() == 0){
                        // not subscribed to any categories so only show their tickets
                        ((MainActivity)getActivity()).addFragment(new MyTicketsFragment());
                    }else {
                        // subscribed to categories so show mentor view as well
                        viewPager.setAdapter(new HelpDeskPagerAdapter(getChildFragmentManager()));
                        setUpTabBar(viewPager);
                    }
                }
            });

        }

    }

    @OnClick(R.id.login)
    void onLogin() {
        MainActivity activity = ((MainActivity) getActivity());
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ProfileFragment.I_EXTRA_FROM, ProfileFragment.I_EXTRA_FROM);
        fragment.setArguments(bundle);
        activity.switchContent(R.id.container, fragment);
    }
}
