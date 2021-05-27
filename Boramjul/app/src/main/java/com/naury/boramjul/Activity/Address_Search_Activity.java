package com.naury.boramjul.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.naury.boramjul.R;

public class Address_Search_Activity extends AppCompatActivity {

    private WebView webView;
    private WebSettings mWebSettings;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_search);

        init_webView();
        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//뒤로가기 버튼 이벤트
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {//웹뷰에서 뒤로가기 버튼을 누르면 뒤로가짐
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
            WebView newWebView = new WebView(Address_Search_Activity.this);
            WebSettings webSettings = newWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            final Dialog dialog = new Dialog(Address_Search_Activity.this);
            dialog.setContentView(newWebView);
            dialog.setCanceledOnTouchOutside(false);
            ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
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
                    //txt_address.setText(String.format("(%s) %s %s", arg1, arg2, arg3));
                    Log.d("Web_TAG","리턴 주소: "+String.format("(%s) %s %s", arg1, arg2, arg3));

                    finish();

                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    //init_webView();
                }
            });
        }
    }

    public void onClick_back(View v){
        finish();
    }

    public void onBackPressed() {
        finish();
    }
}
