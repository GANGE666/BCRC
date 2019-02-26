package com.example.whu.bcrc.util;

import android.content.Context;
import android.widget.Toast;

import java.lang.ref.WeakReference;


/**
 * 优化Toast的显示，多次调用的情况下只显示最新的Toast
 */
public class ToastUtil {
    private static WeakReference<Toast> mToast = null;

    public static void showToast(Context context, String toastText, int duration) {
        if (mToast != null && mToast.get() != null) {
            mToast.get().cancel();
        }
        Toast now = Toast.makeText(context, toastText, duration);
        mToast = new WeakReference<>(now);
        now.show();
    }

    public static void showToast(Context context, String toastText) {
        if (mToast != null && mToast.get() != null) {
            mToast.get().cancel();
        }
        Toast now = Toast.makeText(context, toastText, Toast.LENGTH_SHORT);
        mToast = new WeakReference<>(now);
        now.show();
    }

    public static void showToast(Context context, int resId) {

        if (mToast != null && mToast.get() != null) {
            mToast.get().cancel();
        }
        Toast now = Toast.makeText(context, context.getText(resId), Toast.LENGTH_SHORT);
        mToast = new WeakReference<>(now);
        now.show();
    }
}
