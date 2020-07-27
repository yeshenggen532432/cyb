package com.qwb.view.call.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.qwb.utils.Constans;
import com.qwb.utils.JsonHttpUtil;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.utils.MyGlideUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.ToastUtils;
import com.qwb.view.call.model.CallReplyBean;
import com.qwb.view.call.model.CommentItemBean;
import com.qwb.view.call.model.QueryCallon;
import com.qwb.view.location.ui.MapLocationActivity;
import com.qwb.widget.recordvoice.VoiceManager;
import com.xmsx.cnlife.widget.CircleImageView;
import com.chiyong.t3.R;
import com.zhy.http.okhttp.utils.MyUrlUtil;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import java.util.ArrayList;
import java.util.List;

/**
 * 整理单列表
 */
public class CallPageAdapter extends BaseQuickAdapter<QueryCallon, BaseViewHolder> {

    public static final int COMMENT = 1;
    public static final int REPLY = 2;
    public static final int REPLY_AGAIN = 3;

    public QueryCallon mCurrentItem;
    public int mCurrentPosition;

    private Context context;
    private boolean isShowRepeat = true;
    public CallPageAdapter(Context context, boolean isShowRepeat) {
        super(R.layout.x_adapter_call_page);
        this.context = context;
        this.isShowRepeat = isShowRepeat;
        this.voiceManager = VoiceManager.getInstance(context);
        initPopup();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final QueryCallon item) {
        helper.addOnClickListener(R.id.parent);
        helper.addOnClickListener(R.id.tv_zfcount);

        View parent = helper.getView( R.id.parent);
        CircleImageView iv_memberHead = helper.getView( R.id.iv_memberHead);
        TextView tv_memberNm = helper.getView(R.id.tv_memberNm);
        TextView tv_branchName = helper.getView( R.id.tv_branchName);
        TextView tv_khNm = helper.getView( R.id.tv_khNm);
        TextView tv_bcbfzj = helper.getView( R.id.tv_bcbfzj);// 拜访总结
        TextView tv_time_duan = helper.getView( R.id.tv_time_duan);// 时段
        TextView tv_time_chang = helper.getView( R.id.tv_time_chang);// 时长
        TextView tv_address = helper.getView( R.id.tv_address);// 地址
        LinearLayout ll_address = helper.getView( R.id.ll_address);// 地址
        TextView tv_jl = helper.getView( R.id.tv_jl);// 距离
        TextView tv_zfcount = helper.getView( R.id.tv_zfcount);// 客户重复

        // 评论
        final View iv_zang_pinglun = helper.getView( R.id.iv_zang_pinglun);// 评论
        final View rl_zang_pinglun = helper.getView( R.id.rl_zang_pinglun);
        final TextView tv_zang_num = helper.getView( R.id.tv_zang_num);// 点赞
        final View tv_zang_line = helper.getView( R.id.tv_zang_line);

        if (item != null) {
            tv_zang_num.setVisibility(View.GONE);// 点赞
            tv_zang_line.setVisibility(View.GONE);// 点赞
            MyGlideUtil.getInstance().displayImageSquere(Constans.IMGROOTHOST + item.getMemberHead(), iv_memberHead);
            tv_memberNm.setText(item.getMemberNm());
            tv_branchName.setText("(" + item.getBranchName() + ")");
            tv_khNm.setText(item.getKhNm());
            tv_bcbfzj.setText(item.getBcbfzj());
            tv_time_duan.setText(item.getQddate());
            tv_time_chang.setText(item.getQdtime());
            tv_address.setText(item.getAddress());
            tv_jl.setText(item.getJlm() + "m");

            // 客户重复
            int zfcount = item.getZfcount();
            if (isShowRepeat){
                if (zfcount < 2) {
                    tv_zfcount.setVisibility(View.GONE);
                } else {
                    tv_zfcount.setVisibility(View.VISIBLE);
                    tv_zfcount.setText("客户重复" + zfcount);
                }
            }else{
                tv_zfcount.setVisibility(View.GONE);
            }


            //-----------------------------------图片：start-----------------------------------------
            //九图---配置图片显示在Application
            NineGridView nineGrid = helper.getView(R.id.nineGrid);
            final List<QueryCallon.Pic> images = item.getListpic();
            if (null != images && images.size() > 0) {
                ArrayList<ImageInfo> imageInfo = new ArrayList<>();
                if (images != null) {
                    for (QueryCallon.Pic image : images) {
                        ImageInfo info = new ImageInfo();
                        info.setThumbnailUrl(MyUrlUtil.getUrl(Constans.IMGROOTHOST + image.getPic()));
                        info.setBigImageUrl(MyUrlUtil.getUrl(Constans.IMGROOTHOST + image.getPic()));
                        imageInfo.add(info);
                    }
                }
                nineGrid.setAdapter(new NineGridViewClickAdapter(mContext, imageInfo));
                nineGrid.setVisibility(View.VISIBLE);
            } else {
                nineGrid.setVisibility(View.GONE);
            }
            //-----------------------------------图片：end-----------------------------------------

            //-------------------------语音:start----------------------------------
            TextView tvVoice = helper.getView(R.id.item_tv_voice);//
            View layoutVoice = helper.getView(R.id.item_layout_voice);//
            final ImageView ivVoice = helper.getView(R.id.item_iv_voice);//
            if (MyStringUtil.isEmpty(item.getVoiceUrl())) {
                layoutVoice.setVisibility(View.GONE);
            } else {
                layoutVoice.setVisibility(View.VISIBLE);
                tvVoice.setText(item.getVoiceTime() + "s''");
            }
            layoutVoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downloadFileAndPlayer(item.getVoiceUrl(), "拜访"+ item.getId(), ivVoice);
                }
            });
            //-------------------------语音:end----------------------------------

            // 评论列表
            List<CommentItemBean> commentList = item.getCommentList();
            if (MyCollectionUtil.isEmpty(commentList)) {
                rl_zang_pinglun.setVisibility(View.GONE);
            } else {
                rl_zang_pinglun.setVisibility(View.VISIBLE);
            }

            //-------------------------回复----------------------------------
            RecyclerView recyclerView = helper.getView( R.id.recyclerView);
            if (MyCollectionUtil.isEmpty(commentList)) {
                recyclerView.setVisibility(View.GONE);
            }else{
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                CallPageCommentAdapter adapter = new CallPageCommentAdapter(item, helper.getAdapterPosition());
                recyclerView.setAdapter(adapter);
                adapter.setNewData(commentList);
            }

            //评论
            iv_zang_pinglun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopup(v);
                    mCurrentItem = item;
                    mCurrentPosition = helper.getAdapterPosition();
                }
            });
            //地址
            ll_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MapLocationActivity.class);
                    intent.putExtra("CallQueryActivity", item);
                    intent.putExtra(Constans.type, 4);
                    context.startActivity(intent);
                }
            });
        }
    }

    /**
     * 下载音频文件并播放
     */
    private AnimationDrawable voiceAnimation;
    private VoiceManager voiceManager;
    private String mId = "-1";//记录哪个动画（唯一区分）
    private void downloadFileAndPlayer(final String voiceUrl, String id, final ImageView ivVoice) {
        try {
            if (null != voiceAnimation) {
                voiceAnimation.stop();
                voiceAnimation.selectDrawable(0);
            }
            if (voiceManager.isPlaying() && MyStringUtil.eq(mId, id)) {
                voiceManager.stopPlay();
            } else {
                voiceManager.stopPlay();
                voiceManager.setVoicePlayListener(new VoiceManager.VoicePlayCallBack() {
                    @Override
                    public void voiceTotalLength(long time, String strTime) {
                    }

                    @Override
                    public void playDoing(long time, String strTime) {
                    }

                    @Override
                    public void playPause() {
                    }

                    @Override
                    public void playStart() {
                    }

                    @Override
                    public void playFinish() {
                        if (voiceAnimation != null) {
                            voiceAnimation.stop();
                            voiceAnimation.selectDrawable(0);
                        }
                    }
                });
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String fileName = null;
                        if (MyStringUtil.isNotEmpty(voiceUrl)) {
                            //本地的录音文件
                            if (voiceUrl.startsWith("/storage")){
                                fileName = voiceUrl;
                            }else{
                                fileName = JsonHttpUtil.getInstance().downFile(voiceUrl);
                            }
                        }
                        if (MyStringUtil.isEmpty(fileName)) {
                            ToastUtils.showCustomToast("播放失败,可能文件路径错误");
                        } else {
                            voiceAnimation = (AnimationDrawable) ivVoice.getBackground();
                            if (voiceAnimation != null) {
                                voiceAnimation.start();
                            }
                            voiceManager.startPlay(fileName);
                        }
                    }
                }).start();
            }
            mId = id;
        } catch (Exception e) {
        }
    }


    //TODO--------------------------------评论:start--------------------------------------------------------
    public class CallPageCommentAdapter extends BaseQuickAdapter<CommentItemBean, BaseViewHolder> {
        private QueryCallon parentItem;
        private int parentPosition;
        public CallPageCommentAdapter(QueryCallon parentItem,int parentPosition ) {
            super(R.layout.x_adapter_call_page_comment);
            this.parentItem = parentItem;
            this.parentPosition = parentPosition;
        }

        @Override
        protected void convert(final BaseViewHolder helper, final CommentItemBean item) {
            TextView tvMemberName = helper.getView( R.id.item_tv_member_name);
            TextView tvContent = helper.getView( R.id.item_tv_content);

            String memberNm = item.getMemberNm();
            int voiceTime = item.getVoiceTime();
            String content = item.getContent();

            tvMemberName.setText(memberNm + ":");

            //-------------------------语音:start----------------------------------
            TextView tvVoice = helper.getView(R.id.item_tv_voice);
            View layoutVoice = helper.getView(R.id.item_layout_voice);
            final ImageView ivVoice = helper.getView(R.id.item_iv_voice);
            if (voiceTime > 0) {
                tvContent.setVisibility(View.GONE);
                layoutVoice.setVisibility(View.VISIBLE);
                tvVoice.setText(item.getVoiceTime() + "s''");
            } else {
                layoutVoice.setVisibility(View.GONE);
                if (MyStringUtil.isNotEmpty(content)){
                    tvContent.setText(content);
                    tvContent.setVisibility(View.VISIBLE);
                }else{
                    tvContent.setVisibility(View.GONE);
                }
            }
            layoutVoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downloadFileAndPlayer(item.getContent(), "评论"+ item.getCommentId(), ivVoice);
                }
            });
            //-------------------------语音:end----------------------------------

            //-------------------------回复----------------------------------
            RecyclerView recyclerView = helper.getView( R.id.recyclerView);
            List<CallReplyBean> replyList = item.getRcList();
            if (MyCollectionUtil.isEmpty(replyList)) {
                recyclerView.setVisibility(View.GONE);
            }else{
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
               CallPageReplyAdapter adapter = new CallPageReplyAdapter(parentItem, parentPosition, item);
                recyclerView.setAdapter(adapter);
                adapter.setNewData(replyList);
            }

            //回复评论
            tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener!=null){
                        onClickListener.onClickListener(parentItem, parentPosition, item, null, REPLY);
                    }
                }
            });
        }

        //TODO--------------------------------回复:start--------------------------------------------------------
        public class CallPageReplyAdapter extends BaseQuickAdapter<CallReplyBean, BaseViewHolder> {
            private QueryCallon parentItem;
            private int parentPosition;
            private CommentItemBean commentItem;
            public CallPageReplyAdapter(QueryCallon parentItem,int parentPosition, CommentItemBean commentItem) {
                super(R.layout.x_adapter_call_page_comment);
                this.parentItem = parentItem;
                this.parentPosition = parentPosition;
                this.commentItem = commentItem;
            }

            @Override
            protected void convert(BaseViewHolder helper, final CallReplyBean item) {
                TextView tvMemberName = helper.getView(R.id.item_tv_member_name);
                TextView tvContent = helper.getView(R.id.item_tv_content);

                String memberNm = item.getMemberNm();
                int voiceTime = item.getVoiceTime();
                String content = item.getContent();

                tvMemberName.setText(memberNm + "回复" + item.getRcNm() + ":");

                //-------------------------语音:start----------------------------------
                TextView tvVoice = helper.getView(R.id.item_tv_voice);
                View layoutVoice = helper.getView(R.id.item_layout_voice);
                final ImageView ivVoice = helper.getView(R.id.item_iv_voice);
                if (voiceTime > 0) {
                    tvContent.setVisibility(View.GONE);
                    layoutVoice.setVisibility(View.VISIBLE);
                    tvVoice.setText(item.getVoiceTime() + "s''");
                } else {
                    layoutVoice.setVisibility(View.GONE);
                    if (MyStringUtil.isNotEmpty(content)) {
                        tvContent.setText(content);
                        tvContent.setVisibility(View.VISIBLE);
                    } else {
                        tvContent.setVisibility(View.GONE);
                    }
                }
                layoutVoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        downloadFileAndPlayer(item.getContent(), item.getCommentId()+"_"+item.getCommentId(), ivVoice);
                    }
                });
                //-------------------------语音:end----------------------------------

                //回复评论
                tvContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClickListener!=null){
                            onClickListener.onClickListener(parentItem, parentPosition, commentItem, item, REPLY_AGAIN);
                        }
                    }
                });
            }
        }
    }

    //-----------------回调：start------------------------
    public interface OnClickListener{
        void onClickListener(QueryCallon item, int position, CommentItemBean commentItem, CallReplyBean replyItem, int tag);
    }

    public OnClickListener onClickListener;
    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }
    //-----------------回调：en------------------------


    //TODO----------------弹出评论对话框：start----------------------------------------------------
    EasyPopup mPopup;
    public void initPopup(){
        mPopup = new EasyPopup(context)
                .setContentView( R.layout.x_popup_photowall_replace)
                //是否允许点击PopupWindow之外的地方消失
                .setFocusAndOutsideEnable(true)
                //允许背景变暗
                .setBackgroundDimEnable(false)
                //变暗的透明度(0-1)，0为完全透明
                .setDimValue(0)
                //变暗的背景颜色
                .setDimColor(Color.WHITE)
                .createPopup();
        //点赞
        mPopup.getView(R.id.tv_ding_save).setVisibility(View.GONE);
        //评论
        mPopup.getView(R.id.tv_replay_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopup.dismiss();
                if (onClickListener != null){
                    onClickListener.onClickListener(mCurrentItem, mCurrentPosition, null, null, COMMENT);
                }
            }
        });
    }

    public void showPopup(View view){
        mPopup.showAtAnchorView(view, VerticalGravity.CENTER, HorizontalGravity.LEFT, 0, 0);
    }
    //---------------------弹出评论对话框：end-----------------------------------------------

}
