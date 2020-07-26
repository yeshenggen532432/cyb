package com.qwb.view.table.parsent;


import android.app.Activity;
import com.alibaba.fastjson.JSON;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.table.ui.TableActivity1;
import com.qwb.utils.Constans;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.customer.model.ClientLevelBean;
import com.qwb.view.table.model.MemberCallonBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 业务拜访统计表
 */
public class PTable1 extends XPresent<TableActivity1>{
    private String dataTp = MyLoginUtil.getDataTp(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.TJBB_NEW, ConstantUtils.Apply.BBTJ_OLD);
    private String dataTpMids = MyLoginUtil.getDataTpMids(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.TJBB_NEW, ConstantUtils.Apply.BBTJ_OLD);

    /**
     * 获取：业务拜访统计表
     */
    public void queryData(Activity activity, int pageNo,int pageSize, String date, String khNm,String memberName, String clientLevel) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("qddate", date);
        params.put("khNm", khNm);
        params.put("memberNm", memberName);
        params.put("khdjNm", clientLevel);
        params.put("dataTp", dataTp);
        if("4".equals(dataTp)){
            params.put("mids", dataTpMids);//角色
        }
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryYwbfzxWeb)
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
            MemberCallonBean parseObject = JSON.parseObject(response,MemberCallonBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<MemberCallonBean.YwCanllon> rows = parseObject.getRows();
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
