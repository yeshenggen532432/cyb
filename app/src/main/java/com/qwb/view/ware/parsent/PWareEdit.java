package com.qwb.view.ware.parsent;


import android.app.Activity;
import android.graphics.Bitmap;

import com.alibaba.fastjson.JSON;
import com.nanchen.compresshelper.CompressHelper;
import com.qwb.utils.ActivityManager;
import com.qwb.view.base.model.BaseBean;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.ware.model.WareInfoBean;
import com.qwb.view.ware.ui.WareEditActivity;
import com.qwb.utils.MyHttpUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.ware.model.QueryStkWareType;
import com.qwb.view.base.model.TreeBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：添加商品
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PWareEdit extends XPresent<WareEditActivity>{

    private Activity mActivity;

    /**
     * * 获得商品分类列表
     */
    public void queryDataWareTypes(Activity activity) {
        this.mActivity = activity;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        MyHttpUtil.getInstance()
                .post(activity, params, Constans.queryWareTypeList, new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson1(response);
                    }
                });
    }

    /**
     * 添加
     */
    public void addData(Activity activity, String delPicIds, List<String> picList,
                        String status, String wareTypeId, String wareId, String wareNm,
                        String wareDw, String wareGg, String wareDj, String inPrice, String maxBarCode,
                        String bUnit, String sUnit, String hsNum,
                        String minWareDw, String minWareGg, String minWareDj, String minInPrice, String minBarCode
        ) {
        this.mActivity = activity;

        Map<String, String> params = new HashMap<>();
        //必传
        params.put("token", SPUtils.getTK());
        if(!MyStringUtil.isEmpty(delPicIds)){
            params.put("delPicIds", delPicIds);
        }
        if(!MyStringUtil.isEmpty(wareId)){
            params.put("wareId", wareId);
        }

        params.put("status", status);//启用状态 （1是；2否）
        params.put("wareNm", wareNm);
        params.put("waretype", wareTypeId);
        //大单位
        params.put("wareDw", wareDw);
        params.put("wareGg", wareGg);
        params.put("wareDj", wareDj);
        params.put("inPrice", inPrice);
        params.put("packBarCode", maxBarCode);
        //比例
        params.put("bUnit", bUnit);
        params.put("sUnit", sUnit);
        if(!MyStringUtil.isEmpty(hsNum)){
            params.put("hsNum", hsNum);
        }

        //小单位
        params.put("minUnit", minWareDw);
        params.put("minWareGg", minWareGg);
        params.put("sunitPrice", minWareDj);
        params.put("minInPrice", minInPrice);
        params.put("beBarCode", minBarCode);

        //固定
        params.put("maxUnitCode", "B");
        params.put("minUnitCode", "S");

        // 图片数组(map按key升序)
        TreeMap<String, File> files = new TreeMap<>(new Comparator<String>(){
            @Override
            public int compare(String o1, String o2) {return o1.compareTo(o2);}
        });
        if (null != picList && !picList.isEmpty()) {
            for (int i = 0; i < picList.size(); i++) {
                File oldFile=new File(picList.get(i));
                File newFile = new CompressHelper.Builder(activity)
                        .setMaxWidth(720)  // 默认最大宽度为720
                        .setMaxHeight(1280) // 默认最大高度为960
                        .setQuality(80)    // 默认压缩质量为80
                        .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式i
                        .setFileName(String.valueOf(System.currentTimeMillis()+"_"+i)) // 设置你的文件名
                        .setDestinationDirectoryPath(Constans.DIR_IMAGE_PROCEDURE)
                        .build()
                        .compressToFile(oldFile);
                //TODO 文件拼接“_i”,取出“_i”为key,最后key升序
                String absolutePath=newFile.getAbsolutePath();
                String j=absolutePath.substring(absolutePath.length()-6,absolutePath.length()-5);
                files.put("file" + j, newFile);
            }
        }

        OkHttpUtils
            .post()
            .params(params)
            .files(files)
            .url(Constans.saveWare)
            .id(1)
            .build()
            .execute(new MyHttpCallback(activity) {
                @Override
                public void myOnError(Call call, Exception e, int id) {

                }

                @Override
                public void myOnResponse(String response, int id) {
                    parseJson2(response);
                }
            });
    }

    /**
     * 获取商品信息
     */
    public void queryDataWare(Activity activity, String wareId) {
        String url=Constans.queryWareWeb;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("wareId", String.valueOf(wareId));
        OkHttpUtils.post()
                .params(params)
                .url(url)
                .id(3)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson3(response);
                    }
                });
    }

    //TODO ------------------------接口回調----------------------


    //解析数据-结构图：部门，员工
    private void parseJson1(String response) {
        try {
            List<TreeBean> mDatas = new ArrayList<>();
            mDatas.clear();

            QueryStkWareType bean = JSON.parseObject(response, QueryStkWareType.class);
            if (bean != null) {
                if (bean.isState()) {
                    List<QueryStkWareType.List1> list = bean.getList();
                    if (list != null && list.size() > 0) {
                        // 第一层
                        for (int i = 0; i < list.size(); i++) {
                            QueryStkWareType.List1 list1 = list.get(i);
                            int id_1 = list1.getWaretypeId();
                            mDatas.add(new TreeBean(id_1, 0, list1.getWaretypeNm()));
                            // 第二层
                            List<QueryStkWareType.List2> list2 = list1.getList2();
                            if (list2 != null && list2.size() > 0) {
                                for (int z = 0; z < list2.size(); z++) {
                                    QueryStkWareType.List2 list2_2 = list2.get(z);
                                    int id_2 = list2_2.getWaretypeId();
                                    mDatas.add(new TreeBean(id_2, id_1, list2_2.getWaretypeNm()));
                                }
                            }
                        }
                        if(getV()!=null){
                            // 刷新Tree
                            getV().refreshAdapterTree(mDatas);
                        }
                    }
                } else {
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据
    private void parseJson2(String response) {
        try {
            BaseBean bean = JSON.parseObject(response, BaseBean.class);
            if (bean != null) {
                if(bean.isState()){
                    ActivityManager.getInstance().closeActivity(mActivity);
                }
                ToastUtils.showCustomToast(bean.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //解析数据-商品信息
    private void parseJson3(String response) {
        try {
            WareInfoBean bean = JSON.parseObject(response, WareInfoBean.class);
            if (bean != null) {
                if (bean.isState()) {
                    if(getV()!=null){
                        getV().showData(bean.getSysWare());
                    }
                }else{
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

}
