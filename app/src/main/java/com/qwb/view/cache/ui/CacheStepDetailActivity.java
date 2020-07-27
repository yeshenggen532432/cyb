package com.qwb.view.cache.ui;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.JsonHttpUtil;
import com.qwb.utils.ToastUtils;
import com.qwb.db.DStep1Bean;
import com.qwb.db.DStep2Bean;
import com.qwb.db.DStep3Bean;
import com.qwb.db.DStep6Bean;
import com.qwb.db.DStepAllBean;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.widget.recordvoice.VoiceManager;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 缓存拜访详情
 */
public class CacheStepDetailActivity extends XActivity {

	@Override
	public int getLayoutId() {
		return R.layout.x_activity_cache_step_detail;
	}

	@Override
	public Object newP() {
		return null;
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		initIntent();
		initUI();
		doIntent();
	}

	private DStepAllBean mBean = ConstantUtils.stepAllBean;
	private void initIntent(){
		Intent intent = getIntent();
		if (intent != null) {
			mBean = ConstantUtils.stepAllBean;
		}
	}

	private void doIntent(){
		DStep1Bean bean1 = mBean.getdStep1Bean();
		DStep6Bean bean6 = mBean.getdStep6Bean();
		//基本的
		mTvKhNm.setText(mBean.getKhNm());
		//步骤1
		if(bean1 != null){
			String time1 = bean1.getTime();
			String address1 = bean1.getAddress();
			if(MyStringUtil.isEmpty(time1)){
				mTvTime1.setVisibility(View.GONE);
			}else{
				mTvTime1.setVisibility(View.VISIBLE);
				mTvTime1.setText("签到时间:"+time1);
			}
			if(MyStringUtil.isEmpty(address1)){
				mTvAddress1.setVisibility(View.GONE);
			}else{
				mTvAddress1.setVisibility(View.VISIBLE);
				mTvAddress1.setText("签到地址:"+address1);
			}
		}
		//步骤6
		if(bean6 != null){
			String time6 = bean6.getTime();
			String address6 = bean6.getAddress();
			String fbzj = bean6.getBcbfzj();
			String dbsx = bean6.getDbsx();
			if(MyStringUtil.isEmpty(time6)){
				mTvTime6.setVisibility(View.GONE);
			}else{
				mTvTime6.setVisibility(View.VISIBLE);
				mTvTime6.setText("签退时间:"+time6);
			}
			if(MyStringUtil.isEmpty(address6)){
				mTvAddress6.setVisibility(View.GONE);
			}else{
				mTvAddress6.setVisibility(View.VISIBLE);
				mTvAddress6.setText("签到地址:"+address6);
			}
			if(MyStringUtil.isEmpty(fbzj)){
				mTvBfzj.setVisibility(View.GONE);
			}else{
				mTvBfzj.setVisibility(View.VISIBLE);
				mTvBfzj.setText("拜访总结:"+fbzj);
			}
			if(MyStringUtil.isEmpty(dbsx)){
				mTvDbsx.setVisibility(View.GONE);
			}else{
				mTvDbsx.setVisibility(View.VISIBLE);
				mTvDbsx.setText("待办事项:"+dbsx);
			}
		}
	}

	@BindView(R.id.head_left)
	View mHeadLeft;
	@BindView(R.id.head_right)
	View mHeadRight;
	@BindView(R.id.tv_head_title)
	TextView mTvHeadTitle;
	@BindView(R.id.tv_head_right)
	TextView mTvHeadRight;
	private void initHead() {
		MyStatusBarUtil.getInstance().setColorWhite(context);
		mHeadLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityManager.getInstance().closeActivity(context);
			}
		});
		mTvHeadTitle.setText("拜访详情");
	}

	private void initUI() {
		initHead();
		initBaseUI();
		initStep1();
		initStep2();
		initStep3();
		initStep6();
		initVoice();
	}

	@BindView(R.id.layout_voice)
	View mViewVoice;
	@BindView(R.id.iv_voice)
	ImageView mIvVoice;
	@BindView(R.id.tv_voice)
	TextView mTvVoice;
	private void initVoice() {
		DStep6Bean bean6 = mBean.getdStep6Bean();
		if(bean6 != null){
			final String voice = bean6.getVoice();
			int voiceTime = bean6.getVoiceTime();
			if(MyStringUtil.isEmpty(voice)){
				mViewVoice.setVisibility(View.GONE);
			}else{
				mViewVoice.setVisibility(View.VISIBLE);
				mTvVoice.setText( voiceTime + "s''");
			}
			mViewVoice.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					downloadFileAndPlayer(voice,mIvVoice);
				}
			});
		}
	}

	/**
	 * 下载音频文件并播放
	 */
	// 语音动画
	private AnimationDrawable voiceAnimation;
	private VoiceManager voiceManager;
	private void downloadFileAndPlayer(final String voiceUrl,final ImageView ivVoice) {
		try{
			if(null == voiceManager){
				voiceManager =  VoiceManager.getInstance(context);
			}
			if (null != voiceAnimation) {
				voiceAnimation.stop();
				voiceAnimation.selectDrawable(0);
			}
			if (voiceManager.isPlaying()) {
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
					}
				}).start();
			}
		}catch (Exception e){
		}

	}

	@BindView(R.id.tv_khNm)
	TextView mTvKhNm;
	@BindView(R.id.tv_time1)
	TextView mTvTime1;
	@BindView(R.id.tv_time6)
	TextView mTvTime6;
	@BindView(R.id.tv_bfzj)
	TextView mTvBfzj;
	@BindView(R.id.tv_dbsx)
	TextView mTvDbsx;
	@BindView(R.id.tv_address1)
	TextView mTvAddress1;
	@BindView(R.id.tv_address6)
	TextView mTvAddress6;
	private void initBaseUI() {
	}

	@BindView(R.id.view_step_1)
	View mViewStep1;
	@BindView(R.id.ll_hbzt)
	View mViewHbzt;
	@BindView(R.id.tv_hbzt)
	TextView mTvHbzt;
	@BindView(R.id.ll_ggyy)
	View mViewGgyy;
	@BindView(R.id.tv_ggyy)
	TextView mTvGgyy;
	@BindView(R.id.ll_isXy)
	View mViewIsxy;
	@BindView(R.id.tv_isXy)
	TextView mTvIsxy;
	@BindView(R.id.nineGridView_1)
	NineGridView mNineGridView1;
	private void initStep1() {
		DStep1Bean bean1 = mBean.getdStep1Bean();
		//步骤1
		if(bean1 != null){
			String hbzt = bean1.getHbzt();
			String ggyy = bean1.getGgyy();
			String isXy = bean1.getIsXy();
			if(MyStringUtil.isEmpty(hbzt)){
				mViewHbzt.setVisibility(View.GONE);
			}else{
				mViewHbzt.setVisibility(View.VISIBLE);
				mTvHbzt.setText("及时更换外观破损，肮脏的海报招贴："+hbzt);
			}
			if(MyStringUtil.isEmpty(ggyy)){
				mViewGgyy.setVisibility(View.GONE);
			}else{
				mViewGgyy.setVisibility(View.VISIBLE);
				mTvGgyy.setText("拆除过时的附有旧广告用语的宣传品："+ggyy);
			}
			if(MyStringUtil.isEmpty(isXy)){
				mViewIsxy.setVisibility(View.GONE);
			}else{
				mViewIsxy.setVisibility(View.VISIBLE);
				if("1".equals(isXy)){
					mTvIsxy.setText("检查广告张贴是否显眼：是");
				}else{
					mTvIsxy.setText("检查广告张贴是否显眼：否");
				}
			}

			ArrayList<ImageInfo> imageInfo = new ArrayList<>();
			imageInfo.addAll(getImageInfoList(bean1.getPicList()));
			if (null != imageInfo && imageInfo.size() > 0) {
				mNineGridView1.setAdapter(new NineGridViewClickAdapter(context, imageInfo));
				mNineGridView1.setVisibility(View.VISIBLE);
			} else {
				mNineGridView1.setVisibility(View.GONE);
			}
		}
	}

	@BindView(R.id.view_step_2)
	View mViewStep2;
	@BindView(R.id.ll_pophb)
	View mViewHb;
	@BindView(R.id.tv_pophb)
	TextView mTvHb;
	@BindView(R.id.ll_cq)
	View mViewCq;
	@BindView(R.id.tv_cq)
	TextView mTvCq;
	@BindView(R.id.ll_wq)
	View mViewWq;
	@BindView(R.id.tv_wq)
	TextView mTvWq;
	@BindView(R.id.ll_marker21)
	View mViewMarker21;
	@BindView(R.id.tv_marker21)
	TextView mTvMarker21;
	@BindView(R.id.ll_marker22)
	View mViewMarker22;
	@BindView(R.id.tv_marker22)
	TextView mTvMarker22;
	@BindView(R.id.nineGridView_2)
	NineGridView mNineGridView2;
	private void initStep2() {
		DStep2Bean bean2 = mBean.getdStep2Bean();
		//步骤1
		if(bean2 != null){
			String pophb = bean2.getPophb();
			String cq = bean2.getCq();
			String wq = bean2.getWq();
			String remo1 = bean2.getRemo1();
			String remo2 = bean2.getRemo2();
			if(MyStringUtil.isEmpty(pophb)){
				mViewHb.setVisibility(View.GONE);
			}else{
				mViewHbzt.setVisibility(View.VISIBLE);
				mTvHb.setText("pop海报："+pophb);
			}
			if(MyStringUtil.isEmpty(cq)){
				mViewCq.setVisibility(View.GONE);
			}else{
				mViewCq.setVisibility(View.VISIBLE);
				mTvCq.setText("串旗："+cq);
			}
			if(MyStringUtil.isEmpty(wq)){
				mViewWq.setVisibility(View.GONE);
			}else{
				mViewWq.setVisibility(View.VISIBLE);
				mTvWq.setText("围裙："+wq);
			}
			if(MyStringUtil.isEmpty(remo1)){
				mViewMarker21.setVisibility(View.GONE);
			}else{
				mViewMarker21.setVisibility(View.VISIBLE);
				mTvMarker21.setText("备注1："+remo1);
			}
			if(MyStringUtil.isEmpty(remo2)){
				mViewMarker22.setVisibility(View.GONE);
			}else{
				mViewMarker22.setVisibility(View.VISIBLE);
				mTvMarker22.setText("备注2："+remo2);
			}

			ArrayList<ImageInfo> imageInfo = new ArrayList<>();
			imageInfo.addAll(getImageInfoList(bean2.getPicList()));
			imageInfo.addAll(getImageInfoList(bean2.getPicList2()));
			if (null != imageInfo && imageInfo.size() > 0) {
				mNineGridView2.setAdapter(new NineGridViewClickAdapter(context, imageInfo));
				mNineGridView2.setVisibility(View.VISIBLE);
			} else {
				mNineGridView2.setVisibility(View.GONE);
			}
		}else{
			mViewStep2.setVisibility(View.GONE);
		}

	}

	@BindView(R.id.view_step_3)
	View mViewStep3;
	@BindView(R.id.nineGridView_3)
	NineGridView mNineGridView3;
	private void initStep3() {
		DStep3Bean bean3 = mBean.getdStep3Bean();
		if(bean3 != null){
			ArrayList<ImageInfo> imageInfo = new ArrayList<>();
			imageInfo.addAll(getImageInfoList(bean3.getPicList()));
			imageInfo.addAll(getImageInfoList(bean3.getPicList2()));
			imageInfo.addAll(getImageInfoList(bean3.getPicList3()));
			if (null != imageInfo && imageInfo.size() > 0) {
				mNineGridView3.setAdapter(new NineGridViewClickAdapter(context, imageInfo));
				mNineGridView3.setVisibility(View.VISIBLE);
			} else {
				mNineGridView3.setVisibility(View.GONE);
			}
		}else{
			mViewStep3.setVisibility(View.GONE);
		}
	}
	@BindView(R.id.view_step_6)
	View mViewStep6;
	@BindView(R.id.ll_khState)
	View mViewKhState;
	@BindView(R.id.tv_khState)
	TextView mTvKhState;
	@BindView(R.id.ll_fenlei)
	View mViewFenlei;
	@BindView(R.id.tv_fenlei)
	TextView mTvFenlei;
	@BindView(R.id.ll_xcdate)
	View mViewXcDate;
	@BindView(R.id.tv_xcdate)
	TextView mTvXcDate;
	@BindView(R.id.nineGridView_6)
	NineGridView mNineGridView6;
	private void initStep6() {
		DStep6Bean bean6 = mBean.getdStep6Bean();
		if(bean6 != null){
			String xsjdNm = bean6.getXsjdNm();
			String bfflNm = bean6.getBfflNm();
			String xcdate = bean6.getXcdate();
			if(MyStringUtil.isEmpty(xsjdNm)){
				mViewKhState.setVisibility(View.GONE);
			}else{
				mViewHbzt.setVisibility(View.VISIBLE);
				mTvKhState.setText("客户状态："+xsjdNm);
			}
			if(MyStringUtil.isEmpty(bfflNm)){
				mViewFenlei.setVisibility(View.GONE);
			}else{
				mViewFenlei.setVisibility(View.VISIBLE);
				mTvFenlei.setText("拜访分类："+bfflNm);
			}
			if(MyStringUtil.isEmpty(xcdate)){
				mViewXcDate.setVisibility(View.GONE);
			}else{
				mViewXcDate.setVisibility(View.VISIBLE);
				mTvXcDate.setText("下次拜访日期："+xcdate);
			}

			ArrayList<ImageInfo> imageInfo = new ArrayList<>();
			imageInfo.addAll(getImageInfoList(bean6.getPicList()));
			if (null != imageInfo && imageInfo.size() > 0) {
				mNineGridView6.setAdapter(new NineGridViewClickAdapter(context, imageInfo));
				mNineGridView6.setVisibility(View.VISIBLE);
			} else {
				mNineGridView6.setVisibility(View.GONE);
			}
		}else{
			mViewStep6.setVisibility(View.GONE);
		}
	}


	/**
	 * 获取图片
	 */
	private ArrayList<ImageInfo> getImageInfoList(List<String> images){
		ArrayList<ImageInfo> imageInfo = new ArrayList<>();
		if (images != null) {
			for (String image : images) {
				ImageInfo info = new ImageInfo();
				info.setThumbnailUrl("file://" + image);
				info.setBigImageUrl("file://" + image);
				imageInfo.add(info);
			}
		}
		return imageInfo;
	}

}
