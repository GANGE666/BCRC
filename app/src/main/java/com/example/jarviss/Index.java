package com.example.jarviss;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.preference.DialogPreference;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Index extends BaseActivity {

    private DrawerLayout mDrawerLayout;

    private Device[] devices = {new Device("风扇", R.drawable.fan), new Device("fan2", R.drawable.fan2) };
    private List<Device> deviceList = new ArrayList<>();
    private DeviceAdapter adapter;

    public static final int UPDATE_PIC = 1;

    public Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_PIC:
                    String name = msg.getData().getString("name");
                    Log.d("HandleMessage", name);
                    deviceList.add(new Device(name, getResourceByReflect(name)));
                    setRecycleView();
                    Toast.makeText(GetContext(), "添加成功", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(GetContext(), "添加失败，无效的uid", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        /* Toolbar and DrawerLayout*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item){
                mDrawerLayout.closeDrawers();
                Toast.makeText(getApplicationContext(), "Clicked " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        /* Toolbar and DrawerLayout end*/

        initDevice();
        setRecycleView();
    }

    private void initDevice(){
        deviceList.clear();
        for(int i=0;i<2; i++){
            deviceList.add(devices[i % devices.length]);
        }
    }

    private void setRecycleView(){
        /*GridView*/
        //initDevice();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DeviceAdapter(deviceList);
        recyclerView.setAdapter(adapter);
        /*GridView end*/
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.toobar_add:
                showInputDialog();
                //Toast.makeText(this, "ADD", Toast.LENGTH_LONG).show();
                break;
            default:
        }
        return true;
    }

    //ADD -> InputDialog
    private void showInputDialog(){

        final EditText editText = new EditText(Index.this);
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(Index.this);
        inputDialog.setTitle("输入设备ID号").setView(editText);
        inputDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    runAddClient(editText.getText().toString());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                //Toast.makeText(Index.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        }).show();

    }

    //upload uid to server and receive data from server
    private void runAddClient(final String uid) throws IOException
    {

        new Thread(new Runnable() {
            private Socket s;
            Properties proper = ProperTies.getProperties(getApplicationContext());

            private String serverAddress = proper.getProperty("DeviceAdd_ServerIP");
            private int serverPort = Integer.valueOf(proper.getProperty("DeviceAdd_ServerPort"));
            private BufferedReader br = null;
            //private uid;

            @Override
            public void run() {
                try {
                    s = new Socket(serverAddress, serverPort);
                    Log.d("AddDevice", "Connected " + s.getInetAddress().toString());
                    br = new BufferedReader(new InputStreamReader(s.getInputStream()));

                    PrintStream ps = new PrintStream(s.getOutputStream());
                    ps.println(uid);

                    String content = null;
                    while ((content = br.readLine()) != null) {
                        //Log.d("content", content);
                        if (content.equals("1")) {
                            content = br.readLine();
                            Message message = new Message();
                            message.what = UPDATE_PIC;
                            Bundle bundle = new Bundle();
                            bundle.putString("name", content);
                            message.setData(bundle);
                            handler.sendMessage(message);
                        }
                        else{
                            Message message = new Message();
                            message.what = -1;
                            handler.sendMessage(message);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }



}
