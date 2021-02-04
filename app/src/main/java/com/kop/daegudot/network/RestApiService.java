package com.kop.daegudot.network;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestApiService {

    @GET("repos/{owner}/{repo}/contributors")
    Observable<List<Contributor>> getObContributors(@Path("owner") String owner, @Path("repo") String repo);

    @POST("/user/register")
    Observable<Long> registerUser(@Body UserRequest userRequest);

    @POST("/user/login")
    Observable<UserResponse> requestLogin(@Body UserRequest userRequest);

    @GET("/user/register/{email}")
    Observable<UserResponse> findUserForDuplicate(@Path("email") String email);

    @GET("/places/list")
    Observable<List<Place>> getPlaceList();
}