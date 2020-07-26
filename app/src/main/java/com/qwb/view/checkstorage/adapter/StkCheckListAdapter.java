package com.qwb.view.checkstorage.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.checkstorage.model.StkCheckBean;
import com.xmsx.qiweibao.R;
import java.util.ArrayList;
import java.util.List;

/**
 * 文 件 名:
 * 修改时间：
 * 修改备注：
 */
public class StkCheckListAdapter extends BaseQuickAdapter<StkCheckBean,BaseViewHolder> {

    private List<StkCheckBean> selectList = new ArrayList<>();
    private int type;//2临时盘点单（多人盘点单）

    public StkCheckListAdapter() {
        super(R.layout.x_adapter_stk_check_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, StkCheckBean item) {
        helper.addOnClickListener(R.id.item_iv_cb);

        helper.setText(R.id.tv_orderNo, item.getBillNo());// 订单号
        helper.setText(R.id.tv_name, item.getStaff());
        helper.setText(R.id.tv_orderTime, item.getCheckTimeStr());
        helper.setText(R.id.tv_stkName, item.getStkName());

        TextView tvOrderZt = helper.getView(R.id.tv_orderZt);
        ImageView ivCb = helper.getView(R.id.item_iv_cb);
        TextView tvIsPc = helper.getView(R.id.tv_is_pc);

        Integer isPc = item.getIsPc();
        if (MyStringUtil.isNotEmpty(String.valueOf(isPc)) && "1".equals(String.valueOf(isPc))){
            tvIsPc.setText("批次");
        }else{
            tvIsPc.setText("");
        }

        //status 盘点单：-2暂存；0审批；2作废；临时盘点单（多人盘点单）：-2暂存；0已合并；1审批；2作废
        String status = item.getStatus();
        if("-2".equals(status)){
            tvOrderZt.setText("暂存");
            if(2 == type){
                boolean flag = false;
                if(selectList != null && selectList.size() > 0){
                    for (StkCheckBean bean : selectList) {
                        if((""+bean.getId()).equals("" + item.getId())){
                            flag = true;
                            break;
                        }
                    }
                }
                ivCb.setVisibility(View.VISIBLE);
                if(flag){
                    ivCb.setImageResource(R.drawable.icon_dxz);
                }else{
                    ivCb.setImageResource(R.drawable.icon_dx);
                }
            }else{
                ivCb.setVisibility(View.GONE);
            }
        }else if("0".equals(status)){
            if(type == 2){
                tvOrderZt.setText("已合并");
            }else{
                tvOrderZt.setText("已审批");
            }
            ivCb.setVisibility(View.GONE);
        }else if("1".equals(status)){
            if(type == 2){
                tvOrderZt.setText("已审批");
            }
            ivCb.setVisibility(View.GONE);
        }else if("2".equals(status)){
            tvOrderZt.setText("已作废");
            ivCb.setVisibility(View.GONE);
        }else if("3".equals(status)){
            tvOrderZt.setText("被冲红单");
            ivCb.setVisibility(View.GONE);
        }else if("4".equals(status)){
            tvOrderZt.setText("冲红单");
            ivCb.setVisibility(View.GONE);
        }

    }

    public List<StkCheckBean> getSelectList() {
        return selectList;
    }

    public void setSelectList(List<StkCheckBean> selectList) {
        this.selectList = selectList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
