package com.example.whu.bcrc.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class CommonViewHolder<T> extends RecyclerView.ViewHolder {

    private OnItemEventListener mOnItemEventListener;
    private T mData;

    public CommonViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(LayoutInflater.from(context).inflate(layoutRes, root, false));
    }

    public CommonViewHolder(Context context, View view) {
        super(view);
    }

    protected View getItemView() {
        return itemView;
    }

    protected Context getContext() {
        return itemView.getContext();
    }

    public abstract void bindData(T t);

    public void setData(T t) {
        mData = t;
        bindData(t);
    }

    /**
     * 为ItemView设置一个OnClickListener
     * 注意：设置OnClickListener会导致ItemView变得Clickable！！
     */
    public void setOnClickListener(View.OnClickListener listener) {
        getItemView().setOnClickListener(listener);
    }

    /**
     * 为ItemView设置一个OnClickListener
     * 注意：设置OnClickListener会导致ItemView变得Clickable！！
     */
    public void setOnLongClickListener(View.OnLongClickListener listener) {
        getItemView().setOnLongClickListener(listener);
    }

    public void setCallback(OnItemEventListener onItemEventListener) {
        this.mOnItemEventListener = onItemEventListener;
    }

    public void invokeCallback(int event) {
        if (mOnItemEventListener != null) {
            mOnItemEventListener.onItemEvent(event);
        }
    }

    public interface ViewHolderCreator<VH> {
        VH createByViewGroupAndType(ViewGroup parent, int viewType);
    }

    public interface OnItemEventListener {
        void onItemEvent(int event);
    }

}