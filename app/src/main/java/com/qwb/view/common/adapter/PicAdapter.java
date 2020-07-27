package com.qwb.view.common.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xmsx.cnlife.widget.photo.ImagePagerActivity;
import com.qwb.utils.MyGlideUtil;
import com.chiyong.t3.R;

import java.util.List;


/**
 * 文 件 名: 拍照图片
 * 修改时间：
 * 修改备注：
 */
public class PicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private boolean isDelete = false;
    private Context mContext;
    private OnDeletePicListener onDeletePicListener;

    public PicAdapter(Context context) {
        super(R.layout.x_adapter_common_pic);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, String item) {
        helper.setIsRecyclable(false);//不复用
        //点击事件

        //赋值
        View layout = helper.getView(R.id.item_layout);
        ImageView iv = helper.getView(R.id.item_iv);
        View layoutDel = helper.getView(R.id.item_layout_del);

        MyGlideUtil.getInstance().displayImageSquere(item, iv);
        //设置宽高比例1:1
        int picWidth = (int) mContext.getResources().getDimension(R.dimen.dp_105);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(picWidth, picWidth);
        iv.setLayoutParams(params);

        //删除
        if (isDelete) {
            layoutDel.setVisibility(View.VISIBLE);
        } else {
            layoutDel.setVisibility(View.GONE);
        }

        final int position = helper.getAdapterPosition();
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDelete) {
                    if (null != onDeletePicListener) {
                        onDeletePicListener.onDeletePicListener(position);
                    }
                } else {
                    //放大图片
                    List<String> picList = getData();
                    String[] urls = new String[picList.size()];
                    for (int i = 0; i < picList.size(); i++) {
                        String path = picList.get(i);
                        urls[i] = path;

                    }
                    // 点击放大图片
                    Intent intent = new Intent(mContext, ImagePagerActivity.class);
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                    mContext.startActivity(intent);
                }
            }
        });

    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public void setOnDeletePicListener(OnDeletePicListener onDeletePicListener) {
        this.onDeletePicListener = onDeletePicListener;
    }

    public interface OnDeletePicListener {
        void onDeletePicListener(int position);
    }
}
