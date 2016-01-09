package com.example.spartahack.spartahack2016.Retrofit;

import retrofit.http.GET;
import rx.Observable;

/**
 * Created by ryancasler on 1/4/16.
 */
public interface IParseAPIService {

    @GET("/classes/Company")
    Observable<GSONMock.Companies> getCompany();

    @GET("/classes/Schedule")
    Observable<GSONMock.Schedules> getSchedule();

    @GET("/classes/Prizes?include=sponsor")
    Observable<GSONMock.Prizes> getPrizes();

    @GET("/classes/Announcements")
    Observable<GSONMock.Announcements> getAnnouncements();
}
