package com.smarttradeschool.in.webservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseURL {

    public static final String BASE_URL = "http://smarttradeschool.com/api/";
    public static final String BASE_URL_video = "http://rastar.in/index.php/api/";

    private static Retrofit getBaseUrl() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public static AppAPI getVideoAPI() {
        return BaseURL.getBaseUrl().create(AppAPI.class);
    }


    private static AppAPI apiService;

    public static AppAPI getApiService() {

        if (apiService == null) {

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .baseUrl(BASE_URL).build();

            apiService = retrofit.create(AppAPI.class);
            return apiService;
        } else {
            return apiService;
        }
    }
}
