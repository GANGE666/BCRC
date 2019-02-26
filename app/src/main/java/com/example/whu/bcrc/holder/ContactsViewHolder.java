package com.example.whu.bcrc.holder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whu.bcrc.R;
import com.example.whu.bcrc.bean.ContactBean;
import com.example.whu.bcrc.util.ImageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhang.guochen on 2018/5/20.
 */

public class ContactsViewHolder extends CommonViewHolder<ContactBean> {
    public static ViewHolderCreator<ContactsViewHolder> HOLDER_CREATOR = (parent, viewType) -> new ContactsViewHolder(parent.getContext(), parent);

    @BindView(R.id.img_contacts_avatar)
    ImageView imgAvatar;
    @BindView(R.id.tv_contacts_name)
    TextView tvName;

    public ContactsViewHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.item_contacts);
        ButterKnife.bind(this, getItemView());
    }

    @Override
    public void bindData(ContactBean contactBean) {
        ImageUtil.showAvatarUsingGlide(getContext(), contactBean.getContactImg(), imgAvatar);
        tvName.setText(contactBean.getContactName());
    }
}
