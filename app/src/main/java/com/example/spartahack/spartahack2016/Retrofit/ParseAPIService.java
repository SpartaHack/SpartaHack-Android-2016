package com.example.spartahack.spartahack2016.Retrofit;

import com.example.spartahack.spartahack2016.BuildConfig;
import com.example.spartahack.spartahack2016.Keys;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.RealmObject;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by ryancasler on 1/4/16.
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

        // add headers to any network requests made
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestInterceptor.RequestFacade request) {
                request.addHeader("X-Parse-Application-Id", Keys.PARSE_APP_ID);
                request.addHeader("X-Parse-REST-API-Key", Keys.PARSE_REST_API_KEY);
            }
        };

        // request base url
        String serverAPI = "https://api.parse.com/1";

        // create Retrofit rest adapter
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(serverAPI)
                .setRequestInterceptor(requestInterceptor)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .build();

        apiService = restAdapter.create(IParseAPIService.class);
    }


    public IParseAPIService getRestAdapter() { return apiService; }

}