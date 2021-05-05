package com.naury.boramjul;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    TextView birthday_input, email_check, phone_check, code_check, pw_check, re_pw_check, btn_confirm, pw_notice_text, email_check_bar, phone_check_bar, pw_check_bar, re_pw_check_bar;
    ConstraintLayout main_background_Layout, code_input_layout, phone_input_layout, email_input_layout, pw_layout, re_pw_layout, verify_result_layout,address_layout;
    RadioGroup gender_input;
    EditText name_input;
    EditText phone_input;
    EditText code_input;
    EditText id_input;
    EditText pw_input;
    EditText re_pw_input;
    EditText email_input;
    TextView address_input;

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

    int gender_value = 1;
    int birthday_value = 0;
    String address_string = "";

    private WebView webView;
    private WebSettings mWebSettings;

    private Handler handler;

    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_verify);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        userInfo = new UserInfo();

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
        address_layout = (ConstraintLayout)findViewById(R.id.address_layout);
        name_input = (EditText)findViewById(R.id.name_input);
        phone_input = (EditText)findViewById(R.id.phone_input);
        code_input = (EditText)findViewById(R.id.code_input);
        id_input = (EditText)findViewById(R.id.id_input);
        pw_input = (EditText)findViewById(R.id.pw_input);
        address_input = (TextView) findViewById(R.id.address_input);
        re_pw_input = (EditText)findViewById(R.id.re_pw_input);
        email_input = (EditText)findViewById(R.id.email_input);
        email_check = (TextView)findViewById(R.id.email_check);
        phone_check = (TextView)findViewById(R.id.phone_check);
        email_check_bar = (TextView)findViewById(R.id.email_check_bar);
        phone_check_bar = (TextView)findViewById(R.id.phone_check_bar);
        code_check = (TextView)findViewById(R.id.code_check);
        pw_check = (TextView)findViewById(R.id.pw_check);
        re_pw_check = (TextView)findViewById(R.id.re_pw_check);
        pw_check_bar = (TextView)findViewById(R.id.pw_check_bar);
        re_pw_check_bar = (TextView)findViewById(R.id.re_pw_check_bar);
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
                    email_check_bar.setVisibility(View.VISIBLE);
                    check_email = 0;
                }
                else{
                    email_check.setText("O");
                    email_check_bar.setVisibility(View.GONE);//에러 메세지 제거
                    check_email = 1;
                }
                Enable_btn();
            }// afterTextChanged()..
        });

        phone_input.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

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
                    phone_check_bar.setVisibility(View.GONE);
                    check_phone = 1;
                }else{
                    phone_check.setText("");
                    phone_check_bar.setVisibility(View.VISIBLE);
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
                pw_check_bar.setVisibility(View.GONE);

                if(!Pattern.matches("^(?=.{8,20}$).*", pw_input.getText().toString())){
                    pw_check.setText("");
                    pw_check_bar.setVisibility(View.VISIBLE);
                    check_pw = 0;
                }
                if(!Pattern.matches("^(?=\\S*[A-Z]).*$", pw_input.getText().toString())){
                    pw_check.setText("");
                    pw_check_bar.setVisibility(View.VISIBLE);
                    check_pw = 0;
                }
                if(!Pattern.matches("^(?=\\S*\\d).*$", pw_input.getText().toString())){
                    pw_check.setText("");
                    pw_check_bar.setVisibility(View.VISIBLE);
                    check_pw = 0;
                }
                if(!Pattern.matches("^(?=.*\\W)(?=.*\\S+$).*$", pw_input.getText().toString())){
                    pw_check.setText("");
                    pw_check_bar.setVisibility(View.VISIBLE);
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
                    re_pw_check_bar.setVisibility(View.VISIBLE);
                    check_re_pw = 0;
                }
                else{
                    re_pw_check.setText("O");
                    re_pw_check_bar.setVisibility(View.GONE);
                    check_re_pw = 1;
                }
                Enable_btn();
            }// afterTextChanged()..
        });

        gender_input.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.rd_gender_f){
                    gender_value = 2;
                } else if (i ==R.id.rd_gender_m) {
                    gender_value = 1;
                }
            }
        });

        if(userInfo.getLogin_type()==2|userInfo.getLogin_type()==3|userInfo.getLogin_type()==4){
            email_input.setText(userInfo.getEmail());
            email_check.setVisibility(View.VISIBLE);
            email_input.setFocusable(false);
            email_input.setClickable(false);
        }
    }

    public void Enable_btn(){

        if(userInfo.getLogin_type()==1){
            if(check_email==1&&check_pw==1&&check_re_pw==1&&check_code==1&&check_name==1&&check_phone==1&&check_id==1&&check_birth==1){
                //모두 기입
                btn_confirm.setVisibility(View.VISIBLE);
            }else {
                //빠진것 존재
                btn_confirm.setVisibility(View.GONE);
            }
        }else {
            if(check_email==1&&check_code==1&&check_name==1&&check_phone==1&&check_birth==1){
                //모두 기입
                btn_confirm.setVisibility(View.VISIBLE);
            }else {
                //빠진것 존재
                btn_confirm.setVisibility(View.GONE);
            }
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
            if(userInfo.getLogin_type()==1){
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
                //address_layout.setVisibility(View.VISIBLE);
            }else {
                name_input.setVisibility(View.GONE);
                birthday_input.setVisibility(View.GONE);
                gender_input.setVisibility(View.GONE);
                email_input_layout.setVisibility(View.GONE);
                phone_input_layout.setVisibility(View.GONE);
                code_input_layout.setVisibility(View.GONE);
                verify_result_layout.setVisibility(View.VISIBLE);
                //address_layout.setVisibility(View.VISIBLE);
                btn_confirm.setVisibility(View.VISIBLE);
            }
        }else {
            check_code = 0;
            Toast.makeText(PersonVerifyActivity.this, "인증번호가 틀립니다. 다시 확인해 주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick_Complete(View v){

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id",id_input.getText().toString());
            jsonObject.put("passwd",pw_input.getText().toString());
            jsonObject.put("name",name_input.getText().toString());
            jsonObject.put("email",email_input.getText().toString());
            jsonObject.put("phone",phone_input.getText().toString());
            jsonObject.put("sns",userInfo.getLogin_type());
            //jsonObject.put("gender",gender_value);
            jsonObject.put("birth",Integer.toString(birthday_value));
            //jsonObject.put("address",address_input.getText().toString());
            jsonObject.put("rank",1);
            jsonObject.put("totalprice",0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String join_data = jsonObject.toString();

        InsertData insertData = new InsertData();
        insertData.execute(join_data);

    }

    public void onClick_search_address(View v){
        init_webView();
        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();
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
                birthday_value = Integer.parseInt(year_v+""+(month_v+1)+""+day_v);
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

    String result_check_json;
    //로그인 시에 서버에 로그인 정보 넘겨주고 리턴값 받음
    class InsertData extends AsyncTask<String, Void, String> {

        CustomAnimationLoadingDialog customAnimationLoadingDialog = new CustomAnimationLoadingDialog(PersonVerifyActivity.this);

        @Override
        protected void onPreExecute() {
            customAnimationLoadingDialog.show();
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            customAnimationLoadingDialog.dismiss();
            super.onPostExecute(result);
            Log.d("Login_TAG", "Data Post - App : " + result);

            Intent intent = new Intent(PersonVerifyActivity.this, CompleteSignUpActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            finish();

        }


        @Override
        protected String doInBackground(String... params) {


            String serverURL = "http://www.boramjul.kro.kr/member/androidsignup.do";//서버주소 할당
            String postParameters = (String)params[0];//전송할 파라미터,값
            Log.d("Login_TAG","postParameters : "+postParameters);

            try {

                URL url = new URL(serverURL);//주소입력
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);//5초내 무 응답시 예외처리
                httpURLConnection.setConnectTimeout(5000);//5초내 연결 불가시 예외처리
                httpURLConnection.setRequestMethod("POST");//post방식 요청
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));//전송할 데이터 할당
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();//응답
                Log.d("Login_TAG", "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {//정상응답
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();//에러
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");//수신값 저장
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                System.out.println("json 리턴: " + sb.toString());
                bufferedReader.close();

                result_check_json = sb.toString();//수신된 데이터 스트링으로 변환
                ReturnCheck();
                return sb.toString();


            } catch (Exception e) {

                Log.d("Login_TAG", "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }

    protected void ReturnCheck(){//리턴 데이터 확인
        try{
            JSONObject jsonObject = new JSONObject(result_check_json);


        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void init_webView() {
        // WebView 설정
        webView = (WebView) findViewById(R.id.webView_address);

        webView.setWebChromeClient(new HelloWebChromeClient());
//        mWebSettings = webView.getSettings();
//        mWebSettings.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
//        mWebSettings.setSupportMultipleWindows(true); // 새창 띄우기 허용 여부
//        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
//        mWebSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
//        mWebSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
//        mWebSettings.setSupportZoom(false); // 화면 줌 허용 여부
//        mWebSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
//        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
//        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
//        mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부
        webView.getSettings().setJavaScriptEnabled(true);

        // JavaScript의 window.open 허용
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);

        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        webView.addJavascriptInterface(new AndroidBridge(), "TestApp");
        webView.setWebViewClient(new WebViewClientClass());
        // webview url load. php 파일 주소
        webView.loadUrl("http://www.boramjul.kro.kr/111/postsearch.html");

    }

    private class WebViewClientClass extends WebViewClient {//페이지 이동
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("check URL",url);
            view.loadUrl(url);
            return true;
        }

    }

    public class HelloWebChromeClient extends WebChromeClient {

        @SuppressLint("SetJavaScriptEnabled")
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            WebView newWebView = new WebView(PersonVerifyActivity.this);
            WebSettings webSettings = newWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            final Dialog dialog = new Dialog(PersonVerifyActivity.this);
            dialog.setContentView(newWebView);
            dialog.setCanceledOnTouchOutside(false);
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = size.y;
            dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
            dialog.show();
            newWebView.setWebChromeClient(new WebChromeClient() {
                @Override public void onCloseWindow(WebView window) {
                    dialog.dismiss();
                }
            });
            ((WebView.WebViewTransport)resultMsg.obj).setWebView(newWebView);
            resultMsg.sendToTarget();
            return true;
        }

    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    address_input.setText(String.format("(%s) %s %s", arg1, arg2, arg3));
                    Log.d("Web_TAG","리턴 주소: "+String.format("(%s) %s %s", arg1, arg2, arg3));

                    Enable_btn();
                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    //init_webView();
                }
            });
        }
    }

}
