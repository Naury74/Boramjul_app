package com.naury.boramjul;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.yalantis.phoenix.PullToRefreshView;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private BackPressCloseHendler backPressCloseHendler;//뒤로가기버튼 터치핸들러

    ScrollView mScrollView;
    PullToRefreshView mPullToRefreshView;

    //////////////////////////////// 베스트셀러용
    String bs_thumbnail;
    String bs_category;
    String bs_title;
    String bs_author;
    String bs_price;
    String bs_score_review;

    BookListImageAdapter adapter;
    ArrayList<BookListItem> bs_list;

    //////////////////////////////// 추천 배너용
    String rc_thumbnail;
    String rc_book_cover;
    String rc_title;
    String rc_author;

    AutoScrollAdapter pagerAdapter;
    ViewPager viewPager;
    ArrayList<DayBookItem> rc_list;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;

    TextView count_view;
    TextView reco_title;
    TextView reco_author;

    //////////////////////////////// 신간 도서용
    String nw_thumbnail;
    String nw_category;
    String nw_title;
    String nw_author;
    String nw_price;
    String nw_score_review;

    BookListImageAdapter new_adapter;
    ArrayList<BookListItem> nw_list;

    ///////////////////////////// Firebase 인증용
    private FirebaseAuth mAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        new SlidingRootNavBuilder(this)
                .withMenuLayout(R.layout.sliding_root_nav)
                .withToolbarMenuToggle(toolbar)
                .inject();

        viewPager = findViewById(R.id.viewPager);

        count_view = (TextView)findViewById(R.id.count_view);
        reco_title = (TextView)findViewById(R.id.reco_title);
        reco_author = (TextView)findViewById(R.id.reco_author);

        mScrollView = findViewById(R.id.scrollView);

        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);

        backPressCloseHendler = new BackPressCloseHendler(this);//뒤로가기버튼 동작 제어 핸들러 생성

        MenuClick();

        RecyclerView listView = (RecyclerView) findViewById(R.id.Best_list_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(layoutManager);

        adapter = new BookListImageAdapter(MainActivity.this,R.layout.book_list_item);
        listView.setAdapter(adapter);

        RecyclerView new_listView = (RecyclerView) findViewById(R.id.New_list_view);
        LinearLayoutManager new_layoutManager = new LinearLayoutManager(MainActivity.this);
        new_layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        new_listView.setHasFixedSize(true);
        new_listView.setLayoutManager(new_layoutManager);

        new_adapter = new BookListImageAdapter(MainActivity.this,R.layout.book_list_item);
        new_listView.setAdapter(new_adapter);

        Recommended_JsoupAsyncTask recommended_jsoupAsyncTask = new Recommended_JsoupAsyncTask();
        recommended_jsoupAsyncTask.execute();

        Best_JsoupAsyncTask jsoupAsyncTask = new Best_JsoupAsyncTask();
        jsoupAsyncTask.execute();

        New_JsoupAsyncTask new_jsoupAsyncTask = new New_JsoupAsyncTask();
        new_jsoupAsyncTask.execute();

        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                        timer.cancel();
                        Recommended_JsoupAsyncTask recommended_jsoupAsyncTask = new Recommended_JsoupAsyncTask();
                        recommended_jsoupAsyncTask.execute();

                        Best_JsoupAsyncTask jsoupAsyncTask = new Best_JsoupAsyncTask();
                        jsoupAsyncTask.execute();

                        New_JsoupAsyncTask new_jsoupAsyncTask = new New_JsoupAsyncTask();
                        new_jsoupAsyncTask.execute();
                    }
                }, 2000);
            }
        });

    }

    private class Recommended_JsoupAsyncTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            rc_list = new ArrayList<DayBookItem>();
            ArrayList<String> imgUrl = new ArrayList<>();
            ArrayList<String> coverUrl = new ArrayList<>();

            Document doc = null;
            try {
                doc = Jsoup.connect("http://www.kyobobook.co.kr/todaybook/newTodaybook.laf?category=KOR&orderClick=4aa").get();

                Elements title_contents = doc.select(".title a strong");
                Elements author_contents = doc.select(".author");
                Elements ImageGroupList = doc.select(".card_news tbody tr");
                Elements ImageGroupList_cover = doc.select(".cover a");

                for (Element element : ImageGroupList){
                    rc_thumbnail = element.select("td").attr("data-image");
                    if(!rc_thumbnail.equals("")){
                        Log.d("TAG","\n추천 이미지 주소: "+rc_thumbnail+"\n\n");
                        imgUrl.add(rc_thumbnail);
                    }
                }

                for (Element element : ImageGroupList_cover){
                    rc_book_cover = element.select("img").attr("src");
                    if(!rc_book_cover.equals("")){
                        Log.d("TAG","\n추천 이미지 커버 주소: "+rc_book_cover+"\n\n");
                        coverUrl.add(rc_book_cover);
                    }
                }

                for(int i = 0; i < 10; i++){
                    rc_title = title_contents.get(i).text();
                    rc_author = author_contents.get(i).text();

                    Log.d("TAG","\n추천순번 : "+i);
                    Log.d("TAG","\n추천제목 : "+rc_title);
                    Log.d("TAG","\n추천저자 : "+rc_author);

                    DayBookItem item = new DayBookItem(imgUrl.get(i),coverUrl.get(i), rc_title, rc_author);

                    rc_list.add(item);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){

            pagerAdapter = new AutoScrollAdapter(MainActivity.this, rc_list);
            viewPager.setAdapter(pagerAdapter);

            //서브뷰 초기화
            count_view.setText(1+" / "+pagerAdapter.getCount());
            reco_title.setText(rc_list.get(0).getTitle());
            reco_author.setText(rc_list.get(0).getAuthor());

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    count_view.setText((position+1)+" / "+pagerAdapter.getCount());
                    reco_title.setText(rc_list.get(position).getTitle());
                    reco_author.setText(rc_list.get(position).getAuthor());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                @Override
                public void run() {
                    int image_num = rc_list.size();
                    if(currentPage == image_num) {
                        currentPage = 0;
                    }
                    int position = currentPage;
                    viewPager.setCurrentItem(currentPage++, true);
                }
            };

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, DELAY_MS, PERIOD_MS);
        }

    }

    private class Best_JsoupAsyncTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            bs_list = new ArrayList<BookListItem>();
            ArrayList<String> imgUrl = new ArrayList<>();

            Document doc = null;
            try {
                doc = Jsoup.connect("http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=A&range=1&kind=0&orderClick=DAa").get();

                Elements title_contents = doc.select(".title a strong");
                Elements category_contents = doc.select(".author");
                Elements author_contents = doc.select(".author");
                Elements price_contents = doc.select(".book_price");
                Elements score_review_contents = doc.select(".review");
                Elements ImageGroupList = doc.select(".cover a");

                for (Element element : ImageGroupList){
                    bs_thumbnail = element.select("img").attr("src");
                    if(!bs_thumbnail.equals("")){
                        Log.d("TAG","\n베스트 이미지 주소: "+bs_thumbnail+"\n\n");
                        imgUrl.add(bs_thumbnail);
                    }
                }

                for(int i = 0; i < 20; i++){
                    bs_score_review = score_review_contents.get(i).select("img").attr("alt");
                    bs_category = "";
                    bs_title = title_contents.get(i).text();
                    bs_author = author_contents.get(i).text();
                    bs_price = price_contents.get(i).text();

                    Log.d("TAG","\n베스트 순번 : "+i);
                    Log.d("TAG","\n베스트 제목 : "+bs_title);
                    Log.d("TAG","\n베스트 저자 : "+bs_author);
                    Log.d("TAG","\n베스트 가격 : "+bs_price);
                    Log.d("TAG","\n베스트 평점 : "+bs_score_review);

                    BookListItem item = new BookListItem(imgUrl.get(i), bs_category, bs_title, bs_author, bs_price, bs_score_review);

                    bs_list.add(item);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            adapter.addAll(bs_list);
        }

    }

    private class New_JsoupAsyncTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            nw_list = new ArrayList<BookListItem>();
            ArrayList<String> imgUrl = new ArrayList<>();

            Document doc = null;
            try {
                doc = Jsoup.connect("http://www.kyobobook.co.kr/newproduct/newProductList.laf?orderClick=Ca1").get();

                Elements title_contents = doc.select(".title a strong");
                Elements author_contents = doc.select(".author");
                Elements price_contents = doc.select(".sell_price");
                Elements score_review_contents = doc.select(".score strong");
                Elements ImageGroupList = doc.select(".cover a");

                for (Element element : ImageGroupList){
                    nw_thumbnail = element.select("img").attr("src");
                    if(!nw_thumbnail.equals("")){
                        Log.d("TAG","\n뉴 이미지 주소: "+nw_thumbnail+"\n\n");
                        imgUrl.add(nw_thumbnail);
                    }
                }

                for(int i = 0; i < 20; i++){
                    nw_score_review = score_review_contents.get(i).text();
                    nw_category = "";
                    nw_title = title_contents.get(i).text();
                    nw_author = author_contents.get(i).text();
                    nw_price = price_contents.get(i).text();

                    Log.d("TAG","\n뉴 순번 : "+i);
                    Log.d("TAG","\n뉴 제목 : "+nw_title);
                    Log.d("TAG","\n뉴 저자 : "+nw_author);
                    Log.d("TAG","\n뉴 가격 : "+nw_price);
                    Log.d("TAG","\n뉴 평점 : "+nw_score_review);

                    BookListItem item = new BookListItem(imgUrl.get(i), nw_category, nw_title, nw_author, nw_price, nw_score_review);

                    nw_list.add(item);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            new_adapter.addAll(nw_list);
        }

    }

    public void MenuClick(){
        ((ConstraintLayout) findViewById(R.id.profile_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "프로필 버튼", Toast.LENGTH_LONG).show();
            }
        });
        ((ConstraintLayout) findViewById(R.id.order_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "주문목록 버튼", Toast.LENGTH_LONG).show();
            }
        });
        ((ConstraintLayout) findViewById(R.id.Promo_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "프로모션 버튼", Toast.LENGTH_LONG).show();
            }
        });
        ((ConstraintLayout) findViewById(R.id.Privacy_policy_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "개인정보 이용약관 버튼", Toast.LENGTH_LONG).show();
            }
        });
        ((ConstraintLayout) findViewById(R.id.setting_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "설정 버튼", Toast.LENGTH_LONG).show();
            }
        });
        ((ConstraintLayout) findViewById(R.id.logout_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Google_signOut();
                Intent intent = new Intent(MainActivity.this, SignActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
            }
        });
    }

    private void Google_signOut() {//구글계정 로그아웃처리
        FirebaseAuth.getInstance().signOut();
    }

    private void Google_revokeAccess() {//구글계정 회원탈퇴처리
        mAuth.getCurrentUser().delete();
    }

    public void onBackPressed() {
        backPressCloseHendler.onBackPressed();
    }

    public void onClick_home(View v){//홈탭버튼
        mScrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    public void onClick_search_map(View v){//홈탭버튼
        Intent intent = new Intent(MainActivity.this, SearchOnMapActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
