package com.qwb.view.map.adapter;


import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.Constans;
import com.qwb.view.map.model.TrackListBean;
import com.qwb.utils.MyGlideUtil;
import com.xmsx.cnlife.widget.CircleImageView;
import com.chiyong.t3.R;

import java.net.URLDecoder;

/**
 * 员工在线-拜访地图
 */
public class MapAdapter extends BaseQuickAdapter<TrackListBean.TrackList, BaseViewHolder> {

    private Context context;

    public MapAdapter(Context context) {
        super(R.layout.x_adapter_visit_map);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, TrackListBean.TrackList item) {
        TextView tv_name = helper.getView(R.id.tv_listitem_realTime_name);
        TextView tv_memberJob = helper.getView(R.id.tv_memberJob);
        CircleImageView imgIcon = helper.getView(R.id.img_listitem_realTime);
        TextView tv_phone = helper.getView(R.id.tv_listitem_realTime_phone);
        TextView tv_time = helper.getView(R.id.tv_listitem_realTime_time);
        final TextView tv_address = helper.getView(R.id.tv_listitem_realTime_address);
        TextView tv_type = helper.getView(R.id.tv_listitem_type);

        if (item != null) {
            tv_name.setText(item.getUserNm());
            tv_phone.setText(item.getUserTel());
            tv_time.setText(item.getTimes());
            tv_type.setText(item.getZt());
            tv_memberJob.setText(item.getMemberJob());
            if ("异常".equals(item.getZt())) {
                tv_type.setBackgroundColor(context.getResources().getColor(R.color.yichang));
                tv_type.setText("下线");
            }
            if ("在线".equals(item.getZt())) {
                tv_type.setBackgroundColor(context.getResources().getColor(R.color.yundong));
            }
            if ("下班".equals(item.getZt())) {
                tv_type.setBackgroundColor(context.getResources().getColor(R.color.jingzhi));
            }
            if ("上班".equals(item.getZt())) {
                tv_type.setBackgroundColor(context.getResources().getColor(R.color.shangban));
            }
            //地址
            setAddress(item, tv_address);
            //加载网络图片
            MyGlideUtil.getInstance().displayImageRound(Constans.IMGROOTHOST + item.getUserHead(), imgIcon);
        }

        //子view添加点击事件（拜访回放）
        helper.addOnClickListener(R.id.tv_track).addOnClickListener(R.id.tv_zr);
        ;
    }

    /**
     * 地址信息
     */
    private void setAddress(TrackListBean.TrackList item, final TextView tv_address) {
        try {
                    tv_address.setText(URLDecoder.decode(item.getAddress(), "UTF-8"));
//            if (MyStringUtil.isNotEmpty(item.getAddress())) {
//                try {
//                    tv_address.setText(URLDecoder.decode(item.getAddress(), "UTF-8"));
//                } catch (Exception e) {
//                }
//            } else if (MyStringUtil.isNotEmpty(item.getLocation())) {
//                String location = item.getLocation();
//                if (location.startsWith("[") || location.endsWith("]")) {
//                    location = location.substring(1, location.length() - 1);
//                }
//                if (location.contains(",")) {
//                    String[] split = location.split(",");
//                    LatLng latLng = new LatLng(Double.valueOf(split[1]), Double.valueOf(split[0]));
//                    MyMapUtil.getInstance()
//                            .reverseGeoCode(latLng)
//                            .setOnLocationListener(new MyMapUtil.OnLocationListener() {
//                                @Override
//                                public void setOnSuccessListener(BDLocation bdLocation) {
//                                }
//                                @Override
//                                public void setErrorListener() {
//                                }
//                                @Override
//                                public void setAddressListener(String addressStr) {
//                                    tv_address.setText(addressStr);
//                                }
//                            });
//                }
//            }
        } catch (Exception e) {
        }

    }
}
