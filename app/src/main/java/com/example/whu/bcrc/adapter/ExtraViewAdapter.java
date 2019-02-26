package com.example.whu.bcrc.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.example.whu.bcrc.holder.CommonHeaderItemHolder;
import com.example.whu.bcrc.holder.CommonViewHolder;

import java.util.ArrayList;

/**
 * 支持增添多个额外的header以及footer
 * @param <T> 数据类型
 * @param <VH> 对应的ViewHolder类型
 */
public class ExtraViewAdapter<T, VH extends CommonViewHolder<T>> extends CommonRecyclerAdapter<T, VH> {

    private static final int HEADER_ITEM_TYPE = -1;
    private static final int FOOTER_ITEM_TYPE = -2;
    private static final int COMMON_ITEM_TYPE = 1;
    private final ArrayList<View> mHeaderViews;
    private final ArrayList<View> mFooterViews;

    public ExtraViewAdapter(@NonNull CommonViewHolder.ViewHolderCreator<VH> creator) {
        super(creator);
        mHeaderViews = new ArrayList<>();
        mFooterViews = new ArrayList<>();
    }
    public void clearHeaderView() {
        mHeaderViews.clear();
        notifyDataSetChanged();
    }

    public void addHeaderView(View view) {
        mHeaderViews.add(view);
        notifyDataSetChanged();
    }

    public void clearFooterView() {
        mFooterViews.clear();
        notifyDataSetChanged();
    }

    public void addFooterView(View view) {
        mFooterViews.add(view);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + mHeaderViews.size() + mFooterViews.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (0 <= position && position < mHeaderViews.size()) {
            return HEADER_ITEM_TYPE;
        }

        int count = getItemCount();
        if (count - mFooterViews.size() <= position && position < count) {
            return FOOTER_ITEM_TYPE;
        }

        return COMMON_ITEM_TYPE;
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        if (0 <= position && position < mHeaderViews.size()) {
            ((CommonHeaderItemHolder) holder).setView(mHeaderViews.get(position));
            return;
        }

        int count = getItemCount();
        if (count - mFooterViews.size() <= position && position < count) {
            ((CommonHeaderItemHolder) holder).setView(mFooterViews.get(position - count + mFooterViews.size()));
            return;
        }


        position -= mHeaderViews.size();
        super.onBindViewHolder(holder, position);
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (HEADER_ITEM_TYPE == viewType || FOOTER_ITEM_TYPE == viewType) {
            return new CommonHeaderItemHolder(parent.getContext(), parent);
        }

        return super.onCreateViewHolder(parent, viewType);
    }


    public int getItemPositionByIndex(int index) {
        return index + mHeaderViews.size();
    }
}
