package com.spartahack.spartahack17.Retrofit;

import com.spartahack.spartahack17.Model.Session;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by ryancasler on 9/19/16.
 * SpartaHack2016-Android
 */
public interface ISpartaHackAPIService {

    @POST("sessions")
    Observable<Session> login(@Body GSONMock.Login login);

    @DELETE("sessions/{token}")
    Observable<Session> logout(@Path("token") String token);
}
