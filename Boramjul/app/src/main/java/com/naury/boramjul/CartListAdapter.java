package com.naury.boramjul;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObservable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder>{

    private final int resource;
    private Context context;
    private ArrayList<BookListItem> list;

    public CartListAdapter(Context context, @AnyRes int resource) {
        this.resource = resource;
        this.context = context;
        list = new ArrayList<BookListItem>();
    }

    @Override
    public CartListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resource, null);

        return new CartListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookListItem item = getItem(position);

        Picasso.get()
                .load(item.getThumbnail())
                .error(R.color.light_gray)
                .into(holder.image);

        holder.title.setText(item.getTitle());
        holder.author.setText(item.getAuthor());
        if(item.getScore_review().length()>5){
            holder.score.setText("★ "+item.getScore_review().substring(7,8)+" /5점");
        }else{
            holder.score.setText("★ "+item.getScore_review());
        }
        String temp_price = item.getPrice().replaceAll("[^\\d]","");
        int temp_price_num = Integer.parseInt(temp_price);
        temp_price_num *= item.getCount();
        DecimalFormat format = new DecimalFormat("###,###");
        String price_str = format.format(temp_price_num);
        holder.price.setText(price_str+"원");
        holder.number_text.setText(Integer.toString(position+1));
        holder.count_view.setText(Integer.toString(item.getCount()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private BookListItem getItem(int position) {
        return list.get(position);
    }

    public void clear() {
        if(null != list) {
            list.clear();
        }
    }

    public void addItem(BookListItem item) {
        list.add(item);
        notifyDataSetChanged();
    }

    public int getSearchPosition(String s) {

        int num = 100;

        for(int i=0;i<getItemCount();i++){
            BookListItem item = getItem(i);
            if(item.getAuthor().contains(s)||item.getTitle().contains(s)){
                num = i;
            }
        }
        return num;
    }

    public void delete_Item(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<BookListItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public ArrayList<BookListItem> getList() {
        ArrayList<BookListItem> list = this.list;
        return list;
    }

    public int getTotalPrice(){
        int total_price = 0;
        for (BookListItem item:list){
            String temp_price = item.getPrice().replaceAll("[^\\d]","");
            int temp_price_num = Integer.parseInt(temp_price);
            temp_price_num *= item.getCount();
            total_price += temp_price_num;
        }
        return total_price;
    }

    private void SaveData(ArrayList<BookListItem> friends) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(friends);
        editor.putString("MyCart", json);
        editor.commit();
    }

    private ArrayList<BookListItem> ReadData() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView image;
        public final TextView title;
        public final TextView author;
        public final TextView score;
        public final TextView price;
        public final TextView number_text;
        public final FrameLayout main_layout;
        public final TextView minus_btn;
        public final TextView count_view;
        public final TextView plus_btn;
        public final TextView cancel_btn;

        public ViewHolder(View parent) {
            super(parent);

            image= (ImageView) parent.findViewById(R.id.image);
            title = (TextView) parent.findViewById(R.id.title);
            author = (TextView) parent.findViewById(R.id.author);
            score = (TextView) parent.findViewById(R.id.score);
            price = (TextView) parent.findViewById(R.id.price);
            number_text = (TextView) parent.findViewById(R.id.position_num);
            main_layout = (FrameLayout) parent.findViewById(R.id.main_layout);
            minus_btn = (TextView) parent.findViewById(R.id.minus_btn);
            count_view = (TextView) parent.findViewById(R.id.count_view);
            plus_btn = (TextView) parent.findViewById(R.id.plus_btn);
            cancel_btn = (TextView) parent.findViewById(R.id.cancel_btn);

            minus_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        // 데이터 리스트로부터 아이템 데이터 참조.
                        //delete_Item(pos);
                        BookListItem item = getItem(pos);
                        String temp_price = item.getPrice().replaceAll("[^\\d]","");
                        int temp_price_num = Integer.parseInt(temp_price);
                        if(item.getCount()==1){
                            delete_Item(pos);
                        }else {
                            item.setCount(item.getCount()-1);
                            count_view.setText(Integer.toString(item.getCount()));
                            temp_price_num *= item.getCount();
                            price.setText(Integer.toString(temp_price_num)+"원");
                        }
                        //Toast.makeText(context, getItem(pos).getTitle(), Toast.LENGTH_SHORT).show();
                    }
                    notifyDataSetChanged();
                    SaveData(list);
                }
            });

            plus_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        // 데이터 리스트로부터 아이템 데이터 참조.
                        //delete_Item(pos);
                        BookListItem item = getItem(pos);
                        String temp_price = item.getPrice().replaceAll("[^\\d]","");
                        int temp_price_num = Integer.parseInt(temp_price);
                        item.setCount(item.getCount()+1);
                        count_view.setText(Integer.toString(item.getCount()));
                        temp_price_num *= item.getCount();
                        price.setText(Integer.toString(temp_price_num)+"원");
                        //Toast.makeText(context, getItem(pos).getTitle(), Toast.LENGTH_SHORT).show();
                    }
                    notifyDataSetChanged();
                    SaveData(list);
                }
            });

            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        // 데이터 리스트로부터 아이템 데이터 참조.
                        //delete_Item(pos);
                        BookListItem item = getItem(pos);
                        delete_Item(pos);
                        //Toast.makeText(context, getItem(pos).getTitle(), Toast.LENGTH_SHORT).show();
                    }
                    notifyDataSetChanged();
                    SaveData(list);
                }
            });
        }
    }
}
