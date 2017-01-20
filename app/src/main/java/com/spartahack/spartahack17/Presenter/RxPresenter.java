package com.spartahack.spartahack17.Presenter;

import com.spartahack.spartahack17.View.BaseView;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Presenter designed for rx situations and easier testing
 *
 * @param <V> The view that the the presenter interacts with
 * @param <M> The model used for the on next call
 */
public abstract class RxPresenter<V extends BaseView, M> extends BasePresenter<V>{

    protected Subscriber<M> subscriber;

    /**
     * Unsubscribe the subscriber from the observable
     */
    public void unsubscribe() {
        if (subscriber != null && !subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
        }

        subscriber = null;
    }

    /**
     * Subscribe the observable to this subscriber
     * @param observable to be subscribed to
     */
    public void subscribe(Observable<M> observable) {

        unsubscribe();

        subscriber = new Subscriber<M>() {

            @Override public void onCompleted() {}

            @Override public void onError(Throwable e) {
                RxPresenter.this.onError(e);
            }

            @Override public void onNext(M m) {
                RxPresenter.this.onNext(m);
            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * Error abstract function called by the subscriber to handle onError
     * @param e error to handle
     */
    abstract void onError(Throwable e);

    /**
     * onNext abstract function called by the subscriber to handle onNext
     * @param data the data emitted by the observable
     */
    abstract void onNext(M data);
}
