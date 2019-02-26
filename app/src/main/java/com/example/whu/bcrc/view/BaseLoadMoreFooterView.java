package com.example.whu.bcrc.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.whu.bcrc.R;

public abstract class BaseLoadMoreFooterView extends FrameLayout{

    public BaseLoadMoreFooterView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public BaseLoadMoreFooterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseLoadMoreFooterView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseLoadMoreFooterView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setBackgroundResource(R.drawable.bg_clickable);
        onCreate(context);
    }

    protected abstract void onCreate(Context context);

    public abstract void onLoadStatusChanged(int status);
}
