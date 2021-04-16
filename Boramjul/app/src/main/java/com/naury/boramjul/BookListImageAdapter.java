package com.naury.boramjul;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.AnyRes;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookListImageAdapter extends RecyclerView.Adapter<BookListImageAdapter.ViewHolder>{

    private final int resource;
    private Context context;
    private ArrayList<BookListItem> list;

    public BookListImageAdapter(Context context, @AnyRes int resource) {
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
        public final CardView card_view;
        public final TextView title;
        public final TextView author;

        public ViewHolder(View parent) {
            super(parent);

            image= (ImageView) parent.findViewById(R.id.image);
            card_view= (CardView) parent.findViewById(R.id.card_view);
            title = (TextView) parent.findViewById(R.id.title);
            author = (TextView) parent.findViewById(R.id.author);

            card_view.setOnClickListener(new View.OnClickListener() {
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
