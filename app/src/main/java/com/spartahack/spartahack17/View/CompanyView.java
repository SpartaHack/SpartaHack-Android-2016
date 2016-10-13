package com.spartahack.spartahack17.View;

import com.spartahack.spartahack17.Model.Company;

import java.util.ArrayList;

/**
 * Created by ryancasler on 10/12/16
 * SpartaHack2016-Android
 */
public interface CompanyView extends BaseView {
    void showCompanies(ArrayList<Company> companies);
}
