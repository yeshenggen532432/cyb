package com.qwb.view.company.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.company.model.CompanysBean;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.company.adapter.MyCompanyAdapter;
import com.qwb.view.company.model.SearchCompanyBean;
import com.qwb.view.company.parsent.PMyCompany;
import com.qwb.utils.MyKeyboardUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
import com.xmsx.cnlife.view.widget.MyTextChanged;
import com.chiyong.t3.R;
import com.zyyoona7.lib.EasyPopup;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 我的单位（我的公司）
 * 1.申请加入公司   2.创建公司
 */
public class MyCompanyActivity extends XActivity<PMyCompany> {
    @Override
    public int getLayoutId() {
        return R.layout.x_activity_my_company;
    }

    @Override
    public PMyCompany newP() {
        return new PMyCompany();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
        createPopup();
    }

    @BindView(R.id.parent)
    View parent;
    private void initUI() {
        initHead();
        initAdapter();
        iitAdapterData();
    }

    //头部
    @BindView(R.id.head_left)
    View mHeadLeft;
    @BindView(R.id.head_right)
    View mHeadRight;
    @BindView(R.id.head_right2)
    View mHeadRight2;
    @BindView(R.id.head_right3)
    View mHeadRight3;
    @BindView(R.id.iv_head_right2)
    ImageView mIvHeadRight2;
    @BindView(R.id.iv_head_right3)
    ImageView mIvHeadRight3;
    @BindView(R.id.tv_head_right)
    TextView mTvHeadRight;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTvHeadTitle.setText("我的单位");
    }

    //初始化适配器
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    MyCompanyAdapter mAdapter;
    private void initAdapter() {
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
//        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
//                .colorResId(R.color.gray_e)
//                .sizeResId(R.dimen.dp_0_5)
//                .build());
        mAdapter = new MyCompanyAdapter(context);
        //尾部：入职申请，创建公司
        View footer = LayoutInflater.from(context).inflate(R.layout.x_layout_my_company_footer, null);
        footer.findViewById(R.id.footer_sb_join_company).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopTvTel.setText(""+SPUtils.getSValues(ConstantUtils.Sp.USER_MOBILE));
                mEasyPop.showAtLocation(parent, Gravity.CENTER, 0, 0);
            }
        });
        footer.findViewById(R.id.footer_sb_register_company).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().jumpActivity(context, RegisterCompanyActivity.class);
            }
        });
        mAdapter.addFooterView(footer);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void iitAdapterData() {
        String companys = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_S);
        if (!TextUtils.isEmpty(companys)) {
            List<CompanysBean> companyList = JSON.parseArray(companys, CompanysBean.class);
            if (companyList != null && companyList.size() > 0) {
                mAdapter.setNewData(companyList);
            }
        }
    }

    /**
     * 申请加入公司
     */
    private EasyPopup mEasyPop;
    MyCompanyAdapter mAdapterPop;
    RecyclerView mPopRv;
    TextView mPopTvTel;
    CompanysBean mCurrentBean;
    public void createPopup() {
        mEasyPop = new EasyPopup(context)
                .setContentView(R.layout.x_popup_my_company)
                .createPopup();
        final EditText popEtSearch = mEasyPop.getView(R.id.pop_et_search);
        TextView popTvSearch = mEasyPop.getView(R.id.pop_tv_search);
        mPopTvTel = mEasyPop.getView(R.id.pop_tv_tel);
        final TextView popTvCompanyName = mEasyPop.getView(R.id.pop_tv_company_name);
        mPopRv = mEasyPop.getView(R.id.pop_recyclerView);

        mPopRv.setHasFixedSize(false);
        mPopRv.setLayoutManager(new LinearLayoutManager(context));
        mAdapterPop = new MyCompanyAdapter(context);
        mPopRv.setAdapter(mAdapterPop);
        mAdapterPop.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    mCurrentBean = (CompanysBean) adapter.getData().get(position);
                    popTvCompanyName.setText(mCurrentBean.getCompanyName());
                    mPopRv.setVisibility(View.GONE);
                }catch (Exception e){
                    ToastUtils.showError(e);
                }
            }
        });
        popEtSearch.addTextChangedListener(new MyTextChanged() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s = charSequence.toString();
                if(MyStringUtil.isEmpty(s)){
                    mPopRv.setVisibility(View.GONE);
                }
            }
        });
        popTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyKeyboardUtil.closeKeyboard(context);//强制关闭软键盘
                String searchStr = popEtSearch.getText().toString().trim();
                if(!MyStringUtil.isEmpty(searchStr)){
                    getP().queryCompany(context, searchStr);
                }
            }
        });
        mEasyPop.getView(R.id.popup_tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentBean == null){
                    ToastUtils.showCustomToast("请先搜索要申请的公司");
                    return;
                }
                MyKeyboardUtil.closeKeyboard(context);//强制关闭软键盘
                getP().addJoinCompany(context, mCurrentBean.getCompanyId());
            }
        });
    }

    public void refreshAdapterPop(List<SearchCompanyBean.Company> dataList){
        List<CompanysBean> data = new ArrayList<>();
        if(dataList!=null && dataList.size()>0){
            for (SearchCompanyBean.Company company: dataList) {
                CompanysBean bean= new CompanysBean();
                bean.setCompanyId(company.getDeptId());
                bean.setCompanyName(company.getDeptNm());
                data.add(bean);
            }
        }
        mAdapterPop.setNewData(data);
        mPopRv.setVisibility(View.VISIBLE);
    }

    public void addSuccess(){
        mEasyPop.dismiss();
    }


}
