package com.kop.daegudot.Network;

import com.kop.daegudot.KakaoMap.Documents;
import com.kop.daegudot.Network.Map.Place;
import com.kop.daegudot.Network.Map.PlaceGeo;
import com.kop.daegudot.Network.More.MyInfo.MyCommentList;
import com.kop.daegudot.Network.Recommend.Comment.CommentRegister;
import com.kop.daegudot.Network.Recommend.Comment.CommentRegisterResponseDto;
import com.kop.daegudot.Network.Recommend.Comment.CommentResponseList;
import com.kop.daegudot.Network.More.MyInfo.MyRecommendList;
import com.kop.daegudot.Network.Recommend.Hashtag.HashtagResponseList;
import com.kop.daegudot.Network.Recommend.RecommendRegister;
import com.kop.daegudot.Network.Recommend.RecommendResponseList;
import com.kop.daegudot.Network.Schedule.MainScheduleRegister;
import com.kop.daegudot.Network.Schedule.MainScheduleResponseList;
import com.kop.daegudot.Network.Schedule.SubScheduleRegister;
import com.kop.daegudot.Network.More.MyInfo.NicknameUpdate;
import com.kop.daegudot.Network.More.MyInfo.PasswordUpdate;
import com.kop.daegudot.Network.Schedule.SubScheduleResponseList;
import com.kop.daegudot.Network.User.UserOauth;
import com.kop.daegudot.Network.User.UserOauthResponse;
import com.kop.daegudot.Network.User.UserRegister;
import com.kop.daegudot.Network.User.UserLogin;
import com.kop.daegudot.Network.User.UserResponse;
import com.kop.daegudot.Network.User.UserResponseStatus;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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
    Observable<String> registerUser(@Body UserRegister userRegister);

    @GET("/user/register/email/{email}")
    Observable<UserResponseStatus> checkEmailDup(@Path("email") String email);

    @GET("/user/register/nickname/{nickname}")
    Observable<UserResponseStatus> checkNickDup(@Path("nickname") String nickname);

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
    
    /* MainSchedule */
    
    /* make new main Schedule */
    @POST("/schedule/main/register")
    Observable<Long> saveMainSchedule(@Body MainScheduleRegister mainSchedule);
    
    /* get all main schedules */
    @GET("/schedule/main")
    Observable<MainScheduleResponseList> getMainSchedule();
    
    /* delete a main Schedule */
    @DELETE("/schedule/main/delete/{mainScheduleId}")
    Observable<Long> deleteMainSchedule(@Path("mainScheduleId") long mainScheduleId);
    
    /* update main Schedule */
    @PUT("/schedule/main/update/{mainscheduleId}")
    Observable<Long> updateMainSchedule(@Path("mainscheduleId") long mainScheduleId);
    
    /* SubSchedule */
    
    /* insert sub Schedule*/
    @POST("/schedule/sub/register")
    Observable<Long> registerSubschdule(@Body SubScheduleRegister subScheduleRegister);
    
    /* get sub Schedule by main schedule */
    @GET("/schedule/sub/{mainscheduleId}")
    Observable<SubScheduleResponseList> getSubscheduleList(@Path("mainscheduleId") long mainScheduleId);
    
    /* delete subschedule from main schedule */
    @DELETE("/schedule/sub/delete/{subscheduleId}")
    Observable<Long> deleteSubSchedule(@Path("subscheduleId") long subScheduleId);
    
    /* update Subschedule*/
    @PUT("/schedule/sub/update/{subscheduleId}")
    Observable<Long> updateSubSchedule(@Path("subscheduleId") long subscheduleId);

    @GET("/user/info")
    Observable<UserResponse> getUserFromToken();

    @PUT("/user/update/nickname")
    Observable<Long> updateUserNickname(@Body NicknameUpdate nicknameUpdate);

    @PUT("/user/update/password")
    Observable<Long> updateUserPassword(@Body PasswordUpdate passwordUpdate);

    /* Recommandation */
    @POST("/recommend/register")
    Observable<Long> registerRecommendSchedule(@Body RecommendRegister recommendRegister);
    
    @GET("/recommend/{hashtagId}")
    Observable<RecommendResponseList> selectAllRecommendList(@Path("hashtagId") long hashtagId);
    
    @PUT("/recommend/update/{recommendScheduleId}")
    Observable<Long> updateRecommendSchedule(
            @Path("recommendScheduleId") long recommendScheduleId,
            @Body RecommendRegister recommendRegister);
    
    @DELETE("/recommend/delete/{recommendScheduleId}")
    Observable<Long> deleteRecommendSchedule(@Path("recommendScheduleId") long recommendScheduleId);
    
    /* Hashtag */
    @GET("/hashtag")
    Observable<HashtagResponseList> selectHashtagList();
    
    /* Comment */
    @POST("/comment/register")
    Observable<CommentRegisterResponseDto> registerComment(@Body CommentRegister commentRegister);
//    Observable<Long> registerComment(@Body CommentRegister commentRegister);
    
    @GET("/comment/{recommendScheduleId}")
    Observable<CommentResponseList> selectAllCommentList(@Path("recommendScheduleId") long recommendScheduleId);
    
    @PUT("/comment/update/{commentId}")
    Observable<Long> updateComment(
            @Path("commentId") long commentId,
            @Body String comments);
    
    @DELETE("/comment/delete/{commentId}")
    Observable<Long> deleteComment(@Path("commentId") long commentId);
    
    /* More - My Recommend Schedule & My Comment */
    @GET("/more/recommend")
    Observable<MyRecommendList> selectMyRecommendSchedules();
    
    @GET("/more/comment")
    Observable<MyCommentList> selectMyComments();

    @POST("/user/register/google")
    Observable<UserOauthResponse> oauthGoogle(@Body UserOauth userOauth);

    @POST("/user/register/kakao")
    Observable<UserOauthResponse> oauthKakao(@Body UserOauth userOauth);

    @POST("/user/register/kakao")
    Observable<Long> oauthKakao(@Body UserOauth userOauth);
}
