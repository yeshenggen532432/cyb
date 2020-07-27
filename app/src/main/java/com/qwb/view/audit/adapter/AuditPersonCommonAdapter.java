package com.qwb.view.audit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.qwb.utils.Constans;
import com.qwb.view.member.model.BuMenListBean.MemberBean;
import com.qwb.utils.MyGlideUtil;
import com.xmsx.cnlife.widget.CircleImageView;
import com.chiyong.t3.R;
import java.util.List;

/**
 * 审批人（添加和删除按钮）
 * 默认有添加和删除按钮，当isEdit==false时，没有添加和删除按钮的
 * isEdit：是否可以编辑（默认true）
 */
public class AuditPersonCommonAdapter extends BaseAdapter {

    private boolean isEdit = true;
    private Context context;
    private boolean isDelModel;
    private List<MemberBean> mMemberList;

    public AuditPersonCommonAdapter(Context context, List<MemberBean> members, boolean isEdit) {
        this.mMemberList = members;
        this.isEdit = isEdit;
        this.context = context;
    }


    @Override
    public int getCount() {
        int size = 0;
        if (isEdit) {
            if(mMemberList != null){
                if (mMemberList.size() > 0) {
                    size = mMemberList.size() + 2;
                } else {
                    size = 1;
                }
            }
        } else {
            if(mMemberList != null){
                size = mMemberList.size();
            }
        }
        return size;
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
        View ivDel = inflate.findViewById(R.id.iv_delmodel);
        TextView tvMemberName = (TextView) inflate.findViewById(R.id.tv_membername);
        tvMemberName.setVisibility(View.INVISIBLE);
        CircleImageView ivHead = (CircleImageView) inflate.findViewById(R.id.iv_userhead);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) context.getResources().getDimension(R.dimen.dp_40), (int) context.getResources().getDimension(R.dimen.dp_40));
        params.setMargins(0,(int) context.getResources().getDimension(R.dimen.dp_5),0,0);
        if (isEdit) {
            if (mMemberList.size() == 0) {
                ivHead.setLayoutParams(params);
                ivHead.setImageResource(R.mipmap.ic_add_gray_round);
            } else {
                if (position == mMemberList.size()) {
                    ivHead.setLayoutParams(params);
                    ivHead.setImageResource(R.mipmap.ic_add_gray_round);
                } else if (position == mMemberList.size() + 1) {
                    ivHead.setLayoutParams(params);
                    ivHead.setImageResource(R.mipmap.ic_jian_gray_round);
                } else {
                    if (isDelModel) {
                        ivDel.setVisibility(View.VISIBLE);
                    } else {
                        ivDel.setVisibility(View.GONE);
                    }
                    MemberBean memberBean = mMemberList.get(position);
                    tvMemberName.setVisibility(View.VISIBLE);
                    tvMemberName.setText(memberBean.getMemberNm());
                    MyGlideUtil.getInstance().displayImageRound(Constans.IMGROOTHOST + memberBean.getMemberHead(), ivHead);
                }
            }
        } else {
            ivDel.setVisibility(View.GONE);
            MemberBean memberBean = mMemberList.get(position);
            tvMemberName.setVisibility(View.VISIBLE);
            tvMemberName.setText(memberBean.getMemberNm());
            MyGlideUtil.getInstance().displayImageRound(Constans.IMGROOTHOST + memberBean.getMemberHead(), ivHead);
        }
        return inflate;

    }

    public void refreshAdapter(boolean isDelModel) {
        this.isDelModel = isDelModel;
        this.notifyDataSetChanged();
    }


    public void removeItem(int position) {
        mMemberList.remove(position);
        this.notifyDataSetChanged();
    }

    public void refreshAdapter(boolean isDelModel, List<MemberBean> memberList) {
        this.isDelModel = isDelModel;
        this.mMemberList = memberList;
        this.notifyDataSetChanged();
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
        this.notifyDataSetChanged();
    }
}
