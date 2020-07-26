package com.qwb.view.table.parsent;


import android.app.Activity;
import com.alibaba.fastjson.JSON;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.table.ui.TableActivity4;
import com.qwb.utils.Constans;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.table.model.FooterBean;
import com.qwb.view.table.model.Statement_xsddBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：销售订单统计表
 */
public class PTable4 extends XPresent<TableActivity4> {
    private String dataTp = MyLoginUtil.getDataTp(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.TJBB_NEW, ConstantUtils.Apply.BBTJ_OLD);
    private String dataTpMids = MyLoginUtil.getDataTpMids(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.TJBB_NEW, ConstantUtils.Apply.BBTJ_OLD);

    /**
     * 获取：销售订单统计表
     */
    public void queryData(Activity activity, int pageNo, int pageSize, String sDate, String eDate, String khNm,String memberName, String orderZt, String pszd, String cid) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("stime", sDate);//开始日期
        params.put("etime", eDate);//结束日期（默认当前日期）
        params.put("khNm", khNm);//业务员
        params.put("memberNm", memberName);//业务员
        params.put("orderZt", orderZt);//订单状态
        params.put("pszd", pszd);//配送指定
        params.put("cid ", cid);//客户id(拜访步骤进入)
        params.put("dataTp", dataTp);//客户id(拜访步骤进入)
        if ("4".equals(dataTp)) {
            params.put("mids", dataTpMids);//角色
        }
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryXsddtjWeb)
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


    //TODO ------------------------接口回調----------------------

    //解析数据-销售订单统计表
    private void parseJson1(String response) {
        try {
            Statement_xsddBean parseObject = JSON.parseObject(response, Statement_xsddBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<Statement_xsddBean.Xsdd> rows = parseObject.getRows();
                List<FooterBean> footer = parseObject.getFooter();
                if (getV() != null) {
                    getV().showData(rows, footer);
                }
            } else {
                ToastUtils.showCustomToast(parseObject.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showCustomToast(e.getMessage());
        }
    }


}
