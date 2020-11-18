package com.kop.daegudot.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestfulAdapter {

    private static String BASE_URL = "http://127.0.0.1";

    public RestApiService getServiceApi() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl(BASE_URL)
                .build();

        return retrofit.create(RestApiService.class);
    }

    private RestfulAdapter() {}

    public static RestfulAdapter getInstance() {
        return LazyHolder.instance;
    }

    private static class LazyHolder {
        private static final RestfulAdapter instance = new RestfulAdapter();
    }
}
