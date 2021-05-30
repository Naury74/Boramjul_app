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
import com.naury.boramjul.Adapter.NewBookListAdapter;
import com.naury.boramjul.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class NewBookTotalViewActivity extends AppCompatActivity {

    //////////////////////////////// 베스트셀러용
    String nw_thumbnail;
    String nw_category;
    String nw_title;
    String nw_author;
    String nw_price;
    String nw_score_review;

    AutoCompleteTextView edit;

    NewBookListAdapter new_adapter;
    ArrayList<BookListItem> nw_list;
    RecyclerView new_listView;

    ArrayList<String> search_item = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book_total_view);

        new_listView = (RecyclerView) findViewById(R.id.New_list_view);
        LinearLayoutManager new_layoutManager = new LinearLayoutManager(NewBookTotalViewActivity.this);
        new_layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        new_listView.setHasFixedSize(true);
        new_listView.setLayoutManager(new_layoutManager);

        new_adapter = new NewBookListAdapter(NewBookTotalViewActivity.this,R.layout.listview_book_list_item);
        new_listView.setAdapter(new_adapter);

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

                        int result_pos = new_adapter.getSearchPosition(edit.getText().toString());

                        if(result_pos!=100){
                            new_listView.scrollToPosition(result_pos);
                        }else{
                            Toast.makeText(NewBookTotalViewActivity.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        // 기본 엔터키 동작
                        return false;
                }
                return true;
            }
        });

        New_JsoupAsyncTask new_jsoupAsyncTask = new New_JsoupAsyncTask();
        new_jsoupAsyncTask.execute();

    }

    public void onClick_back(View v){
        finish();
    }

    public void onClick_search(View v){
        InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        int result_pos = new_adapter.getSearchPosition(edit.getText().toString());

        if(result_pos!=100){
            new_listView.scrollToPosition(result_pos);
        }else{
            Toast.makeText(NewBookTotalViewActivity.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
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
            ArrayList<String> barcode_list = new ArrayList<String>();
            String barcode = "";

            Document doc = null;
            try {
                doc = Jsoup.connect("http://www.kyobobook.co.kr/newproduct/newProductList.laf?orderClick=Ca1").get();

                Elements title_contents = doc.select(".title a strong");
                Elements author_contents = doc.select(".author");
                Elements price_contents = doc.select(".sell_price");
                Elements score_review_contents = doc.select(".score strong");
                Elements ImageGroupList = doc.select(".cover a");
                Elements Barcode_cover = doc.select(".cover");

                for (Element element : ImageGroupList){
                    nw_thumbnail = element.select("img").attr("src");
                    if(!nw_thumbnail.equals("")){
                        Log.d("TAG","\n뉴 이미지 주소: "+nw_thumbnail+"\n\n");
                        imgUrl.add(nw_thumbnail);
                    }
                }

                for (Element element : Barcode_cover){
                    barcode = element.select("a").attr("href");
                    if(!barcode.equals("")){
                        barcode_list.add(barcode);
                        Log.d("TAG","\n뉴북 토탈뷰 바코드: "+barcode);
                    }
                }

                for(int i = 0; i < title_contents.size(); i++){
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

                    BookListItem item = new BookListItem(imgUrl.get(i), nw_category, nw_title, nw_author, nw_price, nw_score_review, barcode_list.get(i));

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
}
