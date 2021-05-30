package com.naury.boramjul.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.naury.boramjul.DTO.BookListItem;
import com.naury.boramjul.Adapter.BookListTotalAdapter;
import com.naury.boramjul.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class BestBookTotalViewActivity extends AppCompatActivity {

    //////////////////////////////// 베스트셀러용
    String bs_thumbnail;
    String bs_category;
    String bs_title;
    String bs_author;
    String bs_price;
    String bs_score_review;

    AutoCompleteTextView edit;

    BookListTotalAdapter adapter;
    ArrayList<BookListItem> bs_list;

    RecyclerView listView;

    ArrayList<String> search_item = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_book_total_view);

        adapter = new BookListTotalAdapter(BestBookTotalViewActivity.this,R.layout.listview_book_list_item);

        listView = (RecyclerView) findViewById(R.id.Best_list_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(BestBookTotalViewActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(layoutManager);

        listView.setAdapter(adapter);

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

                        int result_pos = adapter.getSearchPosition(edit.getText().toString());

                        if(result_pos!=100){
                            listView.scrollToPosition(result_pos);
                        }else{
                            Toast.makeText(BestBookTotalViewActivity.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        // 기본 엔터키 동작
                        return false;
                }
                return true;
            }
        });

        Best_JsoupAsyncTask jsoupAsyncTask = new Best_JsoupAsyncTask();
        jsoupAsyncTask.execute();
    }

    public void onClick_back(View v){
        finish();
    }

    public void onClick_search(View v){
        InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        int result_pos = adapter.getSearchPosition(edit.getText().toString());

        if(result_pos!=100){
            listView.scrollToPosition(result_pos);
        }else{
            Toast.makeText(BestBookTotalViewActivity.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
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
            ArrayList<String> barcode_list = new ArrayList<String>();
            String barcode = "";

            Document doc = null;
            try {
                doc = Jsoup.connect("http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=A&range=1&kind=0&orderClick=DAa").get();

                Elements title_contents = doc.select(".title a strong");
                Elements category_contents = doc.select(".author");
                Elements author_contents = doc.select(".author");
                Elements price_contents = doc.select(".book_price");
                Elements score_review_contents = doc.select(".review");
                Elements ImageGroupList = doc.select(".cover a");
                Elements Barcode_cover = doc.select(".cover");

                for (Element element : ImageGroupList){
                    bs_thumbnail = element.select("img").attr("src");
                    if(!bs_thumbnail.equals("")){
                        Log.d("TAG","\n베스트 이미지 주소: "+bs_thumbnail+"\n\n");
                        imgUrl.add(bs_thumbnail);
                    }
                }

                for (Element element : Barcode_cover){
                    barcode = element.select("a").attr("href");
                    if(!barcode.equals("")){
                        barcode_list.add(barcode);
                        Log.d("TAG","\n베스크북 디테일 바코드: "+barcode);
                    }
                }

                for(int i = 0; i < title_contents.size(); i++){
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

                    BookListItem item = new BookListItem(imgUrl.get(i), bs_category, bs_title, bs_author, bs_price, bs_score_review, barcode_list.get(i));

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
}
