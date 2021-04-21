package com.naury.boramjul;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class CustomAnimationLoadingDialog extends ProgressDialog {

    private Context c;
    private ImageView imgLogo;
    Animation anim;

    public CustomAnimationLoadingDialog(Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);

        c=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_loading_dialog);
        imgLogo = (ImageView) findViewById(R.id.img_logo);
        anim = AnimationUtils.loadAnimation(c, R.anim.rotate360);
        imgLogo.setAnimation(anim);
    }
    @Override
    public void show() {
        super.show();
        imgLogo.setAnimation(anim);
    }
    @Override
    public void dismiss() {
        super.dismiss();
    }
}
