package com.spartahack.spartahack17.Retrofit;

import com.spartahack.spartahack17.Model.CreateMentorshipTicketRequest;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by ryancasler on 1/6/17
 * Spartahack-Android
 */
public interface ISlackAPIService {
    @POST("e4Xf6bsbKyw2k1wRWA8pWerK") Observable<ResponseBody> addTicket(@Body CreateMentorshipTicketRequest request);
}
