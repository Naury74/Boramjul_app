package com.naury.boramjul;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class CompleteSignUpActivity extends AppCompatActivity {

    private static final String TAG = "Selyen";

    private BackPressCloseHendler backPressCloseHendler;//뒤로가기버튼 터치핸들러

    Animation scale1000;
    Animation alpha_2000;
    Animation alpha_2000_2000;
    Animation alpha_4000_2000;

    TextView title;
    TextView text1;
    TextView text2;
    TextView text3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_sign_up);

        backPressCloseHendler = new BackPressCloseHendler(this);//뒤로가기버튼 동작 제어 핸들러 생성
        scale1000 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale1000);
        alpha_2000 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_2000);
        alpha_2000_2000 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_2000_2000);
        alpha_4000_2000 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_4000_2000);

        title = (TextView)findViewById(R.id.title);
        text1 = (TextView)findViewById(R.id.text1);
        text2 = (TextView)findViewById(R.id.text2);
        text3 = (TextView)findViewById(R.id.text3);
        title.startAnimation(scale1000);
        text1.startAnimation(alpha_2000_2000);
        text2.startAnimation(alpha_4000_2000);
        text3.startAnimation(alpha_4000_2000);
    }

    public void onClick_login(View v){
        MainActivity MA = (MainActivity)MainActivity._Main_Activity;
        if(MA!=null){
            MA.finish();
        }
        Intent intent = new Intent(this, SignActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }

    public void onBackPressed() {
        //finish();
    }
}
