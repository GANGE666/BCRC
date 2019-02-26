package com.example.whu.bcrc.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whu.bcrc.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhang.guochen on 2018/5/20.
 */

public class AppBar extends FrameLayout {
    View mView;
    @BindView(R.id.app_bar)
    FrameLayout flAppBar;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.fl_left_button)
    FrameLayout flLeftButton;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.fl_right_button)
    FrameLayout flRightButton;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_right)
    TextView tvRight;

    public AppBar(@NonNull Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public AppBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public AppBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }


    private void init(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        mView = mInflater.inflate(R.layout.view_app_bar, this, false);
        addView(mView);
        ButterKnife.bind(this);
        if (flLeftButton != null) {
            flLeftButton.setClickable(false);
        }
    }


    public void showTitle(String title) {
        showTitle(title, getResources().getColor(R.color.color_black));
    }

    public void showTitle(String title, int color) {
        tvTitle.setText(title);
        tvTitle.setTextColor(color);
        tvTitle.setTextSize(22);
    }

    public void showLeftBackButton() {
        showLeftBackButton(null);
    }

    public void showLeftBackButton(OnClickListener listener) {
        hideLeftBtn();
        ivLeft.setImageResource(R.drawable.app_bar_back_black);
        if (listener == null && getContext() instanceof Activity) {
            listener = v -> ((Activity) getContext()).finish();
        }
        flLeftButton.setOnClickListener(listener);


        ivLeft.setVisibility(View.VISIBLE);
        flLeftButton.setVisibility(View.VISIBLE);
        flLeftButton.setClickable(listener != null);
    }

    public void showLeftImageButton(int drawableResId, OnClickListener listener) {
        hideLeftBtn();
        flLeftButton.setOnClickListener(listener);
        ivLeft.setImageResource(drawableResId);
        ivLeft.setVisibility(View.VISIBLE);
        flLeftButton.setVisibility(View.VISIBLE);
        flLeftButton.setClickable(listener != null);
    }

    public void showFirstRightImageButton(int drawableResId, OnClickListener listener) {
        flRightButton.setOnClickListener(listener);
        ivRight.setImageResource(drawableResId);
        ivRight.setVisibility(View.VISIBLE);
        flRightButton.setVisibility(View.VISIBLE);
        flRightButton.setClickable(listener != null);
    }

    private void hideLeftBtn() {
        tvLeft.setVisibility(View.GONE);
        ivLeft.setVisibility(View.GONE);
        flLeftButton.setVisibility(View.GONE);
    }




}
