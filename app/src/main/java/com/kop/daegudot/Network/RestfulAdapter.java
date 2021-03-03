package com.kop.daegudot.Network;

import android.text.TextUtils;

import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.AuthenticationInterceptor;
import com.kop.daegudot.R;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestfulAdapter {

    private static String BASE_URL = "http://14.47.180.109:369/";
    private static String KAKAO_URL = "https://dapi.kakao.com/";

    private final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private final Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private Retrofit retrofit = builder.build();

    //version 2
    public RestApiService getServiceApi(String authToken) {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor authInterceptor =
                    new AuthenticationInterceptor("Bearer " + authToken);
            clientBuilder.addInterceptor(authInterceptor);
        }
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .baseUrl(BASE_URL)
                .build();

        return retrofit.create(RestApiService.class);
    }
    
    public RestApiService getKakaoServiceApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(KAKAO_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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
