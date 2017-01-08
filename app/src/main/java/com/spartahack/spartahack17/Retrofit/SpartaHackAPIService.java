package com.spartahack.spartahack17.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spartahack.spartahack17.BuildConfig;
import com.spartahack.spartahack17.Keys;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ryancasler on 9/19/16.
 * SpartaHack2016-Android
 */
public class SpartaHackAPIService {
    /**
     * Singleton instance of the api service
     */
    public final static SpartaHackAPIService INSTANCE = new SpartaHackAPIService();

    /**
     * Tag for logging
     */
    private static final String TAG = "SpartaHackAPIService";

    /**
     * Retrofit network call interface
     */
    private final ISpartaHackAPIService apiService;

    private SpartaHackAPIService() {

        // create gson object
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            // Customize the request
            Request request = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Authorization", Keys.SPARTAHACK_API_KEY)
                    .method(original.method(), original.body())
                    .build();

            // Customize or return the response
            return chain.proceed(request);
        });
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        // set your desired log level
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        // add your other interceptors …

        // add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!
        OkHttpClient client = httpClient.build();


        // create Retrofit rest adapter
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(BuildConfig.HOST)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = restAdapter.create(ISpartaHackAPIService.class);
    }


    public ISpartaHackAPIService getRestAdapter() { return apiService; }
}
