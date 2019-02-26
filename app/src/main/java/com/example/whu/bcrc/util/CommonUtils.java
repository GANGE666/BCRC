package com.example.whu.bcrc.util;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

import java.util.List;

public class CommonUtils {
    private CommonUtils() {}

    public static boolean isEmpty(Object... args) {
        for (Object obj: args) {
            if (obj instanceof CharSequence) {
                if (TextUtils.isEmpty((CharSequence) obj)) {
                    return true;
                }
            } else {
                if (obj == null) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean grantUriPermission(Context context, Uri uri, Intent intent) {
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        return !resInfoList.isEmpty();
    }
}
