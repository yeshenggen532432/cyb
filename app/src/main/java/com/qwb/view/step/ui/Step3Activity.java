package com.qwb.view.step.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.OtherUtils;
import com.qwb.view.step.parsent.PStep3;
import com.qwb.utils.Constans;
import com.qwb.utils.ILUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.common.adapter.PicAdapter;
import com.qwb.listener.OnNoMoreClickListener;
import com.qwb.db.DStep3Bean;
import com.qwb.utils.MyNetWorkUtil;
import com.qwb.view.step.model.QueryBfcljccjBean;
import com.qwb.view.step.model.QueryBfcljccjBean.QueryBfcljccj;
import com.qwb.view.step.model.QueryCljccjMdlsBean;
import com.xmsx.qiweibao.R;
import com.zhy.http.okhttp.utils.MyUrlUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.log.XLog;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import io.reactivex.functions.Consumer;

/**
 * 创建描述：拜访3：库存检查-注意很多地方用到Constans.pic_type:区分同一个界面有多个“拍照和相册”的功能
 */
public class Step3Activity extends XActivity<PStep3> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_step3;
    }

    @Override
    public PStep3 newP() {
        return new PStep3();
    }

    private ImageLoader imageLoder;
    private DisplayImageOptions options;

    @Override
    public void initData(Bundle savedInstanceState) {
        imageLoder = ILUtil.getImageLoder();// 加载图片
        options = ILUtil.getOptionsSquere();
        getP().loadDataModel(context, pdateStr);//获取模版
        initIntent();
        initUI();
        doIntent();
        getP().queryToken(null);
    }

    /**
     * 初始化Intent
     */
    private String cid;// 客户ID
    private String mKhNm;
    private String count3;
    private String pdateStr;// 补拜访时间(拜访计划)

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            cid = intent.getStringExtra(ConstantUtils.Intent.CLIENT_ID);
            mKhNm = intent.getStringExtra(ConstantUtils.Intent.CLIENT_NAME);
            count3 = intent.getStringExtra(ConstantUtils.Intent.STEP);
            pdateStr = intent.getStringExtra(ConstantUtils.Intent.SUPPLEMENT_TIME);// 补拜访时间
        }
    }

    private void doIntent() {
        mTvHeadTitle.setText(mKhNm);
        if ("1".equals(count3)) {// 已上传
            getP().loadDataInfo(context, cid);//获取上次提交信息
            mTvHeadRight.setText("修改");
        } else {
            mTvHeadRight.setText("提交");
        }
        if (MyUtils.isEmptyString(pdateStr)) {
            mTvCallOnDate.setText(MyTimeUtils.getTodayStr());// 拜访日期
        } else {
            mTvCallOnDate.setText(pdateStr);// 拜访日期
        }
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        initHead();
        initBaseView();
        initAdapter();
    }

    // 头部
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    @BindView(R.id.tv_head_right)
    TextView mTvHeadRight;

    private void initHead() {
        OtherUtils.setStatusBarColor(context);//设置状态栏颜色；透明度
        findViewById(R.id.iv_head_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mTvHeadRight.setOnClickListener(new OnNoMoreClickListener() {
            @Override
            protected void OnMoreClick(View view) {
                addData();
            }
        });
    }

    //基本UI
    private TextView mTvCallOnDate;
    private TextView tv_name1;
    private TextView tv_name2;
    private TextView tv_name3;
    private LinearLayout ll_hj1;
    private LinearLayout ll_hj2;
    private LinearLayout ll_hj3;
    private LinearLayout ll_dj1;
    private LinearLayout ll_dj2;
    private LinearLayout ll_syt1;
    private LinearLayout ll_syt2;
    private LinearLayout ll_bds1;
    private LinearLayout ll_bds2;
    private LinearLayout ll_dj3;
    private LinearLayout ll_syt3;
    private LinearLayout ll_bds3;
    private EditText edit_hj1;
    private EditText edit_hj2;
    private EditText edit_hj3;
    private EditText edit_dj1;
    private EditText edit_dj2;
    private EditText edit_syt1;
    private EditText edit_syt2;
    private EditText edit_bds1;
    private EditText edit_bds2;
    private EditText edit_remo1;
    private EditText edit_remo2;
    private EditText edit_remo3;
    private EditText edit_dj3;
    private EditText edit_syt3;
    private EditText edit_bds3;

    private void initBaseView() {
        mTvCallOnDate = findViewById(R.id.tv_callOnDate);// 名称
        tv_name1 = findViewById(R.id.tv_name1);// 名称
        tv_name2 = findViewById(R.id.tv_name2);
        tv_name3 = findViewById(R.id.tv_name3);
        ll_hj1 = findViewById(R.id.ll_hj1);// 货架
        ll_hj2 = findViewById(R.id.ll_hj2);
        ll_hj3 = findViewById(R.id.ll_hj3);
        ll_dj1 = findViewById(R.id.ll_dj1);// 端架
        ll_dj2 = findViewById(R.id.ll_dj2);
        ll_dj3 = findViewById(R.id.ll_dj3);
        ll_syt1 = findViewById(R.id.ll_syt1);// 收银台
        ll_syt2 = findViewById(R.id.ll_syt2);
        ll_syt3 = findViewById(R.id.ll_syt3);
        ll_bds1 = findViewById(R.id.ll_bds1);// 冰点数
        ll_bds2 = findViewById(R.id.ll_bds2);
        ll_bds3 = findViewById(R.id.ll_bds3);
        edit_hj1 = findViewById(R.id.edit_hj1);// 货架
        edit_hj2 = findViewById(R.id.edit_hj2);
        edit_hj3 = findViewById(R.id.edit_hj3);
        edit_dj1 = findViewById(R.id.edit_dj1);// 端架
        edit_dj2 = findViewById(R.id.edit_dj2);
        edit_dj3 = findViewById(R.id.edit_dj3);
        edit_syt1 = findViewById(R.id.edit_syt1);// 收银台
        edit_syt2 = findViewById(R.id.edit_syt2);
        edit_syt3 = findViewById(R.id.edit_syt3);
        edit_bds1 = findViewById(R.id.edit_bds1);// 冰点数
        edit_bds2 = findViewById(R.id.edit_bds2);
        edit_bds3 = findViewById(R.id.edit_bds3);
        edit_remo1 = findViewById(R.id.edit_remo1);// 摘要
        edit_remo2 = findViewById(R.id.edit_remo2);
        edit_remo3 = findViewById(R.id.edit_remo3);
    }

    //适配器-图片
    @BindView(R.id.iv_add_pic)
    ImageView mIvAddPic;
    @BindView(R.id.iv_del_pic)
    ImageView mIvDelPic;
    @BindView(R.id.recyclerview_pic)
    RecyclerView mRecyclerViewPic;
    private PicAdapter mPicAdapter;
    @BindView(R.id.iv_add_pic2)
    ImageView mIvAddPic2;
    @BindView(R.id.iv_del_pic2)
    ImageView mIvDelPic2;
    @BindView(R.id.recyclerview_pic2)
    RecyclerView mRecyclerViewPic2;
    private PicAdapter mPicAdapter2;
    @BindView(R.id.iv_add_pic3)
    ImageView mIvAddPic3;
    @BindView(R.id.iv_del_pic3)
    ImageView mIvDelPic3;
    @BindView(R.id.recyclerview_pic3)
    RecyclerView mRecyclerViewPic3;
    private PicAdapter mPicAdapter3;
    private int mPhotoType;//默认1生动化拍照；2:堆头拍照

    private void initAdapter() {
        mRecyclerViewPic.setHasFixedSize(true);
        mRecyclerViewPic.setLayoutManager(new GridLayoutManager(context, 3));
        mPicAdapter = new PicAdapter(context);
        mPicAdapter.openLoadAnimation();
        mRecyclerViewPic.setAdapter(mPicAdapter);
        mPicAdapter.setOnDeletePicListener(new PicAdapter.OnDeletePicListener() {
            @Override
            public void onDeletePicListener(int position) {
                mPicList.remove(position);
                refreshAdapter(mPicAdapter.isDelete(), null, position);
            }
        });
        //添加图片
        mIvAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPicAdapter.getData().size() >= 6) {
                    ToastUtils.showCustomToast("最多只能选6张图片！");
                    return;
                }
                mPhotoType = 1;
                doCamera();
            }
        });
        //删除图片
        mIvDelPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshAdapter(!mPicAdapter.isDelete(), null, null);
            }
        });
        //-----------------------------2------------------------------------------------
        mRecyclerViewPic2.setHasFixedSize(true);
        mRecyclerViewPic2.setLayoutManager(new GridLayoutManager(context, 3));
        mPicAdapter2 = new PicAdapter(context);
        mPicAdapter2.openLoadAnimation();
        mRecyclerViewPic2.setAdapter(mPicAdapter2);
        mPicAdapter2.setOnDeletePicListener(new PicAdapter.OnDeletePicListener() {
            @Override
            public void onDeletePicListener(int position) {
                mPicList2.remove(position);
                refreshAdapter2(mPicAdapter2.isDelete(), null, position);
            }
        });
        //添加图片
        mIvAddPic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPicAdapter2.getData().size() >= 6) {
                    ToastUtils.showCustomToast("最多只能选6张图片！");
                    return;
                }
                mPhotoType = 2;
                doCamera();
            }
        });
        //删除图片
        mIvDelPic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshAdapter2(!mPicAdapter2.isDelete(), null, null);
            }
        });
        //-----------------------------3------------------------------------------------
        mRecyclerViewPic3.setHasFixedSize(true);
        mRecyclerViewPic3.setLayoutManager(new GridLayoutManager(context, 3));
        mPicAdapter3 = new PicAdapter(context);
        mPicAdapter3.openLoadAnimation();
        mRecyclerViewPic3.setAdapter(mPicAdapter3);
        mPicAdapter3.setOnDeletePicListener(new PicAdapter.OnDeletePicListener() {
            @Override
            public void onDeletePicListener(int position) {
                mPicList3.remove(position);
                refreshAdapter3(mPicAdapter3.isDelete(), null, position);
            }
        });
        //添加图片
        mIvAddPic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPicAdapter3.getData().size() >= 6) {
                    ToastUtils.showCustomToast("最多只能选6张图片！");
                    return;
                }
                mPhotoType = 3;
                doCamera();
            }
        });
        //删除图片
        mIvDelPic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshAdapter3(!mPicAdapter3.isDelete(), null, null);
            }
        });
    }

    /**
     * 刷新适配器（isDelete：是否删除状态，movePosition：删除的下标）
     */
    private List<String> mPicList = new ArrayList<>();//路径没有拼接file://：用来上传的

    private void refreshAdapter(boolean isDelete, List<String> picList, Integer movePosition) {
        List<String> datas = mPicAdapter.getData();
        mPicAdapter.setDelete(isDelete);
        if (null != picList) {
            if (null == datas || datas.isEmpty()) {
                mPicAdapter.setNewData(picList);
            } else {
                mPicAdapter.addData(picList);
            }
        }
        if (null != movePosition) {
            mPicAdapter.remove(movePosition);
        }
        mPicAdapter.notifyDataSetChanged();
        List<String> datas2 = mPicAdapter.getData();
        if (null == datas2 || datas2.isEmpty()) {
            mIvDelPic.setVisibility(View.GONE);
        } else {
            mIvDelPic.setVisibility(View.VISIBLE);// 删除图标可见或不可见
        }
    }

    private List<String> mPicList2 = new ArrayList<>();//路径没有拼接file://：用来上传的

    private void refreshAdapter2(boolean isDelete, List<String> picList, Integer movePosition) {
        List<String> datas = mPicAdapter2.getData();
        mPicAdapter2.setDelete(isDelete);
        if (null != picList) {
            if (null == datas || datas.isEmpty()) {
                mPicAdapter2.setNewData(picList);
            } else {
                mPicAdapter2.addData(picList);
            }
        }
        if (null != movePosition) {
            mPicAdapter2.remove(movePosition);
        }
        mPicAdapter2.notifyDataSetChanged();
        List<String> datas2 = mPicAdapter2.getData();
        if (null == datas2 || datas2.isEmpty()) {
            mIvDelPic2.setVisibility(View.GONE);
        } else {
            mIvDelPic2.setVisibility(View.VISIBLE);// 删除图标可见或不可见
        }
    }

    private List<String> mPicList3 = new ArrayList<>();//路径没有拼接file://：用来上传的

    private void refreshAdapter3(boolean isDelete, List<String> picList, Integer movePosition) {
        List<String> datas = mPicAdapter3.getData();
        mPicAdapter3.setDelete(isDelete);
        if (null != picList) {
            if (null == datas || datas.isEmpty()) {
                mPicAdapter3.setNewData(picList);
            } else {
                mPicAdapter3.addData(picList);
            }
        }
        if (null != movePosition) {
            mPicAdapter3.remove(movePosition);
        }
        mPicAdapter3.notifyDataSetChanged();
        List<String> datas2 = mPicAdapter3.getData();
        if (null == datas2 || datas2.isEmpty()) {
            mIvDelPic3.setVisibility(View.GONE);
        } else {
            mIvDelPic3.setVisibility(View.VISIBLE);// 删除图标可见或不可见
        }
    }

    // 相机直接拍摄
    public void doCamera() {
        new RxPermissions(this)
                .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {//TODO 许可
                            Intent intent = new Intent(context, ImageGridActivity.class);
                            intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                            startActivityForResult(intent, Constans.TAKE_PIC_XJ);
                        } else {
                            //TODO 未许可
                            ToastUtils.showCustomToast("拍照权限和存储权限未开启，请在手机设置中打开权限！");
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (images != null) {
                List<String> picList = new ArrayList<>();
                for (ImageItem imageItem : images) {
                    picList.add("file://" + imageItem.path);//图片显示
                    if (mPhotoType == 3) {
                        mPicList3.add(imageItem.path);//上传图片的
                        Constans.publish_pics3333.add(imageItem.path);//上传成功后删除手机图片
                    } else if (mPhotoType == 2) {
                        mPicList2.add(imageItem.path);//上传图片的
                        Constans.publish_pics2222.add(imageItem.path);//上传成功后删除手机图片
                    } else {
                        mPicList.add(imageItem.path);//上传图片的
                        Constans.publish_pics1111.add(imageItem.path);//上传成功后删除手机图片
                    }
                }
                if (mPhotoType == 3) {
                    refreshAdapter3(false, picList, null);
                } else if (mPhotoType == 2) {
                    refreshAdapter2(false, picList, null);
                } else {
                    refreshAdapter(false, picList, null);
                }

            }
        }
    }


    //TODO ***************************************接口相关***************************************************
    // 提交或修改
    private String id1;
    private String id2;
    private String id3;
    private List<QueryBfcljccj> BfcljccjList = new ArrayList<>();
    private String mJsonStr;

    private void addData() {
        String hj1Str = edit_hj1.getText().toString().trim();
        String hj2Str = edit_hj2.getText().toString().trim();
        String hj3Str = edit_hj3.getText().toString().trim();
        String dj1Str = edit_dj1.getText().toString().trim();
        String dj2Str = edit_dj2.getText().toString().trim();
        String dj3Str = edit_dj3.getText().toString().trim();
        String syt1Str = edit_syt1.getText().toString().trim();
        String syt2Str = edit_syt2.getText().toString().trim();
        String syt3Str = edit_syt3.getText().toString().trim();
        String bds1Str = edit_bds1.getText().toString().trim();
        String bds2Str = edit_bds2.getText().toString().trim();
        String bds3Str = edit_bds3.getText().toString().trim();
        String remo1Str = edit_remo1.getText().toString().trim();
        String remo2Str = edit_remo2.getText().toString().trim();
        String remo3Str = edit_remo3.getText().toString().trim();

        QueryBfcljccj queryBfcljccj1 = new QueryBfcljccjBean.QueryBfcljccj();
        QueryBfcljccj queryBfcljccj2 = new QueryBfcljccjBean.QueryBfcljccj();
        QueryBfcljccj queryBfcljccj3 = new QueryBfcljccjBean.QueryBfcljccj();
        if ("1".equals(count3)) {
            queryBfcljccj1.setId(String.valueOf(id1));
            queryBfcljccj2.setId(String.valueOf(id2));
            queryBfcljccj3.setId(String.valueOf(id3));
        }

        queryBfcljccj1.setMdid(String.valueOf(mdid1));
        queryBfcljccj1.setHjpms(hj1Str);
        queryBfcljccj1.setDjpms(dj1Str);
        queryBfcljccj1.setSytwl(syt1Str);
        queryBfcljccj1.setBds(bds1Str);
        queryBfcljccj1.setRemo(remo1Str);
        queryBfcljccj2.setMdid(String.valueOf(mdid2));
        queryBfcljccj2.setHjpms(hj2Str);
        queryBfcljccj2.setDjpms(dj2Str);
        queryBfcljccj2.setSytwl(syt2Str);
        queryBfcljccj2.setBds(bds2Str);
        queryBfcljccj2.setRemo(remo2Str);
        queryBfcljccj3.setMdid(String.valueOf(mdid3));
        queryBfcljccj3.setHjpms(hj3Str);
        queryBfcljccj3.setDjpms(dj3Str);
        queryBfcljccj3.setSytwl(syt3Str);
        queryBfcljccj3.setBds(bds3Str);
        queryBfcljccj3.setRemo(remo3Str);

        BfcljccjList.clear();
        BfcljccjList.add(queryBfcljccj1);
        BfcljccjList.add(queryBfcljccj2);
        BfcljccjList.add(queryBfcljccj3);

        mJsonStr = JSON.toJSONString(BfcljccjList);
        XLog.e("mJsonStr", mJsonStr);
        if (!MyNetWorkUtil.isNetworkConnected()) {
            ToastUtils.showCustomToast("网络不流通，请检查网络是否正常");
            showDialogCache();
            return;
        }
        getP().addData(context, count3, cid, mJsonStr, pdateStr, mPicList, mPicList2, mPicList3, mQueryToken);
    }

    //显示模版信息
    private Integer mdid1 = 1;
    private Integer mdid2 = 2;
    private Integer mdid3 = 3;

    public void showModelInfo(List<QueryCljccjMdlsBean.CljccjMdls> list) {
        try {
            QueryCljccjMdlsBean.CljccjMdls cljccjMdls1 = list.get(0);
            QueryCljccjMdlsBean.CljccjMdls cljccjMdls2 = list.get(1);
            QueryCljccjMdlsBean.CljccjMdls cljccjMdls3 = list.get(2);
            if (null != cljccjMdls1.getId()) {
                mdid1 = cljccjMdls1.getId();
            }
            if (null != cljccjMdls2.getId()) {
                mdid2 = cljccjMdls2.getId();
            }
            if (null != cljccjMdls3.getId()) {
                mdid3 = cljccjMdls3.getId();
            }
            tv_name1.setText(cljccjMdls1.getMdNm());
            tv_name2.setText(cljccjMdls2.getMdNm());
            tv_name3.setText(cljccjMdls3.getMdNm());
            if (cljccjMdls1.getIsHjpms() == 1) {// 货架
                ll_hj1.setVisibility(View.VISIBLE);
            } else {
                ll_hj1.setVisibility(View.GONE);
            }
            if (cljccjMdls2.getIsHjpms() == 1) {
                ll_hj2.setVisibility(View.VISIBLE);
            } else {
                ll_hj2.setVisibility(View.GONE);
            }
            if (cljccjMdls3.getIsHjpms() == 1) {
                ll_hj3.setVisibility(View.VISIBLE);
            } else {
                ll_hj3.setVisibility(View.GONE);
            }
            if (cljccjMdls1.getIsDjpms() == 1) {// 端架
                ll_dj1.setVisibility(View.VISIBLE);
            } else {
                ll_dj1.setVisibility(View.GONE);
            }
            if (cljccjMdls2.getIsDjpms() == 1) {
                ll_dj2.setVisibility(View.VISIBLE);
            } else {
                ll_dj2.setVisibility(View.GONE);
            }
            if (cljccjMdls3.getIsDjpms() == 1) {
                ll_dj3.setVisibility(View.VISIBLE);
            } else {
                ll_dj3.setVisibility(View.GONE);
            }
            if (cljccjMdls1.getIsSytwl() == 1) {// 收银台
                ll_syt1.setVisibility(View.VISIBLE);
            } else {
                ll_syt1.setVisibility(View.GONE);
            }
            if (cljccjMdls2.getIsSytwl() == 1) {
                ll_syt2.setVisibility(View.VISIBLE);
            } else {
                ll_syt2.setVisibility(View.GONE);
            }
            if (cljccjMdls3.getIsSytwl() == 1) {
                ll_syt3.setVisibility(View.VISIBLE);
            } else {
                ll_syt3.setVisibility(View.GONE);
            }
            if (cljccjMdls1.getIsBds() == 1) {// 冰点数
                ll_bds1.setVisibility(View.VISIBLE);
            } else {
                ll_bds1.setVisibility(View.GONE);
            }
            if (cljccjMdls2.getIsBds() == 1) {
                ll_bds2.setVisibility(View.VISIBLE);
            } else {
                ll_bds2.setVisibility(View.GONE);
            }
            if (cljccjMdls3.getIsBds() == 1) {
                ll_bds3.setVisibility(View.VISIBLE);
            } else {
                ll_bds3.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //显示上次上传的信息
    public void showInfo(List<QueryBfcljccjBean.QueryBfcljccj> list) {
        try {
            //------------------------1------------------------------
            QueryBfcljccjBean.QueryBfcljccj queryBfcljccj1 = list.get(0);
            id1 = queryBfcljccj1.getId();
            edit_hj1.setText(queryBfcljccj1.getHjpms());
            edit_dj1.setText(queryBfcljccj1.getDjpms());
            edit_syt1.setText(queryBfcljccj1.getSytwl());
            edit_bds1.setText(queryBfcljccj1.getBds());
            edit_remo1.setText(queryBfcljccj1.getRemo());

            List<String> picList = new ArrayList<>();
            List<QueryBfcljccjBean.QueryBfcljccj.ClPhoto> bfxgPicLs1 = queryBfcljccj1.getBfxgPicLs();
            if (bfxgPicLs1 != null && bfxgPicLs1.size() > 0) {
                for (int i = 0; i < bfxgPicLs1.size(); i++) {
                    QueryBfcljccjBean.QueryBfcljccj.ClPhoto clPhoto = bfxgPicLs1.get(i);
                    Constans.publish_pics1.add(MyUrlUtil.getUrl(Constans.IMGROOTHOST + clPhoto.getPic()));
                    picList.add(MyUrlUtil.getUrl(Constans.IMGROOTHOST + clPhoto.getPic()));//图片显示
                }
            }
            //------------------------2------------------------------
            QueryBfcljccjBean.QueryBfcljccj queryBfcljccj2 = list.get(1);
            id2 = queryBfcljccj2.getId();
            edit_hj2.setText(queryBfcljccj2.getHjpms());
            edit_dj2.setText(queryBfcljccj2.getDjpms());
            edit_syt2.setText(queryBfcljccj2.getSytwl());
            edit_bds2.setText(queryBfcljccj2.getBds());
            edit_remo2.setText(queryBfcljccj2.getRemo());

            List<String> picList2 = new ArrayList<>();
            List<QueryBfcljccjBean.QueryBfcljccj.ClPhoto> bfxgPicLs2 = queryBfcljccj2.getBfxgPicLs();
            if (bfxgPicLs2 != null && bfxgPicLs2.size() > 0) {
                for (int i = 0; i < bfxgPicLs2.size(); i++) {
                    QueryBfcljccjBean.QueryBfcljccj.ClPhoto clPhoto = bfxgPicLs2.get(i);
                    Constans.publish_pics2.add(MyUrlUtil.getUrl(Constans.IMGROOTHOST + clPhoto.getPic()));
                    picList2.add(MyUrlUtil.getUrl(Constans.IMGROOTHOST + clPhoto.getPic()));
                }
            }
            //------------------------3------------------------------
            QueryBfcljccjBean.QueryBfcljccj queryBfcljccj3 = list.get(2);
            id3 = queryBfcljccj3.getId();
            edit_hj3.setText(queryBfcljccj3.getHjpms());
            edit_dj3.setText(queryBfcljccj3.getDjpms());
            edit_syt3.setText(queryBfcljccj3.getSytwl());
            edit_bds3.setText(queryBfcljccj3.getBds());
            edit_remo3.setText(queryBfcljccj3.getRemo());

            List<String> picList3 = new ArrayList<>();
            List<QueryBfcljccjBean.QueryBfcljccj.ClPhoto> bfxgPicLs3 = queryBfcljccj3.getBfxgPicLs();
            if (bfxgPicLs3 != null && bfxgPicLs3.size() > 0) {
                for (int i = 0; i < bfxgPicLs3.size(); i++) {
                    QueryBfcljccjBean.QueryBfcljccj.ClPhoto clPhoto = bfxgPicLs3.get(i);
                    Constans.publish_pics3.add(MyUrlUtil.getUrl(Constans.IMGROOTHOST + clPhoto.getPic()));
                    picList3.add(MyUrlUtil.getUrl(Constans.IMGROOTHOST + clPhoto.getPic()));
                }
            }
            //------------------------4------------------------------
            refreshAdapter(false, picList, null);
            refreshAdapter2(false, picList2, null);
            refreshAdapter3(false, picList3, null);
            saveImg(picList, 1);
            saveImg(picList2, 2);
            saveImg(picList3, 3);
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //下载图片并保存文件
    private void saveImg(final List<String> list, final int type) {
        if (null == list || list.isEmpty()) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            final int j = i;
            imageLoder.loadImage(list.get(i), options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    // 第一次保存的文件路径容器不等于图片url容器长度，就下载，添加
                    // 后续选择会往同时往url路径和图片路径增加，相同，就不继续下载
                    File outDir = new File(Constans.DIR_IMAGE_PROCEDURE);// 保存到临时文件夹
                    if (!outDir.exists()) {
                        outDir.mkdirs();
                    }
                    File file = new File(outDir, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        // 压缩图片 100为不压缩
                        FileOutputStream fos = new FileOutputStream(file);
                        loadedImage.compress(CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();
                        if (3 == type) {
                            Constans.publish_pics3333.add(file.getAbsolutePath());
                            //j的作用：避免异步下载图片保存的位置错乱（异步下载图片，可以第一个先下载但第二个先下载完）
                            mPicList3.add(j, file.getAbsolutePath());//上传图片的
                        } else if (2 == type) {
                            Constans.publish_pics2222.add(file.getAbsolutePath());
                            //j的作用：避免异步下载图片保存的位置错乱（异步下载图片，可以第一个先下载但第二个先下载完）
                            mPicList2.add(j, file.getAbsolutePath());//上传图片的
                        } else {
                            Constans.publish_pics1111.add(file.getAbsolutePath());
                            //j的作用：避免异步下载图片保存的位置错乱（异步下载图片，可以第一个先下载但第二个先下载完）
                            mPicList.add(j, file.getAbsolutePath());//上传图片的
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    //数据提交成功-添加或修改
    public void submitDataSuccess() {
        Intent data = new Intent();
        data.putExtra(ConstantUtils.Intent.SUCCESS, true);
        setResult(0, data);
        OtherUtils.resetStepStatus(Step3Activity.this);// 退出界面重置原状态
        ActivityManager.getInstance().closeActivity(context);
    }

    private int mErrorCount;

    public void submitDataError() {
        mErrorCount++;
        if (mErrorCount > 1) {
            showDialogCache();
        }
    }

    public void showDialogCache() {
        NormalDialog dialog = new NormalDialog(context);
        dialog.content("是否数据缓存到本地,待网络正常后，自动缓存数据?").show();
        dialog.setOnBtnClickL(null, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                saveCacheData();
            }
        });
    }

    //保存缓存数据
    public void saveCacheData() {
        ToastUtils.showLongCustomToast("保存数据到缓存中，并自动上传缓存数据");
        DStep3Bean bean = new DStep3Bean();
        bean.setUserId(SPUtils.getID());
        bean.setCompanyId(SPUtils.getCompanyId());
        bean.setCount(count3);
        bean.setCid(cid);
        bean.setKhNm(mKhNm);
        bean.setPicList(mPicList);
        bean.setPicList2(mPicList2);
        bean.setPicList3(mPicList3);
        bean.setXxjh(mJsonStr);
        bean.setTime(MyTimeUtils.getNowTime());
        MyDataUtils.getInstance().saveStep3(bean);

        Intent data = new Intent();
        data.putExtra(ConstantUtils.Intent.SUCCESS, true);
        data.putExtra(ConstantUtils.Intent.COUNT, 2);
        setResult(0, data);
        ActivityManager.getInstance().closeActivity(context);
    }

    //避免重复的token
    private String mQueryToken;

    public void doToken(String data) {
        mQueryToken = data;
    }


}
