package com.qwb.view.map.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.map.model.TrackListBean;
import com.chiyong.t3.R;

/**
 * 员工分布图-员工列表
 */
public class MemberAdapter extends BaseQuickAdapter<TrackListBean.TrackList,BaseViewHolder> {

    private int mid;//选中的id,打钩图片用的
    public MemberAdapter(int mid) {
        super(R.layout.x_adapter_member);
        this.mid=mid;
    }

    @Override
    protected void convert(BaseViewHolder helper, TrackListBean.TrackList item) {
        TextView tv_memberNm=helper.getView(R.id.tv_memberNm);
        ImageView iv_check=helper.getView(R.id.iv_check);
        tv_memberNm.setText(item.getUserNm());
        if (mid == item.getUserId()) {
            iv_check.setVisibility(View.VISIBLE);
        } else {
            iv_check.setVisibility(View.GONE);
        }
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }
}
