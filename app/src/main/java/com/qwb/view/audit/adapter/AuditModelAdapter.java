package com.qwb.view.audit.adapter;


import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.audit.model.AuditModelBean;
import com.qwb.utils.MyStringUtil;
import com.chiyong.t3.R;

/**
 * 审批模版
 */
public class AuditModelAdapter extends BaseQuickAdapter<AuditModelBean, BaseViewHolder> {

    private Context context;

    public AuditModelAdapter(Context context) {
        super(R.layout.x_adapter_sp_medel);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, AuditModelBean item) {
        //点击事件
        helper.addOnClickListener(R.id.item_layout_del);

        //赋值
        //有“别名”显示“别名”，否则显示“审批模板名称”
        String name;
        if(!MyStringUtil.isEmpty(item.getShortName())){
            name = item.getShortName();
        }else{
            name = item.getName();
        }
        helper.setText(R.id.item_name, name);

        ImageView iv = helper.getView(R.id.item_icon);
        iv.setImageResource(item.getImgRes());

    }
}
