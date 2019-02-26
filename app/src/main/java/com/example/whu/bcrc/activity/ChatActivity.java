package com.example.whu.bcrc.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.whu.bcrc.R;
import com.example.whu.bcrc.adapter.ContactMsgAdapter;
import com.example.whu.bcrc.entity.ContactMessage;
import com.example.whu.bcrc.util.StatusBarUtil;
import com.example.whu.bcrc.view.AppBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {
    public static final String KEY_CONTACT_ID = "contact_id";
    public static final String KEY_CONTACT_NAME = "contact_name";

    @BindView(R.id.et_input)
    EditText input;
    @BindView(R.id.btn_send)
    Button send;
    @BindView(R.id.rv_contact)
    RecyclerView content;
    @BindView(R.id.app_bar)
    AppBar appBar;

    private List<ContactMessage> msgList = new ArrayList<>();
    private ContactMsgAdapter msgAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        appBar.showTitle("Chat");
        appBar.showLeftBackButton();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);    //LinearLayoutLayout即线性布局，创建对象后把它设置到RecyclerView当中
        content.setLayoutManager(layoutManager);
        msgAdapter = new ContactMsgAdapter(msgList);
        content.setAdapter(msgAdapter);
    }

    @OnClick({R.id.btn_send})
    public void onSend(View view){
        String strContent=input.getText().toString();              //获取EditText中的内容
        if(!"".equals(strContent)){                                    //内容不为空则创建一个新的ContactMessage对象，并把它添加到msgList列表中
            ContactMessage msg=new ContactMessage(strContent,ContactMessage.TYPE_SENT);//设置格式为sent
            msgList.add(msg);
            msgAdapter.notifyItemInserted(msgList.size()-1);           //调用适配器的notifyItemInserted()用于通知列表有新的数据插入，这样新增的一条消息才能在RecyclerView中显示
            content.scrollToPosition(msgList.size()-1);     //调用scrollToPosition()方法将显示的数据定位到最后一行，以保证可以看到最后发出的一条消息
            input.setText("");                                  //调用EditText的setText()方法将输入的内容清空
        }
    }
    // TODO: 2018/5/26 当前并没有进行从服务器端获取消息，获取到了之后要创建一个TYPE_RECEIVED的ContactMessage对象，并添加到msgList
}
