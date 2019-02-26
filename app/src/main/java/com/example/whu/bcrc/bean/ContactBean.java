package com.example.whu.bcrc.bean;

/**
 * Created by zhang.guochen on 2018/5/20.
 */

public class ContactBean {
    private String contactName; //厂商名字
    private String contactImg;  //厂商头像图片链接
    private String contactId;

    public ContactBean(String contactName, String contactImg, String contactId) {
        this.contactName = contactName;
        this.contactImg = contactImg;
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactImg() {
        return contactImg;
    }

    public String getContactId() {
        return contactId;
    }

}
