package com.qwb.view.checkstorage.adapter;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.checkstorage.model.StkWareBean;
import com.xmsx.qiweibao.R;

import java.util.ArrayList;

/**
 * 文 件 名: 仓库商品
 * 创建日期:
 * 修改时间：
 * 修改备注：
 */
public class StkWareAdapter extends BaseQuickAdapter<StkWareBean,BaseViewHolder> {

    ArrayList<Integer> ids = new ArrayList<>();
    private boolean isFilter = true;

    public StkWareAdapter() {
        super(R.layout.x_adapter_stk_ware);
    }

    @Override
    protected void convert(BaseViewHolder helper, final StkWareBean item) {
        helper.setIsRecyclable(false);//不复用
        //点击事件
        LinearLayout parent = helper.getView(R.id.parent);
        TextView tvWareNm = helper.getView(R.id.item_tv_wareNm);
        TextView tvWareDw = helper.getView(R.id.item_tv_wareDw);
        tvWareNm.setText(item.getWareNm());
        tvWareDw.setText("单位："+item.getUnitName());

//        if(isFilter){
//            if(ids != null && ids.size()> 0){
//                boolean flag = true;
//                for (int i : ids) {
//                    if(i == item.getWareId()){
//                        flag = false;
//                        break;
//                    }
//                }
//                if(flag){
//                    parent.setVisibility(View.VISIBLE);
//                }else{
//                    parent.setVisibility(View.GONE);
//                }
//            }
//        }else{
//            parent.setVisibility(View.VISIBLE);
//        }

    }

    public ArrayList<Integer> getIds() {
        return ids;
    }

    public void setIds(ArrayList<Integer> ids) {
        this.ids = ids;
    }

    public boolean isFilter() {
        return isFilter;
    }

    public void setFilter(boolean filter) {
        isFilter = filter;
    }
}
