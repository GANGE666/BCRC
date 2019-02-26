package com.example.whu.bcrc.activity;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.util.Log;

import com.example.whu.bcrc.util.CommonUtils;
import com.example.whu.bcrc.util.StorageUtils;

import java.io.File;
import java.io.IOException;

public class PhotoActivity extends BaseActivity {

    private static final int REQUEST_CODE_IMAGE_CAPTURE = 0x99;
    private static final int REQUEST_CODE_IMAGE_CHOOSE = 0xff;
    private String mImageCaptureFilePath = null;
    private long mImageCaptureFileTimeStamp = -1;
    private static final String IMAGE_CAPTURE_FILE_PATH = "image_capture_file_path";
    private static final String IMAGE_CAPTURE_FILE_TIME_STAMP = "image_capture_file_time_stamp";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(IMAGE_CAPTURE_FILE_PATH, mImageCaptureFilePath);
        outState.putLong(IMAGE_CAPTURE_FILE_TIME_STAMP, mImageCaptureFileTimeStamp);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mImageCaptureFilePath = savedInstanceState.getString(IMAGE_CAPTURE_FILE_PATH, null);
        mImageCaptureFileTimeStamp = savedInstanceState.getLong(IMAGE_CAPTURE_FILE_TIME_STAMP, -1);
    }

    protected void startCapturePhotoActivity() throws Exception {
        String dir = StorageUtils.getTempDirectory(this);
        if (dir == null) {
            throw new Exception("无法创建目录");
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File file;
            try {
                file = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".jpg", new File(dir));
            } catch (IOException e) {
                throw new Exception("无法创建文件", e);
            }
            //7.0以上必须使用fileprovider
            Uri uri = FileProvider.getUriForFile(this, StorageUtils.AUTHORITY, file);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            if (!CommonUtils.grantUriPermission(this, uri, intent)) {
                //表示没有任何Activity可以处理这个intent
                //一般不会出现，因为此前已经判断过了
                throw new Exception("系统没有处理拍照的能力");
            }
            try {

                Log.d("PhotoActivity",intent.toString());
                startActivityForResult(intent, REQUEST_CODE_IMAGE_CAPTURE);
                mImageCaptureFilePath = file.getAbsolutePath();
                mImageCaptureFileTimeStamp = file.lastModified();
            } catch (Exception e) {
                throw new Exception("启动相机应用时出错", e);
                //报错可能是权限的问题，7.0需要动态获取权限
            }

        } else {
            throw new Exception("系统没有相机应用");
        }
    }

    protected void startChoosePhotoActivity() throws Exception {
        Intent intent = new Intent(Intent.ACTION_PICK, null);

        if (intent.resolveActivity(getPackageManager()) != null) {
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            try {
                startActivityForResult(intent, REQUEST_CODE_IMAGE_CHOOSE);
            } catch (Exception e) {
                throw new Exception("启动图片应用时出错", e);
            }
        } else {
            throw new Exception("系统没有图片应用");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_IMAGE_CAPTURE:// 拍照
                if (!StorageUtils.isEmptyFile(mImageCaptureFilePath)
                        && StorageUtils.hasChangedAfter(mImageCaptureFilePath, mImageCaptureFileTimeStamp)) {
                    onPhotoChosen(mImageCaptureFilePath);
                }
                break;
            case REQUEST_CODE_IMAGE_CHOOSE:// 相册选择
                if (data != null && data.getData() != null) {
                    Uri uri = data.getData();
                    onPhotoCaptured(uri);
                }
                break;
        }
    }

    protected void onPhotoCaptured(Uri uri) {

    }

    protected void onPhotoChosen(String path) {

    }
}
