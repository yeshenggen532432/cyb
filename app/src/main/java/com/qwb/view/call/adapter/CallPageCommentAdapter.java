package com.qwb.view.call.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.JsonHttpUtil;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.utils.MyGlideUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.call.model.CallReplyBean;
import com.qwb.view.call.model.CommentItemBean;
import com.qwb.view.call.model.QueryCallon;
import com.qwb.widget.recordvoice.VoiceManager;
import com.xmsx.cnlife.widget.CircleImageView;
import com.xmsx.cnlife.widget.emoji.CCPTextView;
import com.chiyong.t3.R;
import com.zhy.http.okhttp.utils.MyUrlUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的拜访-评论
 */
public class CallPageCommentAdapter extends BaseQuickAdapter<CommentItemBean, BaseViewHolder> {
    private Context context;
    public CallPageCommentAdapter(Context context) {
        super(R.layout.x_adapter_call_page_comment);
        this.context = context;
        this.voiceManager =  VoiceManager.getInstance(context);
    }

    @Override
    protected void convert( BaseViewHolder helper, final CommentItemBean item) {
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
                downloadFileAndPlayer(item.getContent(), ""+ item.getCommentId(), ivVoice);
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
            CallPageReplyAdapter adapter = new CallPageReplyAdapter();
            recyclerView.setAdapter(adapter);
            adapter.setNewData(replyList);
        }
    }

    //TODO--------------------------------回复:start--------------------------------------------------------
    public class CallPageReplyAdapter extends BaseQuickAdapter<CallReplyBean, BaseViewHolder> {

        public CallPageReplyAdapter() {
            super(R.layout.x_adapter_call_page_comment);
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
        }
    }
    //TODO--------------------------------回复:end-------------------------------------------------------

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
                        String fileName = JsonHttpUtil.getInstance().downFile(voiceUrl);
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


}
