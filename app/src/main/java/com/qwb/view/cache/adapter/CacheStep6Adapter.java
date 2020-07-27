package com.qwb.view.cache.adapter;


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
import com.qwb.utils.JsonHttpUtil;
import com.qwb.utils.ToastUtils;
import com.qwb.db.DStep6Bean;
import com.qwb.utils.MyStringUtil;
import com.qwb.widget.recordvoice.VoiceManager;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 文 件 名: 缓存步骤6
 * 修改时间：
 * 修改备注：
 */
public class CacheStep6Adapter extends BaseQuickAdapter<DStep6Bean,BaseViewHolder> {
    private Context mContext;
    public CacheStep6Adapter(Context context) {
        super(R.layout.x_adapter_cache_step6);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, DStep6Bean item) {
        helper.addOnClickListener(R.id.item_layout_sd);
//        helper.addOnClickListener(R.id.layout_left);
        helper.setText(R.id.item_tv_khNm, item.getKhNm());
        helper.setText(R.id.item_tv_time, item.getTime());

        //备注
        TextView tvBfzj = helper.getView(R.id.item_tv_bfzj);
        TextView tvDbsx = helper.getView(R.id.item_tv_dbsx);
        String bfzj = item.getBcbfzj();
        String dbsx = item.getDbsx();
        if(!MyStringUtil.isEmpty(bfzj)){
            tvBfzj.setVisibility(View.VISIBLE);
            tvBfzj.setText("拜访总结：" + bfzj);
        }else{
            tvBfzj.setVisibility(View.GONE);
        }
        if(!MyStringUtil.isEmpty(dbsx)){
            tvDbsx.setVisibility(View.VISIBLE);
            tvDbsx.setText("待办事项：" + dbsx);
        }else{
            tvDbsx.setVisibility(View.GONE);
        }

        //语音
        TextView tvVoice=helper.getView(R.id.item_tv_voice);
        View layoutVoice=helper.getView(R.id.item_layout_voice);
        final ImageView ivVoice=helper.getView(R.id.item_iv_voice);
        if(MyStringUtil.isEmpty(item.getVoice())){
            layoutVoice.setVisibility(View.GONE);
        }else{
            layoutVoice.setVisibility(View.VISIBLE);
            tvVoice.setText(item.getVoiceTime()+"s''");
        }
        final int position = helper.getAdapterPosition();
        final String voiceUrl = item.getVoice();
        layoutVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadFileAndPlayer(voiceUrl, position, ivVoice);
            }
        });

        //九图---配置图片显示在Application
        NineGridView nineGrid = helper.getView(R.id.nineGrid);//
        final List<String> images = item.getPicList();
        if (null != images && images.size() > 0) {
            ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            if (images != null) {
                for (String image : images) {
                    ImageInfo info = new ImageInfo();
                    info.setThumbnailUrl("file://" + image);
                    info.setBigImageUrl("file://" + image);
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
    // 语音动画
    private AnimationDrawable voiceAnimation;
    private VoiceManager voiceManager;
    private int lastPosition = -1;//记录动画的
    private void downloadFileAndPlayer(final String voiceUrl, int position, final ImageView ivVoice) {
        try{
            if(null == voiceManager){
                voiceManager =  VoiceManager.getInstance(mContext);
            }
            if (null != voiceAnimation) {
                voiceAnimation.stop();
                voiceAnimation.selectDrawable(0);
            }
            if (voiceManager.isPlaying() && lastPosition == position) {
                voiceManager.stopPlay();
            }else{
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
//                        Looper.prepare();
                        String fileName = JsonHttpUtil.getInstance().downFile(voiceUrl);
                        if(MyStringUtil.isEmpty(fileName)){
                            ToastUtils.showCustomToast("播放失败,可能文件路径错误");
                        }else{
                            voiceAnimation = (AnimationDrawable) ivVoice.getBackground();
                            if (voiceAnimation != null) {
                                voiceAnimation.start();
                            }
                            voiceManager.startPlay(fileName);
                        }
//                        Looper.loop();
                    }
                }).start();
            }
            lastPosition = position;
        }catch (Exception e){
        }

    }

}
