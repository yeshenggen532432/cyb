package com.qwb.view.audit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.qwb.utils.Constans;
import com.qwb.view.member.model.BuMenListBean.MemberBean;
import com.qwb.utils.MyGlideUtil;
import com.xmsx.cnlife.widget.CircleImageView;
import com.xmsx.qiweibao.R;

import java.util.List;

/**
 * 审批模块：审批人（没有：添加和删除按钮）
 */
public class ShenpiRenAdapter extends BaseAdapter {

    private Context context;
    private List<MemberBean> shenPiList;

    public ShenpiRenAdapter(Context context, List<MemberBean> members) {
        this.shenPiList = members;
        this.context = context;
    }


    @Override
    public int getCount() {
        return shenPiList == null ? 0 : shenPiList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.x_adapter_sp_person, null);
        }
        View iv_delmodel = convertView.findViewById(R.id.iv_delmodel);
        TextView tv_membername = convertView.findViewById(R.id.tv_membername);
        tv_membername.setVisibility(View.INVISIBLE);
        CircleImageView iv_userhead = convertView.findViewById(R.id.iv_userhead);

        MemberBean memberBean = shenPiList.get(position);
        if (memberBean != null) {
            tv_membername.setVisibility(View.VISIBLE);
            tv_membername.setText(memberBean.getMemberNm());
            MyGlideUtil.getInstance().displayImageRound(Constans.IMGROOTHOST + memberBean.getMemberHead(), iv_userhead);
        }
        return convertView;
    }


}
