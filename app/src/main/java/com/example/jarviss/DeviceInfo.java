package com.example.jarviss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceInfo extends AppCompatActivity {

    private ImageView deviceimg;
    private TextView devicename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);

        deviceimg = (ImageView) findViewById(R.id.deviceinfo_img);
        devicename = (TextView) findViewById(R.id.deviceinfo_name);

        Intent intent = getIntent();
        int imgid = intent.getIntExtra("deviceImageId", -1);
        String name = intent.getStringExtra("deviceName");

        devicename.setText((String)name);
        deviceimg.setImageResource(imgid);

    }
}
