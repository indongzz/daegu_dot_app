package com.kop.daegudot.network;

import com.kop.daegudot.Login.User;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestApiService {

/*    @GET("repos/{owner}/{repo}/contributors")
    Observable<List<Contributor>> getObContributors(@Path("owner") String owner, @Path("repo") String repo);*/

    /* Login */
    @POST("/user/register")
    Observable<Long> registerUser(@Body UserRequest userRequest);

    @GET("/user/register/{email}")
    String checkEmailDup(@Path("email") String email);

    @GET("/user/register/{nickname}")
    Observable<List<User>> checkNickDup(@Path("email") String nickname);

    @POST("/user/login")
    Observable<UserResponse> requestLogin(@Body UserRequest userRequest);

    @GET("/places/list")
    Observable<List<Place>> getPlaceList();

}
