package com.naury.boramjul.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://www.boramjul.kro.kr/member/";

    String URL = BASE_URL; // 서버 API

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RetrofitAPI server = retrofit.create(RetrofitAPI.class);

}
