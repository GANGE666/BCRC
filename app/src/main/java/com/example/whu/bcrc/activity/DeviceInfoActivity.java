package com.example.whu.bcrc.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.whu.bcrc.R;
import com.example.whu.bcrc.jsObject.JSCommand;
import com.example.whu.bcrc.p2p.ClientMain;
import com.example.whu.bcrc.p2p.ProperTies;
import com.example.whu.bcrc.p2p.UserAddInstr;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.Thread.sleep;

public class DeviceInfoActivity extends AppCompatActivity {
    @BindView(R.id.wv_command)
    WebView wvCommand;
    String TargetUID = "1234567890";
    String MyID     = "10086";

    public static Deque<String> receiveMsg = new ArrayDeque<>();


    // TODO: 2018/5/26 对设备的具体信息进行展示
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        receiveMsg.clear();
        Properties proper = ProperTies.getProperties(getApplicationContext());
        TargetUID = proper.getProperty("TargetUID");
        MyID = proper.getProperty("MYUID");
        Log.d("TAG", TargetUID + "____" + MyID);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        int imgid = intent.getIntExtra("deviceImageId", -1);
        String name = intent.getStringExtra("deviceName");

        WebSettings webSettings = wvCommand.getSettings();

        webSettings.setJavaScriptEnabled(true);
        wvCommand.addJavascriptInterface(new JSCommand(), "command");

        wvCommand.loadUrl("file:///android_asset/index.html");

        P2P();
    }

    void P2P(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(500);
                    //Log.d("P2P","Get in");

                    ClientMain clientMain = new ClientMain();
                    clientMain.start();

                    sleep(50);

                    UserAddInstr.Login(Integer.valueOf(MyID));
                    sleep(1000);
                    UserAddInstr.AskConnect(Integer.valueOf(MyID), Integer.valueOf(TargetUID));

                    //System.out.println(UserAddInstr.SendMessage(Integer.valueOf(MyID), Integer.valueOf(TargetUID), "Hello C1"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
