package com.naury.boramjul.Activity;

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
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
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
import com.naury.boramjul.CustomAnimationLoadingDialog;
import com.naury.boramjul.R;
import com.naury.boramjul.DTO.UserInfo;

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
                    // ?????? ?????????
                    check_name = 0;
                }
                else{
                    //?????? ????????? ??????
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
                    email_check.setText("");    // ?????? ?????????
                    email_check_bar.setVisibility(View.VISIBLE);
                    check_email = 0;
                }
                else{
                    email_check.setText("O");
                    email_check_bar.setVisibility(View.GONE);//?????? ????????? ??????
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
                    phone_check.setText("??????");
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
                    code_check.setText("??????");
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
            if(check_email==1&&check_pw==1&&check_re_pw==1&&check_code==1&&check_name==1&&check_phone==1&&check_birth==1){
                //?????? ??????
                btn_confirm.setVisibility(View.VISIBLE);
            }else {
                //????????? ??????
                btn_confirm.setVisibility(View.GONE);
            }
        }else {
            if(check_email==1&&check_code==1&&check_name==1&&check_phone==1&&check_birth==1){
                //?????? ??????
                btn_confirm.setVisibility(View.VISIBLE);
            }else {
                //????????? ??????
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
                phone_check.setText("?????????");
            }else {
                Toast.makeText(this, "??????, ????????????, ???????????? ???????????? ??? ????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
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
                //id_input.setVisibility(View.VISIBLE);
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
            Toast.makeText(PersonVerifyActivity.this, "??????????????? ????????????. ?????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick_Complete(View v){

        JSONObject jsonObject = new JSONObject();

        try {
            //jsonObject.put("id",id_input.getText().toString());
            jsonObject.put("passwd",pw_input.getText().toString());
            jsonObject.put("name",name_input.getText().toString());
            jsonObject.put("email",email_input.getText().toString());
            jsonObject.put("phone",phone_input.getText().toString());
            jsonObject.put("sns",userInfo.getLogin_type());
            //jsonObject.put("gender",gender_value);
            jsonObject.put("birth",Integer.toString(birthday_value));
            //jsonObject.put("address",address_input.getText().toString());
            //jsonObject.put("rank",1);
            //jsonObject.put("totalprice",0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String join_data = jsonObject.toString();

        InsertData insertData = new InsertData();
        insertData.execute(join_data);

    }

    public void onClick_search_address(View v){
        init_webView();
        // ???????????? ?????? JavaScript ????????? ??????
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
                    //?????? ??????????????? ??????????????? ?????? ????????????.
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        //monthOfYear??? 0?????? 1?????? ???????????? 1??? ????????? ???????????? ??????.

                        year_v = year; month_v = monthOfYear; day_v = dayOfMonth;
                    }
                });

        bottomSheetView.findViewById(R.id.set_date_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthday_input.setText(year_v + "??? " + (month_v+1) + "??? " + day_v + "???");
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
        //?????? ????????? ????????? ??????
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_HIGH));
        }

        builder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("????????? ???????????? ??????")
                .setContentText("?????? ????????? "+code_number+"?????????.")
                .setSmallIcon(R.drawable.ic_logo_icon_custom_foreground)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH);
        Notification notification = builder.build();
        //????????? ??????
        manager.notify(0,notification);
    }

    public static String numberGen(int len, int dupCd ) {

        Random rand = new Random();
        String numStr = ""; //????????? ????????? ??????

        for(int i=0;i<len;i++) {

            //0~9 ?????? ?????? ??????
            String ran = Integer.toString(rand.nextInt(10));

            if(dupCd==1) {
                //?????? ????????? numStr??? append
                numStr += ran;
            }else if(dupCd==2) {
                //????????? ???????????? ????????? ????????? ?????? ????????? ????????????
                if(!numStr.contains(ran)) {
                    //????????? ?????? ????????? numStr??? append
                    numStr += ran;
                }else {
                    //????????? ????????? ???????????? ????????? ?????? ????????????
                    i-=1;
                }
            }
        }
        return numStr;
    }

    String result_check_json;
    //????????? ?????? ????????? ????????? ?????? ???????????? ????????? ??????
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


            String serverURL = "http://www.boramjul.kro.kr/member/androidsignup.do";//???????????? ??????
            String postParameters = (String)params[0];//????????? ????????????,???
            Log.d("Login_TAG","postParameters : "+postParameters);

            try {

                URL url = new URL(serverURL);//????????????
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);//5?????? ??? ????????? ????????????
                httpURLConnection.setConnectTimeout(5000);//5?????? ?????? ????????? ????????????
                httpURLConnection.setRequestMethod("POST");//post?????? ??????
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));//????????? ????????? ??????
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();//??????
                Log.d("Login_TAG", "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {//????????????
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();//??????
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");//????????? ??????
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                System.out.println("json ??????: " + sb.toString());
                bufferedReader.close();

                result_check_json = sb.toString();//????????? ????????? ??????????????? ??????
                ReturnCheck();
                return sb.toString();


            } catch (Exception e) {

                Log.d("Login_TAG", "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }

    protected void ReturnCheck(){//?????? ????????? ??????
        try{
            JSONObject jsonObject = new JSONObject(result_check_json);


        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void init_webView() {
        // WebView ??????
        webView = (WebView) findViewById(R.id.webView_address);

        webView.setWebChromeClient(new HelloWebChromeClient());
//        mWebSettings = webView.getSettings();
//        mWebSettings.setJavaScriptEnabled(true); // ???????????? ?????????????????? ?????? ??????
//        mWebSettings.setSupportMultipleWindows(true); // ?????? ????????? ?????? ??????
//        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true); // ?????????????????? ?????? ?????????(?????????) ?????? ??????
//        mWebSettings.setLoadWithOverviewMode(true); // ???????????? ?????? ??????
//        mWebSettings.setUseWideViewPort(true); // ?????? ????????? ????????? ?????? ??????
//        mWebSettings.setSupportZoom(false); // ?????? ??? ?????? ??????
//        mWebSettings.setBuiltInZoomControls(false); // ?????? ?????? ?????? ?????? ??????
//        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // ????????? ????????? ?????????
//        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // ???????????? ?????? ?????? ??????
//        mWebSettings.setDomStorageEnabled(true); // ??????????????? ?????? ??????
        webView.getSettings().setJavaScriptEnabled(true);

        // JavaScript??? window.open ??????
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);

        // JavaScript???????????? ????????? ????????? ?????? ??? ???????????? ?????????
        webView.addJavascriptInterface(new AndroidBridge(), "TestApp");
        webView.setWebViewClient(new WebViewClientClass());
        // webview url load. php ?????? ??????
        webView.loadUrl("http://www.boramjul.kro.kr/111/postsearch.html");

    }

    private class WebViewClientClass extends WebViewClient {//????????? ??????
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
                    Log.d("Web_TAG","?????? ??????: "+String.format("(%s) %s %s", arg1, arg2, arg3));

                    Enable_btn();
                    // WebView??? ????????? ??????????????? ???????????? ??? ??????
                    //init_webView();
                }
            });
        }
    }

}
