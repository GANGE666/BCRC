package com.example.whu.bcrc.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whu.bcrc.R;
import com.example.whu.bcrc.activity.ChatActivity;
import com.example.whu.bcrc.adapter.CommonRecyclerAdapter;
import com.example.whu.bcrc.adapter.ExtraViewAdapter;
import com.example.whu.bcrc.bean.ContactBean;
import com.example.whu.bcrc.holder.ContactsViewHolder;
import com.example.whu.bcrc.view.AppBar;
import com.example.whu.bcrc.view.RefreshableListView;
import com.example.whu.bcrc.view.RemovableLoadMoreFooterView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment implements CommonRecyclerAdapter.OnItemClickListener<ContactBean, ContactsViewHolder>{
    private Unbinder unbinder;
    private List<ContactBean> list = new ArrayList<>();


    @BindView(R.id.app_bar)
    AppBar appBar;
    @BindView(R.id.rfl_contacts)
    RefreshableListView rflContacts;

    private ExtraViewAdapter<ContactBean, ContactsViewHolder> mAdapter;

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_contacts, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        appBar.showTitle("Contact");
        mAdapter = new ExtraViewAdapter<>(ContactsViewHolder.HOLDER_CREATOR);
        rflContacts.setAdapter(mAdapter);
        rflContacts.setLoadMoreFooterView(new RemovableLoadMoreFooterView(getContext()));
        rflContacts.setCallback(new RefreshableListView.Callback() {
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

    /**
     *
     * @param skip：为查询时跳过的数据数量
     */
    private void requestListData(int skip) {
        // TODO: 2018/5/26 从数据库获取聊天联系人的数据，具体的操作如下
        /*从数据库获取聊天联系人的数据
            获取到资源之后，调用rflContacts.setLoadComplete()；
            之后对skip进行判断，
            if (skip == 0) {
               mAdapter.setDataList(list);
            } else {
               mAdapter.addDataList(list);
            }
            再根据所查询得到的数据列表与当页可容纳的数量进行比较
            if (list.size() < PAGE_SIZE) {
                rlvPlayMethod.setFullyLoaded(true);
            } else {
                rlvPlayMethod.setFullyLoaded(false);
            }

            暂时先利用固定的一个数据源进行测试使用
        */

        //演示用
        list.clear();

        list.add(new ContactBean("售后服务", "http://jakewharton.github.io/butterknife/static/logo.png", "test001"));
        rflContacts.setLoadComplete();
        mAdapter.setDataList(list);
        rflContacts.setFullyLoaded(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        rflContacts.startRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onItemClick(ContactBean contactBean, ContactsViewHolder holder, View itemView) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(ChatActivity.KEY_CONTACT_ID, contactBean.getContactId());
        intent.putExtra(ChatActivity.KEY_CONTACT_NAME, contactBean.getContactName());
        startActivity(intent);
    }
}
