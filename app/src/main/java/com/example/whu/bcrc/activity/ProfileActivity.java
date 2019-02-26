package com.example.whu.bcrc.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whu.bcrc.R;
import com.example.whu.bcrc.util.ImageUtil;
import com.example.whu.bcrc.util.StorageUtils;
import com.example.whu.bcrc.view.AppBar;
import com.example.whu.bcrc.view.BottomDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends PhotoActivity  implements View.OnClickListener, BottomDialog.OnClickListener{
    private static final String TAG = "ProfileActivity";

    @BindView(R.id.app_bar)
    AppBar mAppBar;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        mAppBar.showLeftBackButton();
        mAppBar.showTitle("个人信息");
    }
    @OnClick({R.id.rl_avatar, R.id.rl_nickname, R.id.rl_password_change, R.id.rl_marker_manage})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_avatar:
                onChooseAvatar(); //选择头像
                break;
            case R.id.rl_nickname:
                //startActivity(new Intent(getApplicationContext(), NicknameChangeActivity.class));
                break;
            case R.id.rl_password_change:
                //startActivity(new Intent(getApplicationContext(), PasswordChangeActivity.class));
                break;
            case R.id.rl_marker_manage:
                //startActivity(new Intent(getApplicationContext(), MarkerActivity.class));
                break;
        }
    }

    private void onChooseAvatar() {
        new BottomDialog
                .Builder(this)
                .contentLayout(R.layout.dialog_photo)
                .wrapContent()
                .callback((dialog, content) -> {
                    content.findViewById(R.id.tv_take_photo).setOnClickListener(view -> onClick(dialog, view));
                    content.findViewById(R.id.tv_choose_photo).setOnClickListener(view -> onClick(dialog, view));
                    content.findViewById(R.id.btn_cancel).setOnClickListener(view -> onClick(dialog, view));
                })
                .build()
                .show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //在这里去获取用户的信息并进行展示，这里取了一个固定的图片，之后应该获取到UserBean并进行赋值操作
        String strImg = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1526969063249&di=aa752b1b58391d2d2b7ed0329fe1bf19&imgtype=0&src=http%3A%2F%2Fcompass.xbox.com%2Fassets%2Fcc%2F21%2Fcc21c6a5-c489-45ad-ab8c-7c0580595720.jpg%3Fn%3DSWBF-GLP_hero-mobile_768x768.jpg";
        ImageUtil.showAvatarUsingGlide(this, strImg, ivAvatar);
        tvNickName.setText("UserName");
        tvPhoneNumber.setText("PhoneNum");

    }

    @Override
    public void onClick(BottomDialog dialog, View v) {
        if (dialog != null) {
            dialog.dismiss();
        }
        switch (v.getId()) {
            case R.id.tv_take_photo:
                try {
                    startCapturePhotoActivity();
                } catch (Exception e) {
                    Log.e(TAG,e.toString());
                }
                break;
            case R.id.tv_choose_photo:
                try {
                    startChoosePhotoActivity();
                } catch (Exception e) {
                }
                break;
        }
    }

    /**
     * 这部分是获取照片，需要重构，有点混乱
     * @param uri
     */
    @Override
    protected void onPhotoCaptured(Uri uri) {
        String dir = StorageUtils.getTempDirectory(this);
        if (dir == null) {
            Toast.makeText(this, "无法创建目录", Toast.LENGTH_SHORT).show();
            return;
        }
        File file;
        try {
            file = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".jpg", new File(dir));
        } catch (IOException e) {
            Toast.makeText(this, "无法创建文件", Toast.LENGTH_SHORT).show();
            return;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            //拷贝到本地
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        onPhotoChosen(file.getAbsolutePath());
    }

    @Override
    protected void onPhotoChosen(String path) {
        // TODO: 2018/5/26 拿到图片的路径之后，设置用户的头像，进行数据库操作更新用户的头像
        Log.d(TAG,path);
    }
}
