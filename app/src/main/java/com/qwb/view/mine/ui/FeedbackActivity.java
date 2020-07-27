package com.qwb.view.mine.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.qwb.view.mine.parsent.PFeedback;
import com.qwb.utils.MyStringUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.qwb.application.MyApp;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.OtherUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MyPopWindowManager;
import com.qwb.utils.MyPopWindowManager.OnImageBack;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyGlideUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.xmsx.cnlife.widget.photo.ImagePagerActivity;
import com.chiyong.t3.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import io.reactivex.functions.Consumer;

/**
 * 意见反馈
 */
public class FeedbackActivity extends XActivity<PFeedback> implements OnImageBack {

    private InputMethodManager imm = MyApp.getImm();

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_feedback;
    }

    @Override
    public PFeedback newP() {
        return new PFeedback();
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
    }

    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    @BindView(R.id.tv_head_right)
    TextView mTvHeadRight;
    @BindView(R.id.rg_feed)
    RadioGroup mRgFeed;
    @BindView(R.id.et_feed)
    EditText mEtFeed;
    private String feedType = "1";
    private void initUI() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mTvHeadTitle.setText("意见反馈");
        mTvHeadRight.setText("提交");
        mRgFeed.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //feedType 反馈类型 1 异常问题 2 意见改进
                switch (checkedId) {
                    case R.id.rb_yichang:
                        feedType = "1";
                        break;
                    case R.id.rb_yijian:
                        feedType = "2";
                        break;

                }
            }
        });

        gv_pic = findViewById(R.id.gv_pic);
        gv_pic.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long arg3) {
                if (isDelModel) {
                    if (Constans.publish_pics.size() > 0) {

                        if (position == Constans.publish_pics.size()) {
                            addPic(view);
                        } else if (position == Constans.publish_pics.size() + 1) {
                            isDelModel = false;
                            refreshAdapter();
                        } else {
                            Constans.publish_pics.remove(position);
                            selImageList.remove(position);
                            refreshAdapter();
                        }
                    }
                } else {

                    if (Constans.publish_pics.size() <= 0) {
                        addPic(view);
                    } else {
                        if (Constans.publish_pics.size() == position) {
                            addPic(view);
                        } else if (Constans.publish_pics.size() + 1 == position) {
                            isDelModel = true;
                            refreshAdapter();
                        } else {

                            String[] urls = new String[Constans.publish_pics.size()];
                            for (int i = 0; i < Constans.publish_pics.size(); i++) {
                                urls[i] = "file://" + Constans.publish_pics.get(i);
                            }

                            Intent intent = new Intent(FeedbackActivity.this, ImagePagerActivity.class);
                            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
                            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
        refreshAdapter();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //图片选择器-添加图片返回
        if (data != null && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (images != null) {
                selImageList.addAll(images);
                Constans.publish_pics.clear();
                for (int i = 0; i < selImageList.size(); i++) {
                    Constans.publish_pics.add(selImageList.get(i).path);
                }
                //相机-记录拍照的集合（要删除拍照图片）
                if (requestCode == Constans.TAKE_PIC_XJ) {
                    for (int j = 0; j < images.size(); j++) {
                        Constans.publish_pics_xj.add(images.get(j).path);
                    }
                }
                refreshAdapter();
            }
        }
    }

    /**
     * 提交
     * feedType 反馈类型 1 异常问题 2 意见改进
     */
    private void addData() {
        String input = mEtFeed.getText().toString().trim();
        if (MyStringUtil.isEmpty(input)) {
            ToastUtils.showCustomToast("请填写内容描述");
            return;
        }
        getP().addData(context, feedType, input);
    }


    private void addPic(View view) {
        isDelModel = false;
        refreshAdapter();
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
        }
        Constans.pic_tag = 3;
        Constans.current.clear();
        if (Constans.publish_pics.size() > 0) {
            Constans.current.addAll(Constans.publish_pics);
        }
        MyPopWindowManager.getI().show(this, this, "", "");

    }


    private String paths;
    private MyAdapter adapter;

    private void refreshAdapter() {

        if (Constans.publish_pics.size() <= 0) {
            isDelModel = false;
        }

        if (adapter == null) {
            adapter = new MyAdapter();
            gv_pic.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private boolean isDelModel;

    private GridView gv_pic;
    private EditText et_jianyi;


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (Constans.publish_pics.size() > 0) {
                return Constans.publish_pics.size() + 2;
            }
            return 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }


        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View inflate = getLayoutInflater().inflate(R.layout.x_adapter_sp_pic, null);
            ImageView iv_simple = (ImageView) inflate.findViewById(R.id.iv_simple);
            ImageView iv_delpic = (ImageView) inflate.findViewById(R.id.iv_delpic);


            if (Constans.publish_pics.size() <= 0) {

                iv_simple.setImageDrawable(getResources().getDrawable(R.drawable.ckin_addpic));
            } else {
                if (position == Constans.publish_pics.size()) {
                    iv_simple.setImageDrawable(getResources().getDrawable(R.drawable.ckin_addpic));
                } else if (position == Constans.publish_pics.size() + 1) {
                    iv_simple.setImageDrawable(getResources().getDrawable(R.drawable.delpic));
                } else {
                    if (isDelModel) {
                        iv_delpic.setVisibility(View.VISIBLE);
                    } else {
                        iv_delpic.setVisibility(View.GONE);
                    }
                    String sourcePath = Constans.publish_pics.get(position);
                    iv_simple.setTag(sourcePath);
                    MyGlideUtil.getInstance().displayImageSquere("file://" + sourcePath, iv_simple);
                }
            }
            return inflate;
        }

    }

    @OnClick({R.id.head_left, R.id.head_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                ActivityManager.getInstance().closeActivity(context);
                break;
            case R.id.head_right:
                addData();
                break;
        }
    }

    public void doAddSuccess() {
        ActivityManager.getInstance().closeActivity(context);
    }

    private ArrayList<ImageItem> selImageList = new ArrayList<>(); //当前选择的所有图片

    @Override
    public void fromCamera() {
        new RxPermissions(FeedbackActivity.this)
                .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {//TODO 许可
                            ImagePicker.getInstance().setSelectLimit(Constans.maxImgCount - selImageList.size());
                            ImagePicker.getInstance().setCrop(false);
                            ImagePicker.getInstance().setMultiMode(true);
                            Intent intent = new Intent(FeedbackActivity.this, ImageGridActivity.class);
                            intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                            startActivityForResult(intent, Constans.TAKE_PIC_XJ);
                        } else {
                            //TODO 未许可
                            ToastUtils.showCustomToast("权限未开启！");
                        }
                    }
                });
    }

    @Override
    public void fromPhotoAlbum() {
        //打开选择,本次允许选择的数量
        ImagePicker.getInstance().setSelectLimit(Constans.maxImgCount - selImageList.size());
        ImagePicker.getInstance().setCrop(false);
        ImagePicker.getInstance().setMultiMode(true);
        Intent intent1 = new Intent(FeedbackActivity.this, ImageGridActivity.class);
        startActivityForResult(intent1, Constans.TAKE_PIC_XC);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OtherUtils.resetStepStatus(context);//重置默认状态,删除图片
    }


}
