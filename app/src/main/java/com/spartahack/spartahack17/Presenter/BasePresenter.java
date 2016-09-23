package com.spartahack.spartahack17.Presenter;

import com.spartahack.spartahack17.View.BaseView;

/**
 * Created by ryancasler on 9/23/16.
 * SpartaHack2016-Android
 */

public abstract class BasePresenter<V extends BaseView> {

    /**
     * View that is attached to the presenter
     */
    protected V view;

    /**
     * @return the view that is attached
     */
    protected V getView() {return view;}

    /**
     * @return if there is a view attached or not
     */
    protected boolean isViewAttached() {return view != null;}

    /**
     * @param view to attach to this presenter
     */
    public void attachView(V view) {this.view = view;}
}
