package com.example.spartahack.spartahack2016.Retrofit;

import retrofit.http.GET;
import rx.Observable;

/**
 * Created by ryancasler on 1/4/16.
 */
public interface IParseAPIService {

    @GET("/classes/Company")
    Observable<GSONMock.Companies> getCompany();

}
