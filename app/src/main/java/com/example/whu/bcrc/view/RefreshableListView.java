package com.example.whu.bcrc.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.CallSuper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.example.whu.bcrc.R;
import com.example.whu.bcrc.adapter.ExtraViewAdapter;

public class RefreshableListView extends SwipeRefreshLayout {
    public static final String TAG = "RefreshableListView";

    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_REFRESHING = 0x2;
    public static final int STATUS_LOADING_MORE = 0x1;
    public static final int STATUS_FULLY_LOADED = 0x3;

    private int loadStatus;


    private BaseLoadMoreFooterView mBaseLoadMoreFooterView;
    private Callback mCallback;
    private RecyclerView mRecyclerView;
    private Runnable mPendingRefreshRequest = null;
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (mBaseLoadMoreFooterView != null && STATUS_NORMAL == loadStatus) {

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();

                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                //这里应该等最后一个有效item显示时才加载
                if (lastVisibleItem == totalItemCount - 2) {
                    //最后可见item为最后一个item时
                    View view = layoutManager.findViewByPosition(lastVisibleItem);

                    final int[] lastVisibleItemLocation = new int[2];
                    final int[] recyclerViewLocation = new int[2];
                    view.getLocationOnScreen(lastVisibleItemLocation);
                    recyclerView.getLocationOnScreen(recyclerViewLocation);

                    final int recyclerViewBottom = recyclerViewLocation[1] + recyclerView.getHeight();
                    final int lastVisibleItemTop = lastVisibleItemLocation[1];

                    if (lastVisibleItemTop < recyclerViewBottom) {
                        //最后一个item出现了，赶紧更新
                        startLoad();
                    }
                }


            }
        }
    };

    private OnClickListener mFooterListener = v -> startLoad();

    private ExtraViewAdapter mAdapter = null;

    public RefreshableListView(Context context) {
        super(context);
        init(context, null);
    }

    public RefreshableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        int dividerColor = Color.BLACK;
        int dividerDimension = 0;
        if (attrs != null) {
            Resources.Theme theme = getContext().getTheme();
            TypedArray typedArray = theme.obtainStyledAttributes(attrs, R.styleable.RefreshableListView, 0, 0);
            int count = typedArray.getIndexCount();
            for (int i = 0; i < count; i++) {
                int styledAttr = typedArray.getIndex(i);
                if (styledAttr == R.styleable.RefreshableListView_dividerColor) {
                    dividerColor = typedArray.getColor(i, Color.BLACK);
                } else if (styledAttr == R.styleable.RefreshableListView_dividerDimension) {
                    dividerDimension = typedArray.getDimensionPixelSize(i, 0);
                }
            }
            typedArray.recycle();
        }

        setOnRefreshListener(this::startRefresh);


        mRecyclerView = new RecyclerView(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        ColorDividerListItemDecoration dividerItemDecoration = new ColorDividerListItemDecoration(context, DividerItemDecoration.VERTICAL, dividerColor, dividerDimension);
        mRecyclerView.addItemDecoration(dividerItemDecoration);


        LayoutParams lp = generateDefaultLayoutParams();
        lp.height = LayoutParams.MATCH_PARENT;
        lp.width = LayoutParams.MATCH_PARENT;
        mRecyclerView.setLayoutParams(lp);
        addView(mRecyclerView);


        mBaseLoadMoreFooterView = new SimpleLoadMoreFooterView(getContext());
        mBaseLoadMoreFooterView.setOnClickListener(mFooterListener);


        mRecyclerView.addOnScrollListener(mOnScrollListener);

        setLoadStatus(STATUS_NORMAL);
    }


    public void setLoadMoreFooterView(BaseLoadMoreFooterView baseLoadMoreFooterView) {
        if (mBaseLoadMoreFooterView != null) {
            mBaseLoadMoreFooterView.setOnClickListener(null);
        }
        mBaseLoadMoreFooterView = baseLoadMoreFooterView;
        if (mBaseLoadMoreFooterView != null) {
            mBaseLoadMoreFooterView.setOnClickListener(mFooterListener);
        }
        if (mAdapter != null && mBaseLoadMoreFooterView != null) {
            mAdapter.clearFooterView();
            mAdapter.addFooterView(mBaseLoadMoreFooterView);
            mAdapter.notifyDataSetChanged();
        }

    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public void startRefresh() {
        if (loadStatus == STATUS_REFRESHING) {
            return;
        }

        if (!isRefreshing()) {
            setRefreshing(true);
        }

        if (mPendingRefreshRequest == null) {
            Runnable runnable = () -> {

                if (!isRefreshing()) {
                    setRefreshing(true);
                }

                setLoadStatus(STATUS_REFRESHING);
                if (mCallback != null) {
                    mCallback.onRefresh();
                }
            };

            if (loadStatus == STATUS_NORMAL || loadStatus == STATUS_FULLY_LOADED) {
                runnable.run();
            } else if (loadStatus == STATUS_LOADING_MORE) {
                mPendingRefreshRequest = runnable;
            } else {
            }
        }
    }

    private void startLoad() {
        if (loadStatus == STATUS_FULLY_LOADED
                || loadStatus == STATUS_REFRESHING
                || loadStatus == STATUS_LOADING_MORE) {
            //其他三种情况都不能启动加载
            return;
        }

        setLoadStatus(STATUS_LOADING_MORE);
        if (mCallback != null) {
            mCallback.onLoadMore();
        }
    }

    public void setAdapter(ExtraViewAdapter adapter) {
        mAdapter = adapter;
        mRecyclerView.setAdapter(adapter);
        if (mBaseLoadMoreFooterView != null) {
            adapter.clearFooterView();
            adapter.addFooterView(mBaseLoadMoreFooterView);
        }
    }

    /**
     * You should call {@code setLoadComplete} before calling setFullyLoaded(true);
     */
    public void setFullyLoaded(boolean fullyLoaded) {
        final int status = loadStatus;
        if (fullyLoaded && status != STATUS_FULLY_LOADED) {
            setLoadStatus(STATUS_FULLY_LOADED);
        } else if (!fullyLoaded && status == STATUS_FULLY_LOADED) {
            setLoadStatus(STATUS_NORMAL);
        } else {
        }
    }

    public void setLoadComplete() {
        if (loadStatus == STATUS_REFRESHING) {
            setRefreshing(false);
        }
        setLoadStatus(STATUS_NORMAL);

    }

    private void setLoadStatus(int status) {
        loadStatus = status;

        if (mBaseLoadMoreFooterView != null) {
            mBaseLoadMoreFooterView.onLoadStatusChanged(status);
        }

        if (status == STATUS_NORMAL && mPendingRefreshRequest != null) {
            final Runnable runnable = mPendingRefreshRequest;
            mPendingRefreshRequest = null;
            runnable.run();
        }
    }

    @CallSuper
    public void setEnablePullRefresh(boolean enable) {
        setEnabled(enable);
    }
    @CallSuper
    public void setEnableLoadMore(boolean enable) {
        if (enable) {
            setLoadMoreFooterView(new SimpleLoadMoreFooterView(getContext()));
        } else {
            setLoadMoreFooterView(null);
        }
    }

    public LinearLayoutManager getLayoutManager() {
        return (LinearLayoutManager) mRecyclerView.getLayoutManager();
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager){
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public interface Callback {
        void onLoadMore();

        void onRefresh();
    }

}
