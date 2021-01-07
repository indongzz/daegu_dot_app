package com.kop.daegudot.network;

import com.kop.daegudot.Login.User;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestApiService {

/*    @GET("repos/{owner}/{repo}/contributors")
    Observable<List<Contributor>> getObContributors(@Path("owner") String owner, @Path("repo") String repo);*/

    /* Login */
    @POST("/user/register")
    Observable<Long> postRegister(@FieldMap HashMap<String, Object> user);
    
    @GET("/user/register/{email}")
    String checkEmailDup(@Path("email") String email);
    
    @GET("/user/register/{nickname}")
    Observable<List<User>> checkNickDup(@Path("email") String nickname);
    
    @POST("/user/login")
    Observable<User> postLogin(User user);
    
    /* Main Schedule */
    
    @POST("/schedule/main/register")
    void postMainSchedule(String startDate, String endDate, String title, User user);
    
    @GET("/schedule/main/{userId}")
    Observable<String> getMainSchedule(String userId);
    
    
}
