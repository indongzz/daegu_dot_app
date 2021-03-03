package com.kop.daegudot.Network;

import com.kop.daegudot.KakaoMap.Documents;
import com.kop.daegudot.Login.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApiService {

/*    @GET("repos/{owner}/{repo}/contributors")
    Observable<List<Contributor>> getObContributors(@Path("owner") String owner, @Path("repo") String repo);*/

    /* Login */
    @POST("/user/register")
    Observable<Long> registerUser(@Body User userRequest);

    @GET("/user/register/{email}")
    String checkEmailDup(@Path("email") String email);

    @GET("/user/register/{nickname}")
    Observable<List<User>> checkNickDup(@Path("email") String nickname);

    @POST("/user/login")
    Observable<User> requestLogin(@Body User userRequest);

    @GET("/places/list")
    Observable<List<Place>> getPlaceList();
    
    /* get MapPoint by address */
    @GET("v2/local/search/address.json")
    Call<Documents> getSearchAddress(
            @Header("Authorization") String key,
            @Query("query") String query
    );
    
    /* get place by category */
    @GET("v2/local/search/category.{format}")
    Call<Documents> getPlacebyCategory(
            @Header("Authorization") String key,
            @Query("x") String x,
            @Query("y") String y,
            @Query("radius") int radius
    );
}
