package com.qwb.view.ware.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.ToastUtils;
import com.qwb.view.step.model.ShopInfoBean;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 普通选择商品的右边
 */
public class ChooseWareRightAdapter extends BaseQuickAdapter<ShopInfoBean.Data, BaseViewHolder> {

    private List<ShopInfoBean.Data> selectList = new ArrayList<>();

    public ChooseWareRightAdapter() {
        super(R.layout.x_adapter_choose_ware_right);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopInfoBean.Data item) {
        try {
            TextView tvName = helper.getView(R.id.tv_name_choose_shop);
            ImageView ivCb = helper.getView(R.id.item_iv_cb);

            tvName.setText(item.getWareNm());

            //CheckBox
            boolean flag = false;
            if (selectList != null && selectList.size() > 0) {
                for (ShopInfoBean.Data bean : selectList) {
                    if ((String.valueOf(bean.getWareId())).equals(String.valueOf(item.getWareId()))) {
                        flag = true;
                        break;
                    }
                }
            }
            if (flag) {
                ivCb.setImageResource(R.drawable.icon_dxz);
            } else {
                ivCb.setImageResource(R.drawable.icon_dx);
            }


        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


    public List<ShopInfoBean.Data> getSelectList() {
        return selectList;
    }

    public void setSelectList(List<ShopInfoBean.Data> selectList) {
        this.selectList = selectList;
    }
}