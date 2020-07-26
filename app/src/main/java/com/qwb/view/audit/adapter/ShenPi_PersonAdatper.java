package com.qwb.view.audit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.qwb.view.member.model.BuMenListBean.MemberBean;
import com.qwb.utils.MyGlideUtil;
import com.xmsx.cnlife.widget.CircleImageView;
import com.qwb.utils.Constans;
import com.xmsx.qiweibao.R;
import java.util.List;

/**
 * 审批模块：添加审批人（添加和删除按钮）
 *
 * @author yeshenggen
 */
public class ShenPi_PersonAdatper extends BaseAdapter {

    private Context context;
    private boolean isDelModel;
    private List<MemberBean> shenPiList;

    public ShenPi_PersonAdatper(Context context, List<MemberBean> members) {
        this.shenPiList = members;
        this.context = context;
    }


    @Override
    public int getCount() {
        return shenPiList.size() > 0 ? shenPiList.size() + 2 : 1;
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
        if (context == null) {
            return null;
        }

        View inflate = LayoutInflater.from(context).inflate(R.layout.x_adapter_sp_person, null);
        View iv_delmodel = inflate.findViewById(R.id.iv_delmodel);
        TextView tv_membername = inflate.findViewById(R.id.tv_membername);
        tv_membername.setVisibility(View.INVISIBLE);
        CircleImageView iv_userhead = inflate.findViewById(R.id.iv_userhead);

        if (shenPiList.size() <= 0) {
            iv_userhead.setImageResource(R.drawable.tianjia);
        } else {
            if (position == shenPiList.size()) {
                iv_userhead.setImageResource(R.drawable.ckin_addpic);
            } else if (position == shenPiList.size() + 1) {
                iv_userhead.setImageResource(R.drawable.delpic);
            } else {
                if (isDelModel) {
                    iv_delmodel.setVisibility(View.VISIBLE);
                } else {
                    iv_delmodel.setVisibility(View.GONE);
                }
                MemberBean memberBean = shenPiList.get(position);
                tv_membername.setVisibility(View.VISIBLE);
                tv_membername.setText(memberBean.getMemberNm());
                MyGlideUtil.getInstance().displayImageRound(Constans.IMGROOTHOST + memberBean.getMemberHead(), iv_userhead);
            }
        }
        return inflate;

    }

    public void refreshAdapter(boolean isDelModel) {
        this.isDelModel = isDelModel;
        this.notifyDataSetChanged();
    }


    public void removeItem(int position) {
        shenPiList.remove(position);
        this.notifyDataSetChanged();
    }

}
