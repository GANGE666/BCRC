package com.example.whu.bcrc.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.whu.bcrc.R;
import com.example.whu.bcrc.util.Tools;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 闪屏Activity，可以在这里判断是否是第一次打开，
 * 还可以在这里去服务器获取最新的版本信息并进行对比，提醒是否下载更新
 */
public class SplashActivity extends BaseActivity {
    public static final long SPLASH_DURATION = TimeUnit.SECONDS.toMillis(2);


    private String mVersionName;
    private SharedPreferences mSharedPreferences;
    private long mEnterTime;
    private Handler mHandler;

    @BindView(R.id.iv_splash_img)
    ImageView ivSplashImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mVersionName = Tools.getVerName(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mHandler = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEnterTime = System.currentTimeMillis();
        showSplashAndRedirect();
    }

    /**
     * 跳转意图，根据本地保存版本号判断是否更新版本了
     * 如果有更新版本则从服务器获取图片进行跳转引导页，如果未更新版本则跳转主页
     */
    private void showSplashAndRedirect() {
        ivSplashImg.setImageResource(R.drawable.welcome);//设置闪屏图片

        final boolean isFirstRun = !mSharedPreferences.getString("versionName", "").equals(mVersionName);
        boolean hasCurrentUser = false;  //后期应去获取当前的User，User可以卸载Application中作为一个全局可访问的对象
        if (!isFirstRun) {
            //老用户
            // TODO: 2018/5/26 获取当前User
            if (hasCurrentUser) {
                redirectToMainActivity();
            } else {
                //未登录
                redirectToLoginActivity();
            }
        } else {
            //新用户
            mSharedPreferences.edit().putString("versionName", mVersionName).apply();
            redirectToLoginActivity();
        }
    }

    private void redirectToMainActivity() {
        redirect(new Intent(SplashActivity.this, MainActivity.class));
    }

    private void redirectToLoginActivity() {
        redirect(new Intent(SplashActivity.this, LoginActivity.class));

    }

    private void redirect(Intent intent) {
        long duration = SPLASH_DURATION - (System.currentTimeMillis() - mEnterTime);
        if (duration < 0) duration = 0;
        mHandler.postDelayed(
                () -> {
                    if (!isFinishing() && !isDestroyed()) {
                        startActivity(intent);
                        finish();
                    }
                },
                duration);
    }
}
