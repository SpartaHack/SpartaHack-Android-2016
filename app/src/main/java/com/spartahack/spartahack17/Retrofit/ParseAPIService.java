package com.spartahack.spartahack17.Retrofit;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spartahack.spartahack17.Keys;

import io.realm.RealmObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ryancasler on 1/4/16
 * SpartaHack2016-Android
 */
public class ParseAPIService {

    /**
     * Singleton instance of the api service
     */
    public final static ParseAPIService INSTANCE = new ParseAPIService();

    /**
     * Tag for logging
     */
    private static final String TAG = "ParseAPIService";

    /**
     * Retrofit network call interface
     */
    private IParseAPIService apiService;

    private ParseAPIService() {

        // create gson object
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            // Customize the request
            Request request = original.newBuilder()
                    .header("X-Parse-Application-Id", Keys.PARSE_APP_ID)
                    .header("X-Parse-REST-API-Key", Keys.PARSE_REST_API_KEY)
                    .method(original.method(), original.body())
                    .build();

            // Customize or return the response
            return chain.proceed(request);
        });
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // add your other interceptors …

        // add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!
        OkHttpClient client = httpClient.build();

        // request base url
        String serverAPI = "https://api.parse.com/1/";

        // create Retrofit rest adapter
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(serverAPI)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = restAdapter.create(IParseAPIService.class);
    }


    public IParseAPIService getRestAdapter() { return apiService; }

}