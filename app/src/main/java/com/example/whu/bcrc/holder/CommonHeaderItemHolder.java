package com.example.whu.bcrc.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.whu.bcrc.R;


public class CommonHeaderItemHolder extends CommonViewHolder {
    LinearLayout rootLayout;

    public CommonHeaderItemHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.common_header_item_layout);
        rootLayout = (LinearLayout) itemView.findViewById(R.id.common_header_root_view);
    }

    public void setView(View view) {
        rootLayout.removeAllViews();
        if (null != view) {
            rootLayout.addView(view);
        }
    }

    @Override
    public void bindData(Object o) {
        throw new RuntimeException("Not allow to call CommonFooterItemHolder.bindData");
    }
}
