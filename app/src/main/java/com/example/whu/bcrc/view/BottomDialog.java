package com.example.whu.bcrc.view;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.whu.bcrc.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BottomDialog {

    @BindView(R.id.app_bar)
    AppBar mAppBar;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.ll_content)
    LinearLayout llContent;


    private FrameLayout mContainer;
    private PopupWindow mWindow;

    private BottomDialog(Builder builder) {
        init(builder);
    }

    private void init(Builder builder) {
        mContainer = (FrameLayout) builder.activity.findViewById(android.R.id.content);

        View view = View.inflate(builder.activity, R.layout.view_bottom_dialog, null);
        mWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        mWindow.setAnimationStyle(R.style.menuShows);
        ButterKnife.bind(this, view);
        if (builder.mWrapContent) {
            ViewGroup.LayoutParams lp = llContent.getLayoutParams();
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            llContent.setLayoutParams(lp);
        }
        if (!builder.mShowAppBar) {
            mAppBar.setVisibility(View.GONE);
        } else {
            mAppBar.setVisibility(View.VISIBLE);
            if (builder.mTitle != null) {
                mAppBar.showTitle(builder.mTitle);
            }
            if (builder.mLeftIconResId != -1) {
                mAppBar.showLeftImageButton(builder.mLeftIconResId, v -> builder.mLeftListener.onClick(this, v));
            }
            if (builder.mLeftText != null) {
                //mAppBar.showLeftTextButton(builder.mLeftText, v -> builder.mLeftListener.onClick(this, v));
            }
            if (builder.mRightIconResId != -1) {
                mAppBar.showFirstRightImageButton(builder.mRightIconResId, v -> builder.mRightListener.onClick(this, v));
            }
            if (builder.mRightText != null) {
                //mAppBar.showFirstRightTextButton(builder.mRightText, v -> builder.mRightListener.onClick(this, v));
            }
        }
        if (builder.mContentLayoutResId != -1) {
            View content = LayoutInflater.from(builder.activity).inflate(builder.mContentLayoutResId, flContainer, true);
            if (builder.mCallback != null) {
                builder.mCallback.onCreate(this, content);
            }
        }
        if (builder.mContentView != null) {
            flContainer.addView(builder.mContentView);
            if (builder.mCallback != null) {
                builder.mCallback.onCreate(this, builder.mContentView);
            }
        }
    }

    public void show() {
        if (mWindow != null && !mWindow.isShowing()) {
            mWindow.showAtLocation(mContainer, Gravity.BOTTOM, 0, 0);
        }
    }

    public void dismiss() {
        if (mWindow != null && mWindow.isShowing()) {
            mWindow.dismiss();
        }
    }

    public boolean isShowing() {
        return mWindow != null && mWindow.isShowing();
    }

    @OnClick(R.id.view_empty_space)
    public void onClick() {
        dismiss();
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (!super.dispatchTouchEvent(ev)) {
//            int[] location = new int[2];
//            mAppBar.getLocationOnScreen(location);
//            float y = ev.getRawY();
//            if (y < location[1]) {
//                dismiss();
//            }
//        }
//        return true;
//    }

    public static class Builder {
        private final Activity activity;
        private String mTitle = null;
        private int mLeftIconResId = -1;
        private int mRightIconResId = -1;
        private String mLeftText = null;
        private String mRightText = null;
        private int mContentLayoutResId = -1;
        private View mContentView = null;
        private OnClickListener mLeftListener = null;
        private OnClickListener mRightListener = null;
        private boolean mShowAppBar = false;
        private Callback mCallback = null;
        public boolean mWrapContent = false;

        public Builder(@NonNull Activity activity) {
            this.activity = activity;
        }

        /**
         * 设置dialog的title
         */
        public Builder title(String title) {
            mTitle = title;
            return this;
        }

        /**
         * 设置dialog左边的icon
         */
        public Builder leftIcon(int resId) {
            mLeftIconResId = resId;
            mLeftText = null;
            return this;
        }

        /**
         * dialog高度设置为wrap_content
         */
        public Builder wrapContent() {
            mWrapContent = true;
            return this;
        }

        /**
         * 显示dialog的AppBar
         */
        public Builder showAppBar() {
            mShowAppBar = true;
            return this;
        }

        /**
         * 设置dialog右边的icon
         */
        public Builder rightIcon(int resId) {
            mRightIconResId = resId;
            mRightText = null;
            return this;
        }

        /**
         * 在dialog左边设置一个文字按钮
         */
        public Builder leftTextButton(String s) {
            mLeftIconResId = -1;
            mLeftText = s;
            return this;
        }

        /**
         * 在dialog右边设置一个文字按钮
         */
        public Builder rightTextButton(String s) {
            mRightIconResId = -1;
            mRightText = s;
            return this;
        }

        /**
         * 设置dialog左边的按键回调
         */
        public Builder leftButtonListener(OnClickListener listener) {
            mLeftListener = listener;
            return this;
        }


        /**
         * 设置dialog右边的按键回调
         */
        public Builder rightButtonListener(OnClickListener listener) {
            mRightListener = listener;
            return this;
        }

        /**
         * 设置dialog下方内容的layout
         */
        public Builder contentLayout(int resId) {
            mContentLayoutResId = resId;
            mContentView = null;
            return this;
        }

        /**
         * 设置dialog下方内容inflate时的回调，用于绑定
         */
        public Builder callback(Callback callback) {
            mCallback = callback;
            return this;
        }

        /**
         * 设置dialog下方内容的view
         */
        public Builder contentView(View v, FrameLayout.LayoutParams layoutParams) {
            mContentView = v;
            mContentView.setLayoutParams(layoutParams);
            mContentLayoutResId = -1;
            return this;
        }

        public BottomDialog build() {
            return new BottomDialog(this);
        }
    }

    public interface Callback {
        void onCreate(BottomDialog dialog, View content);
    }

    public interface OnClickListener {
        void onClick(BottomDialog dialog, View v);
    }
}
