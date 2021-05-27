package com.naury.boramjul.Retrofit;

import com.naury.boramjul.Model__CheckAlready;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitAPI {
    @GET("memberinfojson.do")
    Call<Model__CheckAlready> get_data();
}
