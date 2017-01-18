package com.spartahack.spartahack17.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.spartahack.spartahack17.Cache;
import com.spartahack.spartahack17.R;
import com.spartahack.spartahack17.Retrofit.SpartaHackAPIService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ryancasler on 1/14/17
 * Spartahack-Android
 */
public class CheckinActivity extends BaseActivity {
    public static final int UNPROCESSABLE_ENTITY = 422;

    @BindView(R.id.result) TextView result;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        progressBar.setVisibility(View.VISIBLE);
        result.setText(scanningResult.getContents());

        SpartaHackAPIService.INSTANCE.getRestAdapter()
                .checkInUser(Cache.INSTANCE.getSession().getAuth_token(), Integer.valueOf(scanningResult.getContents()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(checkInResponse -> {
                    if (checkInResponse.getChecked_in() != 0) {
                        result.setText(checkInResponse.getFullName());
                    } else {
                    showError();
                    }
                }, throwable -> {
                    if (((HttpException) throwable).code() == UNPROCESSABLE_ENTITY) {
                        try {
                            String json = ((HttpException) throwable).response().errorBody().string();
                            JSONObject obj = new JSONObject(json);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        showError();
                    }
                });
    }

    private void showError() {
        result.setText("Error Checking User In");
    }

    @OnClick(R.id.scan) public void scan(){
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES).initiateScan();
    }
}
