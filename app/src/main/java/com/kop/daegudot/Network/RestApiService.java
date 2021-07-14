package com.kop.daegudot.Network;

import com.kop.daegudot.KakaoMap.Documents;
import com.kop.daegudot.Network.Map.Place;
import com.kop.daegudot.Network.Map.PlaceGeo;
import com.kop.daegudot.Network.User.UserRegister;
import com.kop.daegudot.Network.User.UserLogin;
import com.kop.daegudot.Network.User.UserResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApiService {

/*    @GET("repos/{owner}/{repo}/contributors")
    Observable<List<Contributor>> getObContributors(@Path("owner") String owner, @Path("repo") String repo);*/

    /* Login */
    @POST("/user/register")
    Observable<String> registerUser(@Body UserRegister userRegisterRequest);

    @GET("/user/register/email/{email}")
    Observable<UserResponse> checkEmailDup(@Path("email") String email);

    @GET("/user/register/nickname/{nickname}")
    Observable<UserResponse> checkNickDup(@Path("nickname") String nickname);

    @POST("/user/login")
    Observable<UserResponse> requestLogin(@Body UserLogin userLogin);

    @GET("/places/list")
    Observable<List<Place>> getPlaceList();
    
    @PUT("/places/location")
    Observable<Long> updateLocation(@Body List<PlaceGeo> placeGeoList);
    
    /* get MapPoint by address */
    @GET("v2/local/search/address.json")
    Call<Documents> getSearchAddress(
            @Header("Authorization") String key,
            @Query("query") String query
    );
    
    /* get place by category */
    @GET("v2/local/search/category.json")
    Call<Documents> getPlacebyCategory(
            @Header("Authorization") String key,
            @Query("category_group_code") String category_group_code,
            @Query("x") String x,
            @Query("y") String y,
            @Query("radius") int radius,
            @Query("page") int page
    );
    
    @GET("v2/local/search/category.json")
    Call<Documents> getPlacebyCategoryRect(
            @Header("Authorization") String key,
            @Query("category_group_code") String category_group_code,
            @Query("rect") String coord
    );

}
