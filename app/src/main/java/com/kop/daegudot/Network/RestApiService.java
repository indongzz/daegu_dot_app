package com.kop.daegudot.Network;

import com.kop.daegudot.Login.User;
import com.kop.daegudot.Network.Map.MapResponse;
import com.kop.daegudot.Network.User.UserLogin;
import com.kop.daegudot.Network.User.UserRegister;
import com.kop.daegudot.Network.User.UserResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestApiService {

/*    @GET("repos/{owner}/{repo}/contributors")
    Observable<List<Contributor>> getObContributors(@Path("owner") String owner, @Path("repo") String repo);*/

    /* Login */
    @POST("/user/register")
    Observable<Long> registerUser(@Body UserRegister userRegister);

    @GET("/user/register/email/{email}")
    Observable<UserResponse> checkEmailDup(@Path("email") String email);

    @GET("/user/register/nickname/{nickname}")
    Observable<UserResponse> checkNickDup(@Path("nickname") String nickname);

    @POST("/user/login")
    Observable<UserResponse> requestLogin(@Body UserLogin userLogin);

    @GET("/user/info")
    Observable<UserResponse> getUserInfo();

    @GET("/places/list")
    Observable<List<MapResponse>> getPlaceList();

}
