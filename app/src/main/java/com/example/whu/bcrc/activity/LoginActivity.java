package com.example.whu.bcrc.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.whu.bcrc.R;
import com.example.whu.bcrc.util.StatusBarUtil;
import com.example.whu.bcrc.util.ToastUtil;
import com.example.whu.bcrc.util.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    private boolean isRemember;
    private static final String PHONE_NUM = "phoneNum";
    private static final String REMEMBER_PASSWORD = "rememberPassword";
    private static final String PASSWORD = "password";

    @BindView(R.id.ed_phone)
    EditText edPhone;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.iv_remember_password)
    ImageView ivRemember;
    @BindView(R.id.iv_weixin)
    ImageView ivWeixin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
        setDoubleBackEnable();
    }


    private void init() {
        SharedPreferences logInInfo = getSharedPreferences("logInInfo", 0); //利用SharedPreferences来存储和获取是否记住密码和手机号密码
        isRemember = logInInfo.getBoolean(REMEMBER_PASSWORD, true);  //默认记住密码
        String lastPhoneNum = logInInfo.getString(PHONE_NUM, "");
        edPhone.setText(lastPhoneNum);
        setImageOfRememberPsw(isRemember);
        if (isRemember){
            String lastPassword = logInInfo.getString(PASSWORD, "");
            edPassword.setText(lastPassword);
        }
    }

    public void setImageOfRememberPsw(boolean isRemember) {
        ivRemember.setImageResource(isRemember ? R.drawable.box_check : R.drawable.box);
    }

    @OnClick({R.id.tv_register, R.id.tv_forget_password, R.id.iv_remember_password, R.id.bt_login})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.iv_remember_password:
                isRemember = !isRemember;
                setImageOfRememberPsw(isRemember);
                break;
            case R.id.bt_login:
                if (Tools.isNull(edPhone)){
                    ToastUtil.showToast(this, R.string.input_phone_num);
                    return;
                }
                if (Tools.isNull(edPassword)){
                    ToastUtil.showToast(this, R.string.input_psw);
                    return;
                }
                if (!Tools.validatePhone(Tools.getText(edPhone))){
                    ToastUtil.showToast(this, R.string.wrong_form_of_phone_num);
                    return;
                }
                doLogin(Tools.getText(edPhone), Tools.getText(edPassword));
                break;
        }
    }

    private void doLogin(String phoneNum, String password) {
        //进行登陆操作，根据返回的结果确定是跳转到主页面还是返回错误

        //将更新的手机号和密码记录下来
        SharedPreferences logInInfo = getSharedPreferences("logInInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = logInInfo.edit();
        editor.putString(PHONE_NUM,Tools.getText(edPhone));
        editor.putString(PASSWORD,Tools.getText(edPassword));
        editor.putBoolean(REMEMBER_PASSWORD,isRemember);
        editor.apply();
        startActivityOnNewStack(MainActivity.class);

    }
}
