package com.example.whu.bcrc.jsObject;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.example.whu.bcrc.activity.BaseActivity;
import com.example.whu.bcrc.p2p.ProperTies;
import com.example.whu.bcrc.p2p.UserAddInstr;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Properties;

import static java.lang.System.in;

/**
 * Created by zhang.guochen on 2018/6/1.
 */

public class JSCommand {
    private static final String TAG = "JSCommand";  //调试的时候使用
    String TargetUID = "1234567890";
    String MyID     = "10086";

    @JavascriptInterface
    public void control(String string){

        Properties proper = ProperTies.getProperties(BaseActivity.GetContext());
        TargetUID = proper.getProperty("TargetUID");
        MyID = proper.getProperty("MYUID");

        Log.d(TAG, string);

        final String sendMsg = string;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UserAddInstr.AskAgency(Integer.valueOf(MyID), Integer.valueOf(TargetUID), sendMsg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
