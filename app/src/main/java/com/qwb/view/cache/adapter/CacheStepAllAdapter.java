package com.qwb.view.cache.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xmsx.cnlife.widget.ComputeHeightGridView;
import com.xmsx.cnlife.widget.photo.ImagePagerActivity;
import com.qwb.utils.JsonHttpUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.db.DStep1Bean;
import com.qwb.db.DStep6Bean;
import com.qwb.db.DStepAllBean;
import com.qwb.utils.MyGlideUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.widget.recordvoice.VoiceManager;
import com.chiyong.t3.R;

import java.util.List;

import static dev.utils.app.AppUtils.getWindowManager;

/**
 * 文 件 名: 缓存步骤
 * 修改时间：
 * 修改备注：
 */
public class CacheStepAllAdapter extends BaseQuickAdapter<DStepAllBean, BaseViewHolder> {
    // 语音动画
    private AnimationDrawable voiceAnimation;
    private VoiceManager voiceManager;
    private int lastPosition = -1;//记录动画的
    private Context mContext;

    public CacheStepAllAdapter(Context context) {
        super(R.layout.x_adapter_cache_step_all);
        this.mContext = context;
        voiceManager = VoiceManager.getInstance(context);
    }

    @Override
    protected void convert(BaseViewHolder helper, DStepAllBean item) {
        helper.addOnClickListener(R.id.right)
            .addOnClickListener(R.id.item_tv_khNm)
            .addOnClickListener(R.id.item_tv_time)
            .addOnClickListener(R.id.item_tv_address_qd)
            .addOnClickListener(R.id.item_tv_address_qt);

        helper.setText(R.id.item_tv_khNm, item.getKhNm());
        helper.setText(R.id.item_tv_time, item.getTime());
        TextView tvAddressQd = helper.getView(R.id.item_tv_address_qd);
        TextView tvAddressQt = helper.getView(R.id.item_tv_address_qt);
        TextView tvVoice = helper.getView(R.id.item_tv_voice);
        View layoutVoice = helper.getView(R.id.item_layout_voice);
        final ImageView ivVoice = helper.getView(R.id.item_iv_voice);

        //签到地址
        DStep1Bean bean1 = item.getdStep1Bean();
        String addressQd = bean1.getAddress();
        if (MyStringUtil.isEmpty(addressQd)) {
            if (!MyStringUtil.isEmpty(bean1.getLatitude()) && !MyStringUtil.isEmpty(bean1.getLongitude())) {
                LatLng latLng = new LatLng(Double.valueOf(bean1.getLatitude()), Double.valueOf(bean1.getLongitude()));
                reverseGeoCode(latLng, tvAddressQd, 1);
            }else {
                tvAddressQd.setVisibility(View.GONE);
            }
        } else {
            tvAddressQd.setVisibility(View.VISIBLE);
            tvAddressQd.setText("签到地址:" + addressQd);
        }

        //签到地址；语音
        DStep6Bean bean6 = item.getdStep6Bean();
        if(bean6 != null){
            String addressQt = bean6.getAddress();
            if (MyStringUtil.isEmpty(addressQt)) {
                if (!MyStringUtil.isEmpty(bean6.getLatitude()) && !MyStringUtil.isEmpty(bean6.getLongitude())) {
                    LatLng latLng = new LatLng(Double.valueOf(bean6.getLatitude()), Double.valueOf(bean6.getLongitude()));
                    reverseGeoCode(latLng, tvAddressQt, 2);
                }else{
                    tvAddressQt.setVisibility(View.GONE);
                }
            } else {
                tvAddressQt.setVisibility(View.VISIBLE);
                tvAddressQt.setText("签退地址：" + addressQt);
            }

            if (MyStringUtil.isEmpty(bean6.getVoice())) {
                layoutVoice.setVisibility(View.GONE);
            } else {
                layoutVoice.setVisibility(View.VISIBLE);
                tvVoice.setText(bean6.getVoiceTime() + "s''");
            }
            final int position = helper.getAdapterPosition();
            final String voiceUrl = bean6.getVoice();
            layoutVoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downloadFileAndPlayer(voiceUrl, position, ivVoice);
                }
            });
        }else{
            layoutVoice.setVisibility(View.GONE);
        }

//        //九图---配置图片显示在Application
//        NineGridView nineGrid = helper.getView(R.id.nineGrid);
//        final List<DStepAllBean.PicBean> images = item.getPicList();
//        if (null != images && images.size() > 0) {
//            ArrayList<ImageInfo> imageInfo = new ArrayList<>();
//            if (images != null) {
//                for (DStepAllBean.PicBean image : images) {
//                    ImageInfo info = new ImageInfo();
//                    info.setThumbnailUrl("file://" + image.getPic());
//                    info.setBigImageUrl("file://" + image.getPic());
//                    imageInfo.add(info);
//                }
//            }
//            nineGrid.setAdapter(new NineGridViewClickAdapter(mContext, imageInfo));
//            nineGrid.setVisibility(View.VISIBLE);
//        } else {
//            nineGrid.setVisibility(View.GONE);
//        }

        ComputeHeightGridView gridView = helper.getView(R.id.gridView);
        final List<DStepAllBean.PicBean> listpic = item.getPicList();
        if (listpic != null && listpic.size() > 0) {
            gridView.setVisibility(View.VISIBLE);
            gridView.setAdapter(new BaseAdapter() {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.x_adapter_photo_callon, null);
                    }
                    DStepAllBean.PicBean pic = listpic.get(position);
                    ImageView iv_simple = MyUtils.getViewFromVH(convertView, R.id.iv_simple);
                    MyGlideUtil.getInstance().displayImageSquere("file://" + pic.getPic(),iv_simple );
                    TextView tv_title = MyUtils.getViewFromVH(convertView, R.id.tv_title);
                    if (!MyUtils.isEmptyString(pic.getName())) {
                        tv_title.setText(pic.getName());
                    }
//                    // 代码中设置图片ImageView的宽高-宽高比例：1:1
                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    int imgWidth = (dm.widthPixels - 100) / 3;// (100:减去)
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_simple.getLayoutParams();
                    params.width = imgWidth;
                    params.height = imgWidth;
                    iv_simple.setLayoutParams(params);
                    return convertView;
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public Object getItem(int position) {
                    return null;
                }

                @Override
                public int getCount() {
                    return listpic.size();
                }
            });

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String[] urls = new String[listpic.size()];
                    for (int i = 0; i < listpic.size(); i++) {
                        urls[i] = "file://"+listpic.get(i).getPic();
                    }
//                    // 点击放大图片
                    Intent intent = new Intent(mContext, ImagePagerActivity.class);
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                    mContext. startActivity(intent);
                }
            });
        } else {
            gridView.setVisibility(View.GONE);
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
            lastPosition = position;
        } catch (Exception e) {
        }
    }

    //经纬度转地址
    public void reverseGeoCode(LatLng latLng, final TextView textView, final int addressType) {
        // 创建地理编码检索实例
        GeoCoder geoCoder = GeoCoder.newInstance();
        // 设置地理编码检索监听者
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    textView.setVisibility(View.GONE);
                    return;
                }
                String address = result.getAddress();
                if (!MyStringUtil.isEmpty(address)) {
                    if (1 == addressType) {
                        textView.setText("签到地址:" + address);
                    } else {
                        textView.setText("签退地址:" + address);
                    }
                    textView.setVisibility(View.VISIBLE);
                }else{
                    textView.setVisibility(View.GONE);
                }
            }
        });
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
    }
}
