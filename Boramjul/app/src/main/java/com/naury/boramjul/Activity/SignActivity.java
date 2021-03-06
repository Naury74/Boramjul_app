package com.naury.boramjul.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;
import com.naury.boramjul.CustomAnimationLoadingDialog;
import com.naury.boramjul.Model__CheckAlready;
import com.naury.boramjul.R;
import com.naury.boramjul.DTO.UserInfo;
import com.naury.boramjul.Retrofit.CallRetrofit;
import com.naury.boramjul.Retrofit.RetrofitClient;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = null;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private SignInButton signInButton;

    CustomAnimationLoadingDialog customAnimationLoadingDialog;

    OAuthLogin oAuthLogin;

    private ISessionCallback sessionCallback;
    Session session;

    UserInfo userInfo;

    String get_id = "";
    String get_pw = "";

    boolean result_check = false;

    int request_type = 0;
    String sns_mail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        signInButton = findViewById(R.id.signInButton);
        mAuth = FirebaseAuth.getInstance();
        customAnimationLoadingDialog = new CustomAnimationLoadingDialog(SignActivity.this);

        userInfo = new UserInfo();

        sessionCallback = new ISessionCallback() {
            @Override
            public void onSessionOpened() {
                requestMe();
            }

            @Override
            public void onSessionOpenFailed(KakaoException exception) {
                Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("557406782955-u5q6t763nvueu24sqpnmjqs9h52j4ug0.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if (mAuth.getCurrentUser() != null) {
//            Log.d("TAG","????????? ??????: "+mAuth.getCurrentUser().getDisplayName());//get+????????? ????????? ?????? ?????? ?????? ??? ??????
//            Intent intent = new Intent(getApplication(), MainActivity.class);
//            userInfo.setLogin_type(2);
//            userInfo.setName(mAuth.getCurrentUser().getEmail());
//            startActivity(intent);
//            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//            finish();
        }

        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        getHashKey();
        apply_account();
    }

    private void signIn() {
        customAnimationLoadingDialog.show();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void store_account(String email, String pw){
        SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();

        editor.putString("email", email);
        editor.putString("pw", pw);

        editor.commit();
    }

    private void apply_account(){
        SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);

        String email = pref.getString("email","");
        String pw = pref.getString("pw","");

        if(email.equals("")||pw.equals("")){

        }else {
            get_id = email;
            get_pw = pw;

            InsertData task = new InsertData();
            task.execute("1");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // ?????? ?????? ??????
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // ????????????|????????? ??????????????? ?????? ????????? ????????? SDK??? ??????

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
            }
        }

        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        customAnimationLoadingDialog.dismiss();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("GOOGLE_TAG","Authentication Successed.");
                            //Snackbar.make(findViewById(R.id.layout_main), "Authentication Successed.", Snackbar.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("GOOGLE_TAG","Authentication Failed.");
                            //Snackbar.make(findViewById(R.id.layout_main), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) { //update ui code here
        if (user != null) {
            Log.d("TAG","?????? ????????? ??????: "+user.getDisplayName());
            Log.d("TAG","?????? ????????? ??????: "+user.getPhoneNumber());
            sns_mail = user.getEmail();
            InsertData task = new InsertData();
            task.execute("2");

        }
    }

    public void onClick_SignUp(View v) {
        userInfo.setLogin_type(1);
        Intent intent = new Intent(SignActivity.this, TOS_Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void onClick_kakaoLogin(View v) {
        session.open(AuthType.KAKAO_LOGIN_ALL, SignActivity.this);
    }

    public void onClick_NaverLogin(View v) {
        oAuthLogin = OAuthLogin.getInstance();
        oAuthLogin.init(
                this
                ,getString(R.string.naver_client_id)
                ,getString(R.string.naver_client_secret)
                ,getString(R.string.naver_client_name)
                //,OAUTH_CALLBACK_INTENT
                // SDK 4.1.4 ??????????????? OAUTH_CALLBACK_INTENT????????? ???????????? ????????????.
        );

        @SuppressLint("HandlerLeak")
        OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
            @Override
            public void run(boolean success) {
                if (success) {
                    String accessToken = oAuthLogin.getAccessToken(SignActivity.this);
                    String refreshToken = oAuthLogin.getRefreshToken(SignActivity.this);
                    long expiresAt = oAuthLogin.getExpiresAt(SignActivity.this);
                    String tokenType = oAuthLogin.getTokenType(SignActivity.this);

                    Log.i("LoginData","accessToken : "+ accessToken);
                    Log.i("LoginData","refreshToken : "+ refreshToken);
                    Log.i("LoginData","expiresAt : "+ expiresAt);
                    Log.i("LoginData","tokenType : "+ tokenType);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String header = "Bearer " +  accessToken;
                            Map<String, String> requestHeaders = new HashMap<>();
                            requestHeaders.put("Authorization", header);
                            String apiURL = "https://openapi.naver.com/v1/nid/me"; //????????? ???????????? ??????????????? ????????? ??????
                            String responseBody = get(apiURL,requestHeaders);
                            Log.d("responseBody ?????? ",responseBody); //????????? ?????? ???????????? (?????????)
                            NaverUserInfo(responseBody);

                        }
                    }).start();

                } else {
                    String errorCode = oAuthLogin
                            .getLastErrorCode(SignActivity.this).getCode();
                    String errorDesc = oAuthLogin.getLastErrorDesc(SignActivity.this);
                    Toast.makeText(SignActivity.this, "errorCode:" + errorCode
                            + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
                }
            };
        };

        oAuthLogin.startOauthLoginActivity(SignActivity.this, mOAuthLoginHandler);
    }

    public void onClick_login(View v){
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SignActivity.this,R.style.BottomSheetDialogTheme);
        final View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.login_bottomsheet_layout,(LinearLayout)findViewById(R.id.container_bottom_sheet));
        bottomSheetDialog.setCanceledOnTouchOutside(false);

        final EditText id_input, pw_input;
        id_input = bottomSheetView.findViewById(R.id.id_input);
        pw_input = bottomSheetView.findViewById(R.id.pw_input);

        bottomSheetView.findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!id_input.getText().toString().equals("")&&!pw_input.getText().toString().equals("")){

                    get_id = id_input.getText().toString();
                    get_pw = pw_input.getText().toString();

                    InsertData task = new InsertData();
                    task.execute("1");

                    bottomSheetDialog.dismiss();
                }else {
                    Toast.makeText(SignActivity.this, "????????? ?????? ??????????????? ????????? ?????????", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomSheetView.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetView.findViewById(R.id.container_bottom_sheet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(id_input.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(pw_input.getWindowToken(), 0);
                id_input.setCursorVisible(false);
                pw_input.setCursorVisible(false);
            }
        });

        id_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b==true){
                    id_input.setCursorVisible(true);
                }
            }
        });

        pw_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b==true){
                    pw_input.setCursorVisible(true);
                }
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    public void requestMe() {
        UserManagement.getInstance()
                .me(new MeV2ResponseCallback() {
                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "????????? ?????? ??????: " + errorResult);
                    }

                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "????????? ?????? ?????? ??????: " + errorResult);
                    }

                    @Override
                    public void onSuccess(MeV2Response result) {
                        Log.i("KAKAO_API", "????????? ?????????: " + result.getId());

                        UserAccount kakaoAccount = result.getKakaoAccount();
                        if (kakaoAccount != null) {

                            // ?????????
                            String email = kakaoAccount.getEmail();
                            String ph_num = kakaoAccount.getPhoneNumber();

                            if (email != null) {
                                Log.i("KAKAO_API", "email: " + email);
                                Log.i("KAKAO_API", "phone: " + ph_num);

                            } else if (kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE) {
                                // ?????? ?????? ??? ????????? ?????? ??????
                                // ???, ?????? ????????? ???????????? ????????? ????????? ?????? ???????????? ????????? ????????? ????????? ???????????? ???????????? ?????????.

                            } else {
                                // ????????? ?????? ??????
                            }

                            // ?????????
                            Profile profile = kakaoAccount.getProfile();

                            if (profile != null) {
                                Log.d("KAKAO_API", "nickname: " + profile.getNickname());
                                Log.d("KAKAO_API", "profile image: " + profile.getProfileImageUrl());
                                Log.d("KAKAO_API", "thumbnail image: " + profile.getThumbnailImageUrl());

                                sns_mail = email;
                                InsertData task = new InsertData();
                                task.execute("4");

                            } else if (kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE) {
                                // ?????? ?????? ??? ????????? ?????? ?????? ??????

                            } else {
                                // ????????? ?????? ??????
                            }
                        }
                    }
                });
    }

    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

    String result_check_json;
    //????????? ?????? ????????? ????????? ?????? ???????????? ????????? ??????
    class InsertData extends AsyncTask<String, Void, String> {

        //CustomAnimationLoadingDialog customAnimationLoadingDialog = new CustomAnimationLoadingDialog(SignActivity.this);

        @Override
        protected void onPreExecute() {
            //customAnimationLoadingDialog.show();
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            //customAnimationLoadingDialog.dismiss();
            super.onPostExecute(result);
            Log.d("Login_TAG", "Data Post - App : " + result);

            if(result_check==true){
                Log.d("Login_TAB","????????? ??????");

                if(request_type==1){
                    Intent intent = new Intent(SignActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();
                }else if(request_type==2){//??????
                    Intent intent = new Intent(SignActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();
                }else if(request_type==3){//?????????
                    Intent intent = new Intent(SignActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();
                }else if(request_type==4){//?????????
                    Intent intent = new Intent(SignActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();
                }else {
                    Toast.makeText(SignActivity.this, "????????? ???????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                }
            }else{

                if(request_type==1){
                    Toast.makeText(SignActivity.this, "????????? ?????? ??????????????? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(LogIn_Activity.this, "???????????? ????????? ?????? ??????????????????", Toast.LENGTH_SHORT).show();
                }else if(request_type==2||request_type==3||request_type==4){//??????, ?????????, ?????????
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SignActivity.this,R.style.BottomSheetDialogTheme);
                    final View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.sns_join_alert_bottomsheet_layout,(LinearLayout)findViewById(R.id.container_bottom_sheet));
                    bottomSheetDialog.setCanceledOnTouchOutside(false);

                    bottomSheetView.findViewById(R.id.join_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(SignActivity.this, TOS_Activity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                            bottomSheetDialog.dismiss();
                        }
                    });

                    bottomSheetView.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            bottomSheetDialog.dismiss();
                        }
                    });

                    bottomSheetDialog.setContentView(bottomSheetView);
                    bottomSheetDialog.show();
                }else {
                    Toast.makeText(SignActivity.this, "????????? ???????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                }

            }
        }


        @Override
        protected String doInBackground(String... params) {

            request_type = Integer.parseInt((String)params[0]);

            String serverURL = "http://www.boramjul.kro.kr/member/memberinfojson.do";//???????????? ??????

            try {

                URL url = new URL(serverURL);//????????????
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);//5?????? ??? ????????? ????????????
                httpURLConnection.setConnectTimeout(5000);//5?????? ?????? ????????? ????????????
                httpURLConnection.setRequestMethod("POST");//post?????? ??????
                httpURLConnection.connect();

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
//            RetrofitClient retrofitConnection = new RetrofitClient();
//            Call<Model__CheckAlready> call =  retrofitConnection.server.get_data();
//            call.enqueue(new Callback<Model__CheckAlready>() {
//                @Override
//                public void onResponse(Call<Model__CheckAlready> call, Response<Model__CheckAlready> response) {
//                    if (response.isSuccessful()) {
//                        // ??????????????? ???????????? ????????? ?????????.
//                        Log.d("????????? ????????? : ", response.body().toString());
//                        Model__CheckAlready checkAlready = response.body();
//                        ReturnCheck(checkAlready);
//                    }else {
//                        Log.e("????????? ???????????? : ", "error code : " + response.code());
//                        // ????????? ????????? ????????????, ?????? ??????
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Model__CheckAlready> call, Throwable t) {
//                    Log.d("Retrofit_Sign", "onFailure: " + t.toString()); //????????? ?????? ??????
//                }
//            });
//
//            return null;
        }
    }

    protected void ReturnCheck(){//?????? ????????? ??????
        try{
            JSONArray jsonArray = new JSONArray(result_check_json);

            if(request_type==1){
                userInfo.setLogin_type(request_type);
                for (int i=0; i < jsonArray.length(); i++)
                {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        // Pulling items from the array
                        String id = jsonObject.getString("email");
                        String passwd = jsonObject.getString("passwd");
                        if(id.equals(get_id)&&passwd.equals(get_pw)){
                            if(jsonObject.getInt("sns")==1){
                                userInfo.setLogin_type(jsonObject.getInt("sns"));
                                userInfo.setName(jsonObject.getString("name"));
                                userInfo.setBirthday(jsonObject.getString("birth"));
                                //userInfo.setSex(jsonObject.getString("gender"));
                                userInfo.setEmail(jsonObject.getString("email"));
                                userInfo.setPh_num(jsonObject.getString("phone"));
                                userInfo.setAddress(jsonObject.getString("address"));
                                userInfo.setRank(jsonObject.getString("rank"));
                                userInfo.setPoint(jsonObject.getString("reserves"));
                                userInfo.setJoindate(jsonObject.getString("joindate"));
                                store_account(get_id, get_pw);
                                result_check = true;
                                break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else if(request_type==2){//??????
                userInfo.setLogin_type(request_type);
                for (int i=0; i < jsonArray.length(); i++)
                {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        // Pulling items from the array
                        if(sns_mail.equals(jsonObject.getString("email"))&&jsonObject.getInt("sns")==2){
                            userInfo.setLogin_type(jsonObject.getInt("sns"));
                            userInfo.setName(jsonObject.getString("name"));
                            userInfo.setBirthday(jsonObject.getString("birth"));
                            //userInfo.setSex(jsonObject.getString("gender"));
                            userInfo.setEmail(jsonObject.getString("email"));
                            userInfo.setPh_num(jsonObject.getString("phone"));
                            userInfo.setAddress(jsonObject.getString("address"));
                            userInfo.setRank(jsonObject.getString("rank"));
                            userInfo.setPoint(jsonObject.getString("reserves"));
                            userInfo.setJoindate(jsonObject.getString("joindate"));
                            result_check = true;
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                userInfo.setEmail(sns_mail);
            }else if(request_type==3){//?????????
                userInfo.setLogin_type(request_type);
                for (int i=0; i < jsonArray.length(); i++)
                {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        // Pulling items from the array
                        if(sns_mail.equals(jsonObject.getString("email"))&&jsonObject.getInt("sns")==3){
                            userInfo.setLogin_type(jsonObject.getInt("sns"));
                            userInfo.setName(jsonObject.getString("name"));
                            userInfo.setBirthday(jsonObject.getString("birth"));
                            //userInfo.setSex(jsonObject.getString("gender"));
                            userInfo.setEmail(jsonObject.getString("email"));
                            userInfo.setPh_num(jsonObject.getString("phone"));
                            userInfo.setAddress(jsonObject.getString("address"));
                            userInfo.setRank(jsonObject.getString("rank"));
                            userInfo.setPoint(jsonObject.getString("reserves"));
                            userInfo.setJoindate(jsonObject.getString("joindate"));
                            result_check = true;
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                userInfo.setEmail(sns_mail);

            }else if(request_type==4){//?????????
                userInfo.setLogin_type(request_type);
                for (int i=0; i < jsonArray.length(); i++)
                {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        // Pulling items from the array
                        if(sns_mail.equals(jsonObject.getString("email"))&&jsonObject.getInt("sns")==4){
                            userInfo.setLogin_type(jsonObject.getInt("sns"));
                            userInfo.setName(jsonObject.getString("name"));
                            userInfo.setBirthday(jsonObject.getString("birth"));
                            //userInfo.setSex(jsonObject.getString("gender"));
                            userInfo.setEmail(jsonObject.getString("email"));
                            userInfo.setPh_num(jsonObject.getString("phone"));
                            userInfo.setAddress(jsonObject.getString("address"));
                            userInfo.setRank(jsonObject.getString("rank"));
                            userInfo.setPoint(jsonObject.getString("reserves"));
                            userInfo.setJoindate(jsonObject.getString("joindate"));
                            result_check = true;
                            userInfo.setLogin_type(request_type);
                            userInfo.setEmail(sns_mail);
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                userInfo.setEmail(sns_mail);

            }else {

            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void NaverUserInfo(String msg){

        try {
            JSONObject jsonObject = new JSONObject(msg);
            String resultcode = jsonObject.get("resultcode").toString();
            Log.d("resultcode ?????? ",resultcode);

            String message = jsonObject.get("message").toString();
            Log.d("message ?????? ",message);

            if(resultcode.equals("00")){
                if(message.equals("success")){
                    JSONObject naverJson = (JSONObject) jsonObject.get("response");

                    String id = naverJson.get("id").toString();
                    //String nickName = naverJson.get("nickname").toString();
                    //String profileImage = naverJson.get("profile_image").toString();
                    String email = naverJson.get("email").toString();
                    String name = naverJson.get("name").toString();

                    sns_mail = email;

                    Log.d("id ?????? ",id);
                    //Log.d("nickName ?????? ",nickName);
                    //Log.d("profileImage ?????? ",profileImage);
                    Log.d("email ?????? ",email);
                    Log.d("name ?????? ",name);

                    InsertData task = new InsertData();
                    task.execute("3");
                }
            }
            else{
                //Toast.makeText(getApplicationContext(),"????????? ????????? ??????????????????.",Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // ?????? ??????
                return readBody(con.getInputStream());
            } else { // ?????? ??????
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API ????????? ?????? ??????", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            java.net.URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL??? ?????????????????????. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("????????? ??????????????????. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API ????????? ????????? ??????????????????.", e);
        }
    }
}
