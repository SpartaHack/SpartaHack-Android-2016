package com.spartahack.spartahack17.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spartahack.spartahack17.BuildConfig;
import com.spartahack.spartahack17.Keys;
import com.spartahack.spartahack17.MyApplication;
import com.spartahack.spartahack17.Utility;

import okhttp3.Cache;
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

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();

        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();

                    // Customize the request
                    Request request = original.newBuilder()
                            .header("Content-Type", "application/json")
                            .header("Authorization", Keys.SPARTAHACK_API_KEY)
                            .method(original.method(), original.body())
                            .build();

                    // Customize or return the response
                    return chain.proceed(request);
                })
                .cache(new Cache(MyApplication.getContext().getCacheDir(), 10 * 1024 * 1024)) // 10 MB
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    if (Utility.isNetworkAvailable(MyApplication.getContext())) { // use cached data if less than a minute old
                        request = request.newBuilder().header("Cache-Control", "public, max-age=" + 60).build();
                    } else { // if data is cached and less than 30 minutes old
                        request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 30).build();
                    }
                    return chain.proceed(request);
                })
                .addInterceptor(new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE))
                .build();

        // create Retrofit rest adapter
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(BuildConfig.HOST)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = restAdapter.create(ISpartaHackAPIService.class);
    }


    public ISpartaHackAPIService getRestAdapter() {
        return apiService;
    }
}
