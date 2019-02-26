package com.example.whu.bcrc.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.whu.bcrc.R;


public class SimpleLoadMoreFooterView extends BaseLoadMoreFooterView {

    protected LinearLayout llLoadMoreFooterLoading;
    protected TextView tvLoadMoreFooterHint;
    protected TextView tvLoadMoreFooterFullyLoaded;

    public SimpleLoadMoreFooterView(@NonNull Context context) {
        super(context);
    }

    public SimpleLoadMoreFooterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleLoadMoreFooterView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SimpleLoadMoreFooterView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onCreate(Context context) {
        LayoutInflater.from(context).inflate(R.layout.load_more_footer_layout, this, true);
        llLoadMoreFooterLoading = (LinearLayout) findViewById(R.id.ll_load_more_footer_loading);
        tvLoadMoreFooterHint = (TextView) findViewById(R.id.tv_load_more_footer_hint);
        tvLoadMoreFooterFullyLoaded = (TextView) findViewById(R.id.tv_load_more_footer_fully_loaded);
    }

    @Override
    public void onLoadStatusChanged(int status) {
        switch (status) {
            case RefreshableListView.STATUS_FULLY_LOADED:
                llLoadMoreFooterLoading.setVisibility(GONE);
                tvLoadMoreFooterHint.setVisibility(GONE);
                tvLoadMoreFooterFullyLoaded.setVisibility(VISIBLE);
                setClickable(false);
                break;
            case RefreshableListView.STATUS_LOADING_MORE:
                llLoadMoreFooterLoading.setVisibility(VISIBLE);
                tvLoadMoreFooterHint.setVisibility(GONE);
                tvLoadMoreFooterFullyLoaded.setVisibility(GONE);
                setClickable(false);
                break;
            case RefreshableListView.STATUS_NORMAL:
                llLoadMoreFooterLoading.setVisibility(GONE);
                tvLoadMoreFooterHint.setVisibility(VISIBLE);
                tvLoadMoreFooterFullyLoaded.setVisibility(GONE);
                setClickable(true);
                break;
        }
    }

}
