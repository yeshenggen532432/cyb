package com.qwb.view.call.parsent;


import android.app.Activity;
import android.database.Cursor;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.qwb.db.DWareBean;
import com.qwb.db.DWareTypeBean;
import com.qwb.utils.Constans;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.MyHttpUtil;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.MyRequestUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.BaseBean;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.cache.ui.CacheActivity;
import com.qwb.view.call.model.CallCommentBean;
import com.qwb.view.call.model.CallReplyBean;
import com.qwb.view.call.model.CommentItemBean;
import com.qwb.view.call.model.QueryCallon;
import com.qwb.view.call.model.QueryCallonBean2;
import com.qwb.view.call.ui.CallPageActivity;
import com.qwb.view.customer.model.MineClientBean;
import com.qwb.view.customer.model.MineClientInfo;
import com.qwb.view.member.model.BranchBean;
import com.qwb.view.member.model.BranchListBean2;
import com.qwb.view.member.model.MemberBean;
import com.qwb.view.step.model.ShopInfoBean;
import com.qwb.view.ware.model.QueryStkWareType;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 我的拜访
 */
public class PCallPage extends XPresent<CallPageActivity>{

    private String dataTp = MyLoginUtil.getDataTp(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.BFCX_NEW, ConstantUtils.Apply.BFCX_OLD);
    private String dataTpMids = MyLoginUtil.getDataTpMids(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.BFCX_NEW, ConstantUtils.Apply.BFCX_OLD);

    // 初始化数据
    public void queryData(Activity activity, int pageNo, int pageSize, String search, String startDate, String endDate, String customerId, String bfId, String mIds) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("khNm", search);
        params.put("sdate", startDate);
        params.put("edate", endDate);
        params.put("dataTp", dataTp);
        params.put("cid", customerId);
        params.put("id", bfId);// 拜访id
        if (MyStringUtil.isNotEmpty(mIds)) {
            params.put("mids", mIds);// 角色
        }else{
            if (MyStringUtil.eq("4", dataTp)) {
                params.put("mids", dataTpMids);// 角色
            }
        }
        OkHttpUtils.post().params(params).url(Constans.queryBfkhLsWeb2).id(1).build().execute(new MyHttpCallback(activity) {
            @Override
            public void myOnError(Call call, Exception e, int id) {

            }

            @Override
            public void myOnResponse(String response, int id) {
                parseJson1(response);
            }
        });
    }

    // 初始化数据--消息评论
    public void queryComment(Activity activity, String bfId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("bfId", String.valueOf(bfId));
        OkHttpUtils.post().params(params).url(Constans.queryBfkhLsWebOne).id(7).build().execute(new MyHttpCallback(activity) {
            @Override
            public void myOnError(Call call, Exception e, int id) {

            }

            @Override
            public void myOnResponse(String response, int id) {
                parseJson7(response);
            }
        });
    }


    public void addComment(Activity activity, String bfId, String content, int voiceTime, File voiceFile){
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("bfId", String.valueOf(bfId));// 拜访id
        params.put("content", content);
        // 添加语音
        Map<String, java.io.File> files = new HashMap<>();
        if (voiceTime > 0 && voiceFile != null) {
            files.put("voice", voiceFile);// 语音文件
            params.put("voiceTime", String.valueOf((int) voiceTime));
        }
        OkHttpUtils.post().files(files).params(params).url(Constans.addBfcomment).id(4).build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson4(response);
                    }
                });
    }


    public void addReply(Activity activity, String bfId, String content, int commentId, String replyMemberName, String replyMemberId, int voiceTime, File voiceFile){
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("content", content);
        params.put("bfId", String.valueOf(bfId));// 拜访id
        params.put("belongId", String.valueOf(commentId));
        params.put("rcNm", replyMemberName);
        params.put("rcId", String.valueOf(replyMemberId));
        // 添加语音
        Map<String, File> files = new HashMap<>();
        if (voiceTime > 0 && voiceFile != null) {
            files.put("voice", voiceFile);
            params.put("voiceTime", String.valueOf((int) voiceTime));
        }
        OkHttpUtils.post().files(files).params(params).url(Constans.addBfcomment).id(4).build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson4(response);
                    }
                });
    }



    /**
     * 获取结构图：部门，员工
     */
    public void queryMember(Activity activity) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("dataTp", dataTp);// 角色
        if ("4".equals(dataTp)) {
            params.put("mids", dataTpMids);// 角色
        }
        MyHttpUtil.getInstance()
                .post(activity, params, Constans.queryDepartMemLs)
                .setResultListener(new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        closeRefresh();
                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson2(response);
                    }
                });
    }

    /**
     * 上传轨迹数据--目前定位完马上上传
     */
    public void queryDb() {
//        callonList.clear();
        Cursor cursor = MyUtils.getDB().query("bfcx",null,  "userId=?", new String[]{SPUtils.getID()}, null, null, null);
        if(cursor!=null){
            boolean first = cursor.moveToFirst();
            while (first) {
                String bcbfzj = cursor.getString(cursor.getColumnIndex("bcbfzj"));
                String voiceUrl = cursor.getString(cursor.getColumnIndex("voiceUrl"));
                String voiceTime = cursor.getString(cursor.getColumnIndex("voiceTime"));
                String memberNm = cursor.getString(cursor.getColumnIndex("memberNm"));
                String khdjNm = cursor.getString(cursor.getColumnIndex("khdjNm"));
                String qddate = cursor.getString(cursor.getColumnIndex("qddate"));
                String qdtime = cursor.getString(cursor.getColumnIndex("qdtime"));
                String khNm = cursor.getString(cursor.getColumnIndex("khNm"));
                String memberHead = cursor.getString(cursor.getColumnIndex("memberHead"));
                String branchName = cursor.getString(cursor.getColumnIndex("branchName"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String longitude = cursor.getString(cursor.getColumnIndex("longitude"));
                String latitude = cursor.getString(cursor.getColumnIndex("latitude"));
                String longitude2 = cursor.getString(cursor.getColumnIndex("longitude2"));
                String latitude2 = cursor.getString(cursor.getColumnIndex("latitude2"));
                String longitude3 = cursor.getString(cursor.getColumnIndex("longitude3"));
                String latitude3 = cursor.getString(cursor.getColumnIndex("latitude3"));
                int zfcount = cursor.getInt(cursor.getColumnIndex("zfcount"));
                int jlm = cursor.getInt(cursor.getColumnIndex("jlm"));
                int mid = cursor.getInt(cursor.getColumnIndex("mid"));
                int cid = cursor.getInt(cursor.getColumnIndex("cid"));
                int bfid = cursor.getInt(cursor.getColumnIndex("bfid"));

                QueryCallon queryCallon = new QueryCallon();
                queryCallon.setBcbfzj(bcbfzj);
                queryCallon.setVoiceUrl(voiceUrl);
                queryCallon.setVoiceTime(voiceTime);
                queryCallon.setMemberNm(memberNm);
                queryCallon.setKhdjNm(khdjNm);;
                queryCallon.setQdtime(qdtime);
                queryCallon.setQddate(qddate);
                queryCallon.setKhNm(khNm);
                queryCallon.setMemberHead(memberHead);
                queryCallon.setBranchName(branchName);
                queryCallon.setAddress(address);
                queryCallon.setLongitude(longitude);
                queryCallon.setLatitude(latitude);
                queryCallon.setLongitude2(longitude2);
                queryCallon.setLatitude2(latitude2);
                queryCallon.setLongitude3(longitude3);
                queryCallon.setLatitude3(latitude3);
                queryCallon.setZfcount(zfcount);
                queryCallon.setJlm(jlm);
                queryCallon.setMid(mid);
                queryCallon.setCid(cid);
                queryCallon.setId(bfid);
                List<CommentItemBean> commentList=new ArrayList<>();
                List<QueryCallon.Pic> listpic = new ArrayList<>();
                //评论
                Cursor cursor_pl = MyUtils.getDB().query("comment",null,  "userId=? and bfid=?", new String[]{SPUtils.getID(),""+bfid}, null, null, null);
                if(cursor_pl!=null){
                    boolean first_pl = cursor_pl.moveToFirst();
                    while (first_pl) {
                        String content = cursor_pl.getString(cursor_pl.getColumnIndex("content"));
                        String memberNm_pl = cursor_pl.getString(cursor_pl.getColumnIndex("memberNm"));
                        String rcNm = cursor_pl.getString(cursor_pl.getColumnIndex("rcNm"));
                        int voiceTime_pl = cursor_pl.getInt(cursor_pl.getColumnIndex("voiceTime"));
                        int commentId = cursor_pl.getInt(cursor_pl.getColumnIndex("commentId"));
                        int memberId = cursor_pl.getInt(cursor_pl.getColumnIndex("memberId"));
                        int belongId = cursor_pl.getInt(cursor_pl.getColumnIndex("belongId"));
                        int position = cursor_pl.getInt(cursor_pl.getColumnIndex("position"));

                        List<CallReplyBean> rcList=new ArrayList<>();
                        CommentItemBean commentItemBean = new CommentItemBean();
                        //回复
                        Cursor cursor_rc = MyUtils.getDB().query("rc",null,  "userId=? and plId=?", new String[]{SPUtils.getID(),""+commentId}, null, null, null);
                        if(cursor_rc!=null){
                            boolean first_rc = cursor_rc.moveToFirst();
                            while (first_rc) {
                                String content_rc = cursor_rc.getString(cursor_rc.getColumnIndex("content"));
                                String memberNm_rc = cursor_rc.getString(cursor_rc.getColumnIndex("memberNm"));
                                String rcNm_rc = cursor_rc.getString(cursor_rc.getColumnIndex("rcNm"));
                                int voiceTime_rc = cursor_rc.getInt(cursor_rc.getColumnIndex("voiceTime"));
                                int commentId_rc = cursor_rc.getInt(cursor_rc.getColumnIndex("commentId"));
                                int memberId_rc = cursor_rc.getInt(cursor_rc.getColumnIndex("memberId"));
                                int belongId_rc = cursor_rc.getInt(cursor_rc.getColumnIndex("belongId"));
                                int position_rc = cursor_rc.getInt(cursor_rc.getColumnIndex("rcPosition"));

                                CallReplyBean RcBean = new CallReplyBean();
                                RcBean.setContent(content_rc);
                                RcBean.setMemberNm(memberNm_rc);;
                                RcBean.setRcNm(rcNm_rc);
                                RcBean.setVoiceTime(voiceTime_rc);
                                RcBean.setCommentId(commentId_rc);
                                RcBean.setMemberId(memberId_rc);
                                RcBean.setBelongId(belongId_rc);
                                RcBean.setRcPosition(position_rc);
                                rcList.add(RcBean);
                                first_rc=cursor_rc.moveToNext();
                            }
                            cursor_rc.close();
                        }

                        commentItemBean.setContent(content);
                        commentItemBean.setMemberNm(memberNm_pl);;
                        commentItemBean.setRcNm(rcNm);
                        commentItemBean.setVoiceTime(voiceTime_pl);
                        commentItemBean.setCommentId(commentId);
                        commentItemBean.setMemberId(memberId);
                        commentItemBean.setBelongId(belongId);
                        commentItemBean.setPosition(position);
                        commentItemBean.setRcList(rcList);
                        commentList.add(commentItemBean);
                        first_pl=cursor_pl.moveToNext();
                    }
                    cursor_pl.close();
                }

                //图片
                Cursor cursor_pic = MyUtils.getDB().query("pic",null,  "userId=? and bfid=?", new String[]{SPUtils.getID(),""+bfid}, null, null, null);
                if(cursor_pic!=null){
                    boolean first_pic = cursor_pic.moveToFirst();
                    while (first_pic) {
                        String pic = cursor_pic.getString(cursor_pic.getColumnIndex("pic"));
                        String picMin = cursor_pic.getString(cursor_pic.getColumnIndex("picMin"));
                        String nm = cursor_pic.getString(cursor_pic.getColumnIndex("nm"));

                        QueryCallon.Pic pic2 = new QueryCallon.Pic();
                        pic2.setPic(pic);
                        pic2.setPicMin(picMin);
                        pic2.setNm(nm);
                        listpic.add(pic2);
                        first_pic=cursor_pic.moveToNext();
                    }
                    cursor_pic.close();
                }

                queryCallon.setCommentList(commentList);
                queryCallon.setListpic(listpic);
//                callonList.add(queryCallon);
                first=cursor.moveToNext();
            }
            cursor.close();
//            refreshAdapter2();
        }
    }


    //TODO ------------------------接口回調----------------------

    //解析数据-商品类型
    private void parseJson1(String response) {
        try {
            QueryCallonBean2 bean = JSON.parseObject(response, QueryCallonBean2.class);
            if (MyRequestUtil.isSuccess(bean)) {
                getV().refreshAdapter(bean);
            }else{
                ToastUtils.showCustomToast(bean.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    private void parseJson7(String response) {
        try {
//            CallCommentBean bean = JSON.parseObject(response, CallCommentBean.class);
//            if (MyRequestUtil.isSuccess(bean)) {
//                QueryCallon data = bean.getXx();
//                if (data != null) {
//                    callonList.add(row);
//                    refreshAdapter2();
//                    tv_headTitle.setText("拜访次数：" + callonList.size() + "  拜访家数：" + callonList.size());
//                }
//            } else {
//                ToastUtils.showCustomToast(bean.getMsg());
//            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //解析数据-结构图：部门，员工
    private void parseJson2(String response) {
        try {
            List<TreeBean> mDatas = new ArrayList<TreeBean>();
            mDatas.clear();

            BranchListBean2 parseObject = JSON.parseObject(response, BranchListBean2.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    List<BranchBean> list = parseObject.getList();
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            // 第一层
                            BranchBean branchBean = list.get(i);
                            Integer branchId = branchBean.getBranchId();
                            String branchName = branchBean.getBranchName();
                            if (branchId != null && branchName != null) {
                                TreeBean fileBean = new TreeBean(branchId, -1, branchName);
                                if (fileBean != null) {
                                    mDatas.add(fileBean);
                                }
                                Constans.branchMap.put(branchId, branchBean);// 父

                                // 第二层
                                List<MemberBean> memls2 = branchBean.getMemls2();
                                if (memls2 != null && memls2.size() > 0) {
                                    for (int j = 0; j < memls2.size(); j++) {
                                        MemberBean memberBean = memls2.get(j);
                                        Integer memberId = memberBean.getMemberId() + ConstantUtils.TREE_ID;// 默认+10万--防止父ID与子ID重复
                                        String memberNm = memberBean.getMemberNm();
                                        if (memberId != null && memberNm != null) {
                                            mDatas.add(new TreeBean(memberId, branchId, memberNm));
                                            Constans.memberMap.put(memberId, memberBean);// 子
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if(getV()!=null){
                        getV().showDialogMember(mDatas);
                    }
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    private void parseJson4(String response) {
        try {
            BaseBean bean = JSON.parseObject(response, BaseBean.class);
            if (MyRequestUtil.isSuccess(bean)) {
            } else {
            }
            ToastUtils.showCustomToast(bean.getMsg());
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }




}
