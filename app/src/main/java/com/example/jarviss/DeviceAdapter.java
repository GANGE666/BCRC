package com.example.jarviss;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by 肖 on 2018/4/20.
 */

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    private Context mContext;
    private List<Device> mDeviceList;


    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView deviceImage;
        TextView deviceName;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            deviceImage = (ImageView) view.findViewById(R.id.device_image);
            deviceName = (TextView) view.findViewById(R.id.device_name);
        }
    }

    public DeviceAdapter(List<Device> deviceList){
        mDeviceList = deviceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.device_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Device device = mDeviceList.get(position);
        holder.deviceName.setText(device.getName());
        Glide.with(mContext).load(device.getImageId()).into(holder.deviceImage);
    }

    @Override
    public int getItemCount(){
        return mDeviceList.size();
    }

}

