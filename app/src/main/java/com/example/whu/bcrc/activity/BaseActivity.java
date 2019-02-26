package com.example.whu.bcrc.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.whu.bcrc.R;
import com.example.whu.bcrc.util.StatusBarUtil;
import com.example.whu.bcrc.util.ToastUtil;

public class BaseActivity extends AppCompatActivity {

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();
        super.onCreate(savedInstanceState);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.StatusFullBarLightMode(this);
        //设置沉浸式
        // TODO: 2018/5/26 后期需要根据当前的Android版本进行判定适配,否则会在低版本上出现顶部不适配的问题
    }

    // 判断是否已经点击过一次回退键
    private boolean isDoubleBackEnable = false;
    private boolean isBackPressed = false;
    private String mDoubleBackMessage = null;

    protected void setDoubleBackEnable() {
        isDoubleBackEnable = true;
    }

    protected void setDoubleBackEnable(String message) {
        isDoubleBackEnable = true;
        mDoubleBackMessage = message;
    }

    @Override
    public void onBackPressed() {
        if (isDoubleBackEnable) {
            doubleBack();
        } else {
            super.onBackPressed();
        }
    }


    /**
     * 双击退出
     */
    private void doubleBack() {
        if (!isBackPressed) {
            isBackPressed = true;
            ToastUtil.showToast(getApplicationContext(), mDoubleBackMessage == null ? "再次点击返回退出程序" : mDoubleBackMessage);
        } else {
            finish();
        }
        new Handler().postDelayed(() -> isBackPressed = false, 3000);
    }

    /**
     * 在新的Task栈中开启Activity
     * @param clazz：跳转到的Activity
     */
    protected void startActivityOnNewStack(Class<? extends Activity> clazz) {
        Intent intent = new Intent(this, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public static Context GetContext(){
        return context;
    }
}
