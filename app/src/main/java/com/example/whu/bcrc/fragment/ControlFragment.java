package com.example.whu.bcrc.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whu.bcrc.R;
import com.example.whu.bcrc.activity.BaseActivity;
import com.example.whu.bcrc.activity.DeviceInfoActivity;
import com.example.whu.bcrc.adapter.CommonRecyclerAdapter;
import com.example.whu.bcrc.adapter.ExtraViewAdapter;
import com.example.whu.bcrc.bean.DeviceBean;
import com.example.whu.bcrc.holder.DevicesViewHolder;
import com.example.whu.bcrc.p2p.ProperTies;
import com.example.whu.bcrc.view.AppBar;
import com.example.whu.bcrc.view.RefreshableListView;
import com.example.whu.bcrc.view.RemovableLoadMoreFooterView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ControlFragment extends Fragment implements CommonRecyclerAdapter.OnItemClickListener<DeviceBean, DevicesViewHolder>{
    private Unbinder unbinder;   // TODO: 2018/5/26 注意——在Fragment中因为生命周期的不同，所以要在Fragment销毁的时候对已绑定的组件进行解绑

    private ExtraViewAdapter<DeviceBean, DevicesViewHolder> mAdapter;
    private List<DeviceBean> devices = new ArrayList<>();
    public static final int UPDATE_PIC = 1;
    public Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_PIC:
                    String name = msg.getData().getString("name");
                    Log.d("HandleMessage", name);
                    devices.add(new DeviceBean(name, getResourceByReflect(name)));
                    rflControl.startRefresh();
                    Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(getContext(), "添加失败，无效的uid", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };




    @BindView(R.id.app_bar)
    AppBar appBar;
    @BindView(R.id.rfl_control)
    RefreshableListView rflControl;


    public ControlFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_control, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        appBar.showTitle("Control");
        appBar.showFirstRightImageButton(R.drawable.add, (v)->{
            showInputDialog();
        });
        mAdapter = new ExtraViewAdapter<>(DevicesViewHolder.HOLDER_CREATOR);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rflControl.setLayoutManager(layoutManager);
        rflControl.setAdapter(mAdapter);
        rflControl.setLoadMoreFooterView(new RemovableLoadMoreFooterView(getContext()));
        rflControl.setCallback(new RefreshableListView.Callback() {
            @Override
            public void onLoadMore() {
                requestListData(mAdapter.getDataList().size());
            }

            @Override
            public void onRefresh() {
                requestListData(0);
            }
        });
        mAdapter.setOnItemClickListener(this);

    }

    private void requestListData(int skip) {
        //清空
        devices.clear();
        devices.add(new DeviceBean("智能小车1", R.drawable.car1));
        devices.add(new DeviceBean("智能小车2", R.drawable.car2));
        rflControl.setLoadComplete();
        mAdapter.setDataList(devices);
        rflControl.setFullyLoaded(true);

    }

    @Override
    public void onResume() {
        super.onResume();
        rflControl.startRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(DeviceBean deviceBean, DevicesViewHolder holder, View itemView) {
        Intent intent = new Intent(getContext(), DeviceInfoActivity.class);
        intent.putExtra("deviceName", deviceBean.getDeviceName());
        intent.putExtra("deviceImageId", deviceBean.getDeviceImg());
        getContext().startActivity(intent);
    }
    private void showInputDialog(){

        final EditText editText = new EditText(getContext());
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(getContext());
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

    private void runAddClient(final String uid) throws IOException
    {

        new Thread(new Runnable() {
            private Socket s;
            private String serverAddress = "182.61.15.180";
            private int serverPort = 10101;
            private BufferedReader br = null;
            //private uid;

            @Override
            public void run() {
                try {
                    Properties proper = ProperTies.getProperties(BaseActivity.GetContext());
                    serverAddress = proper.getProperty("DeviceAdd_ServerIP");
                    serverPort = Integer.valueOf(proper.getProperty("DeviceAdd_ServerPort"));

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

    public int getResourceByReflect(String imageName){

        int r_id ;
        try {
            Log.d("imageName", imageName);
            Context ctx = getContext();
            r_id = getResources().getIdentifier(imageName, "drawable" , ctx.getPackageName());
        } catch (Exception e) {
            r_id = -1;
            Log.e("ERROR", "PICTURE NOT　FOUND！");
        }
        return r_id;
    }


}
