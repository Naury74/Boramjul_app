package com.naury.boramjul;

import android.app.Activity;
import android.widget.Toast;

public class BackPressCloseHendler {

    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;//해당 액티비티 감지

    public BackPressCloseHendler(Activity context) {
        this.activity = context;
    }
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {//2초내에 백버튼 다시 클릭시 토스트 출력
            backKeyPressedTime = System.currentTimeMillis();
            //activity.setResult(Activity.RESULT_CANCELED);
            //ActivityCompat.finishAffinity(activity);
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
            toast.cancel();
        }

    }
    public void showGuide() {
        toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }

}
