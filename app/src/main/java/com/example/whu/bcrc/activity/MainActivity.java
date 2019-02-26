package com.example.whu.bcrc.activity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.whu.bcrc.R;
import com.example.whu.bcrc.fragment.ContactsFragment;
import com.example.whu.bcrc.fragment.ControlFragment;
import com.example.whu.bcrc.fragment.MyFragment;
import com.example.whu.bcrc.fragment.UnsignedFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener{
    private static final String TAG = "MainActivity";  //调试的时候使用
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 123;


    @BindView(R.id.iv_control)
    ImageView ivControl;
    @BindView(R.id.iv_contact)
    ImageView ivContact;
    @BindView(R.id.iv_unsigned)
    ImageView ivUnsigned;
    @BindView(R.id.iv_my)
    ImageView ivMy;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        setDoubleBackEnable();
    }

    /**
     * 对MainActivity进行初始化，包括：
     * 创建并添加需要的Fragment
     * 设置ViewPager的缓存页数，以及适配器和监听
     * 并设置初始为选中控制页面
     * 检查权限
     */
    private void init() {
        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ControlFragment());
        fragments.add(new ContactsFragment());
        fragments.add(new UnsignedFragment());
        fragments.add(new MyFragment());
        vpContent.setOffscreenPageLimit(2);
        vpContent.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        vpContent.addOnPageChangeListener(this);
        selectControlTab();
        checkMarshmallowPermission();

    }

    @OnClick({R.id.iv_control, R.id.iv_contact, R.id.iv_unsigned, R.id.iv_my})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_control:
                selectControlTab();
                vpContent.setCurrentItem(0,true);
                break;
            case R.id.iv_contact:
                selectContactTab();
                vpContent.setCurrentItem(1,true);
                break;
            case R.id.iv_unsigned:
                selectUnsignedTab();
                vpContent.setCurrentItem(2,true);
                break;
            case R.id.iv_my:
                selectMyTab();
                vpContent.setCurrentItem(3,true);
                break;
        }
    }

    /**
     * 单独选中某按钮
     */
    private void selectControlTab() {
        resetTabs();
        ivControl.setImageResource(R.drawable.tab_control_click);
    }

    private void selectContactTab() {
        resetTabs();
        ivContact.setImageResource(R.drawable.tab_contact_click);
    }

    private void selectUnsignedTab() {
        resetTabs();
        ivUnsigned.setImageResource(R.drawable.tab_control_click);
    }

    private void selectMyTab() {
        resetTabs();
        ivMy.setImageResource(R.drawable.tab_my_click);
    }

    /**
     * 设置全部按钮为未选中
     */
    private void resetTabs() {
        ivControl.setImageResource(R.drawable.tab_control_no);
        ivContact.setImageResource(R.drawable.tab_contact_no);
        ivUnsigned.setImageResource(R.drawable.tab_control_no);
        ivMy.setImageResource(R.drawable.tab_my_no);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                selectControlTab();
                break;
            case 1:
                selectContactTab();
                break;
            case 2:
                selectUnsignedTab();
                break;
            case 3:
                selectMyTab();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 在Android6.0及以上版本中需要对部分敏感权限进行动态授予
     */
    private void checkMarshmallowPermission() {
        if(Build.VERSION.SDK_INT >= 23){
            List<String> permissionsNeeded = new ArrayList<>();
            final List<String> permissionsList = new ArrayList<String>();

            if(!addPermission(permissionsList, android.Manifest.permission.CAMERA))
                permissionsNeeded.add("CAMERA");
            // TODO: 2018/5/26  动态增加所需的权限
            if(permissionsList.size() > 0){
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
        }else {
            return ;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if(checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED){
            permissionsList.add(permission);
        }
        return true;
    }



}
