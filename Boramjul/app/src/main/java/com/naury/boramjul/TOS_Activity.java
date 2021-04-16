package com.naury.boramjul;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TOS_Activity extends AppCompatActivity {

    private static final String TAG = "Boramjul_TOS_Activity";

    private BackPressCloseHendler backPressCloseHendler;//뒤로가기버튼 터치핸들러

    ConstraintLayout all_option_select;
    ConstraintLayout service_tos_layout;
    ConstraintLayout event_agree_layout;
    int all_option_select_state = 0;
    int all_service_tos_option_select_state = 0;
    int event_receive_option_select_state = 0;
    int boramjul_tos_result = 0;
    int personal_information_collection_result = 0;
    int map_service_tos_result = 0;
    int receive_push_result = 0;
    int receive_sms_result = 0;
    int receive_email_result = 0;
    ImageView all_option_select_check;
    ImageView service_tos_check;
    ImageView service_tos_check1;
    ImageView service_tos_check2;
    ImageView service_tos_check3;
    ImageView event_agree_check;
    ImageView event_agree_check1;
    ImageView event_agree_check2;
    ImageView event_agree_check3;
    TextView service_tos_text;

    Animation scale500;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tos);

        all_option_select = (ConstraintLayout)findViewById(R.id.all_option_select);
        service_tos_layout = (ConstraintLayout)findViewById(R.id.service_tos_layout);
        event_agree_layout = (ConstraintLayout)findViewById(R.id.event_agree_layout);
        all_option_select_check = (ImageView)findViewById(R.id.all_option_select_check);
        service_tos_check = (ImageView)findViewById(R.id.service_tos_check);
        service_tos_check1 = (ImageView)findViewById(R.id.service_tos_check1);
        service_tos_check2 = (ImageView)findViewById(R.id.service_tos_check2);
        service_tos_check3 = (ImageView)findViewById(R.id.service_tos_check3);
        event_agree_check = (ImageView)findViewById(R.id.event_agree_check);
        event_agree_check1 = (ImageView)findViewById(R.id.event_agree_check1);
        event_agree_check2 = (ImageView)findViewById(R.id.event_agree_check2);
        event_agree_check3 = (ImageView)findViewById(R.id.event_agree_check3);
        service_tos_text = (TextView)findViewById(R.id.service_tos_text);

        scale500 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.scale500);
        backPressCloseHendler = new BackPressCloseHendler(this);//뒤로가기버튼 동작 제어 핸들러 생성

        Spannable span = (Spannable) service_tos_text.getText();
        span.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), 15, 16, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        if(receive_push_result == 1&&receive_sms_result == 1&&receive_email_result == 1){
            event_receive_option_select_state = 1;
            event_agree_check.setBackgroundResource(R.drawable.ic_check_purple);
            event_agree_check.startAnimation(scale500);
        }
        if(boramjul_tos_result == 1&&personal_information_collection_result == 1&&map_service_tos_result == 1){
            all_service_tos_option_select_state = 1;
            service_tos_check.setBackgroundResource(R.drawable.ic_check_purple);
            service_tos_check.startAnimation(scale500);
        }
        if(all_service_tos_option_select_state == 1 && event_receive_option_select_state == 1){
            all_option_select_state = 1;
            all_option_select_check.setBackgroundResource(R.drawable.ic_check_purple);
            all_option_select_check.startAnimation(scale500);
        }

        if(boramjul_tos_result == 1){
            service_tos_check1.setBackgroundResource(R.drawable.ic_tick_purple);
            service_tos_check1.startAnimation(scale500);
        }
        if(personal_information_collection_result == 1){
            service_tos_check2.setBackgroundResource(R.drawable.ic_tick_purple);
            service_tos_check2.startAnimation(scale500);
        }
        if(map_service_tos_result == 1){
            service_tos_check3.setBackgroundResource(R.drawable.ic_tick_purple);
            service_tos_check3.startAnimation(scale500);
        }

        if(receive_push_result == 1){
            event_agree_check1.setBackgroundResource(R.drawable.ic_check_purple);
            event_agree_check1.startAnimation(scale500);
        }
        if(receive_sms_result == 1){
            event_agree_check2.setBackgroundResource(R.drawable.ic_check_purple);
            event_agree_check2.startAnimation(scale500);
        }
        if(receive_email_result == 1){
            event_agree_check3.setBackgroundResource(R.drawable.ic_check_purple);
            event_agree_check3.startAnimation(scale500);
        }
    }

    public void onClick_event_receive_option_select(View v){
        if(event_receive_option_select_state == 0){
            event_receive_option_select_state = 1;
            receive_push_result = 1;
            receive_sms_result = 1;
            receive_email_result = 1;
            event_agree_check.setBackgroundResource(R.drawable.ic_check_purple);
            event_agree_check.startAnimation(scale500);
            event_agree_check1.setBackgroundResource(R.drawable.ic_check_purple);
            event_agree_check1.startAnimation(scale500);
            event_agree_check2.setBackgroundResource(R.drawable.ic_check_purple);
            event_agree_check2.startAnimation(scale500);
            event_agree_check3.setBackgroundResource(R.drawable.ic_check_purple);
            event_agree_check3.startAnimation(scale500);
            all_option_select_state = 0;
            if(all_service_tos_option_select_state == 1 && event_receive_option_select_state == 1){
                all_option_select_state = 1;
                all_option_select_check.setBackgroundResource(R.drawable.ic_check_purple);
                all_option_select_check.startAnimation(scale500);
            }
        }else {
            event_receive_option_select_state = 0;
            receive_push_result = 0;
            receive_sms_result = 0;
            receive_email_result = 0;
            event_agree_check.setBackgroundResource(R.drawable.ic_check);
            event_agree_check1.setBackgroundResource(R.drawable.ic_check);
            event_agree_check2.setBackgroundResource(R.drawable.ic_check);
            event_agree_check3.setBackgroundResource(R.drawable.ic_check);
            all_option_select_check.setBackgroundResource(R.drawable.ic_check);
            all_option_select_state = 0;
        }
    }

    public void onClick_event_push_select(View v){
        if(receive_push_result == 0){
            event_receive_option_select_state = 0;
            receive_push_result = 1;
            event_agree_check1.setBackgroundResource(R.drawable.ic_check_purple);
            event_agree_check1.startAnimation(scale500);
            all_option_select_state = 0;
            if(receive_push_result == 1&&receive_sms_result == 1&&receive_email_result == 1){
                event_receive_option_select_state = 1;
                event_agree_check.setBackgroundResource(R.drawable.ic_check_purple);
                event_agree_check.startAnimation(scale500);
            }
            if(all_service_tos_option_select_state == 1 && event_receive_option_select_state == 1){
                all_option_select_state = 1;
                all_option_select_check.setBackgroundResource(R.drawable.ic_check_purple);
                all_option_select_check.startAnimation(scale500);
            }
        }else {
            event_receive_option_select_state = 0;
            receive_push_result = 0;
            event_agree_check1.setBackgroundResource(R.drawable.ic_check);
            event_agree_check.setBackgroundResource(R.drawable.ic_check);
            all_option_select_check.setBackgroundResource(R.drawable.ic_check);
            all_option_select_state = 0;
        }
    }

    public void onClick_event_sms_select(View v){
        if(receive_sms_result == 0){
            event_receive_option_select_state = 0;
            receive_sms_result = 1;
            event_agree_check2.setBackgroundResource(R.drawable.ic_check_purple);
            event_agree_check2.startAnimation(scale500);
            all_option_select_state = 0;
            if(receive_push_result == 1&&receive_sms_result == 1&&receive_email_result == 1){
                event_receive_option_select_state = 1;
                event_agree_check.setBackgroundResource(R.drawable.ic_check_purple);
                event_agree_check.startAnimation(scale500);
            }
            if(all_service_tos_option_select_state == 1 && event_receive_option_select_state == 1){
                all_option_select_state = 1;
                all_option_select_check.setBackgroundResource(R.drawable.ic_check_purple);
                all_option_select_check.startAnimation(scale500);
            }
        }else {
            event_receive_option_select_state = 0;
            receive_sms_result = 0;
            event_agree_check2.setBackgroundResource(R.drawable.ic_check);
            event_agree_check.setBackgroundResource(R.drawable.ic_check);
            all_option_select_check.setBackgroundResource(R.drawable.ic_check);
            all_option_select_state = 0;
        }
    }

    public void onClick_event_email_select(View v){
        if(receive_email_result == 0){
            event_receive_option_select_state = 0;
            receive_email_result = 1;
            event_agree_check3.setBackgroundResource(R.drawable.ic_check_purple);
            event_agree_check3.startAnimation(scale500);
            all_option_select_state = 0;
            if(receive_push_result == 1&&receive_sms_result == 1&&receive_email_result == 1){
                event_receive_option_select_state = 1;
                event_agree_check.setBackgroundResource(R.drawable.ic_check_purple);
                event_agree_check.startAnimation(scale500);
            }
            if(all_service_tos_option_select_state == 1 && event_receive_option_select_state == 1){
                all_option_select_state = 1;
                all_option_select_check.setBackgroundResource(R.drawable.ic_check_purple);
                all_option_select_check.startAnimation(scale500);
            }
        }else {
            event_receive_option_select_state = 0;
            receive_email_result = 0;
            event_agree_check3.setBackgroundResource(R.drawable.ic_check);
            event_agree_check.setBackgroundResource(R.drawable.ic_check);
            all_option_select_check.setBackgroundResource(R.drawable.ic_check);
            all_option_select_state = 0;
        }
    }

    public void onClick_all_service_tos_option_select(View v){
        if(all_service_tos_option_select_state == 0){
            all_service_tos_option_select_state = 1;
            boramjul_tos_result = 1;
            personal_information_collection_result = 1;
            map_service_tos_result = 1;
            service_tos_check.setBackgroundResource(R.drawable.ic_check_purple);
            service_tos_check.startAnimation(scale500);
            service_tos_check1.setBackgroundResource(R.drawable.ic_tick_purple);
            service_tos_check1.startAnimation(scale500);
            service_tos_check2.setBackgroundResource(R.drawable.ic_tick_purple);
            service_tos_check2.startAnimation(scale500);
            service_tos_check3.setBackgroundResource(R.drawable.ic_tick_purple);
            service_tos_check3.startAnimation(scale500);
            all_option_select_state = 0;
            if(all_service_tos_option_select_state == 1 && event_receive_option_select_state == 1){
                all_option_select_state = 1;
                all_option_select_check.setBackgroundResource(R.drawable.ic_check_purple);
                all_option_select_check.startAnimation(scale500);
            }
        }else {
            all_service_tos_option_select_state = 0;
            boramjul_tos_result = 0;
            personal_information_collection_result = 0;
            map_service_tos_result = 0;
            service_tos_check.setBackgroundResource(R.drawable.ic_check);
            service_tos_check1.setBackgroundResource(R.drawable.ic_tick);
            service_tos_check2.setBackgroundResource(R.drawable.ic_tick);
            service_tos_check3.setBackgroundResource(R.drawable.ic_tick);
            all_option_select_check.setBackgroundResource(R.drawable.ic_check);
            all_option_select_state = 0;
        }
    }

    public void onClick_personal_information_collection_select(View v){
        if(personal_information_collection_result == 0){
            all_service_tos_option_select_state = 0;
            personal_information_collection_result = 1;
            service_tos_check2.setBackgroundResource(R.drawable.ic_tick_purple);
            service_tos_check2.startAnimation(scale500);
            all_option_select_state = 0;
            if(boramjul_tos_result == 1&&personal_information_collection_result == 1&&map_service_tos_result == 1){
                all_service_tos_option_select_state = 1;
                service_tos_check.setBackgroundResource(R.drawable.ic_check_purple);
                service_tos_check.startAnimation(scale500);
            }
            if(all_service_tos_option_select_state == 1 && event_receive_option_select_state == 1){
                all_option_select_state = 1;
                all_option_select_check.setBackgroundResource(R.drawable.ic_check_purple);
                all_option_select_check.startAnimation(scale500);
            }
        }else {
            all_service_tos_option_select_state = 0;
            personal_information_collection_result = 0;
            service_tos_check2.setBackgroundResource(R.drawable.ic_tick);
            service_tos_check.setBackgroundResource(R.drawable.ic_check);
            all_option_select_check.setBackgroundResource(R.drawable.ic_check);
            all_option_select_state = 0;
        }
    }

    public void onClick_map_service_tos_select(View v){
        if(map_service_tos_result == 0){
            all_service_tos_option_select_state = 0;
            map_service_tos_result = 1;
            service_tos_check3.setBackgroundResource(R.drawable.ic_tick_purple);
            service_tos_check3.startAnimation(scale500);
            all_option_select_state = 0;
            if(boramjul_tos_result == 1&&personal_information_collection_result == 1&&map_service_tos_result == 1){
                all_service_tos_option_select_state = 1;
                service_tos_check.setBackgroundResource(R.drawable.ic_check_purple);
                service_tos_check.startAnimation(scale500);
            }
            if(all_service_tos_option_select_state == 1 && event_receive_option_select_state == 1){
                all_option_select_state = 1;
                all_option_select_check.setBackgroundResource(R.drawable.ic_check_purple);
                all_option_select_check.startAnimation(scale500);
            }
        }else {
            all_service_tos_option_select_state = 0;
            map_service_tos_result = 0;
            service_tos_check3.setBackgroundResource(R.drawable.ic_tick);
            service_tos_check.setBackgroundResource(R.drawable.ic_check);
            all_option_select_check.setBackgroundResource(R.drawable.ic_check);
            all_option_select_state = 0;
        }
    }

    public void onClick_boramjul_tos_select(View v){
        if(boramjul_tos_result == 0){
            all_service_tos_option_select_state = 0;
            boramjul_tos_result = 1;
            service_tos_check1.setBackgroundResource(R.drawable.ic_tick_purple);
            service_tos_check1.startAnimation(scale500);
            all_option_select_state = 0;
            if(boramjul_tos_result == 1&&personal_information_collection_result == 1&&map_service_tos_result == 1){
                all_service_tos_option_select_state = 1;
                service_tos_check.setBackgroundResource(R.drawable.ic_check_purple);
                service_tos_check.startAnimation(scale500);
            }
            if(all_service_tos_option_select_state == 1 && event_receive_option_select_state == 1){
                all_option_select_state = 1;
                all_option_select_check.setBackgroundResource(R.drawable.ic_check_purple);
                all_option_select_check.startAnimation(scale500);
            }
        }else {
            all_service_tos_option_select_state = 0;
            boramjul_tos_result = 0;
            service_tos_check1.setBackgroundResource(R.drawable.ic_tick);
            service_tos_check.setBackgroundResource(R.drawable.ic_check);
            all_option_select_check.setBackgroundResource(R.drawable.ic_check);
            all_option_select_state = 0;
        }
    }

    public void onClick_all_option_select(View v){
        if(all_option_select_state == 0){
            all_option_select_state = 1;
            all_service_tos_option_select_state = 1;
            event_receive_option_select_state = 1;
            boramjul_tos_result = 1;
            personal_information_collection_result = 1;
            map_service_tos_result = 1;
            receive_push_result = 1;
            receive_sms_result = 1;
            receive_email_result = 1;
            all_option_select_check.setBackgroundResource(R.drawable.ic_check_purple);
            all_option_select_check.startAnimation(scale500);
            service_tos_check.setBackgroundResource(R.drawable.ic_check_purple);
            service_tos_check.startAnimation(scale500);
            service_tos_check1.setBackgroundResource(R.drawable.ic_tick_purple);
            service_tos_check1.startAnimation(scale500);
            service_tos_check2.setBackgroundResource(R.drawable.ic_tick_purple);
            service_tos_check2.startAnimation(scale500);
            service_tos_check3.setBackgroundResource(R.drawable.ic_tick_purple);
            service_tos_check3.startAnimation(scale500);
            event_agree_check.setBackgroundResource(R.drawable.ic_check_purple);
            event_agree_check.startAnimation(scale500);
            event_agree_check1.setBackgroundResource(R.drawable.ic_check_purple);
            event_agree_check1.startAnimation(scale500);
            event_agree_check2.setBackgroundResource(R.drawable.ic_check_purple);
            event_agree_check2.startAnimation(scale500);
            event_agree_check3.setBackgroundResource(R.drawable.ic_check_purple);
            event_agree_check3.startAnimation(scale500);
        }else {
            all_option_select_state = 0;
            all_service_tos_option_select_state = 0;
            event_receive_option_select_state = 0;
            boramjul_tos_result = 0;
            personal_information_collection_result = 0;
            map_service_tos_result = 0;
            receive_push_result = 0;
            receive_sms_result = 0;
            receive_email_result = 0;
            all_option_select_check.setBackgroundResource(R.drawable.ic_check);
            service_tos_check.setBackgroundResource(R.drawable.ic_check);
            service_tos_check1.setBackgroundResource(R.drawable.ic_tick);
            service_tos_check2.setBackgroundResource(R.drawable.ic_tick);
            service_tos_check3.setBackgroundResource(R.drawable.ic_tick);
            event_agree_check.setBackgroundResource(R.drawable.ic_check);
            event_agree_check1.setBackgroundResource(R.drawable.ic_check);
            event_agree_check2.setBackgroundResource(R.drawable.ic_check);
            event_agree_check3.setBackgroundResource(R.drawable.ic_check);
        }
    }

    public void onClick_back(View v){
        finish();
    }

    public void onClick_selyen_tos(View v){
//        Intent intent = new Intent(this, Selyen_TosView_Activity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void onClick_personal_info_tos(View v){
//        Intent intent = new Intent(this, PersonalInfo_TosView_Activity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void onClick_gps_service_tos(View v){
//        Intent intent = new Intent(this, GPS_Service_TosView_Activity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void onClick_next(View v){

        if(all_service_tos_option_select_state == 1){
            Intent intent = new Intent(this, PersonVerifyActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            finish();
        }else {
            Toast.makeText(this, "가입을 위해 서비스 이용약관 필수항목에 동의해 주세요", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed() {
        finish();
    }
}
