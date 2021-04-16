package com.naury.boramjul;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.AnyRes;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewBookListAdapter extends RecyclerView.Adapter<NewBookListAdapter.ViewHolder>{

    private final int resource;
    private Context context;
    private ArrayList<BookListItem> list;

    public NewBookListAdapter(Context context, @AnyRes int resource) {
        this.resource = resource;
        this.context = context;
        list = new ArrayList<BookListItem>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resource, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BookListItem item = getItem(position);

        Picasso.get()
                .load(item.getThumbnail())
                .error(R.color.light_gray)
                .into(holder.image);

        holder.title.setText(item.getTitle());
        holder.author.setText(item.getAuthor());
        holder.score.setText("★ "+item.getScore_review());
        holder.price.setText(item.getPrice());
        holder.number_text.setText("New");
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

    public void delete_Item(int position) {
        list.remove(position);
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

    public void addAll(ArrayList<BookListItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public ArrayList<BookListItem> getList() {
        ArrayList<BookListItem> list = this.list;
        return list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView image;
        public final TextView title;
        public final TextView author;
        public final TextView score;
        public final TextView price;
        public final TextView number_text;
        public final FrameLayout main_layout;

        public ViewHolder(View parent) {
            super(parent);

            image= (ImageView) parent.findViewById(R.id.image);
            title = (TextView) parent.findViewById(R.id.title);
            author = (TextView) parent.findViewById(R.id.author);
            score = (TextView) parent.findViewById(R.id.score);
            price = (TextView) parent.findViewById(R.id.price);
            number_text = (TextView) parent.findViewById(R.id.position_num);
            main_layout = (FrameLayout) parent.findViewById(R.id.main_layout);

            main_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        // 데이터 리스트로부터 아이템 데이터 참조.
                        //delete_Item(pos);
                        BookListItem item = getItem(pos);
                        Intent intent = new Intent(context, DetailBookActivity.class);
                        intent.putExtra("OBJECT",item);
                        context.startActivity(intent);
                        //Toast.makeText(context, getItem(pos).getTitle(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
