package com.naury.boramjul;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MyInfoActivity extends AppCompatActivity {

    UserInfo userInfo;
    TextView order_list_btn, sns_info_textView2, sns_info_textView3, sns_info_textView4;
    CardView Google_Login;
    CardView Naver_Login;
    CardView KaKao_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        userInfo = new UserInfo();

        order_list_btn = (TextView)findViewById(R.id.order_list_btn);
        sns_info_textView2 = (TextView)findViewById(R.id.sns_info_textView2);
        sns_info_textView3 = (TextView)findViewById(R.id.sns_info_textView3);
        sns_info_textView4 = (TextView)findViewById(R.id.sns_info_textView4);
        Google_Login = (CardView)findViewById(R.id.Google_Login);
        Naver_Login = (CardView)findViewById(R.id.Naver_Login);
        KaKao_Login = (CardView)findViewById(R.id.KaKao_Login);

        int type = userInfo.getLogin_type();
        if(type==1){
            order_list_btn.setVisibility(View.VISIBLE);
        }else {
            if(type==2) {
                Google_Login.setVisibility(View.VISIBLE);
                sns_info_textView2.setText(userInfo.getName());
            }else if(type==3){
                Naver_Login.setVisibility(View.VISIBLE);
                sns_info_textView3.setText(userInfo.getName());
            }else if(type==4){
                KaKao_Login.setVisibility(View.VISIBLE);
                sns_info_textView4.setText(userInfo.getName());
            }
        }
    }

    public void onClick_back(View v){
        finish();
    }

    public void onClick_Join(View v){
        Intent intent = new Intent(MyInfoActivity.this, TOS_Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
