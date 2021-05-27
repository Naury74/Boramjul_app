package com.naury.boramjul.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.naury.boramjul.Activity.DetailBookActivity;
import com.naury.boramjul.DTO.BookListItem;
import com.naury.boramjul.DTO.DayBookItem;
import com.naury.boramjul.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AutoScrollAdapter extends PagerAdapter {

    private Context context = null;
    int image_num = 0;

    ArrayList<DayBookItem> images = new ArrayList<DayBookItem>();

    // Context 를 전달받아 context 에 저장하는 생성자 추가.
    public AutoScrollAdapter(Context context,ArrayList<DayBookItem> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // position 값을 받아 주어진 위치에 페이지를 생성한다

        View view = null;

        if(context != null) {
            // LayoutInflater 를 통해 "/res/layout/page.xml" 을 뷰로 생성.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.auto_viewpager, container, false);

            ImageView poster_img = view.findViewById(R.id.poster_img);

            Picasso.get()
                    .load(images.get(position).getThumbnail())
                    .error(R.color.light_gray)
                    .into(poster_img);

            poster_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DayBookItem pos_item = images.get(position);

                    BookListItem item = new BookListItem(pos_item.getBook_cover(), "", pos_item.getTitle(), pos_item.getAuthor(), pos_item.getPrice(), pos_item.getScore());
                    Intent intent = new Intent(context, DetailBookActivity.class);
                    intent.putExtra("OBJECT",item);
                    context.startActivity(intent);
                }
            });

        }

        // 뷰페이저에 추가
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // position 값을 받아 주어진 위치의 페이지를 삭제한다
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        // 사용 가능한 뷰의 개수를 return 한다
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void ViewDetail(int index){
        DayBookItem pos_item = images.get(index);

        BookListItem item = new BookListItem(pos_item.getBook_cover(), "", pos_item.getTitle(), pos_item.getAuthor(), pos_item.getPrice(), pos_item.getScore());
        Intent intent = new Intent(context, DetailBookActivity.class);
        intent.putExtra("OBJECT",item);
        context.startActivity(intent);
    }
}
