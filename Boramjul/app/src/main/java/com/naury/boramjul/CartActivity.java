package com.naury.boramjul;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    ConstraintLayout empty_layout;
    TextView total_price;

    ArrayList<BookListItem> cart_list;

    CartListAdapter adapter;
    RecyclerView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        empty_layout = (ConstraintLayout)findViewById(R.id.empty_layout);
        total_price = (TextView)findViewById(R.id.total_price);

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
                    }else {
                        listView.setVisibility(View.VISIBLE);
                        empty_layout.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    public void onClick_back(View v){
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
}
