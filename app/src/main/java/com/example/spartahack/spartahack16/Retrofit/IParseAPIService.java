package com.example.spartahack.spartahack16.Retrofit;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;
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

    @PUT("/classes/HelpDeskTickets/{oid}")
    Observable<GSONMock.UpdateObj> updateTicketStatus(@Path("oid") String o, @Body GSONMock.UpdateTicketStatusRequest del);

    @GET("/classes/HelpDeskTickets/{tid}")
    Observable<GSONMock.Ticket> getTicket(@Path( "tid") String tid);
}
