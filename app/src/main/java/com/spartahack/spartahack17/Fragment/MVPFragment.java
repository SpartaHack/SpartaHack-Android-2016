package com.spartahack.spartahack17.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spartahack.spartahack17.Presenter.BasePresenter;
import com.spartahack.spartahack17.View.BaseView;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by ryancasler on 9/23/16.
 * SpartaHack2016-Android
 */

public abstract class MVPFragment<V extends BaseView, P extends BasePresenter>
        extends Fragment implements BaseView {

    /**
     * Presenter for the activity.
     */
    private P presenter;

    /**
     * If the presenter does not exist yet, create then return
     * @return the presenter
     */
    protected P getMVPPresenter() {
        if (presenter == null)
            this.presenter = createPresenter();
        return presenter;
    }

    /**
     * This base fragment implements {@link BaseView} so that it is always the view
     * @return the view
     */
    protected V getMVPView() {
        return (V) this;
    }

    /**
     * @return the res layout id for the fragment
     */
    @SuppressWarnings("SameReturnValue")
    @LayoutRes  abstract int getLayout();

    /**
     * @return if the fragment should register for event bus
     */
    @SuppressWarnings("SameReturnValue")
    abstract boolean registerEventbus();

    /**
     * @return a new instance of the presenter
     */
    @NonNull public abstract P createPresenter();

    /**
     * Sets up the tabbar with the view pager
     * @param viewPager the view pager to set up tabs with
     */
    protected void setUpTabBar(ViewPager viewPager){
        EventBus.getDefault().post(viewPager);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(getLayout(), container, false);

        // bind to butterknife
        ButterKnife.bind(this, view);

        return view;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // attach the view to the presenter 
        // TODO: 9/23/16 figure out unchecked call
        getMVPPresenter().attachView(getMVPView());
    }

    @Override public void onResume() {
        // register for event bus
        if (registerEventbus()) EventBus.getDefault().register(this);
        super.onResume();
    }

    @Override public void onPause() {
        // unregister event bus
        if (registerEventbus()) EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        // have to unbind ButterKnife from fragments 
        ButterKnife.unbind(this);
    }
}