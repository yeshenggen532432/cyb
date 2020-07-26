package com.qwb.view.cache.parsent;


import android.app.Activity;

import com.qwb.view.cache.ui.CacheWareActivity;
import com.qwb.utils.MyDataUtils;
import com.qwb.db.DWareBean;
import com.qwb.db.DWareTypeBean;
import com.qwb.view.step.model.ShopInfoBean;
import com.qwb.view.base.model.TreeBean;

import java.util.ArrayList;
import java.util.List;
import cn.droidlover.xdroidmvp.mvp.XPresent;

/**
 * 创建描述：缓存商品，商品分类
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PCacheWare extends XPresent<CacheWareActivity>{

    /**
     *
     */
    public void queryCacheWareType(Activity activity) {
        try {
            List<DWareTypeBean> wareTypeBeans =  MyDataUtils.getInstance().queryWareType();
            if(null == wareTypeBeans || wareTypeBeans.isEmpty()){
                return;
            }

            List<TreeBean> mData = new ArrayList();
            for (DWareTypeBean bean: wareTypeBeans) {
                mData.add(new TreeBean(bean.getWareTypeId(), bean.getPid(), bean.getWareTypeNm()));
            }

            if(null != getV()){
                // 刷新Tree
                getV().refreshAdapterTree(mData);
            }
        }catch (Exception e){
        }

    }
    /**
     *
     */
    public void queryCacheWare(Activity activity, String search, String wareType, int pageNo, int pageSize) {
        try {
            List<DWareBean> wareBeans =  MyDataUtils.getInstance().queryWare(search, wareType, pageNo, pageSize);
            if(null != wareBeans){
                List<ShopInfoBean.Data> dataList = new ArrayList<>();
                for (DWareBean bean: wareBeans) {
                    ShopInfoBean.Data data = new ShopInfoBean.Data();
                    data.setWareId(bean.getWareId());
                    data.setWareNm(bean.getWareNm());
                    data.setHsNum(bean.getHsNum());
                    data.setWareGg(bean.getWareGg());
                    data.setWareDw(bean.getWareDw());
                    data.setMinUnit(bean.getMinUnit());
                    data.setWareDj(bean.getWareDj());
                    data.setMaxUnitCode(bean.getMaxUnitCode());
                    data.setMinUnitCode(bean.getMinUnitCode());
                    data.setSunitFront(bean.getSunitFront());
                    dataList.add(data);
                }
                getV().refreshAdapterRight(dataList,true);
            }
        }catch (Exception e){
        }
    }


}
