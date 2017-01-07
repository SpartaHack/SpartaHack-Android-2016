package com.spartahack.spartahack17.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spartahack.spartahack17.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ryancasler on 1/6/17
 * Spartahack-Android
 */
public class SlackAPIService  {
    /**
     * Singleton instance of the api service
     */
    public final static SlackAPIService INSTANCE = new SlackAPIService();

    /**
     * Tag for logging
     */
    private static final String TAG = "SlackAPIService";

    /**
     * Retrofit network call interface
     */
    private final ISlackAPIService apiService;

    private SlackAPIService() {

        // create gson object
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            // Customize the request
            Request request = original.newBuilder()
                    .method(original.method(), original.body())
                    .build();

            // Customize or return the response
            return chain.proceed(request);
        });
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        // set your desired log level
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        // add logging as last interceptor
        httpClient.addInterceptor(logging);
        OkHttpClient client = httpClient.build();


        // create Retrofit rest adapter
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl("https://hooks.slack.com/services/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")
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = restAdapter.create(ISlackAPIService.class);
    }
    
    public ISlackAPIService getRestAdapter() { return apiService; }
}
