package com.qwb.view.car.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.MyMathUtils;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.MyUnitUtil;
import com.qwb.view.step.model.ShopInfoBean;
import com.chiyong.t3.R;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 文 件 名: 车销单-仓库商品
 * 修改时间：
 * 修改备注：
 */
public class CarStkWareAdapter extends BaseQuickAdapter<ShopInfoBean.Data,BaseViewHolder> {

    private List<ShopInfoBean.Data> selectList = new ArrayList<>();

    public CarStkWareAdapter() {
        super(R.layout.x_adapter_car_stk_ware);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopInfoBean.Data item) {
        helper.setText(R.id.tv_name_choose_shop, item.getWareNm());
        TextView tvCount=helper.getView(R.id.tv_count_choose_shop);
        TextView tvCountOoc=helper.getView(R.id.tv_count_ooc);
        TextView tvCountSy=helper.getView(R.id.tv_count_sy);
        ImageView ivCb = helper.getView(R.id.item_iv_cb);

        ivCb.setVisibility(View.VISIBLE);
        ivCb.setImageResource(R.drawable.icon_dx);
        if(selectList != null && selectList.size() > 0){
            for (ShopInfoBean.Data bean : selectList) {
                if((""+bean.getWareId()).equals("" + item.getWareId())){
                    ivCb.setImageResource(R.drawable.icon_dxz);
                    break;
                }
            }
        }

//        tvCount.setText("库存数量："+item.getSumQty()+ item.getWareDw());
//        tvCountOoc.setText("大小数量："+MyUnitUtil.maxMinUnit2(item.getWareDw(), item.getMinUnit(), item.getHsNum(), item.getSumQty()));
//        tvCountSy.setVisibility(View.GONE);

        //库存
        String stkQty = item.getSumQty();
        String occQty = item.getOccQty();
        try {
            if (MyStringUtil.isNotEmpty(stkQty)) {
                tvCount.setVisibility(View.VISIBLE);
                tvCount.setText("实际库存："+ MyUnitUtil.maxMinUnit(item.getWareDw(), item.getMinUnit(), item.getHsNum(), stkQty));
                if (MyStringUtil.isNotEmpty(occQty)) {
                    tvCountOoc.setText("已占："+ MyUnitUtil.maxMinUnit(item.getWareDw(), item.getMinUnit(), item.getHsNum(), occQty));
                    tvCountSy.setText("剩余："+ MyUnitUtil.maxMinUnit(item.getWareDw(), item.getMinUnit(), item.getHsNum(), "" + MyMathUtils.subtract(new BigDecimal(stkQty), new BigDecimal(occQty))));
                    tvCountOoc.setVisibility(View.VISIBLE);
                    tvCountSy.setVisibility(View.VISIBLE);
                } else {
                    tvCountOoc.setVisibility(View.GONE);
                    tvCountSy.setVisibility(View.GONE);
                }
            } else {
                tvCount.setVisibility(View.GONE);
                tvCountOoc.setVisibility(View.GONE);
                tvCountSy.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            tvCount.setText("实际库存："+ stkQty);
        }

    }

    public List<ShopInfoBean.Data> getSelectList() {
        return selectList;
    }

    public void setSelectList(List<ShopInfoBean.Data> selectList) {
        this.selectList = selectList;
    }
}
