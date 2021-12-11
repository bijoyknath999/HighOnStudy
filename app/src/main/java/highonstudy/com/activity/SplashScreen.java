package highonstudy.com.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import highonstudy.com.R;
import highonstudy.com.utility.AppUtils;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000;
    private Context mContext;
    private Activity mActivity;
    private RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mContext = getApplicationContext();
        mActivity = SplashScreen.this;

        rootLayout = findViewById(R.id.splashScreen);

        if (Build.VERSION.SDK_INT>15)
        {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(uiOptions);

        }

    }

    private void initFunctionality() {
        if (AppUtils.isNetworkAvailable(mContext)) {
            rootLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            }, SPLASH_DURATION);
        } else {
            AppUtils.noInternetWarning(rootLayout, mContext);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFunctionality();
    }
}