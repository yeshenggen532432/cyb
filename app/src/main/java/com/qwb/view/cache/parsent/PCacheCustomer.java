package com.qwb.view.cache.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.qwb.view.cache.ui.CacheCustomerActivity;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.db.DMineCustomerBean;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.customer.model.MineClientBean;
import com.qwb.view.customer.model.MineClientInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

import static com.github.promeg.pinyinhelper.Pinyin.toPinyin;

/**
 * 创建描述：我的客户
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PCacheCustomer extends XPresent<CacheCustomerActivity>{
    private double mLat;
    private double mLon;

    // 请求个人全部客户--用于缓存
    public void loadDataAllClient(Activity activity, double latitude, double longitude) {
        mLat = latitude;
        mLon = longitude;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", "1");
        params.put("pageSize", "10000");
        params.put("latitude", "" + latitude);
        params.put("longitude", "" + longitude);
        params.put("dataTp", "1");
//        params.put("mids", SPUtils.getID());// 角色
        OkHttpUtils.post()
                .params(params)
                .url(Constans.mineClient)
                .id(5)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson5(response);
                    }
                });
    }

    //TODO ------------------------接口回調----------------------

    //解析数据-我的客户(全部用于缓存)
    private void parseJson5(String response) {
        try {
            MineClientBean parseObject = JSON.parseObject(response, MineClientBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<MineClientInfo> dataList = parseObject.getRows();
                if(dataList!=null && !dataList.isEmpty()){
                    MyDataUtils.getInstance().saveMineClient(dataList);

                    List<DMineCustomerBean> beanList = new ArrayList<>();
                    for (MineClientInfo client:dataList) {
                        DMineCustomerBean bean =new DMineCustomerBean();
                        bean.setUserId(SPUtils.getID());
                        bean.setCompanyId(SPUtils.getCompanyId());
                        bean.setCid(client.getId()+"");
                        bean.setKhTp(client.getKhTp()+"");
                        bean.setKhNm(client.getKhNm());
                        bean.setMemId(client.getMemId()+"");
                        bean.setMemberNm(client.getMemberNm());
                        bean.setBranchName(client.getBranchName());
                        bean.setLongitude(client.getLongitude());
                        bean.setLatitude(client.getLatitude());
                        bean.setAddress(client.getAddress());
                        bean.setScbfDate(client.getScbfDate());
                        bean.setLinkman(client.getLinkman());
                        bean.setMobile(client.getMobile());
                        bean.setTel(client.getTel());
                        bean.setLinkman(client.getLinkman());
                        bean.setProvince(client.getProvince());
                        bean.setCity(client.getCity());
                        bean.setArea(client.getArea());
                        bean.setQdtpNm(client.getQdtpNm());
                        bean.setXsjdNm(client.getXsjdNm());

                        String cLat = client.getLatitude();
                        String cLon = client.getLongitude();
                        if(!MyStringUtil.isEmpty(cLat) && !MyStringUtil.isEmpty(cLon)){
                            LatLng p1 = new LatLng(Double.valueOf(mLat), Double.valueOf(mLon));
                            LatLng p2 = new LatLng(Double.valueOf(cLat), Double.valueOf(cLon));
                            bean.setDistance((Math.floor(DistanceUtil. getDistance(p1, p2))));
                        }else{
                            bean.setDistance(100000000);//1百万米
                        }
                        beanList.add(bean);
                    }
                    Collections.sort(beanList, new Comparator<DMineCustomerBean>() {
                        public int compare(DMineCustomerBean b1, DMineCustomerBean b2) {
                            return new Double(b1.getDistance()).compareTo(new Double(b2.getDistance())); //升序
                        }
                    });

                    getV().refreshAdpater(beanList);
                }
            } else {
                ToastUtils.showCustomToast(parseObject.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }
}
