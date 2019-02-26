package com.example.whu.bcrc.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whu.bcrc.R;
import com.example.whu.bcrc.activity.ProfileActivity;
import com.example.whu.bcrc.util.ImageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends Fragment {
    Unbinder unbinder;

    @BindView(R.id.iv_avatar)
    ImageView ivAvata;
    @BindView(R.id.tv_user_nick_name)
    TextView tvUserNickName;
    @BindView(R.id.iv_headimg_mohu)
    ImageView ivHeadImg;




    public MyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestPageData();
    }

    private void requestPageData() {
        // TODO: 2018/5/26 从数据库获取本页面所需的用户信息
        /*去获取当前用户的个人信息和头像
            放在onResume中是为了在更改了用户头像或者在当前页的信息之后，再回到整个页面能够进行更新
         */
        String strImg = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1526969063249&di=aa752b1b58391d2d2b7ed0329fe1bf19&imgtype=0&src=http%3A%2F%2Fcompass.xbox.com%2Fassets%2Fcc%2F21%2Fcc21c6a5-c489-45ad-ab8c-7c0580595720.jpg%3Fn%3DSWBF-GLP_hero-mobile_768x768.jpg";
        ImageUtil.showAvatarUsingGlide(getContext(), strImg, ivAvata);
        ImageUtil.showBlurUsingGlide(getContext(), strImg,ivHeadImg);
        tvUserNickName.setText("UserTest");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_avatar, R.id.iv_headimg_mohu})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_avatar:
            case R.id.iv_headimg_mohu:
                startActivity(new Intent(getActivity(), ProfileActivity.class));
                break;


        }
    }
}
