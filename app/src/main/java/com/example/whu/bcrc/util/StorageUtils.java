package com.example.whu.bcrc.util;


import android.content.Context;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;


import com.example.whu.bcrc.Configuration;

import java.io.File;

import javax.annotation.CheckForNull;

/**
 * 三种路径，按优先级获取
 * 1.外部私有路径 Context.getExternal***Dir()
 * 2.外部公有路径 Environment.getExternalStorageDirectory() +  "/"
 * 3.内部私有路径 Context.get***Dir()
 * <p>
 * 另外分为缓存路径与文件路径，路径之下有独立的文件夹，务必在下方以DIR_***声明
 */
public class StorageUtils {
    public static final String AUTHORITY = Configuration.getFileProviderAuthority();
    private static final String DIR_TEMP = "temp";
    private static final String DIR_IMAGE = "image";
    private static final String DIR_LOG = "log";
    private static final String DIR_DOWN = "download";


    /**
     * @return 一个可用的临时目录的路径，将位于cache目录中
     */
    public static String getTempDirectory(Context context) {
        return getCacheDirectory(context, DIR_TEMP);
    }

    /**
     * @return 一个用于缓存图像的目录，将位于cache目录中
     */
    public static String getImageCacheDirectory(Context context) {
        return getCacheDirectory(context, DIR_IMAGE);
    }

    /**
     * @param subDir 子目录名
     * @return 一个用于储存日志的目录，将位于file目录中
     */
    @CheckForNull
    @Nullable
    public static String getLogDirectory(Context context, String subDir) {
        File file = new File(getFileDirectory(context, DIR_LOG), subDir);
        if (file.exists() || (file.mkdirs() && file.exists())) {
            return file.getAbsolutePath();
        } else {
            return null;
        }
    }

    /**
     * @param subDir 子目录名
     * @return 一个用于储存下载文件的目录，将位于file目录中
     */
    @CheckForNull
    @Nullable
    public static String getDownloadDirectory(Context context, String subDir) {
        File file = new File(getFileDirectory(context, DIR_DOWN), subDir);
        if (file.exists() || (file.mkdirs() && file.exists())) {
            return file.getAbsolutePath();
        } else {
            return null;
        }
    }
    /**
     * @return 一个位于cache目录的子目录
     */
    @CheckForNull
    @Nullable
    private static String getCacheDirectory(Context context, String dir) {
        String path;
        if (hasExternalStorage()) {
            //有外部储存
            File externalPrivateCacheDir = context.getExternalCacheDir();
            if (externalPrivateCacheDir != null) {
                path = externalPrivateCacheDir.getAbsolutePath();
            } else {
                path = getExternalPublicDirectory("cache");
            }
        } else {
            path = context.getCacheDir().getAbsolutePath();
        }
        if (path == null) {
            return null;
        }
        File file = new File(path, dir);
        if (!(file.exists() || (file.mkdirs() && file.exists()))) {
            return null;
        }
        return file.getAbsolutePath();
    }

    /**
     * @return 一个位于file目录的子目录
     */
    @CheckForNull
    @Nullable
    private static String getFileDirectory(Context context, String dir) {
        String path;
        if (hasExternalStorage()) {
            //有外部储存
            File externalPrivateFileDir = context.getExternalFilesDir(null);
            if (externalPrivateFileDir != null) {
                path = externalPrivateFileDir.getAbsolutePath();
            } else {
                path = getExternalPublicDirectory("file");
            }
        } else {
            path = context.getFilesDir().getAbsolutePath();
        }
        if (path == null) {
            return null;
        }
        File file = new File(path, dir);
        if (!(file.exists() || (file.mkdirs() && file.exists()))) {
            return null;
        }
        return file.getAbsolutePath();
    }

    public static boolean hasExternalStorage() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    @CheckForNull
    @Nullable
    public static String getExternalPublicDirectory(String name) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Argar/" + name);
        if (!(file.exists() || (file.mkdirs() && file.exists()))) {
            return null;
        }
        return file.getAbsolutePath();
    }

    public static boolean isEmptyFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return true;
        }
        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (!file.isFile()) {
            return true;
        }
        if (file.length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean hasChangedAfter(String path, long time) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        if (file.lastModified() <= time) {
            return false;
        }
        return true;
    }

}
