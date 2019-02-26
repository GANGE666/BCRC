package com.example.whu.bcrc.bean;

/**
 * Created by zhang.guochen on 2018/5/25.
 */

public class DeviceBean {
    private String deviceName;
    private int deviceImg;

    public DeviceBean(String deviceName, int deviceImg) {
        this.deviceName = deviceName;
        this.deviceImg = deviceImg;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public int getDeviceImg() {
        return deviceImg;
    }
}
