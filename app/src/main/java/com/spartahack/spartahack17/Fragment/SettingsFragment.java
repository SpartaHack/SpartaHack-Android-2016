package com.spartahack.spartahack17.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.spartahack.spartahack17.Constants;
import com.spartahack.spartahack17.R;

import butterknife.BindView;
import de.greenrobot.event.EventBus;

/**
 * Created by memuyskens on 1/12/17.
 * SpartaHack-Android
 */

public class SettingsFragment extends BaseFragment implements Switch.OnCheckedChangeListener {

    @BindView(R.id.push_switch) Switch pushSwitch;

    @Override int getLayout() {
        return R.layout.fragment_settings;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set switch to correct value
        pushSwitch.setChecked(getActivity().getSharedPreferences(getActivity().getApplication().getPackageName(), Activity.MODE_PRIVATE).getBoolean(Constants.PREF_PUSH, true));
        pushSwitch.setOnCheckedChangeListener(this);
    }

    @Override public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
        EventBus.getDefault().post(isChecked);
    }
}
