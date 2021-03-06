package com.naury.boramjul.Activity;

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
import com.naury.boramjul.DTO.BookListItem;
import com.naury.boramjul.Adapter.CartListAdapter;
import com.naury.boramjul.Post;
import com.naury.boramjul.R;
import com.naury.boramjul.DTO.UserInfo;
import com.naury.boramjul.Retrofit.JsonPlaceHolderApi;
import com.naury.boramjul.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    int order_price = 0;
    int discount_membership = 0;
    String pay_method = "????????????";

    private WebView webView;
    private WebSettings mWebSettings;

    private Handler handler;

    private JsonPlaceHolderApi jsonPlaceHolderApi;

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
        total_price.setText(format.format(adapter.getTotalPrice())+"???");
        final_price.setText(format.format(adapter.getTotalPrice())+"???");

        item_count.setText("??? "+adapter.getItemCount()+"?????? ??????");

        ph_num_input.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        pay_input.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.method_card){
                    easy_pay_select.setVisibility(View.GONE);
                    pay_method = "????????????";
                } else if (i ==R.id.method_other) {
                    easy_pay_select.setVisibility(View.VISIBLE);
                }
            }
        });

        easy_pay_select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.kakao_pay){
                    pay_method = "???????????????";
                } else if (i ==R.id.naver_pay) {
                    pay_method = "???????????????";
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

        order_price = adapter.getTotalPrice();

        if(userInfo.getRank().equals("?????????")){
            total_price.setText(format.format(adapter.getTotalPrice())+"???");
            discount_price.setText(format.format((adapter.getTotalPrice()/100)*5)+"???");
            final_price.setText(format.format(adapter.getTotalPrice()-((adapter.getTotalPrice()/100)*5))+"???");
            discount_membership = (adapter.getTotalPrice()/100)*5;
        }else if(userInfo.getRank().equals("??????")){
            total_price.setText(format.format(adapter.getTotalPrice())+"???");
            discount_price.setText(format.format((adapter.getTotalPrice()/100)*10)+"???");
            final_price.setText(format.format(adapter.getTotalPrice()-((adapter.getTotalPrice()/100)*10))+"???");
            discount_membership = (adapter.getTotalPrice()/100)*10;
        }else if(userInfo.getRank().equals("??????")){
            total_price.setText(format.format(adapter.getTotalPrice())+"???");
            discount_price.setText(format.format((adapter.getTotalPrice()/100)*15)+"???");
            final_price.setText(format.format(adapter.getTotalPrice()-((adapter.getTotalPrice()/100)*15))+"???");
            discount_membership = (adapter.getTotalPrice()/100)*15;
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
            point_having_price.setText("0???");
            point_discount_price.setVisibility(View.GONE);
            point_having_price.setVisibility(View.GONE);
            DecimalFormat format = new DecimalFormat("###,###");
            final_price.setText(format.format(order_price-discount_membership)+"???");
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
            point_having_price.setText(format.format(total_point_val)+"???");
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
                            Toast.makeText(OrderActivity.this, "?????? ???????????? ???????????? ????????? ??? ????????????.", Toast.LENGTH_SHORT).show();
                            point_discount_price.setText("0");
                            point_val=0;
                        }else {
                            if(point_val>=(adapter.getTotalPrice()-((adapter.getTotalPrice()/100)*5))){
                                DecimalFormat format = new DecimalFormat("###,###");
                                final_price.setText(0+"???");
                            }else {
                                DecimalFormat format = new DecimalFormat("###,###");
                                if(userInfo.getRank().equals("?????????")){
                                    final_price.setText(format.format(adapter.getTotalPrice()-((adapter.getTotalPrice()/100)*5)-point_val)+"???");
                                }else if(userInfo.getRank().equals("??????")){
                                    final_price.setText(format.format(adapter.getTotalPrice()-((adapter.getTotalPrice()/100)*10)-point_val)+"???");
                                }else if(userInfo.getRank().equals("??????")){
                                    final_price.setText(format.format(adapter.getTotalPrice()-((adapter.getTotalPrice()/100)*15)-point_val)+"???");
                                }
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
        // ???????????? ?????? JavaScript ????????? ??????
        handler = new Handler();
    }

    public void onClick_next(View v){
        if(!order_name_input.getText().toString().equals("")&&!name_input.getText().toString().equals("")&&!address_num_input.getText().toString().equals("")&&!address_input.getText().toString().equals("")&&!ph_num_input.getText().toString().equals("")){
            if(confirm_check.isChecked()){
                createPost();
            }else {
                Toast.makeText(this, "???????????? ????????? ????????? ?????????", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "??????????????? ?????? ????????? ?????????", Toast.LENGTH_SHORT).show();
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
                    Log.d("Web_TAG","?????? ??????: "+String.format("(%s) %s %s", arg1, arg2, arg3));

                    // WebView??? ????????? ??????????????? ???????????? ??? ??????
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

    private void createPost() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.boramjul.kro.kr/test/mypage/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<Post> call = jsonPlaceHolderApi.createPost(Make_Object());

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    Log.d("CON_RESULT","code: " + response.code());
                    Toast.makeText(OrderActivity.this, "code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Post postResponse = response.body();

                String content = "";
                content += "Code : " + response.code() + "\n";
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(OrderActivity.this, "fail: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String Make_Object(){

        JSONObject jsonObject = new JSONObject();
        int pay_price = 0;

        try {
            jsonObject.put("email",userInfo.getEmail());
            jsonObject.put("name",order_name_input.getText().toString());
            jsonObject.put("phone",ph_num_input.getText().toString());
            jsonObject.put("totalprice",order_price);
            jsonObject.put("saleprice",discount_membership);
            if(point_select){
                jsonObject.put("usereserves",point_val);
                jsonObject.put("payprice",order_price-discount_membership-point_val);
                pay_price = order_price-discount_membership-point_val;
            }else {
                jsonObject.put("usereserves",0);
                jsonObject.put("payprice",order_price-discount_membership);
                pay_price = order_price-discount_membership;
            }
            jsonObject.put("addreserves",(pay_price/100));
            jsonObject.put("pay",pay_method);
            jsonObject.put("address",address_num_input.getText().toString()+address_input.getText().toString()+address_detail_input.getText().toString());
            jsonObject.put("request",request_input.getText().toString());
            jsonObject.put("list",Make_List_Object());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String order_data = jsonObject.toString();
        Log.d("ORDER_POST","Order_Post_json: "+order_data);

        return order_data;

    }

    public JSONArray Make_List_Object(){

        JSONArray jsonArray = new JSONArray();

        try {
            ArrayList<BookListItem> cart_list = adapter.getList();
            for(BookListItem item : cart_list){
                JSONObject jsonObject = new JSONObject();
                String prodnum = item.getProdnum();
                //jsonObject.put("prodnum",prodnum);
                jsonObject.put("name",item.getTitle());
                jsonObject.put("image",item.getThumbnail());
                jsonObject.put("quantity",item.getCount());
                jsonObject.put("price",item.getPrice());
                String temp_price = item.getPrice().replaceAll("[^\\d]","");
                int temp_price_num = Integer.parseInt(temp_price);
                jsonObject.put("price",temp_price_num*item.getCount());
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String order_data = jsonArray.toString();
        Log.d("ORDER_CHECK","Order_list_json: "+order_data);

        return jsonArray;
    }


}
