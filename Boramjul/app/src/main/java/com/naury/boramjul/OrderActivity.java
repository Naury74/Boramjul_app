package com.naury.boramjul;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderActivity extends AppCompatActivity {

    ArrayList<BookListItem> cart_list;

    UserInfo userInfo;

    TextView total_price, final_price, item_count, discount_price, point_discount_title, point_having_price;
    EditText order_name_input,name_input,address_num_input,address_input,address_detail_input,ph_num_input,request_input, point_discount_price;
    ImageView point_discount_check;

    RadioGroup pay_input,easy_pay_select;

    CartListAdapter adapter;
    RecyclerView listView;

    CheckBox confirm_check;

    LinearLayout having_point;

    int point_val = 0;
    int total_point_val = 0;

    private WebView webView;
    private WebSettings mWebSettings;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        userInfo = new UserInfo();

        cart_list = new ArrayList<BookListItem>();
        cart_list = ReadData();

        pay_input = (RadioGroup)findViewById(R.id.pay_input);
        easy_pay_select = (RadioGroup)findViewById(R.id.easy_pay_select);
        total_price = (TextView)findViewById(R.id.total_price);
        final_price = (TextView)findViewById(R.id.final_price);
        item_count = (TextView)findViewById(R.id.item_count);
        discount_price = (TextView)findViewById(R.id.discount_price);
        point_discount_title = (TextView)findViewById(R.id.point_discount_title);
        confirm_check = (CheckBox)findViewById(R.id.confirm_check);
        point_discount_price = (EditText)findViewById(R.id.point_discount_price);
        point_having_price = (TextView)findViewById(R.id.point_having_price);

        point_discount_check = (ImageView)findViewById(R.id.point_discount_check);

        order_name_input = (EditText)findViewById(R.id.order_name_input);
        name_input = (EditText)findViewById(R.id.name_input);
        address_num_input = (EditText)findViewById(R.id.address_num_input);
        address_input = (EditText)findViewById(R.id.address_input);
        address_detail_input = (EditText)findViewById(R.id.address_detail_input);
        ph_num_input = (EditText)findViewById(R.id.ph_num_input);
        request_input = (EditText)findViewById(R.id.request_input);

        having_point = (LinearLayout)findViewById(R.id.having_point);

        adapter = new CartListAdapter(OrderActivity.this,R.layout.order_list_item);

        listView = (RecyclerView) findViewById(R.id.Best_list_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(OrderActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(layoutManager);

        listView.setAdapter(adapter);
        listView.setVisibility(View.VISIBLE);
        adapter.addAll(cart_list);

        DecimalFormat format = new DecimalFormat("###,###");
        total_price.setText(format.format(adapter.getTotalPrice())+"원");
        final_price.setText(format.format(adapter.getTotalPrice())+"원");

        item_count.setText("총 "+adapter.getItemCount()+"가지 상품");

        ph_num_input.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        pay_input.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.method_card){
                    easy_pay_select.setVisibility(View.GONE);
                } else if (i ==R.id.method_other) {
                    easy_pay_select.setVisibility(View.VISIBLE);
                }
            }
        });

        order_name_input.setText(userInfo.getName());
        name_input.setText(userInfo.getName());
        ph_num_input.setText(userInfo.getPh_num());

//        String address_result = userInfo.getAddress();
//        String address_num = substringBetween(address_result, "(", ")");
//        address_num_input.setText(address_num);
//        address_input.setText(address_result.substring(address_result.indexOf(")")+1));

        if(userInfo.getRank().equals("브론즈")){
            total_price.setText(format.format(adapter.getTotalPrice())+"원");
            discount_price.setText(format.format((adapter.getTotalPrice()/100)*5)+"원");
            final_price.setText(format.format(adapter.getTotalPrice()-((adapter.getTotalPrice()/100)*5))+"원");
        }else if(userInfo.getRank().equals("실버")){
            total_price.setText(format.format(adapter.getTotalPrice())+"원");
            discount_price.setText(format.format((adapter.getTotalPrice()/100)*5)+"원");
            final_price.setText(format.format(adapter.getTotalPrice()-((adapter.getTotalPrice()/100)*10))+"원");
        }else if(userInfo.getRank().equals("골드")){
            total_price.setText(format.format(adapter.getTotalPrice())+"원");
            discount_price.setText(format.format((adapter.getTotalPrice()/100)*5)+"원");
            final_price.setText(format.format(adapter.getTotalPrice()-((adapter.getTotalPrice()/100)*15))+"원");
        }
    }

    public void onClick_back(View v){
        finish();
    }

    boolean point_select = false;

    public void onClick_point_use(View v){
        if(point_select){
            point_discount_check.setImageResource(R.drawable.ic_check);
            point_discount_title.setTextColor(Color.parseColor("#CDCDCD"));
            point_having_price.setText("0원");
            point_discount_price.setVisibility(View.GONE);
            point_having_price.setVisibility(View.GONE);
            DecimalFormat format = new DecimalFormat("###,###");
            final_price.setText(format.format(adapter.getTotalPrice()-((adapter.getTotalPrice()/100)*15))+"원");
            having_point.setVisibility(View.GONE);
            point_select = false;
        }else {
            point_discount_check.setImageResource(R.drawable.ic_check_purple);
            point_discount_title.setTextColor(Color.parseColor("#000000"));
            point_discount_price.setVisibility(View.VISIBLE);
            having_point.setVisibility(View.VISIBLE);
            point_discount_price.setText("0");
            point_val = 0;
            total_point_val = Integer.parseInt(userInfo.getPoint());
            DecimalFormat format = new DecimalFormat("###,###");
            point_having_price.setText(format.format(total_point_val)+"원");
            point_discount_price.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(point_discount_price.getText().toString().equals("")){

                    }else {
                        point_val = Integer.parseInt(point_discount_price.getText().toString());
                        if(point_val>total_point_val){
                            Toast.makeText(OrderActivity.this, "보유 포인트를 초과하여 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                            point_discount_price.setText("0");
                            point_val=0;
                        }else {
                            if(point_val>=(adapter.getTotalPrice()-((adapter.getTotalPrice()/100)*5))){
                                DecimalFormat format = new DecimalFormat("###,###");
                                final_price.setText(0+"원");
                            }else {
                                DecimalFormat format = new DecimalFormat("###,###");
                                final_price.setText(format.format(adapter.getTotalPrice()-((adapter.getTotalPrice()/100)*15)-point_val)+"원");
                            }
                        }
                    }
                }
            });
            point_having_price.setVisibility(View.VISIBLE);
            having_point.setVisibility(View.VISIBLE);
            point_select = true;
        }
    }

    public void onClick_search_address(View v){
        init_webView();
        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();
    }

    public void onClick_next(View v){
        if(!order_name_input.getText().toString().equals("")&&!name_input.getText().toString().equals("")&&!address_num_input.getText().toString().equals("")&&!address_input.getText().toString().equals("")&&!ph_num_input.getText().toString().equals("")){
            if(confirm_check.isChecked()){

            }else {
                Toast.makeText(this, "주문내역 확인에 체크해 주세요", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "주문정보를 모두 입력해 주세요", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<BookListItem> ReadData() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("MyCart", "EMPTY");
        ArrayList<BookListItem> arrayList;
        if(json.equals("EMPTY")){
            arrayList = new ArrayList<BookListItem>();
        }else{
            Type type = new TypeToken<ArrayList<BookListItem>>() {
            }.getType();
            arrayList = gson.fromJson(json, type);
        }
        return arrayList;
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
            WebView newWebView = new WebView(OrderActivity.this);
            WebSettings webSettings = newWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            final Dialog dialog = new Dialog(OrderActivity.this);
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

                    String address_result = String.format("(%s) %s %s", arg1, arg2, arg3);
                    String address_num = substringBetween(address_result, "(", ")");
                    address_num_input.setText(address_num);
                    address_input.setText(address_result.substring(address_result.indexOf(")")+1));
                    Log.d("Web_TAG","리턴 주소: "+String.format("(%s) %s %s", arg1, arg2, arg3));

                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    //init_webView();
                }
            });
        }
    }

    private String substringBetween(String str, String open, String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        int start = str.indexOf(open);
        if (start != -1) {
            int end = str.indexOf(close, start + open.length());
            if (end != -1) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }

}
