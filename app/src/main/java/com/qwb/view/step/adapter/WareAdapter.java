package com.qwb.view.step.adapter;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.step.model.WarePicBean;
import com.qwb.utils.MyDoubleUtils;
import com.xmsx.cnlife.widget.photo.ImagePagerActivity;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyGlideUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.step.model.ShopInfoBean;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 文 件 名: 商品
 * 修改时间：
 * 修改备注：
 */
public class WareAdapter extends BaseQuickAdapter<ShopInfoBean.Data, BaseViewHolder> {

    public boolean showAdd = true;//是否显示“添加”按钮（商品管理没有）
    private List<ShopInfoBean.Data> selectList = new ArrayList<>();

    private String type = "4";//4.销售小结 5.订货下单  6.选择商品（有复选框）

    public WareAdapter() {
        super(R.layout.x_adapter_choose_shop_right);
    }

    public WareAdapter(String type) {
        super(R.layout.x_adapter_choose_shop_right);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopInfoBean.Data item) {
        helper.addOnClickListener(R.id.iv_save_choose_shop);// 收藏
        helper.addOnClickListener(R.id.iv_add_choose_shop);// 添加商品

        try {
            LinearLayout parent = helper.getView(R.id.parent);//
            TextView tvName = helper.getView(R.id.tv_name_choose_shop);//距离
            TextView tvCount = helper.getView(R.id.tv_count_choose_shop);//实际库存
            TextView tvCountOoc = helper.getView(R.id.tv_count_ooc);//已占
            TextView tvCountSy = helper.getView(R.id.tv_count_sy);//剩余
            ImageView ivSave = helper.getView(R.id.iv_save_choose_shop);
            ImageView ivAdd = helper.getView(R.id.iv_add_choose_shop);
            ImageView ivWare = helper.getView(R.id.item_iv_ware);
            ImageView ivCb = helper.getView(R.id.item_iv_cb);

            tvName.setText(item.getWareNm());

            //是否显示“+”按钮，是否可收藏
            if (showAdd) {
                ivAdd.setVisibility(View.VISIBLE);
            } else {
                //商品管理-没有添加按钮，收藏按钮没有点击事件
                ivAdd.setVisibility(View.GONE);
                ivSave.setEnabled(false);
            }

            if("4".equals(type) || "5".equals(type)){
                //收藏：可编辑；添加商品+：显示；复选框：隐藏
                ivSave.setEnabled(true);
                ivAdd.setVisibility(View.VISIBLE);
                ivCb.setVisibility(View.GONE);
            }else if("6".equals(type)){
                //收藏：不可编辑；添加商品+：隐藏；复选框：显示
                //---------------------cb:start-----------------------
                ivSave.setEnabled(false);
                ivAdd.setVisibility(View.GONE);
                ivCb.setVisibility(View.VISIBLE);
                boolean flag = false;
                if(selectList != null && selectList.size() > 0){
                    for (ShopInfoBean.Data bean : selectList) {
                        if((String.valueOf(bean.getWareId())) .equals( String.valueOf(item.getWareId()) )){
                            flag = true;
                            break;
                        }
                    }
                }
                if(flag){
                    ivCb.setImageResource(R.drawable.icon_dxz);
                }else{
                    ivCb.setImageResource(R.drawable.icon_dx);
                }
                //---------------------cb:end-----------------------
            }

            //收藏
            if (0 == item.getUserId()) {//未收藏
                ivSave.setImageResource(R.drawable.ic_unsave_choose_shop);
            } else if (1 == item.getUserId()) {//收藏
                ivSave.setImageResource(R.drawable.ic_save_choose_shop);
            }

            Boolean isShowWarePic = SPUtils.getBoolean(ConstantUtils.Sp.CHOOSE_WARE_PIC_SHOW);
            if(isShowWarePic !=null && isShowWarePic){
                //服务器的图片
                final List<WarePicBean> warePicList = item.getWarePicList();
                if (warePicList != null && !warePicList.isEmpty()) {
                    String sourcePath = Constans.IMGROOTHOST + warePicList.get(0).getPicMini();
                    for (WarePicBean goodsPic : warePicList) {
                        //1:为主图
                        if (goodsPic.getType() == 1) {
                            sourcePath = Constans.IMGROOTHOST + goodsPic.getPicMini();
                            break;
                        }
                    }
                    MyGlideUtil.getInstance().displayImageSquere(sourcePath, ivWare);
                }else{
                    ivWare.setImageResource(R.drawable.empty_photo);
                }


                ivWare.setVisibility(View.VISIBLE);
                ivWare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //放大图片
                        int positionPic = 0;
                        if (warePicList != null && !warePicList.isEmpty()) {
                            String[] urls = new String[warePicList.size()];
                            for (int i = 0; i < warePicList.size(); i++) {
                                urls[i]= Constans.IMGROOTHOST + warePicList.get(i).getPic();
                                //1:为主图
                                if(warePicList.get(i).getType() == 1){
                                    positionPic=i;
                                }
                            }
                            // 点击放大图片
                            Intent intent = new Intent(mContext, ImagePagerActivity.class);
                            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
                            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, positionPic);
                            mContext.startActivity(intent);
                        }
                    }
                });
            }else{
                ivWare.setVisibility(View.GONE);
            }

            //库存
            if (MyStringUtil.isEmpty(item.getStkQty())) {
                tvCount.setText("");
            } else {
                try {
                    String stkQty = item.getStkQty();
                    String occQty = item.getOccQty();

                    if (!MyUtils.isEmptyString(stkQty)) {
                        //开单辅单位排前；1:是
                        int SunitFront = item.getSunitFront();
                        if (SunitFront == 1) {
                            double douleStkQty = Double.valueOf(stkQty);
                            double hsNum = Double.valueOf(item.getHsNum());
                            String minUnit = item.getMinUnit();
                            tvCount.setVisibility(View.VISIBLE);
                            //备注：如果hsNum大于等于1说明：大单位在前，小单位再后
                            tvCount.setText("实际库存:" + MyDoubleUtils.getDecimalNotRounded(douleStkQty * hsNum) + minUnit);
                            if (hsNum >= 1) {
                                tvCount.setText("实际库存:" + Integer.parseInt(new java.text.DecimalFormat("0").format(douleStkQty * hsNum)) + minUnit);
                            }
                            if (!MyUtils.isEmptyString(occQty)) {
                                double douleOocQty = MyDoubleUtils.getDecimalNotRounded(Double.valueOf(occQty));
                                double douleSyQty = MyDoubleUtils.getDecimalNotRounded(Double.valueOf(stkQty) - Double.valueOf(occQty));
                                tvCountOoc.setText("已占:" + MyDoubleUtils.getDecimalNotRounded(douleOocQty * hsNum));
                                tvCountSy.setText("剩余:" + MyDoubleUtils.getDecimalNotRounded(douleSyQty * hsNum) + minUnit);
                                if (hsNum >= 1) {
                                    int iStkQty = Integer.parseInt(new java.text.DecimalFormat("0").format(douleStkQty * hsNum));
                                    int iOccQty = Integer.parseInt(new java.text.DecimalFormat("0").format(douleOocQty * hsNum));
                                    tvCountOoc.setText("已占:" + iOccQty);
                                    tvCountSy.setText("剩余:" + (iStkQty - iOccQty) + minUnit);
                                }
                                if (douleSyQty < 0) {
                                    tvCountSy.setTextColor(mContext.getResources().getColor(R.color.red));
                                } else {
                                    tvCountSy.setTextColor(mContext.getResources().getColor(R.color.blue));
                                }
                                tvCountOoc.setVisibility(View.VISIBLE);
                                tvCountSy.setVisibility(View.VISIBLE);
                            } else {
                                tvCountOoc.setVisibility(View.GONE);
                                tvCountSy.setVisibility(View.GONE);
                            }
                        } else {
                            double douleStkQty = MyDoubleUtils.getDecimalNotRounded(Double.valueOf(stkQty));
                            double hsNum = Double.valueOf(item.getHsNum());
                            tvCount.setText("实际库存:" + douleStkQty + item.getWareDw());
                            //备注：如果hsNum小于1说明：大单位在后，小单位再前
                            if (hsNum < 1) {
                                tvCount.setText("实际库存:" + Integer.parseInt(new java.text.DecimalFormat("0").format(douleStkQty)) + item.getWareDw());
                            }
                            tvCount.setVisibility(View.VISIBLE);
                            if (!MyUtils.isEmptyString(occQty)) {
                                double douleOocQty = MyDoubleUtils.getDecimalNotRounded(Double.valueOf(occQty));
                                double douleSyQty = MyDoubleUtils.getDecimalNotRounded(Double.valueOf(stkQty) - Double.valueOf(occQty));
                                tvCountOoc.setText("已占:" + douleOocQty);
                                tvCountSy.setText("剩余:" + douleSyQty + item.getWareDw());
                                if (hsNum < 1) {
                                    int iStkQty = Integer.parseInt(new java.text.DecimalFormat("0").format(douleStkQty));
                                    int iOccQty = Integer.parseInt(new java.text.DecimalFormat("0").format(douleOocQty));
                                    tvCountOoc.setText("已占:" + iOccQty);
                                    tvCountSy.setText("剩余:" + (iStkQty - iOccQty) + item.getWareDw());
                                }
                                if (douleSyQty < 0) {
                                    tvCountSy.setTextColor(mContext.getResources().getColor(R.color.red));
                                } else {
                                    tvCountSy.setTextColor(mContext.getResources().getColor(R.color.blue));
                                }
                                tvCountOoc.setVisibility(View.VISIBLE);
                                tvCountSy.setVisibility(View.VISIBLE);
                            } else {
                                tvCountOoc.setVisibility(View.GONE);
                                tvCountSy.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        tvCount.setVisibility(View.GONE);
                        tvCountOoc.setVisibility(View.GONE);
                        tvCountSy.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    tvCount.setText("实际库存:" + item.getStkQty());
                }
            }
        }catch (Exception e){
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