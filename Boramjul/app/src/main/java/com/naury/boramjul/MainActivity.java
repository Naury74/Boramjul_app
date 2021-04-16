package com.naury.boramjul;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
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

    //////////////////////////////// 검색 출력용
    String search_thumbnail;
    String search_category;
    String search_title;
    String search_author;
    String search_price;
    String search_score_review;

    BookListImageAdapter search_adapter;
    ArrayList<BookListItem> search_list;

    TextView content_string;
    TextView title;

    //////////////////////////////// 주간 도서용
    String week_address;
    String week_title;
    String week_author;
    String week_cover;
    String week_sub_title;

    ArrayList<String> title_list;
    ArrayList<String> sub_title_list;
    ArrayList<String> author_list;
    ArrayList<String> coverUrl;

    ImageView week_book_cover;
    TextView week_book_sub_title;
    TextView week_book_title;

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
    String rc_price;
    String rc_score;

    AutoScrollAdapter pagerAdapter;
    ViewPager viewPager;
    ArrayList<DayBookItem> rc_list;

    int rc_position = 0;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 4000;

    TextView count_view;
    TextView reco_title;
    TextView reco_author;
    ImageView reco_book_cover;

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

    //////////////////////////// Search 관련 변수
    ArrayList<String> search_item = new ArrayList<String>();
    AutoCompleteTextView edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitle("");
        toolbar.setBackgroundColor(getColor(R.color.transparent));
        setSupportActionBar(toolbar);

        new SlidingRootNavBuilder(this)
                .withMenuLayout(R.layout.sliding_root_nav)
                .withToolbarMenuToggle(toolbar)
                .inject();

        viewPager = findViewById(R.id.viewPager);

        count_view = (TextView)findViewById(R.id.count_view);
        reco_title = (TextView)findViewById(R.id.reco_title);
        reco_author = (TextView)findViewById(R.id.reco_author);
        reco_book_cover = (ImageView)findViewById(R.id.reco_book_cover);

        week_book_cover = (ImageView)findViewById(R.id.week_book_cover);;
        week_book_sub_title  = (TextView)findViewById(R.id.week_book_sub_title);;
        week_book_title  = (TextView)findViewById(R.id.week_book_title);;

        mScrollView = findViewById(R.id.scrollView);

        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);

        backPressCloseHendler = new BackPressCloseHendler(this);//뒤로가기버튼 동작 제어 핸들러 생성

        edit = (AutoCompleteTextView) findViewById(R.id.search_bar);

        edit.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, search_item));

        edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                switch (i) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        // 검색 동작
                        InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        if(edit.getText().toString().equals("")){
                            Toast.makeText(MainActivity.this, "검색어를 입력하세요.", Toast.LENGTH_SHORT).show();
                        }else {
                            Search();
                        }
                        break;
                    default:
                        // 기본 엔터키 동작
                        return false;
                }
                return true;
            }
        });

        MenuClick();

        search_adapter  = new BookListImageAdapter(MainActivity.this,R.layout.search_book_list_item);

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

        Week_Book_JsoupAsyncTask week_book_jsoupAsyncTask = new Week_Book_JsoupAsyncTask();
        week_book_jsoupAsyncTask.execute();

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
                        Week_Book_JsoupAsyncTask week_book_jsoupAsyncTask = new Week_Book_JsoupAsyncTask();
                        week_book_jsoupAsyncTask.execute();

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

    private void Search(){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this,R.style.BottomSheetDialogTheme);
        final View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.search_result_bottomsheet_layout,(LinearLayout)findViewById(R.id.container_bottom_sheet));
        bottomSheetDialog.setCanceledOnTouchOutside(false);

        content_string = bottomSheetView.findViewById(R.id.content_string);
        title = bottomSheetView.findViewById(R.id.title);

        RecyclerView listView = (RecyclerView) bottomSheetView.findViewById(R.id.search_list_view);
        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this,2);
        listView.setLayoutManager(layoutManager);

        listView.setAdapter(search_adapter);

        bottomSheetView.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog.dismiss();
            }
        });

        Search_JsoupAsyncTask search_jsoupAsyncTask = new Search_JsoupAsyncTask();
        search_jsoupAsyncTask.execute();

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private class Week_Book_JsoupAsyncTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            title_list = new ArrayList<String>();
            sub_title_list = new ArrayList<String>();
            author_list = new ArrayList<String>();

            coverUrl = new ArrayList<>();

            Document doc = null;
            try {
                doc = Jsoup.connect("http://www.kyobobook.co.kr/killingpart/mainCategory.laf?orderClick=c0z").get();

                Elements title_contents = doc.select(".title a strong");
                Elements author_contents = doc.select(".author");
                Elements sub_title_contents = doc.select(".card_news tbody tr td p");
                Elements ImageGroupList_cover = doc.select(".cover a");

                for (Element element : ImageGroupList_cover){
                    week_cover = element.select("img").attr("src");
                    if(!week_cover.equals("")){
                        Log.d("TAG","\n주간 이미지 커버 주소: "+week_cover+"\n\n");
                        coverUrl.add(week_cover);
                    }
                }

                for(int i = 0; i < 10; i++){
                    week_title = title_contents.get(i).text();
                    week_author = author_contents.get(i).text();
                    week_sub_title = sub_title_contents.get(i*2).text();

                    Log.d("TAG","\n주간순번 : "+i);
                    Log.d("TAG","\n주간제목 : "+week_title);
                    Log.d("TAG","\n주간저자 : "+week_author);
                    Log.d("TAG","\n주간 서브 텍스트 : "+week_sub_title);

                    title_list.add(week_title);
                    sub_title_list.add(week_sub_title);
                    author_list.add(week_author);

                    search_item.add(week_title);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){

        }

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
                Elements price_contents = doc.select(".sell_price");
                Elements score_contents = doc.select(".score strong");
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
                    rc_price = price_contents.get(i).text();
                    rc_score = score_contents.get(i).text();

                    Log.d("TAG","\n추천순번 : "+i);
                    Log.d("TAG","\n추천제목 : "+rc_title);
                    Log.d("TAG","\n추천저자 : "+rc_author);
                    Log.d("TAG","\n추천가격 : "+rc_price);
                    Log.d("TAG","\n추천평점 : "+rc_score);

                    DayBookItem item = new DayBookItem(imgUrl.get(i),coverUrl.get(i), rc_title, rc_author, rc_price, rc_score);

                    rc_list.add(item);
                    search_item.add(rc_title);
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
            Picasso.get()
                    .load(rc_list.get(0).getBook_cover())
                    .error(R.color.light_gray)
                    .into(reco_book_cover);

            Picasso.get()
                    .load(coverUrl.get(0))
                    .error(R.color.light_gray)
                    .into(week_book_cover);
            week_book_sub_title.setText(sub_title_list.get(0));
            week_book_title.setText(title_list.get(0));

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    count_view.setText((position+1)+" / "+pagerAdapter.getCount());
                    reco_title.setText(rc_list.get(position).getTitle());
                    reco_author.setText(rc_list.get(position).getAuthor());
                    Picasso.get()
                            .load(rc_list.get(position).getBook_cover())
                            .error(R.color.light_gray)
                            .into(reco_book_cover);

                    Picasso.get()
                            .load(coverUrl.get(position))
                            .error(R.color.light_gray)
                            .into(week_book_cover);
                    week_book_sub_title.setText(sub_title_list.get(position));
                    week_book_title.setText(title_list.get(position));
                    rc_position = position;
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
                    search_item.add(bs_title);
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
                    search_item.add(nw_title);
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

    private class Search_JsoupAsyncTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            search_list = new ArrayList<BookListItem>();
            ArrayList<String> imgUrl = new ArrayList<>();

            Document doc = null;
            try {
                doc = Jsoup.connect("https://search.kyobobook.co.kr/web/search?vPstrKeyWord="+edit.getText().toString()).get();

                Elements title_contents = doc.select(".title a strong");
                Elements author_contents = doc.select(".author a");
                Elements price_contents = doc.select(".sell_price strong");
                Elements score_review_contents = doc.select(".rating");
                Elements ImageGroupList = doc.select(".cover a");

                for (Element element : ImageGroupList){
                    search_thumbnail = element.select("img").attr("src");
                    if(!search_thumbnail.equals("")){
                        Log.d("TAG","\n검색 이미지 주소: "+search_thumbnail+"\n\n");
                        imgUrl.add(search_thumbnail);
                    }
                }



                for(int i = 0; i < score_review_contents.size(); i++){
                    search_score_review = score_review_contents.get(i).select("img").attr("alt");
                    search_category = "";
                    search_title = title_contents.get(i).text();
                    search_author = author_contents.get(i).text();
                    search_price = price_contents.get(i).text();

                    Log.d("TAG","\n검색 순번 : "+i);
                    Log.d("TAG","\n검색 제목 : "+search_title);
                    Log.d("TAG","\n검색 저자 : "+search_author);
                    Log.d("TAG","\n검색 가격 : "+search_price);
                    Log.d("TAG","\n검색 평점 : "+search_score_review);

                    BookListItem item = new BookListItem(imgUrl.get(i), search_category, search_title, search_author, search_price, search_score_review);

                    search_list.add(item);
                    search_item.add(search_title);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            search_adapter.addAll(search_list);
            content_string.setText("\'"+edit.getText().toString()+"\'");
            title.setText("검색결과 "+search_adapter.getItemCount()+"개");
            edit.setText("");
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

    public void onClick_Show_RecommendBook_Detail(View v){
        pagerAdapter.ViewDetail(rc_position);
    }

    public void onClick_search(View v){//검색

        InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if(edit.getText().toString().equals("")){
            Toast.makeText(MainActivity.this, "검색어를 입력하세요.", Toast.LENGTH_SHORT).show();
        }else {
            Search();
        }
    }

    public void onClick_search_map(View v){//홈탭버튼
        Intent intent = new Intent(MainActivity.this, SearchOnMapActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void onClick_total_bs(View v){//베스트셀러 모두보기
        Intent intent = new Intent(MainActivity.this, BestBookTotalViewActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void onClick_total_nw(View v){//뉴셀러 모두보기
        Intent intent = new Intent(MainActivity.this, NewBookTotalViewActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
