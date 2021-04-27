package com.naury.boramjul;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.xml.transform.Templates;

public class CartActivity extends AppCompatActivity {

    ConstraintLayout empty_layout, discount_layout;
    TextView total_price, discount_title;

    ArrayList<BookListItem> cart_list;

    CartListAdapter adapter;
    RecyclerView listView;

    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        userInfo = new UserInfo();

        empty_layout = (ConstraintLayout)findViewById(R.id.empty_layout);
        discount_layout = (ConstraintLayout)findViewById(R.id.discount_layout);
        total_price = (TextView)findViewById(R.id.total_price);
        discount_title = (TextView)findViewById(R.id.discount_title);

        cart_list = new ArrayList<BookListItem>();
        cart_list = ReadData();
        if(cart_list.size()==0){
            empty_layout.setVisibility(View.VISIBLE);
        }else {
            adapter = new CartListAdapter(CartActivity.this,R.layout.cart_item);

            listView = (RecyclerView) findViewById(R.id.Best_list_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(CartActivity.this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            listView.setHasFixedSize(true);
            listView.setLayoutManager(layoutManager);

            listView.setAdapter(adapter);
            listView.setVisibility(View.VISIBLE);
            adapter.addAll(cart_list);

            DecimalFormat format = new DecimalFormat("###,###");
            total_price.setText(format.format(adapter.getTotalPrice())+"원");

            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();

                    DecimalFormat format = new DecimalFormat("###,###");
                    total_price.setText(format.format(adapter.getTotalPrice())+"원");
                    cart_list = adapter.getList();
                    if(cart_list.size()==0){
                        listView.setVisibility(View.GONE);
                        empty_layout.setVisibility(View.VISIBLE);
                        if(userInfo.getLogin_type()==1){

                            discount_layout.setVisibility(View.GONE);
                        }
                    }else {
                        listView.setVisibility(View.VISIBLE);
                        empty_layout.setVisibility(View.GONE);
                        if(userInfo.getLogin_type()==1){

                            discount_layout.setVisibility(View.VISIBLE);

                            if(userInfo.getRank().equals("1")){
                                discount_title.setText("5% 할인 적용 - 맴버쉽");
                                total_price.setText(format.format(adapter.getTotalPrice()-((adapter.getTotalPrice()/100)*5))+"원");
                            }else if(userInfo.getRank().equals("2")){
                                discount_title.setText("10% 할인 적용 - 맴버쉽");
                                total_price.setText(format.format(adapter.getTotalPrice()-((adapter.getTotalPrice()/100)*10))+"원");
                            }else if(userInfo.getRank().equals("3")){
                                discount_title.setText("15% 할인 적용 - 맴버쉽");
                                total_price.setText(format.format(adapter.getTotalPrice()-((adapter.getTotalPrice()/100)*15))+"원");
                            }
                        }
                    }
                }
            });

            if(cart_list.size()==0){

            }else {
                if(userInfo.getLogin_type()==1){

                    discount_layout.setVisibility(View.VISIBLE);

                    if(userInfo.getRank().equals("1")){
                        discount_title.setText("5% 할인 적용 - 맴버쉽");
                        total_price.setText(format.format(adapter.getTotalPrice()-((adapter.getTotalPrice()/100)*5))+"원");
                    }else if(userInfo.getRank().equals("2")){
                        discount_title.setText("10% 할인 적용 - 맴버쉽");
                        total_price.setText(format.format(adapter.getTotalPrice()-((adapter.getTotalPrice()/100)*10))+"원");
                    }else if(userInfo.getRank().equals("3")){
                        discount_title.setText("15% 할인 적용 - 맴버쉽");
                        total_price.setText(format.format(adapter.getTotalPrice()-((adapter.getTotalPrice()/100)*15))+"원");
                    }
                }
            }
        }
    }

    public void onClick_back(View v){
        finish();
    }

    public void onClick_Order(View v){
        if(cart_list.size()==0){

        }else {
            Intent intent = new Intent(CartActivity.this, OrderActivity.class);
            startActivity(intent);
            finish();
        }
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
}
