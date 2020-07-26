package com.qwb.view.object.parsent;

import android.app.Activity;
import com.alibaba.fastjson.JSON;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.member.model.MemberListBean;
import com.qwb.view.object.ui.ObjectMemberFragment;
import com.qwb.utils.MyHttpUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 对象员工
 */
public class PObjectMember extends XPresent<ObjectMemberFragment>{

    /**
     */
    public void queryCompanyMemberList(Activity activity, String search) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("memberNm", search);
        MyHttpUtil.getInstance()
                .post(activity, params, Constans.queryCompanyMemberList)
                .setResultListener(new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson1(response);
                    }
                });
    }

    //解析数据
    private void parseJson1(String response) {
        try {
            MemberListBean bean = JSON.parseObject(response, MemberListBean.class);
            if (bean != null && bean.isState()) {
                List<MemberListBean.MemberBean> dataList = bean.getList();
                getV().refreshAdapter(dataList);
            } else {
                ToastUtils.showCustomToast(bean.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

}
