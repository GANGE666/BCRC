package com.example.jarviss;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Properties;

import static java.lang.Thread.sleep;

public class DeviceInfo extends BaseActivity {

    WebView mWebView;
    Button Init, Update, Send, Agency, Connect, Test;
    EditText sendInfo;
    String TargetUID = "1234567890";
    String MyID     = "10086";

    public static Deque<String> receiveMsg = new ArrayDeque<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_info);

        receiveMsg.clear();
        Properties proper = ProperTies.getProperties(getApplicationContext());
        TargetUID = proper.getProperty("TargetUID");
        MyID = proper.getProperty("MYUID");

        mWebView =(WebView) findViewById(R.id.Device_info_WebView);

        WebSettings webSettings = mWebView.getSettings();

        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        // 先载入JS代码
        // 格式规定为:file:///android_asset/文件名.html
        mWebView.loadUrl("file:///android_asset/test.html");
        //mWebView.loadUrl("www.baidu.com");

        Update = (Button) findViewById(R.id.Device_info_Button);
        Init = (Button) findViewById(R.id.Device_info_Init);
        Send = (Button) findViewById(R.id.Device_info_Send);
        Agency = (Button) findViewById(R.id.Device_info_Agency);
        Connect = (Button) findViewById(R.id.Device_info_Connect);
        sendInfo = (EditText) findViewById(R.id.Device_info_EditText);
        Test = (Button) findViewById(R.id.Device_info_Test);

        Init.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setStringToWebView("name", "fan");
            }
        });

        Test.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            UserAddInstr.Login(Integer.valueOf(MyID));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });

        Connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStringToWebView("statu", "Connecting");
                P2P();

                if(ClientMain.ClientTable.containsKey(TargetUID))
                    setStringToWebView("statu", "Connected");


            }
        });

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = "NO message to show(Empty Queue)";
                if(!receiveMsg.isEmpty()){
                    msg = receiveMsg.poll();
                }
                setStringToWebView("msg", msg);
            }
        });

        Send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                final String sendMsg = sendInfo.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            System.out.println(UserAddInstr.SendMessage(Integer.valueOf(MyID), Integer.valueOf(TargetUID), sendMsg));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        Agency.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final String sendMsg = sendInfo.getText().toString();
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
        });
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


                    System.out.println(UserAddInstr.SendMessage(Integer.valueOf(MyID), Integer.valueOf(TargetUID), "Hello C1"));



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void setStringToWebView(final String key, final String value){
    //一般在onClick里面
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                try{
                    JSONObject obj = new JSONObject();
                    obj.put("name", key);
                    obj.put("msg", value);
                    Log.d("SetName", key + "\t__" + value);
                    mWebView.loadUrl("javascript:setInfo(" + obj.toString() + ")");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

}
