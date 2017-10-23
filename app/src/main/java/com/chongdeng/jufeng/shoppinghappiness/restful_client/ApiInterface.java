package com.chongdeng.jufeng.shoppinghappiness.restful_client;

import android.database.Observable;


import com.chongdeng.jufeng.shoppinghappiness.restful_model.UploadResult;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ApiInterface {

    @Multipart
    @POST("/v1/UploadAvatarImg")
    Call<UploadResult> uploadProfileImage(
            @Part("UserId") int UserId,
            @Part("avatar\"; filename=\"profile_pic.jpg") RequestBody file);

    @Multipart
    @POST("/v1/UploadMerchandise")
    Call<UploadResult> UploadMerchandise(@Part("merchandise_name") String merchandise_name,
                              @Part("merchandise_price") String merchandise_price,
                              @Part("merchandise_desc") String merchandise_desc,
                              @PartMap Map<String,RequestBody> params);


}






