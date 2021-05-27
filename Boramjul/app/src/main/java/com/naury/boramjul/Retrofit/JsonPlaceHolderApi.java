package com.naury.boramjul.Retrofit;

import com.naury.boramjul.Post;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {
    @FormUrlEncoded
    @POST("memberinfojson.do")
    Call<Post> createPost(
            @Field("body") String text
    );

}
