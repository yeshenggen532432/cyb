package com.qwb.view.car.adapter;


import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.stk.StorageBean;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.xmsx.qiweibao.R;

/**
 * 文 件 名: 车销：仓库
 */
public class CarStorageAdapter extends BaseQuickAdapter<StorageBean.Storage,BaseViewHolder> {

    private Context context;
    public CarStorageAdapter(Context context) {
        super(R.layout.x_adapter_car_storage);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, StorageBean.Storage item) {
        helper.addOnClickListener(R.id.tv_default);
        helper.setText(R.id.tv_Storage, item.getStkName());// 仓库名
        TextView tvDefault=helper.getView(R.id.tv_default);//设置默认

        String defalutStorage=SPUtils.getSValues(ConstantUtils.Sp.CAR_DEFAULT_STORAGE);
        if(MyUtils.isEmptyString(defalutStorage)){
            tvDefault.setText("设置为默认");
            tvDefault.setTextColor(context.getResources().getColor(R.color.gray_6));
        }else{
            if(defalutStorage.equals(String.valueOf(item.getId()))){
                tvDefault.setText("已设置默认");
                tvDefault.setTextColor(context.getResources().getColor(R.color.blue));
                tvDefault.setEnabled(false);
            }else{
                tvDefault.setText("设置为默认");
                tvDefault.setTextColor(context.getResources().getColor(R.color.gray_6));
                tvDefault.setEnabled(true);
            }
        }


//        //电话
//        if (!MyUtils.isEmptyString(item.getTel())) {
//            tvTel.setVisibility(View.GONE);
//        } else {
//            tvTel.setText(item.getTel());
//        }
//        //金额
//        if (MyUtils.isEmptyString(item.getCjje())) {
//            tvJinE.setText("0");
//        } else {
//            tvJinE.setText(item.getCjje());
//        }
//        // 数量
//        if (!MyUtils.isEmptyString(item.getDdNum())) {
//            tvNum.setText(item.getDdNum());
//        }
//        // 时间拼接
//        StringBuffer sb=new StringBuffer();
//        sb.append("下单日期:");
//        if (!MyUtils.isEmptyString(item.getOddate())) {
//            String date = item.getOddate().substring(5, item.getOddate().length());
//            sb.append(date);
//        }
//        if (!MyUtils.isEmptyString(item.getOdtime())) {
//            String time = item.getOdtime().substring(0, 5);
//            sb.append(" "+time);
//        }
//        tvOrderTime.setText(sb.toString());
    }
}
