package com.example.whu.bcrc.util;

import android.content.Context;
import android.widget.ImageView;

import com.example.whu.bcrc.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ImageUtil {
    public static void showImageUsingGlide(Context context, String imageUrlOrPath, ImageView imageView) {
        showImageUsingGlide(context, imageUrlOrPath, imageView, R.drawable.loading_img, R.drawable.error_img);
    }

    public static void showImageUsingGlide(Context context, String imageUrlOrPath, ImageView imageView, int loadingResId, int errorResId) {
        Glide
                .with(context)
                .load(imageUrlOrPath)
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(loadingResId)
                .error(errorResId)
                .into(imageView);
    }

    public static void showAvatarUsingGlide(Context context, String imageUrlOrPath, ImageView imageView) {
        showAvatarUsingGlide(context, imageUrlOrPath, imageView, R.drawable.ic_contact, R.drawable.ic_contact);
    }

    //图片加载时进行高斯模糊
    public static void showBlurUsingGlide(Context context, String imageUrlPath, ImageView imageView){
        Glide
                .with(context)
                .load(imageUrlPath)
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.loading_img)
                .error(R.drawable.error_img)
                .transform(new BlurTransformation(context,23,4))
                .into(imageView);
    }

    //图片加载时进行圆角设定
    public static void showRoundUsingGlide(Context context, String imageUrlPath, ImageView imageView, int radia){
        Glide
                .with(context)
                .load(imageUrlPath)
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.loading_img)
                .error(R.drawable.error_img)
                .transform(new RoundedCornersTransformation(context, radia, 0, RoundedCornersTransformation.CornerType.ALL))
                .into(imageView);
    }

    public static void showAvatarUsingGlide(Context context, String imageUrlOrPath, ImageView imageView, int loadingResId, int errorResId) {
        Glide
                .with(context)
                .load(imageUrlOrPath)
                .bitmapTransform(new CropCircleTransformation(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(loadingResId)
                .error(errorResId)
                .into(imageView);
    }

}
