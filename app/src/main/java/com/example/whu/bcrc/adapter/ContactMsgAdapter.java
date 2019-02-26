package com.example.whu.bcrc.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.whu.bcrc.R;
import com.example.whu.bcrc.entity.ContactMessage;


import java.util.List;


/**
 * Created by zhang.guochen on 2018/5/19.
 */

public class ContactMsgAdapter extends RecyclerView.Adapter<ContactMsgAdapter.ViewHolder> {
    private List<ContactMessage> mMsgList;

    public ContactMsgAdapter(List<ContactMessage> mMsgList) {
        this.mMsgList = mMsgList;
    }

    @Override
    public ContactMsgAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item,parent,false);
        return new ViewHolder(view);                                                   //把加载出来的布局传到构造函数中，再返回
    }

    @Override
    public void onBindViewHolder(ContactMsgAdapter.ViewHolder holder, int position) {
        ContactMessage msg=mMsgList.get(position);
        if(msg.getMsgType()==ContactMessage.TYPE_RECIEVED){                                         //增加对消息类的判断，如果这条消息是收到的，显示左边布局，是发出的，显示右边布局
            holder.leftMsg.setVisibility(View.VISIBLE);
            holder.rightMsg.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
        }else if(msg.getMsgType()==ContactMessage.TYPE_SENT) {
            holder.rightMsg.setVisibility(View.VISIBLE);
            holder.leftMsg.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView leftMsg;
        TextView rightMsg;
        public ViewHolder(View view){
            super(view);
            leftMsg=(TextView)view.findViewById(R.id.left_msg);
            rightMsg=(TextView)view.findViewById(R.id.right_msg);
        }
    }
}
