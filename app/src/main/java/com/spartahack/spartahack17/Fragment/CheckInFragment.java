package com.spartahack.spartahack17.Fragment;

import android.content.Intent;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.spartahack.spartahack17.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ryancasler on 12/19/16
 * SpartaHack2016-Android
 */

public class CheckInFragment extends BaseFragment {

    @BindView(R.id.result) TextView result;

    @Override int getLayout() {return R.layout.fragment_check_in;}

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        result.setText(scanningResult.getContents());
    }

    @OnClick(R.id.scan) public void scan(){
        IntentIntegrator.forFragment(this).setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES).initiateScan();
    }

}
