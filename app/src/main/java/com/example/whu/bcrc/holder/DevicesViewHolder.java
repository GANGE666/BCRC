package com.example.whu.bcrc.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.whu.bcrc.R;
import com.example.whu.bcrc.bean.DeviceBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhang.guochen on 2018/5/25.
 */

public class DevicesViewHolder extends CommonViewHolder<DeviceBean> {
    @BindView(R.id.iv_device)
    ImageView ivDevice;
    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;

    public static ViewHolderCreator<DevicesViewHolder> HOLDER_CREATOR = (parent, viewType) -> new DevicesViewHolder(parent.getContext(), parent);


    public DevicesViewHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.item_device);
        ButterKnife.bind(this, getItemView());
    }

    @Override
    public void bindData(DeviceBean deviceBean) {
        tvDeviceName.setText(deviceBean.getDeviceName());
        Glide.with(getContext()).load(deviceBean.getDeviceImg()).into(ivDevice);
    }
}
