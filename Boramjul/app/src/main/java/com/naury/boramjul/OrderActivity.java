package com.naury.boramjul;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    ArrayList<BookListItem> cart_list;

    TextView total_price, final_price, item_count;
    EditText order_name_input,name_input,address_num_input,address_input,address_detail_input,ph_num_input,request_input;

    RadioGroup pay_input,easy_pay_select;

    CartListAdapter adapter;
    RecyclerView listView;

    CheckBox confirm_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        cart_list = new ArrayList<BookListItem>();
        cart_list = ReadData();

        pay_input = (RadioGroup)findViewById(R.id.pay_input);
        easy_pay_select = (RadioGroup)findViewById(R.id.easy_pay_select);
        total_price = (TextView)findViewById(R.id.total_price);
        final_price = (TextView)findViewById(R.id.final_price);
        item_count = (TextView)findViewById(R.id.item_count);
        confirm_check = (CheckBox)findViewById(R.id.confirm_check);

        order_name_input = (EditText)findViewById(R.id.order_name_input);
        name_input = (EditText)findViewById(R.id.name_input);
        address_num_input = (EditText)findViewById(R.id.address_num_input);
        address_input = (EditText)findViewById(R.id.address_input);
        address_detail_input = (EditText)findViewById(R.id.address_detail_input);
        ph_num_input = (EditText)findViewById(R.id.ph_num_input);
        request_input = (EditText)findViewById(R.id.request_input);

        adapter = new CartListAdapter(OrderActivity.this,R.layout.order_list_item);

        listView = (RecyclerView) findViewById(R.id.Best_list_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(OrderActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(layoutManager);

        listView.setAdapter(adapter);
        listView.setVisibility(View.VISIBLE);
        adapter.addAll(cart_list);

        DecimalFormat format = new DecimalFormat("###,###");
        total_price.setText(format.format(adapter.getTotalPrice())+"원");
        final_price.setText(format.format(adapter.getTotalPrice())+"원");

        item_count.setText("총 "+adapter.getItemCount()+"가지 상품");

        pay_input.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.method_card){
                    easy_pay_select.setVisibility(View.GONE);
                } else if (i ==R.id.method_other) {
                    easy_pay_select.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void onClick_back(View v){
        finish();
    }

    public void onClick_next(View v){
        if(!order_name_input.getText().toString().equals("")&&!name_input.getText().toString().equals("")&&!address_num_input.getText().toString().equals("")&&!address_input.getText().toString().equals("")&&!ph_num_input.getText().toString().equals("")&&!request_input.getText().toString().equals("")){
            if(confirm_check.isChecked()){

            }else {
                Toast.makeText(this, "주문내역 확인에 체크해 주세요", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "주문정보를 모두 입력해 주세요", Toast.LENGTH_SHORT).show();
        }
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
