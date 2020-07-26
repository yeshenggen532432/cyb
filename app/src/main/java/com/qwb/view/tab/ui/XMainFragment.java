package com.qwb.view.tab.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.scanlibrary.ScanActivity;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.qwb.common.ScanTypeEnum;
import com.qwb.view.tab.ui.main.XGztFragment3;
import com.qwb.view.tab.ui.main.XYunFragment3;
import com.qwb.event.ChangeCompanyEvent;
import com.qwb.event.CreateCompanyEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.company.model.CompanysBean;
import com.qwb.view.base.model.ScanBean;
import com.qwb.utils.SPUtils;
import com.qwb.view.common.model.TabEntity;
import com.qwb.view.tab.parsent.PXMain;
import com.qwb.view.base.ui.XApplyActivity;
import com.qwb.utils.MyStringUtil;
import com.xmsx.qiweibao.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.base.XFragmentAdapter;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import io.reactivex.functions.Consumer;

/**
 * tab:首页
 */
public class XMainFragment extends XFragment<PXMain> {

    private String[] mTitles = {"工作台", "应用模块"};
    /*未选择时的icon*/
    private int[] mIconUnselectIds = {R.mipmap.home_gzt_g, R.mipmap.home_yun_g};
    /*选择时的icon*/
    private int[] mIconSelectIds = {R.mipmap.home_gzt_b, R.mipmap.home_yun_b};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    public XMainFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.x_fragment_main;
    }

    @Override
    public PXMain newP() {
        return new PXMain();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    private void initEvent() {
        //切换公司
        BusProvider.getBus().toFlowable(CreateCompanyEvent.class)
                .subscribe(new Consumer<CreateCompanyEvent>() {
                    @Override
                    public void accept(CreateCompanyEvent event) throws Exception {
                        if (event.getTag() == ConstantUtils.Event.TAG_CREATE_COMPANY) {
                            //重新登录，重新获取应用列表
                            getP().queryDataLogin(context, SPUtils.getSValues(ConstantUtils.Sp.USER_MOBILE), SPUtils.getSValues(ConstantUtils.Sp.PASSWORD));
                        }
                    }
                });
        //切换公司
        BusProvider.getBus().toFlowable(ChangeCompanyEvent.class)
                .subscribe(new Consumer<ChangeCompanyEvent>() {
                    @Override
                    public void accept(ChangeCompanyEvent event) throws Exception {
                        //备注：改变公司名称；应用列表
                        doCompany();
                    }
                });
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initEvent();
        initUI();
    }

    @BindView(R.id.commonTabLayout)
    CommonTabLayout mCommonTabLayout;
    @BindView(R.id.vp)
    ViewPager mViewPager;

    public void initUI() {
        initHead();
        initTab();
        initViewPager();
    }

    @BindView(R.id.tv_company_name)
    TextView mTvCompanyName;

    private void initHead() {
        doCompany();
    }


    XGztFragment3 gztFragment3 = null;
    XYunFragment3 yunFragment3 = null;

    private void initTab() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mCommonTabLayout.setTabData(mTabEntities);

        if (null == gztFragment3) {
            gztFragment3 = new XGztFragment3();
        }
        if (null == yunFragment3) {
            yunFragment3 = new XYunFragment3();
        }
        mFragments.add(gztFragment3);
        mFragments.add(yunFragment3);

        mCommonTabLayout.setTabData(mTabEntities);
        mCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {
                }
            }
        });
    }

    private void initViewPager() {
        mViewPager.setAdapter(new XFragmentAdapter(getChildFragmentManager(), mFragments, mTitles));
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCommonTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 点击事件
     */
    @OnClick({R.id.layout_company, R.id.layout_span, R.id.layout_apply})
    public void onClickView(View v) {
        switch (v.getId()) {
            //切换公司
            case R.id.layout_company:
                String companys = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_S);
                if (!MyStringUtil.isEmpty(companys)) {
                    List<CompanysBean> companyList = JSON.parseArray(companys, CompanysBean.class);
                    if (companyList != null && companyList.size() > 1) {
                        //多公司才弹出
                        showDialogChangeCompany();
                    }
                }
                break;
            //扫一扫
            case R.id.layout_span:
                scan();
                break;
            //编辑应用
            case R.id.layout_apply:
                ActivityManager.getInstance().jumpActivity(context, XApplyActivity.class);
                break;
        }
    }

    //TODO*******************************切换公司:start***********************************
    //处理：公司名称；公司列表数据(dialog用到)
    private void doCompany() {
        String companys = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_S);
        String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
        mTvCompanyName.setText("直购猫");
        if (!TextUtils.isEmpty(companys) && !TextUtils.isEmpty(companyId)) {
            List<CompanysBean> companyList = JSON.parseArray(companys, CompanysBean.class);
            if (companyList != null && companyList.size() > 0) {
                baseItems.clear();
                for (CompanysBean bean : companyList) {
                    DialogMenuItem item = new DialogMenuItem(bean.getCompanyName(), bean.getCompanyId());
                    baseItems.add(item);
                    if (companyId.equals(String.valueOf(bean.getCompanyId()))) {
                        String companyName = bean.getCompanyName();
                        SPUtils.setValues(ConstantUtils.Sp.COMPANY_NAME, companyName);
                        mTvCompanyName.setText(companyName);
                    }
                }
            }
        }
    }

    //dialog:切换公司
    private ArrayList<DialogMenuItem> baseItems = new ArrayList<>();

    private void showDialogChangeCompany() {
        NormalListDialog dialog = new NormalListDialog(context, baseItems);
        dialog.title("切换公司")
                .show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                String loginBaseUrl = SPUtils.getSValues(ConstantUtils.Sp.LOGIN_BASE_URL);
                if (MyStringUtil.isNotEmpty(loginBaseUrl)) {
                    getP().queryJwt(context, loginBaseUrl, String.valueOf(baseItems.get(position).mResId));
                } else {
                    getP().queryDataChangeCompany(context, String.valueOf(baseItems.get(position).mResId));
                }
            }
        });
    }

    //TODO*******************************切换公司:start***********************************


    //TODO******************************扫描:start************************************
    //扫描
    public void scan() {
//        ActivityManager.getInstance().jumpScanActivity(context, false);
        //备注：不要采用上面方法跳转：不然Fragment中onActivityResult回调不了
        Intent intent = new Intent(context,ScanActivity.class);
        intent.putExtra(ScanActivity.EXTRAS_MULTIPLE, false);
        startActivityForResult(intent, ConstantUtils.Intent.REQUEST_CODE_SCAN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == context.RESULT_OK){
                //扫描二维码/条码回传
                if (data != null && requestCode == ConstantUtils.Intent.REQUEST_CODE_SCAN) {
                    String result = data.getStringExtra(ScanActivity.SCAN_RESULT);
                    if (MyStringUtil.isNotEmpty(result)) {
                        ScanBean scanBean = JSON.parseObject(result, ScanBean.class);
                        dialogNormalStyleScan(scanBean);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * dialog-扫描二维码
     */
    private void dialogNormalStyleScan(final ScanBean scanBean) {
        String content = "";
        String btnText = "确定";
        if (MyStringUtil.eq(ScanTypeEnum.FOLLOW.getType(), scanBean.getType())) {
            content = "是否关注:" + scanBean.getCompanyName() + "?";
            btnText = "关注";
        } else if (MyStringUtil.eq(ScanTypeEnum.LOGIN.getType(), scanBean.getType())) {
            content = "是否授权登录？";
            btnText = "授权登录";
            getP().doScaned(context, scanBean.getTicket());
        }
        NormalDialog dialog = new NormalDialog(context);
        dialog.setCanceledOnTouchOutside(false);//外部点击不消失
        dialog.content(content)
                .btnText(btnText)
                .show();
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                if (MyStringUtil.eq(ScanTypeEnum.LOGIN.getType(), scanBean.getType())) {
                    getP().doScanLogin(context, scanBean.getTicket(), 1);
                }
            }
        }, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                getP().doScan(context, scanBean);
            }
        });
    }

}
