package com.qwb.view.audit.parsent;


import android.app.Activity;
import android.graphics.Bitmap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nanchen.compresshelper.CompressHelper;
import com.qwb.event.ObjectEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.common.model.TokenBean;
import com.qwb.view.member.model.MemberListBean;
import com.qwb.view.audit.model.AccountListBean;
import com.qwb.view.audit.model.AuditModelResult;
import com.qwb.view.audit.model.AuditZdyListResult;
import com.qwb.view.audit.model.AuditZdyResult;
import com.qwb.view.file.model.FileBean;
import com.qwb.view.audit.ui.AuditModelActivity;
import com.qwb.view.audit.ui.AuditDetailActivity;
import com.qwb.utils.MyHttpUtil;
import com.qwb.utils.MyStringUtil;
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
import cn.droidlover.xdroidmvp.router.Router;
import okhttp3.Call;

/**
 * 自定义审批模板
 */
public class PAuditModel extends XPresent<AuditModelActivity> {

    private Activity activity;

    /**
     * 提交数据:这里审批人是上面传的（固定的），不是手动再添加
     */
    public void addData(Activity activity, String title, String type, String startTime, String endTime, String detail, String remo, String amount, String mids,
                        String zdyNm, Integer modelId, String isSy, ArrayList<FileBean> fileList, String execIds, Integer approverId,
                        ObjectEvent objectEvent, AccountListBean.AccountBean accountBean, String queryToken, String zdyId) {
        this.activity = activity;
        // auditTp(必传1 请假 2 报销 3 出差 4 物品领用5 通用审批' 6:私用 7:公用
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("dsc", detail);
        params.put("auditData", remo);
        params.put("amount", amount);
        params.put("memIds", mids);
        params.put("execIds", execIds);//TODO 前后接口逗号
        params.put("title", title);
        params.put("tp", type);// 类型
        params.put("zdyNm", zdyNm);
        if (modelId != null && 0 != modelId) {
            params.put("modelId", String.valueOf(modelId));//模板id
        }
        if (approverId !=null && 0 != approverId) {
            params.put("approverId", "" + approverId);// 最终审批人
        }
//        // 6:私用 -7:公用
//        if ("1".equals(isSy)) {
//            params.put("auditTp", "6");// 6:私用
//        } else if ("2".equals(isSy)) {
//            params.put("auditTp", "7");// 7:公用
//        }

        params.put("auditTp", "6");// 自定义审批
        // 时间
        if (startTime.contains("必填")) {
            params.put("stime", "");
        } else {
            params.put("stime", startTime);
        }
        if (endTime.contains("必填")) {
            params.put("etime", "");
        } else {
            params.put("etime", endTime);
        }
        if(objectEvent != null){
            params.put("objectType", "" + objectEvent.getType());
            params.put("objectId", "" + objectEvent.getId());
            params.put("objectName", "" + objectEvent.getName());
        }
        if (accountBean != null){
            params.put("accountId", "" + accountBean.getId());
        }
        if (!MyStringUtil.isEmpty(queryToken)) {
            params.put("page_token", queryToken);
        }
        if (!MyStringUtil.isEmpty(zdyId)) {
            params.put("zdyId", zdyId);
        }

        //附件
        if (fileList != null && fileList.size() > 0) {
            StringBuffer sb = new StringBuffer();
            if (fileList.size() == 1) {
                sb.append(fileList.get(0).getFileNm());
            } else {
                for (int i = 0; i < fileList.size(); i++) {
                    FileBean fileBean = fileList.get(i);
                    String fileNm = fileBean.getFileNm();
                    if (i == fileList.size() - 1) {
                        sb.append(fileNm);
                    } else {
                        sb.append(fileNm + ",");
                    }
                }
            }
            params.put("fileNms", sb.toString().trim());
        }
        // 图片数组(map按key升序)
        TreeMap<String, File> files = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);//将o1和o2调换位置是倒序排序
            }
        });
        int size = Constans.publish_pics.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                File oldFile = new File(Constans.publish_pics.get(i));
                File newFile = new CompressHelper.Builder(activity)
                        .setMaxWidth(1080)  // 默认最大宽度为720
                        .setMaxHeight(1920) // 默认最大高度为960
                        .setQuality(100)    // 默认压缩质量为80
                        .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式i
                        .setFileName(String.valueOf(System.currentTimeMillis() + "_" + i)) // 设置你的文件名
                        .setDestinationDirectoryPath(Constans.DIR_IMAGE_PROCEDURE)
                        .build()
                        .compressToFile(oldFile);
                //TODO 文件拼接“_i”,取出“_i”为key,最后key升序
                String absolutePath = newFile.getAbsolutePath();
                String j = absolutePath.substring(absolutePath.length() - 6, absolutePath.length() - 5);
                files.put("file" + j, newFile);
            }
        }
        OkHttpUtils.post().files(files).params(params).url(Constans.addAuditURL).id(1).build()
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
     * 查询审批模板
     */
    public void queryAuditModelById(Activity activity, Integer id) {
        this.activity = activity;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("id", String.valueOf(id));

        OkHttpUtils.post()
                .params(params)
                .url(Constans.queryAuditModelById)
                .id(2)
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
     * 查询账户列表
     */
    public void queryAccountList(Activity activity) {
        this.activity = activity;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());

        OkHttpUtils.post()
                .params(params)
                .url(Constans.queryAccountList)
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

    /**
     * 根据modelId查询审批流列表
     */
    public void queryAuditZdyListByModelId(Activity activity, Integer modelId) {
        this.activity = activity;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("modelId", String.valueOf(modelId));

        OkHttpUtils.post()
                .params(params)
                .url(Constans.queryAuditZdyListByModelId)
                .id(3)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        try {
                            AuditZdyListResult bean = JSON.parseObject(response, AuditZdyListResult.class);
                            if (bean != null) {
                                if (bean.isState()) {
                                    getV().showDialogAuditZdy(bean.getList());                                }
                            }
                        }catch (Exception e){
                            ToastUtils.showCustomToast(e.getMessage());
                        }
                    }
                });
    }

    /**
     * 获取token
     */
    public void queryToken(Activity activity) {
        OkHttpUtils
                .get()
                .addParams("token", SPUtils.getTK())
                .url(Constans.queryToken)
                .id(28)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson28(response);
                    }
                });
    }
    /**
     * 根据审批流id查询审批流
     */
    public void queryAuditZdyById(Activity activity,Integer id) {
        OkHttpUtils
                .get()
                .addParams("token", SPUtils.getTK())
                .addParams("id", String.valueOf(id))
                .url(Constans.queryAuditZdyById)
                .id(29)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson29(response);
                    }
                });
    }

    /**
     * 查询部门，员工
     */
    public void queryDepartMember(Activity activity) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("dataTp", "1");//1:全部
        OkHttpUtils.post()
                .params(params)
                .url(Constans.queryDepartMemLs)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson30(response);
                    }
                });
    }

    /**
     * 查询部门，员工
     */
    public void queryMemberList(Activity activity) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("memberNm", "");
        MyHttpUtil.getInstance()
                .post(activity, params, Constans.queryCompanyMemberList)
                .setResultListener(new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson30(response);
                    }
                });
    }



    //解析数据
    private void parseJson1(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            if (object != null) {
                boolean state = object.getBoolean("state");
                if (state) {
                    ToastUtils.showCustomToast("提交成功");
                    String auditNo = object.getString("auditNo");
                    Router.newIntent(activity)
                            .to(AuditDetailActivity.class)
                            .putInt(ConstantUtils.Intent.NEED_CHECK, 2)
                            .putString(ConstantUtils.Intent.ID, auditNo)
                            .launch();
                    ActivityManager.getInstance().closeActivity(activity);
                } else {
                    ToastUtils.showCustomToast(object.getString("msg"));
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //解析数据
    private void parseJson2(String response) {
        try {
            AuditModelResult bean = JSON.parseObject(response, AuditModelResult.class);
            if (bean != null) {
                if (bean.isState()) {
//                    getV().doAuditModel(bean.getData());
                } else {
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //解析数据
    private void parseJson3(String response) {
        try {
            AccountListBean bean = JSON.parseObject(response, AccountListBean.class);
            if (bean != null) {
                if (bean.isState()) {
                    getV().showDialogAccount(bean.getList());
                } else {
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }




    //解析数据--获取token
    private void parseJson28(String response) {
        try {
            TokenBean bean = JSON.parseObject(response, TokenBean.class);
            if (bean != null) {
                if (200 == bean.getCode()) {
                    getV().doToken(bean.getData());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //自定义审批
    private void parseJson29(String response) {
        try {
            AuditZdyResult bean = JSON.parseObject(response, AuditZdyResult.class);
            if (bean != null) {
                if (200 == bean.getCode()) {
//                    getV().doUI(bean.getData());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }


    //解析数据-结构图：部门，员工
    private void parseJson30(String response) {
        try {
            List<TreeBean> mDatas = new ArrayList<>();
            mDatas.clear();
            MemberListBean bean = JSON.parseObject(response, MemberListBean.class);
            if (bean != null && bean.isState()) {
                List<MemberListBean.MemberBean> dataList = bean.getList();
                for (MemberListBean.MemberBean memberBean:dataList) {
                    Integer memberId = memberBean.getMemberId();// 默认+10万--防止父ID与子ID重复
                    String memberNm = memberBean.getMemberNm();
                    if (memberId != null && memberNm != null) {
                        mDatas.add(new TreeBean(memberId, 0, memberNm));
                    }
                }
                if(getV()!=null){
                    getV().refreshAdapterMemberTree(mDatas,dataList);
                }
            } else {
                ToastUtils.showCustomToast(bean.getMsg());
            }

        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }




}
