package com.example.jarviss;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Index extends BaseActivity {

    private DrawerLayout mDrawerLayout;

    private Device[] devices = {new Device("风扇", R.drawable.fan), new Device("fan2", R.drawable.fan2) };
    private List<Device> deviceList = new ArrayList<>();
    private DeviceAdapter adapter;


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

        /*GridView*/
        initDevice();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DeviceAdapter(deviceList);
        recyclerView.setAdapter(adapter);
        /*GridView end*/
    }

    private void initDevice(){
        deviceList.clear();
        for(int i=0;i<20; i++){
            deviceList.add(devices[i % devices.length]);
        }
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
                Toast.makeText(Index.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        }).show();

    }


}
