package com.naury.boramjul;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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

    BookListImageAdapter adapter;
    ArrayList<BookListItem> bs_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_book_total_view);

        adapter = new BookListImageAdapter(BestBookTotalViewActivity.this,R.layout.book_list_item);

        RecyclerView listView = (RecyclerView) findViewById(R.id.Best_list_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(BestBookTotalViewActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(layoutManager);

        adapter = new BookListImageAdapter(BestBookTotalViewActivity.this,R.layout.book_list_item);
        listView.setAdapter(adapter);

        Best_JsoupAsyncTask jsoupAsyncTask = new Best_JsoupAsyncTask();
        jsoupAsyncTask.execute();
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
}
