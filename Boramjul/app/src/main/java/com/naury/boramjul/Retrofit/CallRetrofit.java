package com.naury.boramjul.Retrofit;

import android.util.Log;

import com.naury.boramjul.Model__CheckAlready;
import com.naury.boramjul.SingleObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallRetrofit {
    public List<SingleObject> callPhoneAlreadyCheck(){

        String response = "";

        //Retrofit 호출
        Model__CheckAlready modelCheckAlready = new Model__CheckAlready();
        RetrofitClient retrofitConnection = new RetrofitClient();
        Call<Model__CheckAlready> call =  retrofitConnection.server.get_data();
        call.enqueue(new Callback<Model__CheckAlready>() {
            @Override
            public void onResponse(Call<Model__CheckAlready> call, Response<Model__CheckAlready> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적 : ", "error code : " + response.code());
                    return;
                }
                Model__CheckAlready checkAlready = response.body();
                Log.d("연결이 성공적 : ", response.body().toString());
//                if(modelCheckAlready.getMessage() == "can use this number"){
//                    Log.d("중복검사: ", "중복된 번호가 아닙니다");
//                    modelCheckAlready.setRight(true);
//                }
            }
            @Override
            public void onFailure(Call<Model__CheckAlready> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });

        return modelCheckAlready.getUser();
    }
}
