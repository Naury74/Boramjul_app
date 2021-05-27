package com.naury.boramjul.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.naury.boramjul.Adapter.BookListImageAdapter;
import com.naury.boramjul.DTO.BookListItem;
import com.naury.boramjul.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity{

    ChipGroup chip_group;
    Chip chip_0,chip_1,chip_2,chip_3,chip_4,chip_5,chip_6,chip_7,chip_8,chip_9,chip_10,
            chip_11,chip_12,chip_13,chip_14,chip_15,chip_16,chip_17,chip_18,chip_19,chip_20,
            chip_21,chip_22,chip_23,chip_24,chip_25,chip_26,chip_27;

    String getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=A&range=1&kind=0&orderClick=DAa";

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
        setContentView(R.layout.activity_category);

        chip_group = (ChipGroup)findViewById(R.id.chip_group);
        setChipView();

        RecyclerView listView = (RecyclerView) findViewById(R.id.Best_list_view);
        GridLayoutManager layoutManager = new GridLayoutManager(CategoryActivity.this,2);
        listView.setLayoutManager(layoutManager);

        adapter = new BookListImageAdapter(CategoryActivity.this,R.layout.search_book_list_item);
        listView.setAdapter(adapter);

        Best_JsoupAsyncTask jsoupAsyncTask = new Best_JsoupAsyncTask();
        jsoupAsyncTask.execute();

        chip_group.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {

                if (checkedId!=-1) {
                    Chip chip = (Chip)findViewById(checkedId);
                    String category = chip.getText().toString();

                    chipClick(category);

                } else {
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                }

            }
        });

    }

    public void onClick_back(View v){
        finish();
    }

    private class Best_JsoupAsyncTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            adapter.clear();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            bs_list = new ArrayList<BookListItem>();
            ArrayList<String> imgUrl = new ArrayList<>();

            Document doc = null;
            try {
                doc = Jsoup.connect(getAddress).get();

                Elements title_contents = doc.select(".title a strong");
                Elements category_contents = doc.select(".author");
                Elements author_contents = doc.select(".author");
                Elements price_contents = doc.select(".book_price");
                Elements score_review_contents = doc.select(".review");
                Elements ImageGroupList = doc.select(".cover a");

                for (Element element : ImageGroupList){
                    bs_thumbnail = element.select("img").attr("src");
                    if(!bs_thumbnail.equals("")){
                        Log.d("TAG","\n카테고리 이미지 주소: "+bs_thumbnail+"\n\n");
                        imgUrl.add(bs_thumbnail);
                    }
                }

                for(int i = 0; i < title_contents.size(); i++){
                    bs_score_review = score_review_contents.get(i).select("img").attr("alt");
                    bs_category = "";
                    bs_title = title_contents.get(i).text();
                    bs_author = author_contents.get(i).text();
                    bs_price = price_contents.get(i).text();

                    Log.d("TAG","\n카테고리 순번 : "+i);
                    Log.d("TAG","\n카테고리 제목 : "+bs_title);
                    Log.d("TAG","\n카테고리 저자 : "+bs_author);
                    Log.d("TAG","\n카테고리 가격 : "+bs_price);
                    Log.d("TAG","\n카테고리 평점 : "+bs_score_review);

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
            adapter.notifyDataSetChanged();
        }

    }

    private void setChipView(){
        chip_0 = (Chip)findViewById(R.id.chip_0);
        chip_1 = (Chip)findViewById(R.id.chip_1);
        chip_2 = (Chip)findViewById(R.id.chip_2);
        chip_3 = (Chip)findViewById(R.id.chip_3);
        chip_4 = (Chip)findViewById(R.id.chip_4);
        chip_5 = (Chip)findViewById(R.id.chip_5);
        chip_6 = (Chip)findViewById(R.id.chip_6);
        chip_7 = (Chip)findViewById(R.id.chip_7);
        chip_8 = (Chip)findViewById(R.id.chip_8);
        chip_9 = (Chip)findViewById(R.id.chip_9);
        chip_10 = (Chip)findViewById(R.id.chip_10);
        chip_11 = (Chip)findViewById(R.id.chip_11);
        chip_12 = (Chip)findViewById(R.id.chip_12);
        chip_13 = (Chip)findViewById(R.id.chip_13);
        chip_14 = (Chip)findViewById(R.id.chip_14);
        chip_15 = (Chip)findViewById(R.id.chip_15);
        chip_16 = (Chip)findViewById(R.id.chip_16);
        chip_17 = (Chip)findViewById(R.id.chip_17);
        chip_18 = (Chip)findViewById(R.id.chip_18);
        chip_19 = (Chip)findViewById(R.id.chip_19);
        chip_20 = (Chip)findViewById(R.id.chip_20);
        chip_21 = (Chip)findViewById(R.id.chip_21);
        chip_22 = (Chip)findViewById(R.id.chip_22);
        chip_23 = (Chip)findViewById(R.id.chip_23);
        chip_24 = (Chip)findViewById(R.id.chip_24);
        chip_25 = (Chip)findViewById(R.id.chip_25);
        chip_26 = (Chip)findViewById(R.id.chip_26);

    }

    private void chipClick(String category){
        switch (category){
            case "종합":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=A&range=1&kind=0&orderClick=DAa";
                break;
            case "소설":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=B&range=1&kind=0&orderClick=DAb";
                break;
            case "에세이":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=C&range=1&kind=0&orderClick=DAb";
                break;
            case "국내소설":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=D&range=1&kind=0&orderClick=DAb";
                break;
            case "국외소설":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=E&range=1&kind=0&orderClick=DAb";
                break;
            case "시":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=F&range=1&kind=0&orderClick=DAb";
                break;
            case "어린이":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=G&range=1&kind=0&orderClick=DAb";
                break;
            case "가정생활":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=H&range=1&kind=0&orderClick=DAb";
                break;
            case "인문":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=I&range=1&kind=0&orderClick=DAb";
                break;
            case "정치사회":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=J&range=1&kind=0&orderClick=DAb";
                break;
            case "경제경영":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=K&range=1&kind=0&orderClick=DAb";
                break;
            case "건강":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=L&range=1&kind=0&orderClick=DAb";
                break;
            case "교양과학":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=M&range=1&kind=0&orderClick=DAb";
                break;
            case "외국어":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=N&range=1&kind=0&orderClick=DAb";
                break;
            case "예술":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=Q&range=1&kind=0&orderClick=DAb";
                break;
            case "취미/스포츠":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=R&range=1&kind=0&orderClick=DAb";
                break;
            case "TOEIC/TOEFL":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=S&range=1&kind=0&orderClick=DAb";
                break;
            case "유아":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=T&range=1&kind=0&orderClick=DAb";
                break;
            case "종교":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=U&range=1&kind=0&orderClick=DAb";
                break;
            case "아동만화":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=V&range=1&kind=0&orderClick=DAb";
                break;
            case "요리/와인":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=a&range=1&kind=0&orderClick=DAb";
                break;
            case "역사/문화":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=b&range=1&kind=0&orderClick=DAb";
                break;
            case "자기계발":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=c&range=1&kind=0&orderClick=DAb";
                break;
            case "여행":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=d&range=1&kind=0&orderClick=DAb";
                break;
            case "기술/컴퓨터":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=e&range=1&kind=0&orderClick=DAb";
                break;
            case "만화":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=f&range=1&kind=0&orderClick=DAb";
                break;
            case "청소년":
                getAddress = "http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?mallGb=KOR&linkClass=g&range=1&kind=0&orderClick=DAb";
                break;
        }
        Best_JsoupAsyncTask jsoupAsyncTask = new Best_JsoupAsyncTask();
        jsoupAsyncTask.execute();
    }

}
