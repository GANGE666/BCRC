package com.example.whu.bcrc.view;


import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;

public class RemovableLoadMoreFooterView extends SimpleLoadMoreFooterView{
    public RemovableLoadMoreFooterView(@NonNull Context context) {
        super(context);
    }

    public RemovableLoadMoreFooterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RemovableLoadMoreFooterView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RemovableLoadMoreFooterView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLoadStatusChanged(int status) {
        super.onLoadStatusChanged(status);
        if (status == RefreshableListView.STATUS_FULLY_LOADED) {
            setVisibility(GONE);
        } else if (status == RefreshableListView.STATUS_NORMAL || status == RefreshableListView.STATUS_LOADING_MORE) {
            setVisibility(VISIBLE);
        }
    }
}
