package com.qwb.view.storehouse.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.MyColorUtil;
import com.qwb.view.storehouse.model.StorehouseOutSubBean;
import com.qwb.utils.ToastUtils;
import com.chiyong.t3.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 出仓单:左边商品
 */
public class StorehouseOutLeftAdapter extends BaseQuickAdapter<StorehouseOutSubBean, BaseViewHolder> {
    private Map<Integer, Integer> repeatMap = new HashMap<>();

    public StorehouseOutLeftAdapter() {
        super(R.layout.x_step5_table_left_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, StorehouseOutSubBean item) {
        TextView tvWareNm = helper.getView(R.id.tv_table_content_item_left);
        TextView tvNum = helper.getView(R.id.tv_table_num);
        try{
            int position = helper.getAdapterPosition();
            tvWareNm.setText(item.getWareNm());
            tvNum.setText((position + 1) +"");
            //商品重复的颜色变红
            Integer repeat = repeatMap.get(item.getWareId());
            if(null != repeat && repeat > 1){
                tvWareNm.setTextColor(MyColorUtil.getColorResId(R.color.blue));
            }else{
                tvWareNm.setTextColor(MyColorUtil.getColorResId(R.color.gray_3));
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    public Map<Integer, Integer> getRepeatMap() {
        return repeatMap;
    }

    public void setRepeatMap(Map<Integer, Integer> repeatMap) {
        this.repeatMap = repeatMap;
    }
}
