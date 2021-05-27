package com.naury.boramjul.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.naury.boramjul.R;
import com.naury.boramjul.DTO.UserInfo;

public class MyInfoActivity extends AppCompatActivity {

    UserInfo userInfo;
    TextView order_list_btn, sns_info_textView2, sns_info_textView3, sns_info_textView4, user_name_text, user_mail_text, user_ph_num_text, membership_rank, membership_sub;
    CardView KaKao_Login, Naver_Login, Google_Login, membership_layout, my_review_layout;
    LinearLayout boramjul_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        userInfo = new UserInfo();

        order_list_btn = (TextView)findViewById(R.id.order_list_btn);
        sns_info_textView2 = (TextView)findViewById(R.id.sns_info_textView2);
        sns_info_textView3 = (TextView)findViewById(R.id.sns_info_textView3);
        sns_info_textView4 = (TextView)findViewById(R.id.sns_info_textView4);
        user_name_text = (TextView)findViewById(R.id.user_name_text);
        user_mail_text = (TextView)findViewById(R.id.user_mail_text);
        user_ph_num_text = (TextView)findViewById(R.id.user_ph_num_text);
        membership_rank = (TextView)findViewById(R.id.membership_rank);
        membership_sub = (TextView)findViewById(R.id.membership_sub);
        Google_Login = (CardView)findViewById(R.id.Google_Login);
        Naver_Login = (CardView)findViewById(R.id.Naver_Login);
        KaKao_Login = (CardView)findViewById(R.id.KaKao_Login);
        membership_layout = (CardView)findViewById(R.id.membership_layout);
        my_review_layout = (CardView)findViewById(R.id.my_review_layout);
        boramjul_Login = (LinearLayout) findViewById(R.id.boramjul_Login);

        int type = userInfo.getLogin_type();
        if(type==1){
            set_layout();
        }else {
            if(type==2) {
                Google_Login.setVisibility(View.VISIBLE);
                sns_info_textView2.setText(userInfo.getEmail());
                set_layout();
            }else if(type==3){
                Naver_Login.setVisibility(View.VISIBLE);
                sns_info_textView3.setText(userInfo.getEmail());
                set_layout();
            }else if(type==4){
                KaKao_Login.setVisibility(View.VISIBLE);
                sns_info_textView4.setText(userInfo.getEmail());
                set_layout();
            }
        }
    }

    private void set_layout(){
        order_list_btn.setVisibility(View.VISIBLE);
        boramjul_Login.setVisibility(View.VISIBLE);
        membership_layout.setVisibility(View.VISIBLE);

        user_name_text.setText(userInfo.getName()+"님, 반가워요!");
        user_mail_text.setText(userInfo.getEmail());
        user_ph_num_text.setText(userInfo.getPh_num());
        if(userInfo.getRank().equals("브론즈")){
            membership_rank.setText("브론즈");
            membership_rank.setTextColor(Color.parseColor("#BB5353"));
            membership_sub.setText("5%할인 혜택");
        }else if(userInfo.getRank().equals("실버")){
            membership_rank.setText("실버");
            membership_rank.setTextColor(Color.parseColor("#A4A9B8"));
            membership_sub.setText("10%할인 혜택");
        }else if(userInfo.getRank().equals("골드")){
            membership_rank.setText("골드");
            membership_rank.setTextColor(Color.parseColor("#F1BA2C"));
            membership_sub.setText("15%할인 혜택");
        }
        my_review_layout.setVisibility(View.VISIBLE);
    }

    public void onClick_back(View v){
        finish();
    }

    public void onClick_Join(View v){
        Intent intent = new Intent(MyInfoActivity.this, TOS_Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }
}
