package com.naury.boramjul;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;

public class PointInfoActivity extends AppCompatActivity {

    ImageView membership_icon;
    TextView membership_text, PointView;
    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_info);

        membership_icon = (ImageView)findViewById(R.id.membership_icon);
        membership_text = (TextView)findViewById(R.id.membership_text);
        PointView = (TextView)findViewById(R.id.PointView);

        userInfo = new UserInfo();

        DecimalFormat format = new DecimalFormat("###,###");
        PointView.setText(format.format(Integer.parseInt(userInfo.getPoint())));

        if(userInfo.getRank().equals("브론즈")){
            membership_icon.setImageResource(R.drawable.ic_bronze_medal);
            membership_text.setText("브론즈");
        }else if(userInfo.getRank().equals("실버")){
            membership_icon.setImageResource(R.drawable.ic_silver_medal);
            membership_text.setText("실버");
        }else if(userInfo.getRank().equals("골드")){
            membership_icon.setImageResource(R.drawable.ic_gold_medal);
            membership_text.setText("골드");
        }
    }

    public void onClick_back(View v){
        finish();
    }

    public void onClick_membership_guide(View v){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PointInfoActivity.this,R.style.BottomSheetDialogTheme);
        final View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.membership_guide_bottomsheet_layout,(LinearLayout)findViewById(R.id.container_bottom_sheet));
        bottomSheetDialog.setCanceledOnTouchOutside(false);

        bottomSheetView.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }
}