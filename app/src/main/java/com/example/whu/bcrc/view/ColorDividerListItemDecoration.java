package com.example.whu.bcrc.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DividerItemDecoration;

public class ColorDividerListItemDecoration extends DividerItemDecoration {
    public ColorDividerListItemDecoration(Context context, final int orientation, int color, final int dimension) {
        super(context, orientation);
        setDrawable(new ColorDrawable(color) {
            @Override
            public int getIntrinsicWidth() {
                if (orientation == HORIZONTAL) {
                    return dimension;
                } else {
                    return super.getIntrinsicWidth();
                }
            }

            @Override
            public int getIntrinsicHeight() {
                if (orientation == VERTICAL) {
                    return dimension;
                } else {
                    return super.getIntrinsicHeight();
                }
            }
        });
    }
}
