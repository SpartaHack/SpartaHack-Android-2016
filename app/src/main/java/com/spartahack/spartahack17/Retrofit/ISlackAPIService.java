package com.spartahack.spartahack17.Retrofit;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by ryancasler on 1/6/17
 * Spartahack-Android
 */
public interface ISlackAPIService {
    @POST() Observable<Boolean> addTicket(@Body GSONMock.CreateMentorshipTicketRequest request);
}
