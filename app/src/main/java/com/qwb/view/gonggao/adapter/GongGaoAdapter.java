package com.qwb.view.gonggao.adapter;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.gonggao.model.GongGaoBean;
import com.qwb.utils.Constans;
import com.qwb.utils.MyGlideUtil;
import com.qwb.utils.MyStringUtil;
import com.chiyong.t3.R;

/**
 * 公告
 */
public class GongGaoAdapter extends BaseQuickAdapter<GongGaoBean,BaseViewHolder> {
    private Context mContext;

    public GongGaoAdapter(Context context) {
        super(R.layout.x_adapter_gonggao);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, GongGaoBean item) {

        helper.setText(R.id.item_tv_title, item.getNoticeTitle());
        ImageView imageView = helper.getView(R.id.item_iv);
        String url = item.getNoticePic();
        if(MyStringUtil.isEmpty(url)){
            imageView.setVisibility(View.GONE);
        }else{
            MyGlideUtil.getInstance().setRadius(mContext, imageView, Constans.IMGROOTHOST + item.getNoticePic(),1);
            imageView.setVisibility(View.VISIBLE);
        }


    }


}
