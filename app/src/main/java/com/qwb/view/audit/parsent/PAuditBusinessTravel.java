package com.qwb.view.audit.parsent;


import android.app.Activity;
import android.graphics.Bitmap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nanchen.compresshelper.CompressHelper;
import com.qwb.view.member.model.MemberListBean;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.file.model.FileBean;
import com.qwb.view.audit.ui.AuditBusinessTravelActivity;
import com.qwb.view.audit.ui.AddCurrentIds;
import com.qwb.view.audit.ui.AuditDetailActivity;
import com.qwb.view.base.model.TreeBean;
import com.qwb.utils.MyHttpUtil;
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
 * 审批:出差
 */
public class PAuditBusinessTravel extends XPresent<AuditBusinessTravelActivity> {

    private Activity activity;
    /**
     * 提交数据:这里审批人是上面传的（固定的），不是手动再添加
     */
    public void addData(Activity activity, String title, String type,String dsc, String auditData,String mids,String startTime, String endTime, ArrayList<FileBean> fileList) {
        this.activity = activity;
        // auditTp(必传1 请假 2 报销 3 出差 4 物品领用5 通用审批'
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("dsc", dsc);
        params.put("auditData", auditData);
        params.put("auditTp", "3");
        params.put("memIds", mids);
        params.put("tp", type);
        params.put("title", title);
//        params.put("checkNm",checkNm);
        params.put("stime", startTime);
        params.put("etime", endTime);

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
        TreeMap<String, File> files = new TreeMap<>(new Comparator<String>(){
            @Override
            public int compare(String o1, String o2) {return o1.compareTo(o2);//将o1和o2调换位置是倒序排序
            }
        });
        int size = Constans.publish_pics.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                File oldFile=new File(Constans.publish_pics.get(i));
                File newFile = new CompressHelper.Builder(activity)
                        .setMaxWidth(1080)  // 默认最大宽度为720
                        .setMaxHeight(1920) // 默认最大高度为960
                        .setQuality(100)    // 默认压缩质量为80
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
                    AddCurrentIds.getI().saveToDB(AddCurrentIds.TYPE_CC);
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
        }catch (Exception e){
            ToastUtils.showError(e);
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
                    getV().showDialogMember(mDatas,dataList);
                }
            } else {
                ToastUtils.showCustomToast(bean.getMsg());
            }

        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }


}
