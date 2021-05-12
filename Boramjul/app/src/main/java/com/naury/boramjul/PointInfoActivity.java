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

public class PointInfoActivity extends AppCompatActivity {

    ImageView membership_icon;
    TextView membership_text;
    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_info);

        membership_icon = (ImageView)findViewById(R.id.membership_icon);
        membership_text = (TextView)findViewById(R.id.membership_text);

        userInfo = new UserInfo();

        if(userInfo.getRank().equals("1")){
            membership_icon.setImageResource(R.drawable.ic_bronze_medal);
            membership_text.setText("브론즈");
        }else if(userInfo.getRank().equals("2")){
            membership_icon.setImageResource(R.drawable.ic_silver_medal);
            membership_text.setText("실버");
        }else if(userInfo.getRank().equals("3")){
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