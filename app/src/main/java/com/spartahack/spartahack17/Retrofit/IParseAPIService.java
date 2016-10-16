package com.spartahack.spartahack17.Retrofit;


import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by ryancasler on 1/4/16
 * SpartaHack2016-Android
 */
public interface IParseAPIService {

    @GET("classes/Company")
    Observable<GSONMock.Companies> getCompany();

    @GET("classes/Schedule")
    Observable<GSONMock.Events> getSchedule();

    @GET("classes/Prizes?include=sponsor")
    Observable<GSONMock.Prizes> getPrizes();

    @GET("classes/Announcements")
    Observable<GSONMock.Announcements> getAnnouncements();

    @PUT("classes/HelpDeskTickets/{oid}")
    Observable<GSONMock.UpdateObj> updateTicketStatus(@Path("oid") String o, @Body GSONMock.UpdateTicketStatusRequest del);

    @GET("classes/HelpDeskTickets/{tid}")
    Observable<GSONMock.Ticket> getTicket(@Path( "tid") String tid);
}
