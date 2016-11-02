package com.example.seonoh2.smarttoliet01.manager;

import com.example.seonoh2.smarttoliet01.model.TaskSignUp;
import com.example.seonoh2.smarttoliet01.model.products.Request;
import com.example.seonoh2.smarttoliet01.model.user.EditUser;
import com.example.seonoh2.smarttoliet01.model.user.SignUp;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by tylenol on 2016. 10. 30..
 */

public class RetrofitManager {

  public RetrofitManager() {

  }

  public Retrofit getRetrofit() {
    return new Retrofit.Builder()
        .baseUrl("http://14.63.226.110:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }

  public interface MemberEdit {
    @PUT("api/0.1v/members/{id}")
    Call<EditUser> requests(@Query("access_token") String authorization,
                            @Query("pwd") String pwd,
                            @Query("chgPwd") String chgPwd,
                            @Query("ndPwd") String ndPwd,
                            @Query("phoneNum") String phoneNum);
  }

  public interface MemberSignUp {
    @POST("api/0.1v/members")
    Call<SignUp> requests(@Body TaskSignUp taskSignUp);
  }

  public interface Products {
    @GET("api/0.1v/toilets/{toiletIdx}/product?offset=0")
    Call<Request> requests(@Path("toiletIdx") int idx);
  }

  public interface Buy {
    @GET("api/0.1v/toilets/{toiletIdx}/buy/{productIdx}")
    Call<com.example.seonoh2.smarttoliet01.model.products.Buy> requests(@Path("toiletIdx") int toiletIdx, @Path("productIdx") int productIdx);
  }

  public interface Toilets {
    @GET("api/0.1v/toilets?offset=1")
    Call<com.example.seonoh2.smarttoliet01.model.toilets.Toilets> requests(@Query("access_token") String authorization);
  }

  public interface NearbyToilets {
    @GET("api/0.1v/toiletFind")
    Call<com.example.seonoh2.smarttoliet01.model.toilets.NearbyToilets> requests(@Query("access_token") String authorization,
                                                                                 @Query("latitude") float latitude,
                                                                                 @Query("longitude") float longitude);
  }

  public interface Report {
    @POST("api/0.1v/reports")
    Call<com.example.seonoh2.smarttoliet01.model.Report> requests(@Query("toiletIdx") int idx, @Query("contents") String contents);
  }

  public interface OpenDoor {
    @POST("api/0.1v/conn/dopen")
    Call<com.example.seonoh2.smarttoliet01.model.hardware.OpenDoor> requests(
        @Query("access_token") String authorization,
        @Query("beaconId") String beaconId,
        @Query("ndPwd") int ndPwd);
  }

  public interface Login {
    @POST("api/0.1v/login")
    Call<com.example.seonoh2.smarttoliet01.model.login.Login> requests(@Query("id") String id, @Query("pwd") String pwd);
  }

  public interface LoginCheck {
    @GET("api/0.1v/login_check")
    Call<com.example.seonoh2.smarttoliet01.model.login.LoginCheck> requests(@Query("access_token") String authorization);
  }
}
