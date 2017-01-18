package com.spartahack.spartahack17.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.spartahack.spartahack17.Cache;
import com.spartahack.spartahack17.Model.CheckIn;
import com.spartahack.spartahack17.R;
import com.spartahack.spartahack17.Retrofit.SpartaHackAPIService;

import org.json.JSONArray;
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
    private int currentScanId;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        currentScanId = Integer.valueOf(scanningResult.getContents());
        attemptCheckIn(0);
    }

    private void attemptCheckIn(int hasForms) {
        SpartaHackAPIService.INSTANCE.getRestAdapter()
                .checkInUser(Cache.INSTANCE.getSession().getAuth_token(), new CheckIn(currentScanId, hasForms))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(checkInResponse -> {
                    if (checkInResponse.getChecked_in() != 0) {
                        result.setText(checkInResponse.getFullName() + "is checked in!");
                    } else {
                        showError(null);
                    }
                }, throwable -> {
                    if (((HttpException) throwable).code() == UNPROCESSABLE_ENTITY) {

                        String error;

                        try {
                            String json = ((HttpException) throwable).response().errorBody().string();
                            JSONObject o = (JSONObject) new JSONObject(json).get("errors");
                            error = (String) ((JSONArray)o.get("user")).get(0);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            showError(null);
                            return;
                        }

                        if (error.toLowerCase().equals("is a minor")){
                            minorCheckIn();
                            result.setText(error);

                        } else {
                            showError(error);
                        }

                    } else {
                        showError(null);
                    }
                });
    }

    private void minorCheckIn() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.minor_check_in)
                .setMessage(R.string.minor_checkin_question)
                .setPositiveButton(R.string.check_in, (dialog, which) -> attemptCheckIn(1))
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    result.setText("");
                    currentScanId = -1;
                })
                .show();
    }

    private void showError(String error) {
        currentScanId = -1;
        result.setText(TextUtils.isEmpty(error) ? "Error Checking User In" : error);
    }

    @OnClick(R.id.scan) public void scan(){
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES).initiateScan();
    }

    @OnClick(R.id.navigate_before_button) public void onNavigateBeforeButtonClicked() {
        onBackPressed();
    }
}
