package com.qwb.view.table.parsent;


import android.app.Activity;
import com.alibaba.fastjson.JSON;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.table.ui.TableActivity2;
import com.qwb.utils.Constans;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.customer.model.ClientLevelBean;
import com.qwb.view.table.model.Statement_khbfBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.droidlover.xdroidmvp.log.XLog;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：客户拜访统计表
 * 创建作者：yeshenggen
 * 创建时间：2018/05/24
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PTable2 extends XPresent<TableActivity2>{
    private String dataTp = MyLoginUtil.getDataTp(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.TJBB_NEW, ConstantUtils.Apply.BBTJ_OLD);
    private String dataTpMids = MyLoginUtil.getDataTpMids(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.TJBB_NEW, ConstantUtils.Apply.BBTJ_OLD);

    /**
     * 获取：客户拜访统计表
     */
    public void queryData(Activity activity, int pageNo,int pageSize, String sDate, String eDate, String search, String clientLevel, String bfCount) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("stime", sDate);//开始日期
        params.put("etime", eDate);//默认当前日期）
        params.put("memberNm", search);//业务员
        params.put("khdjNm", clientLevel);//客户等级
        params.put("bfpl", bfCount);//拜访频率
        params.put("dataTp", dataTp);//客户id(拜访步骤进入)
        if("4".equals(dataTp)){
            params.put("mids", dataTpMids);//角色
        }
        XLog.e("pageNo",String.valueOf(pageNo));
        XLog.e("stime",sDate);
        XLog.e("etime",eDate);
        XLog.e("memberNm",search);
        XLog.e("khdjNm",clientLevel);
        XLog.e("bfpl",bfCount);
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryKhbftjWeb)
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
     * 获取：客户等级
     */
    public void queryDataClientLevel(Activity context) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("qdId", "");
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.Khlevells)
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


    //解析数据-渠道类型;客户类型
    private void parseJson1(String response) {
        try {
            Statement_khbfBean parseObject = JSON.parseObject(response,Statement_khbfBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<Statement_khbfBean.KhBf> rows = parseObject.getRows();
                if(getV()!=null){
                    getV().showData(rows);
                }
            } else {
                ToastUtils.showCustomToast(parseObject.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }
    //解析数据-客户等级
    private void parseJson2(String response) {
        try {
            List<String> clientLevelList=new ArrayList<>();
            ClientLevelBean parseObject = JSON.parseObject(response, ClientLevelBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<ClientLevelBean.ClientLevel> khlevells = parseObject.getKhlevells();
                clientLevelList.clear();
                clientLevelList.add("全部");
                for (int i = 0; i < khlevells.size(); i++) {
                    ClientLevelBean.ClientLevel clientLevel = khlevells.get(i);
                    String khdjNm = clientLevel.getKhdjNm();
                    clientLevelList.add(khdjNm);
                }
                if(getV()!=null){
                    getV().showDialogCustomerLevel(clientLevelList);
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }


}
