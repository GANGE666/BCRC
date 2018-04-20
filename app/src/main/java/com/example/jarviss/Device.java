package com.example.jarviss;

/**
 * Created by 肖 on 2018/4/20.
 */

public class Device {

    private String name;
    private int imageId;

    public Device(String name, int imageId){
        this.name = name;
        this.imageId = imageId;
    }

    public String getName(){
        return name;
    }

    public int getImageId(){
        return imageId;
    }

}
