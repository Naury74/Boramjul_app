package com.naury.boramjul;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

public class PlaceInfoAdapter extends RecyclerView.Adapter<PlaceInfoAdapter.ViewHolder>{

    private final int resource;
    private Context context;
    private ArrayList<PlaceListItem> list;

    public PlaceInfoAdapter(Context context, @AnyRes int resource) {
        this.resource = resource;
        this.context = context;
        list = new ArrayList<PlaceListItem>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resource, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PlaceListItem item = getItem(position);

        holder.name_text.setText(item.getPlace_name());
        holder.address_text.setText(item.getPlace_address());
        holder.vicinity_text.setText(item.getPlace_sub_address());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private PlaceListItem getItem(int position) {
        return list.get(position);
    }

    public void clear() {
        if(list != null) {
            list.clear();
            notifyDataSetChanged();
        }
    }

    public int getSearchPosition(String s) {

        int num = 100;

        for(int i=0;i<getItemCount();i++){
            PlaceListItem item = getItem(i);
            if(item.getPlace_name().contains(s)){
                num = i;
            }
        }
        return num;
    }

    public void addItem(PlaceListItem item) {
        list.add(item);
        notifyDataSetChanged();
    }

    public void delete_Item(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<PlaceListItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public ArrayList<PlaceListItem> getList() {
        ArrayList<PlaceListItem> list = this.list;
        return list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView name_text;
        public final TextView address_text;
        public final TextView vicinity_text;
        public final FrameLayout main_layout;

        public ViewHolder(View parent) {
            super(parent);

            name_text = (TextView) parent.findViewById(R.id.name_text);
            address_text = (TextView) parent.findViewById(R.id.address_text);
            vicinity_text = (TextView) parent.findViewById(R.id.vicinity_text);
            main_layout = (FrameLayout) parent.findViewById(R.id.main_layout);

            main_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        // 데이터 리스트로부터 아이템 데이터 참조.
                        Uri gmmIntentUri = Uri.parse("google.streetview:cbll="+getItem(pos).getLatitude()+","+getItem(pos).getLongitude());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        context.startActivity(mapIntent);
                    }
                }
            });
        }
    }

    public void view_image(int position) {

    }
}
