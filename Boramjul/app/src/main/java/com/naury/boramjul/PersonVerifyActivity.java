package com.naury.boramjul;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Calendar;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonVerifyActivity extends AppCompatActivity {

    NotificationManager manager;
    NotificationCompat.Builder builder;
    private static String CHANNEL_ID = "boramjul_verify";
    private static String CHANEL_NAME = "BoramJul";


    DatePicker datePicker;
    int year_v = 0;
    int month_v = 0;
    int day_v = 0;

    InputMethodManager imm;

    TextView birthday_input, email_check, phone_check, code_check, pw_check, re_pw_check, btn_confirm, pw_notice_text;
    ConstraintLayout main_background_Layout, code_input_layout, phone_input_layout, email_input_layout, pw_layout, re_pw_layout, verify_result_layout;
    RadioGroup gender_input;
    EditText name_input;
    EditText phone_input;
    EditText code_input;
    EditText id_input;
    EditText pw_input;
    EditText re_pw_input;
    EditText email_input;

    int check_re_pw = 0;
    int check_birth = 0;
    int check_pw = 0;
    int check_id = 0;
    int check_code = 0;
    int check_email = 0;
    int check_phone = 0;
    int check_name = 0;

    String code_number = "";

    CustomAnimationLoadingDialog customAnimationLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_verify);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        customAnimationLoadingDialog = new CustomAnimationLoadingDialog(PersonVerifyActivity.this);

        gender_input = (RadioGroup)findViewById(R.id.gender_input);
        birthday_input = (TextView)findViewById(R.id.birthday_input);
        main_background_Layout = (ConstraintLayout)findViewById(R.id.main_background_Layout);
        code_input_layout = (ConstraintLayout)findViewById(R.id.code_input_layout);
        phone_input_layout = (ConstraintLayout)findViewById(R.id.phone_input_layout);
        email_input_layout = (ConstraintLayout)findViewById(R.id.email_input_layout);
        pw_layout = (ConstraintLayout)findViewById(R.id.pw_layout);
        re_pw_layout = (ConstraintLayout)findViewById(R.id.re_pw_layout);
        verify_result_layout = (ConstraintLayout)findViewById(R.id.verify_result_layout);
        name_input = (EditText)findViewById(R.id.name_input);
        phone_input = (EditText)findViewById(R.id.phone_input);
        code_input = (EditText)findViewById(R.id.code_input);
        id_input = (EditText)findViewById(R.id.id_input);
        pw_input = (EditText)findViewById(R.id.pw_input);
        re_pw_input = (EditText)findViewById(R.id.re_pw_input);
        email_input = (EditText)findViewById(R.id.email_input);
        email_check = (TextView)findViewById(R.id.email_check);
        phone_check = (TextView)findViewById(R.id.phone_check);
        code_check = (TextView)findViewById(R.id.code_check);
        pw_check = (TextView)findViewById(R.id.pw_check);
        re_pw_check = (TextView)findViewById(R.id.re_pw_check);
        btn_confirm = (TextView)findViewById(R.id.btn_confirm);
        pw_notice_text = (TextView)findViewById(R.id.pw_notice_text);

        Calendar cal = Calendar.getInstance();

        year_v = cal.get(Calendar.YEAR);
        month_v = cal.get(Calendar.MONTH);
        day_v = cal.get(Calendar.DAY_OF_MONTH);

        main_background_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(name_input.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(phone_input.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(code_input.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(id_input.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(pw_input.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(re_pw_input.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(email_input.getWindowToken(), 0);
            }
        });

        name_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(name_input.getText().toString().equals("")){
                    // 경고 메세지
                    check_name = 0;
                }
                else{
                    //에러 메세지 제거
                    check_name = 1;
                }
                Enable_btn();
            }// afterTextChanged()..
        });

        email_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()){
                    email_check.setText("");    // 경고 메세지
                    check_email = 0;
                }
                else{
                    email_check.setText("O");         //에러 메세지 제거
                    check_email = 1;
                }
                Enable_btn();
            }// afterTextChanged()..
        });

        phone_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isValidCellPhoneNumber(s.toString())){
                    phone_check.setText("인증");
                    check_phone = 1;
                }else{
                    phone_check.setText("");
                    check_phone = 0;
                }
                Enable_btn();
            }
        });

        code_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString().length()==6){
                    code_check.setText("확인");
                }else {
                    code_check.setText("");
                }
                Enable_btn();
            }
        });

        id_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!id_input.getText().equals("")){
                    check_id = 1;
                }else{
                    check_id = 0;
                }
                Enable_btn();
            }
        });

        pw_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                pw_check.setText("O");
                check_pw = 1;

                if(!Pattern.matches("^(?=.{8,20}$).*", pw_input.getText().toString())){
                    pw_check.setText("");
                    check_pw = 0;
                }
                if(!Pattern.matches("^(?=\\S*[A-Z]).*$", pw_input.getText().toString())){
                    pw_check.setText("");
                    check_pw = 0;
                }
                if(!Pattern.matches("^(?=\\S*\\d).*$", pw_input.getText().toString())){
                    pw_check.setText("");
                    check_pw = 0;
                }
                if(!Pattern.matches("^(?=.*\\W)(?=.*\\S+$).*$", pw_input.getText().toString())){
                    pw_check.setText("");
                    check_pw = 0;
                }
                Enable_btn();
            }// afterTextChanged()..
        });

        re_pw_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                re_pw_check.setText("O");
                if(!pw_input.getText().toString().equals(re_pw_input.getText().toString())){
                    re_pw_check.setText("");
                    check_re_pw = 0;
                }
                else{
                    re_pw_check.setText("O");
                    check_re_pw = 1;
                }
                Enable_btn();
            }// afterTextChanged()..
        });
    }

    public void Enable_btn(){

        if(check_email==1&&check_pw==1&&check_re_pw==1&&check_code==1&&check_name==1&&check_phone==1&&check_id==1&&check_birth==1){
            //모두 기입
            btn_confirm.setVisibility(View.VISIBLE);
        }else {
            //빠진것 존재
            btn_confirm.setVisibility(View.GONE);
        }

    }

    public static boolean isValidCellPhoneNumber(String cellphoneNumber) {

        boolean returnValue = false;

        Log.i("cell", cellphoneNumber);

        String regex = "^\\s*(010|011|012|013|014|015|016|017|018|019)(-|\\)|\\s)*(\\d{3,4})(-|\\s)*(\\d{4})\\s*$";

        Pattern p = Pattern.compile(regex);

        Matcher m = p.matcher(cellphoneNumber);

        if (m.matches()) {

            returnValue = true;

        }

        return returnValue;

    }

    public void onClick_RequestCode(View v){
        imm.hideSoftInputFromWindow(phone_input.getWindowToken(), 0);
        if(check_phone == 1){
            if(check_phone == 1&&check_email==1&&check_name==1&&check_birth==1){
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run(){
                        try
                        {
                            Thread.sleep(2000);
                        }
                        catch (Exception e)
                        {

                        }
                        showNoti();
                    }
                });
                thread.start();
                code_input_layout.setVisibility(View.VISIBLE);
                phone_input.setClickable(false);
                phone_input.setFocusable(false);
                phone_check.setText("재전송");
            }else {
                Toast.makeText(this, "이름, 생년월일, 이메일을 입력하신 후 인증을 진행해 주세요.", Toast.LENGTH_SHORT).show();
            }
        }else {

        }
    }

    public void onClick_VerifyCode(View v){
        imm.hideSoftInputFromWindow(code_input.getWindowToken(), 0);
        customAnimationLoadingDialog.show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run(){
                try
                {
                    Thread.sleep(1000);
                }
                catch (Exception e)
                {

                }
                customAnimationLoadingDialog.dismiss();
            }
        });
        thread.start();
        if(code_input.getText().toString().equals(code_number)){
            check_code = 1;
            name_input.setVisibility(View.GONE);
            birthday_input.setVisibility(View.GONE);
            gender_input.setVisibility(View.GONE);
            email_input_layout.setVisibility(View.GONE);
            phone_input_layout.setVisibility(View.GONE);
            code_input_layout.setVisibility(View.GONE);
            id_input.setVisibility(View.VISIBLE);
            pw_layout.setVisibility(View.VISIBLE);
            pw_notice_text.setVisibility(View.VISIBLE);
            re_pw_layout.setVisibility(View.VISIBLE);
            verify_result_layout.setVisibility(View.VISIBLE);
        }else {
            check_code = 0;
            Toast.makeText(PersonVerifyActivity.this, "인증번호가 틀립니다. 다시 확인해 주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick_Complete(View v){

        customAnimationLoadingDialog.show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run(){
                try
                {
                    Thread.sleep(1000);
                }
                catch (Exception e)
                {

                }
                customAnimationLoadingDialog.dismiss();
                Intent intent = new Intent(PersonVerifyActivity.this, CompleteSignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
            }
        });
        thread.start();

        //Toast.makeText(this, "가입 완료", Toast.LENGTH_SHORT).show();
    }

    public void onClick_back(View v){
        finish();
    }

    public void onClick_calendar(View v){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PersonVerifyActivity.this,R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.date_picker_bottomsheet_layout,(LinearLayout)findViewById(R.id.container_bottom_sheet));
        bottomSheetDialog.setCanceledOnTouchOutside(true);

        datePicker = bottomSheetView.findViewById(R.id.dataPicker);

        datePicker.setMaxDate(System.currentTimeMillis());

        datePicker.init(year_v, month_v, day_v,
                new DatePicker.OnDateChangedListener() {
                    //값이 바뀔때마다 텍스트뷰의 값을 바꿔준다.
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        //monthOfYear는 0값이 1월을 뜻하므로 1을 더해줌 나머지는 같다.

                        year_v = year; month_v = monthOfYear; day_v = dayOfMonth;
                    }
                });

        bottomSheetView.findViewById(R.id.set_date_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthday_input.setText(year_v + "년 " + (month_v+1) + "월 " + day_v + "일");
                check_birth = 1;
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    public void showNoti(){
        builder = null;
        code_number = numberGen(6,1);
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //버전 오레오 이상일 경우
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_HIGH));
        }

        builder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("보람줄 회원가입 인증")
                .setContentText("인증 코드는 "+code_number+"입니다.")
                .setSmallIcon(R.drawable.ic_logo_icon_custom_foreground)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH);
        Notification notification = builder.build();
        //알림창 실행
        manager.notify(0,notification);
    }

    public static String numberGen(int len, int dupCd ) {

        Random rand = new Random();
        String numStr = ""; //난수가 저장될 변수

        for(int i=0;i<len;i++) {

            //0~9 까지 난수 생성
            String ran = Integer.toString(rand.nextInt(10));

            if(dupCd==1) {
                //중복 허용시 numStr에 append
                numStr += ran;
            }else if(dupCd==2) {
                //중복을 허용하지 않을시 중복된 값이 있는지 검사한다
                if(!numStr.contains(ran)) {
                    //중복된 값이 없으면 numStr에 append
                    numStr += ran;
                }else {
                    //생성된 난수가 중복되면 루틴을 다시 실행한다
                    i-=1;
                }
            }
        }
        return numStr;
    }


}
