package com.example.spartahack.spartahack2016.Fragment;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

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
    ParseUser user;
    List<ParseObject> categoryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        user = ParseUser.getCurrentUser();
        if (user == null){
            Log.d("NOT LOGGED IN", "SHIT IS NOT BROKEN YO");
            return inflater.inflate(R.layout.layout_notloggedin, container, false);
        }
        else {
            Log.d("NOT LOGGED IN", "SHIT IS BROKEN YO");
            view = inflater.inflate(R.layout.fragment_help, container, false);

            ButterKnife.bind(this, view);

            // Category Spinner
            final ArrayList<String> categoryArray = new ArrayList<String>();
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("HelpDesk");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> markers, ParseException e) {
                    if (e == null) {
                        categoryList = markers;
                        for (ParseObject object : markers) {
                            categoryArray.add(object.get("category").toString());
                        }
                        ArrayAdapter<String> spinnerCategoryArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categoryArray);
                        categorySpinner.setAdapter(spinnerCategoryArrayAdapter);
                    } else {
                        // handle Parse Exception here
                    }
                }
            });

            // Room Spinner
            ArrayAdapter<String> spinnerRoomArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.roomArray));
            roomSpinner.setAdapter(spinnerRoomArrayAdapter);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    submitForm();
                }
            });
        }

        return view;
    }

    private void submitForm(){
        if(!validateName() || !validateDesc()){
            return;
        }

        ParseObject data = new ParseObject("HelpDeskTickets");
//        if (user != null) {
//            data.put("userId", user.getObjectId().toString());
//        }
        //data.put("name", name.getText().toString());
        //data.put("room", roomSpinner.getSelectedItem().toString());
        data.put("description", description.getText().toString());

        //Category Relation Object
        ParseObject categoryObject = null;
        for (ParseObject object : categoryList){
            if (object.get("category") == categorySpinner.getSelectedItem().toString()){
                categoryObject = object;
            }
        }

        data.put("category", categoryObject);
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

}
