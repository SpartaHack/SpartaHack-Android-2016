package com.example.spartahack.spartahack2016.Fragment;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.spartahack.spartahack2016.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HelpFragment extends BaseFragment {



    @Bind(R.id.categorySpinner) Spinner categorySpinner;
    @Bind(R.id.roomSpinner) Spinner roomSpinner;
    @Bind(R.id.submit) Button button;
    @Bind(R.id.name) EditText name;
    @Bind(R.id.nameLayout) TextInputLayout inputLayoutName;
    @Bind(R.id.description) EditText description;
    @Bind(R.id.descLayout) TextInputLayout inputLayoutDesc;

    //String spinnerText;
    final ParseUser user = ParseUser.getCurrentUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        ButterKnife.bind(this, view);

        // Category Spinner
        ArrayList<String> categoryArray = new ArrayList<String>();
        categoryArray.add("iOS");
        categoryArray.add("Android");
        categoryArray.add("Backend");
        categoryArray.add("Frontend");
        categoryArray.add("Windows");

        ArrayAdapter<String> spinnerCategoryArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, categoryArray);
        categorySpinner.setAdapter(spinnerCategoryArrayAdapter);

        // Room Spinner
        ArrayList<String> roomArray = new ArrayList<String>();
        roomArray.add("Cedar Village");
        roomArray.add("Chandler Crossings");
        roomArray.add("University Villa");
        roomArray.add("University Village");
        roomArray.add("2900 Apartments");

        ArrayAdapter<String> spinnerRoomArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, roomArray);
        roomSpinner.setAdapter(spinnerRoomArrayAdapter);

        //name.addTextChangedListener(new HelpTextWatcher(name));
        //description.addTextChangedListener(new HelpTextWatcher(description));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });


        return view;
    }

    private void submitForm(){
        if(!validateName() || !validateDesc()){
            return;
        }

        ParseObject data = new ParseObject("Concierge");
        //data.put("userId", user.getObjectId().toString());
        data.put("name", name.getText().toString());
        data.put("room", roomSpinner.getSelectedItem().toString());
        data.put("description", description.getText().toString());
        data.put("category", categorySpinner.getSelectedItem().toString());
        data.saveInBackground();
        Toast.makeText(getActivity().getBaseContext(), "Submitted Successfully", Toast.LENGTH_SHORT).show();
        name.setText("");
        description.setText("");
        requestFocus(name);
    }

    private boolean validateName(){
        if (name.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(name);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateDesc(){
        if (description.getText().toString().trim().isEmpty()) {
            inputLayoutDesc.setError(getString(R.string.err_msg_desc));
            requestFocus(description);
            return false;
        } else {
            inputLayoutDesc.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class HelpTextWatcher implements TextWatcher {

        private View view;

        private HelpTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.name:
                    validateName();
                    break;
                case R.id.description:
                    validateDesc();
                    break;
            }
        }
    }

}
