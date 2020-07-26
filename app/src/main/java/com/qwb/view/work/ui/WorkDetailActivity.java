package com.qwb.view.work.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.qwb.utils.MyCollectionUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.work.parsent.PWorkDetail;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.work.model.WorkDetailBean;
import com.qwb.view.work.model.WorkDetailBean.PicList;
import com.qwb.utils.MyGlideUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.xmsx.cnlife.widget.ComputeHeightGridView;
import com.qwb.utils.Constans;
import com.xmsx.qiweibao.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 考勤明细
 */
public class WorkDetailActivity extends XActivity<PWorkDetail> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_work_detail;
    }

    @Override
    public PWorkDetail newP() {
        return new PWorkDetail();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        intiUI();
        getP().queryData(context, id);
    }

    private String id;

    private void initIntent() {
        Intent intent = getIntent();
        id = intent.getStringExtra(ConstantUtils.Intent.ID);
    }

    private void intiUI() {
        initHead();
        initOther();
        initAdapterPic();
    }

    @BindView(R.id.qiandao_gzneirong)
    TextView mTvWorkStatus;
    @BindView(R.id.sv_content)
    ScrollView mSvContent;
    @BindView(R.id.qiandao_didian)
    TextView mTvAddress;
    @BindView(R.id.tv_title)
    TextView mTvMemberName;
    @BindView(R.id.qiandao_time)
    TextView mTvTime;
    @BindView(R.id.tv_bz)
    TextView mTvRemarks;

    private void initOther() {
        mTvAddress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().jumpActivityNavMap(context, currentBean.getLatitude(), currentBean.getLongitude(), currentBean.getLocation());
            }
        });
    }

    @BindView(R.id.qiandaomingxi_gridview)
    ComputeHeightGridView mGridView;
    private ArrayList<PicList> picList = new ArrayList<>();
    private MyAdapter mAdapter;
    private String[] urls;
    private WorkDetailBean currentBean;

    private void initAdapterPic() {
        mAdapter = new MyAdapter();
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ActivityManager.getInstance().zoomPic(context, urls, position);
            }
        });
    }

    @BindView(R.id.head_left)
    View mViewLeft;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;

    private void initHead() {
        MyStatusBarUtil.getInstance().setColorBlue(context);
        mTvHeadTitle.setText("考勤详情");
        mViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return picList.size();
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
            PicList pic = picList.get(position);
            View inflate = getLayoutInflater().inflate(R.layout.x_adapter_sp_pic, null);
            ImageView imageview = inflate.findViewById(R.id.iv_simple);
            MyGlideUtil.getInstance().displayImageSquere(Constans.IMGROOTHOST + pic.getPicMini(), imageview);
            return inflate;
        }
    }


    public void doUI(WorkDetailBean bean) {
        currentBean = bean;
        mTvRemarks.setText(bean.getRemark());
        mTvWorkStatus.setText(bean.getTp());
        mTvAddress.setText(bean.getLocation());
        if (MyStringUtil.isNotEmpty(bean.getLongitude()) && MyStringUtil.isNotEmpty(bean.getLatitude())) {
            mTvAddress.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_dw, 0, 0, 0);
        }
        mTvTime.setText(bean.getCheckTime());
        mTvMemberName.setText(bean.getMemberNm());

        //图片
        List<WorkDetailBean.PicList> pic = bean.getPicList();
        if (MyCollectionUtil.isNotEmpty(pic)) {
            urls = new String[pic.size()];
            for (int i = 0; i < pic.size(); i++) {
                urls[i] = Constans.IMGROOTHOST + pic.get(i).getPic();
            }
        }
        picList.addAll(pic);
        mAdapter.notifyDataSetChanged();
        mSvContent.post(new Runnable() {
            @Override
            public void run() {
                mSvContent.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

}
