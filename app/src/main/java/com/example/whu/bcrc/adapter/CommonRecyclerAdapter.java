package com.example.whu.bcrc.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import  com.example.whu.bcrc.holder.CommonViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CommonRecyclerAdapter<T, VH extends CommonViewHolder<T>> extends RecyclerView.Adapter<CommonViewHolder> {
    private final CommonViewHolder.ViewHolderCreator<VH> mViewHolderCreator;
    private List<T> dataList = new ArrayList<>();
    private OnItemClickListener<T, VH> mOnItemClickListener;
    private OnItemLongClickListener<T, VH> mOnItemLongClickListener;
    private OnItemEventListener mOnItemEventListener;

    public CommonRecyclerAdapter(@NonNull CommonViewHolder.ViewHolderCreator<VH> creator) {
        this.mViewHolderCreator = creator;
    }

    public List<T> getDataList() {
        return dataList;
    }

    /**
     * 设置数据，会主动更新页面
     *
     * @param data 新数据
     */
    public void setDataList(List<T> data) {
        dataList.clear();
        if (null != data) {
            dataList.addAll(data);
        }
        notifyDataSetChanged();
    }

    /**
     * 添加数据，会主动更新页面
     * 默认在最后插入
     *
     * @param data 新数据
     */
    public void addDataList(List<T> data) {
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(T data) {
        dataList.add(data);
        notifyDataSetChanged();
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mViewHolderCreator.createByViewGroupAndType(parent, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        if (position >= 0 && position < dataList.size()) {
            final T data = dataList.get(position);
            //需要在bindData之前设置OnClickListener
            //因为设置OnClickListener会改变ItemView的Clickable属性，应当避免
            if (mOnItemClickListener != null) {
                holder.setOnClickListener(v -> mOnItemClickListener.onItemClick(data, (VH) holder, v));
            }
            if (mOnItemLongClickListener != null) {
                holder.setOnLongClickListener(v -> mOnItemLongClickListener.onItemLongClick(data, (VH) holder, v));
            }
            if (mOnItemEventListener != null) {
                holder.setCallback(event -> mOnItemEventListener.onItemEvent(data, (VH) holder, event));
            }
            holder.setData(data);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public interface OnItemClickListener<T, VH extends CommonViewHolder<T>> {
        void onItemClick(T t, VH holder, View itemView);
    }

    public interface OnItemLongClickListener<T, VH extends CommonViewHolder<T>> {
        boolean onItemLongClick(T t, VH holder, View itemView);
    }

    public interface OnItemEventListener<T, VH extends CommonViewHolder<T>> {
        void onItemEvent(T t, VH holder, int event);
    }

    public void setOnItemClickListener(OnItemClickListener<T, VH> listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T, VH> listener) {
        this.mOnItemLongClickListener = listener;
    }

    public void setItemEventCallback(OnItemEventListener listener) {
        this.mOnItemEventListener = listener;
    }
}