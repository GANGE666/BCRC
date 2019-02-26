package com.example.whu.bcrc.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whu.bcrc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnsignedFragment extends Fragment {
// TODO: 2018/5/26 注意在Fragment中ButterKnife的绑定与解绑，本页还是未定义，定义之后更改文件名

    public UnsignedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_unsigned, container, false);
    }

}
