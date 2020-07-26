package com.qwb.view.table.parsent;


import android.app.Activity;
import com.alibaba.fastjson.JSON;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.table.ui.TableActivity3;
import com.qwb.utils.Constans;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.table.model.FooterBean;
import com.qwb.view.step.model.QueryXstypeBean;
import com.qwb.view.table.model.Statement_cpddBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 产品订单统计表
 */
public class PTable3 extends XPresent<TableActivity3>{
    private String dataTp = MyLoginUtil.getDataTp(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.TJBB_NEW, ConstantUtils.Apply.BBTJ_OLD);
    private String dataTpMids = MyLoginUtil.getDataTpMids(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.TJBB_NEW, ConstantUtils.Apply.BBTJ_OLD);

    /**
     * 获取：产品订单统计表
     */
    public void queryData(Activity activity, int pageNo,int pageSize, String sDate, String eDate, String khNm,String memberName, String jepx, String xstypeStr, String pszdStr) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("stime", sDate);//开始日期
        params.put("etime", eDate);//结束日期（默认当前日期）
        params.put("khNm", khNm);//业务员
        params.put("memberNm", memberName);//业务员
        params.put("jepx", jepx);//金额排序1高到低；2低到高
        params.put("xsTp", xstypeStr);//销售类型
        params.put("pszd", pszdStr);//配送指定

        params.put("dataTp", dataTp);//客户id(拜访步骤进入)
        if("4".equals(dataTp)){
            params.put("mids", dataTpMids);//角色
        }

        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryCpddtjWeb)
                .id(1)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson1(response);
                    }
                });

    }

    /**
     * 获取：销售类型
     */
    public void queryDataXstp(Activity context) {
        OkHttpUtils
                .post()
                .addParams("token", SPUtils.getTK())
                .url(Constans.queryXstypels)
                .id(2)
                .build()
                .execute(new MyHttpCallback(context) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson2(response);
                    }
                });
    }


    //解析数据-产品订单统计表
    private void parseJson1(String response) {
        try {
            Statement_cpddBean parseObject = JSON.parseObject(response,Statement_cpddBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<Statement_cpddBean.Cpdd> rows = parseObject.getRows();
                List<FooterBean> footer = parseObject.getFooter();
                if(getV()!=null){
                    getV().showData(rows,footer);
                }
            } else {
                ToastUtils.showCustomToast(parseObject.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据-销售类型
    private void parseJson2(String response) {
        try {
            List<String> xstpList=new ArrayList<>();
            QueryXstypeBean parseObject = JSON.parseObject(response, QueryXstypeBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<QueryXstypeBean.QueryXstype> xstypels = parseObject.getXstypels();
                xstpList.clear();
                xstpList.add("全部");
                for (int i = 0; i < xstypels.size(); i++) {
                    QueryXstypeBean.QueryXstype queryXstype = xstypels.get(i);
                    String xstpNm = queryXstype.getXstpNm();
                    xstpList.add(xstpNm);
                }
                if(getV()!=null){
                    getV().showDialogXsTp(xstpList);
                }
            }

        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }


}
