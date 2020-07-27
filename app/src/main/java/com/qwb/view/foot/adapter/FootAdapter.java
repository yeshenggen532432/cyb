package com.qwb.view.foot.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.common.model.PicBean;
import com.qwb.view.foot.model.FootBean;
import com.qwb.utils.Constans;
import com.qwb.utils.JsonHttpUtil;
import com.qwb.utils.SPUtils;
import com.qwb.utils.MyGlideUtil;
import com.qwb.widget.recordvoice.VoiceManager;
import com.xmsx.cnlife.widget.CircleImageView;
import com.chiyong.t3.R;
import com.zhy.http.okhttp.utils.MyUrlUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的足迹
 */
public class FootAdapter extends BaseQuickAdapter<FootBean, BaseViewHolder> {
    // 语音动画
    private AnimationDrawable voiceAnimation;
    private VoiceManager voiceManager;
    private int lastPosition = -1;//记录动画的
    private Context mContext;

    public FootAdapter(Context context) {
        super(R.layout.x_adapter_query_flow);
        this.mContext = context;
        voiceManager = VoiceManager.getInstance(context);
    }

    @Override
    protected void convert(BaseViewHolder helper, FootBean item) {
        helper.setText(R.id.item_tv_memberNm, SPUtils.getUserName());
        helper.setText(R.id.item_tv_time, item.getCreateTime());
        helper.setText(R.id.item_tv_address, item.getAddress());
        helper.setText(R.id.item_tv_bz, item.getRemarks());

        //备注
        TextView tvBz = helper.getView(R.id.item_tv_bz);
        String remarks = item.getRemarks();
        if (!MyStringUtil.isEmpty(remarks)) {
            tvBz.setVisibility(View.VISIBLE);
            tvBz.setText(item.getRemarks());
        } else {
            tvBz.setVisibility(View.GONE);
        }

        //语音
        TextView tvVoice = helper.getView(R.id.item_tv_voice);//
        View layoutVoice = helper.getView(R.id.item_layout_voice);//
        final ImageView ivVoice = helper.getView(R.id.item_iv_voice);//
        if (MyStringUtil.isEmpty(item.getVoicePath())) {
            layoutVoice.setVisibility(View.GONE);
        } else {
            layoutVoice.setVisibility(View.VISIBLE);
            tvVoice.setText(item.getVoiceTime() + "s''");
        }
        final int position = helper.getAdapterPosition();
        final String voiceUrl = item.getVoicePath();
        layoutVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadFileAndPlayer(voiceUrl, position, ivVoice);
            }
        });

        //头像
        CircleImageView ivHead = helper.getView(R.id.item_iv_memberHead);
        MyGlideUtil.getInstance().displayImageRound(Constans.IMGROOTHOST + item.getMemberHead(), ivHead);

        //九图---配置图片显示在Application
        NineGridView nineGrid = helper.getView(R.id.nineGrid);//
        final List<PicBean> images = item.getPicList();
        if (null != images && images.size() > 0) {
            ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            if (images != null) {
                for (PicBean image : images) {
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
    }


    /**
     * 下载音频文件并播放
     */
    private void downloadFileAndPlayer(final String voiceUrl, int position, final ImageView ivVoice) {
        try {
            if (null != voiceAnimation) {
                voiceAnimation.stop();
                voiceAnimation.selectDrawable(0);
            }
            if (voiceManager.isPlaying() && lastPosition == position) {
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
                        if (MyStringUtil.isNotEmpty(fileName)){
                            voiceAnimation = (AnimationDrawable) ivVoice.getBackground();
                            if (voiceAnimation != null) {
                                voiceAnimation.start();
                            }
                            voiceManager.startPlay(fileName);
                        }
                    }
                }).start();
            }
            lastPosition = position;
        } catch (Exception e) {
        }

    }


}
