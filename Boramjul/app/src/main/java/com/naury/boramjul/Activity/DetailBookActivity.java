package com.naury.boramjul.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.naury.boramjul.Adapter.BookListImageAdapter;
import com.naury.boramjul.DTO.BookListItem;
import com.naury.boramjul.R;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class DetailBookActivity extends AppCompatActivity {

    ImageView week_book_cover;
    TextView title, author, rating_text, price_text;

    BookListItem item;
    ArrayList<BookListItem> cart_list;

    //////////////////////////////// 검색 출력용
    String search_thumbnail;
    String search_category;
    String search_title;
    String search_author;
    String search_price;
    String search_score_review;

    BookListImageAdapter search_adapter;
    ArrayList<BookListItem> search_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);

        week_book_cover = (ImageView)findViewById(R.id.week_book_cover);
        title = (TextView)findViewById(R.id.title);
        author = (TextView)findViewById(R.id.author);
        rating_text = (TextView)findViewById(R.id.rating_text);
        price_text = (TextView)findViewById(R.id.price_text);

        Intent intent = getIntent();
        item = (BookListItem)intent.getSerializableExtra("OBJECT");

        cart_list = new ArrayList<BookListItem>();
        cart_list = ReadData();

        title.setText(item.getTitle());
        author.setText(item.getAuthor());
        rating_text.setText(item.getScore_review().replace("5점 만점에 ","★ "));
        price_text.setText(item.getPrice());

        title.setSelected(true);
        author.setSelected(true);
        rating_text.setSelected(true);
        price_text.setSelected(true);

        Picasso.get()
                .load(item.getThumbnail())
                .error(R.color.light_gray)
                .into(week_book_cover);

        RecyclerView listView = (RecyclerView) findViewById(R.id.list_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DetailBookActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(layoutManager);

        search_adapter  = new BookListImageAdapter(DetailBookActivity.this,R.layout.book_list_item);
        listView.setAdapter(search_adapter);

        Search_JsoupAsyncTask search_jsoupAsyncTask = new Search_JsoupAsyncTask();
        search_jsoupAsyncTask.execute();
    }

    public void onClick_back(View v){
        finish();
    }

    public void onClick_AddCart(View v){
        cart_list.add(item);
        SaveData(cart_list);
        Intent intent = new Intent(DetailBookActivity.this, CartActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }

    private void SaveData(ArrayList<BookListItem> friends) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(friends);
        editor.putString("MyCart", json);
        editor.commit();
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
                String content_string = item.getAuthor();
                String[] split_string = content_string.split("\\|");
                split_string[0].replaceAll("저자 더보기 ","");
                doc = Jsoup.connect("https://search.kyobobook.co.kr/web/search?vPstrKeyWord="+split_string[0]).get();

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
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            search_adapter.addAll(search_list);
        }

    }
}
