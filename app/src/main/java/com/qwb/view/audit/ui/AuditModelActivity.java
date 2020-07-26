package com.qwb.view.audit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.qwb.widget.MyDatePickerDialog;
import com.qwb.event.ObjectEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.MyOnImageBack;
import com.qwb.view.audit.adapter.AuditPersonCommonAdapter;
import com.qwb.listener.OnNoMoreClickListener;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.member.model.MemberListBean;
import com.qwb.view.audit.model.AccountListBean;
import com.qwb.view.audit.model.AuditModelBean;
import com.qwb.view.audit.model.AuditZdyBean;
import com.qwb.view.audit.parsent.PAuditModel;
import com.qwb.view.member.model.BuMenListBean.MemberBean;
import com.qwb.view.object.ui.ChooseObjectActivity;
import com.qwb.utils.MyStringUtil;
import com.qwb.widget.treedialog.MyTreeDialog;
import com.xmsx.cnlife.widget.ComputeHeightGridView;
import com.xmsx.cnlife.widget.ComputeHeightListView;
import com.qwb.utils.OtherUtils;
import com.qwb.view.audit.adapter.FuJianAdapter;
import com.qwb.view.audit.adapter.ShenPi_PicAdatper;
import com.qwb.utils.Constans;
import com.qwb.utils.MyPopWindowManager;
import com.qwb.utils.ToastUtils;
import com.qwb.view.file.ui.ChooseFileActivity;
import com.qwb.view.file.model.FileBean;
import com.xmsx.qiweibao.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import io.reactivex.functions.Consumer;

/**
 * 审批模块：提交审批自定义模块
 */
public class AuditModelActivity extends XActivity<PAuditModel> {

    private int mAddMemberType;//1：审批人 2：最终审批人 3：执行人

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_audit_model;
    }

    @Override
    public PAuditModel newP() {
        return new PAuditModel();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    ObjectEvent mCurrentObjectEvent;

    @Override
    public void bindEvent() {
        super.bindEvent();
        BusProvider.getBus().toFlowable(ObjectEvent.class)
                .subscribe(new Consumer<ObjectEvent>() {
                    @Override
                    public void accept(ObjectEvent event) throws Exception {
                        if (event.getTag() == ConstantUtils.Event.TAG_OBJECT && event != null) {
                            ToastUtils.showCustomToast(event.getName());
                            mTvObject.setText(event.getName());
                            mCurrentObjectEvent = event;
                        }
                    }
                });
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initUI();
        doIntent();
        doUI(mAuditModel);
        getP().queryToken(null);
        getP().queryAuditZdyListByModelId(context, mAuditModel.getId());
    }

    private AuditModelBean mAuditModel;
    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            mAuditModel = (AuditModelBean) intent.getSerializableExtra(ConstantUtils.Intent.SHENPI_MODEL);
        }
    }

    private void doIntent() {
    }

    private void initUI() {
        initHead();
        initOther();
        initAdapterFile();
        initAdapterPic();
    }

    /**
     * 头部
     */
    private TextView tv_head_right;
    private TextView tv_head_title;

    private void initHead() {
        ImageView img_head_back = findViewById(R.id.iv_head_back);
        tv_head_title = findViewById(R.id.tv_head_title);
        tv_head_right = findViewById(R.id.tv_head_right);
        ImageView img_head_right = findViewById(R.id.img_head_right);
        tv_head_right.setText("提交");
        img_head_right.setVisibility(View.GONE);
        img_head_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        tv_head_right.setOnClickListener(new OnNoMoreClickListener() {
            @Override
            protected void OnMoreClick(View view) {
                addData();
            }
        });
    }

    @BindView(R.id.view_type)
    View mViewType;
    @BindView(R.id.view_amount)
    View mViewAmount;
    @BindView(R.id.rl_startTime)
    RelativeLayout mViewStartTime;
    @BindView(R.id.rl_endTime)
    RelativeLayout mViewEndTime;
    @BindView(R.id.rl_detail)
    RelativeLayout mViewDetail;
    @BindView(R.id.rl_remo)
    RelativeLayout mViewRemarks;
    @BindView(R.id.view_object)
    View mViewObject;
    @BindView(R.id.view_account)
    View mViewAccount;
    @BindView(R.id.btn_xiugai)
    View mBtnUpdate;
    @BindView(R.id.tv_startTime)
    TextView mTvStartTime;
    @BindView(R.id.tv_endTime)
    TextView mTvEndTime;
    @BindView(R.id.edit_remo)
    EditText mEtRemarks;
    @BindView(R.id.edit_amount)
    EditText mEtAmount;
    @BindView(R.id.edit_detail)
    EditText mEtDetail;
    @BindView(R.id.edit_type)
    EditText mEtType;
    @BindView(R.id.edit_title)
    EditText mEtTitle;
    @BindView(R.id.view_audit_zdy)
    View mViewAuditZdy;
    @BindView(R.id.tv_audit_zdy_name)
    TextView mTvAuditZdyName;
    @BindView(R.id.view_show_hide)
    View mViewShowHide;
    @BindView(R.id.iv_show_hide)
    ImageView mIvShowHide;

    private void initOther() {
        // 开始时间
        mTvStartTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDate(1);
            }
        });
        // 结束时间
        mTvEndTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDate(2);
            }
        });
        // 修改
        mBtnUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShenpiModelActivity.class);
                intent.putExtra(ConstantUtils.Intent.TYPE, 2);// 1:添加 2：修改
                intent.putExtra(ConstantUtils.Intent.SHENPI_MODEL, mAuditModel);
                startActivityForResult(intent, 222);
            }
        });
        // 选择对象
        mViewObject.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().jumpActivity(context, ChooseObjectActivity.class);
            }
        });
        // 账户列表
        mViewAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAccountList != null && mAccountList.size() > 0) {
                    showDialogAccount(mAccountList);
                } else {
                    getP().queryAccountList(context);
                }
            }
        });
        // 审批流
        mViewAuditZdy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowNormalAudit = false;
                if (mAuditZdyList != null && mAuditZdyList.size() > 0) {
                    showDialogAuditZdy(mAuditZdyList);
                } else {
                    getP().queryAuditZdyListByModelId(context, mAuditModel.getId());
                }
            }
        });
        // 显示和隐藏
        mIvShowHide.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewShowHide.getVisibility();
                if (mViewShowHide.getVisibility() == View.VISIBLE) {
                    mViewShowHide.setVisibility(View.GONE);
                    mIvShowHide.setImageResource(R.mipmap.ic_arrow_down_gray6);
                } else {
                    mViewShowHide.setVisibility(View.VISIBLE);
                    mIvShowHide.setImageResource(R.mipmap.ic_arrow_up_gray6);
                }
            }
        });
    }

    /**
     * 附件
     */
    ComputeHeightListView mLvFile;
    private FuJianAdapter mAdapterFile;
    private ArrayList<FileBean> mFileList = new ArrayList<>();
    private void initAdapterFile() {
        findViewById(R.id.img_fujian).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChooseFileActivity.class);
                intent.putExtra(ConstantUtils.Intent.IS_SINGLE, false);// 是否单选，true:单选，false：多选
                startActivityForResult(intent, Constans.TAKE_PIC_YUNPAN);
            }
        });
        mLvFile = findViewById(R.id.listview_fujian);
        mAdapterFile = new FuJianAdapter(context, mFileList, true);
        mLvFile.setAdapter(mAdapterFile);
    }

    /**
     * 审批模板赋值
     */
    public void doUI(AuditModelBean auditModelBean) {
        if (auditModelBean != null) {
            tv_head_title.setText(auditModelBean.getName());
            String tp = auditModelBean.getTp();
//            if (tp.indexOf("1") != -1) {
//                mViewType.setVisibility(View.VISIBLE);
//            } else {
//                mViewType.setVisibility(View.GONE);
//            }
            mViewType.setVisibility(View.GONE);
            if (tp.indexOf("2") != -1) {
                mViewStartTime.setVisibility(View.VISIBLE);
                mViewEndTime.setVisibility(View.VISIBLE);
            } else {
                mViewStartTime.setVisibility(View.GONE);
                mViewEndTime.setVisibility(View.GONE);
            }
            if (tp.indexOf("3") != -1) {
                mViewDetail.setVisibility(View.VISIBLE);
            } else {
                mViewDetail.setVisibility(View.GONE);
            }
            if (tp.indexOf("4") != -1) {
                mViewRemarks.setVisibility(View.VISIBLE);
            } else {
                mViewRemarks.setVisibility(View.GONE);
            }
            if (tp.indexOf("5") != -1) {
                mViewAmount.setVisibility(View.VISIBLE);
            } else {
                mViewAmount.setVisibility(View.GONE);
            }
            if (tp.indexOf("6") != -1) {
                mViewObject.setVisibility(View.VISIBLE);
            } else {
                mViewObject.setVisibility(View.GONE);
            }
            if (tp.indexOf("7") != -1) {
                mViewAccount.setVisibility(View.VISIBLE);
            } else {
                mViewAccount.setVisibility(View.GONE);
            }

            mBtnUpdate.setVisibility(View.GONE);

            initAdapterAudit();
            initAdapterApprover();
            initAdapterExec();

            doAuditModel(auditModelBean);
        }
    }

    /**
     * 审批流赋值
     */
    private AuditZdyBean mAuditZdy;
    public void doAuditZdy(AuditZdyBean bean) {
        this.mAuditZdy = bean;
        //付款账户
        Integer accountId = bean.getAccountId();
        if (accountId != null) {
            AccountListBean.AccountBean accountBean = new AccountListBean.AccountBean();
            accountBean.setId(accountId);
            accountBean.setAccName(bean.getAccountName());
            mCurrentAccountBean = accountBean;
            mTvAccount.setText(bean.getAccountName());
        }

        mAuditList.clear();
        if (bean.getMlist() != null && !bean.getMlist().isEmpty()) {
            mAuditList.addAll(bean.getMlist());
        }
        mApproverList.clear();
        if (bean.getApprover() != null) {
            mApproverList.add(bean.getApprover());
        }
        mExecList.clear();
        if (bean.getExecList() != null && !bean.getExecList().isEmpty()) {
            mExecList.addAll(bean.getExecList());
        }
        doAdapterAudit();
        doAdapterApprover();
        doAdapterExec();
    }

    /**
     * 审批人
     */
    AuditPersonCommonAdapter mAdapterAudit;
    boolean mIsEditAudit = true;
    boolean mIsDelAudit;
    @BindView(R.id.gv_person)
    ComputeHeightGridView mGridViewAudit;
    private List<MemberBean> mAuditList = new ArrayList<>();
    public void initAdapterAudit() {
        mAdapterAudit = new AuditPersonCommonAdapter(context, mAuditList, mIsEditAudit);
        mGridViewAudit.setAdapter(mAdapterAudit);
        mGridViewAudit.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!mIsEditAudit) {
                    return;
                }
                mAddMemberType = 1;
                int count = parent.getAdapter().getCount();
                if (count >= 3) {
                    if (position == count - 2) {
                        // 倒数第二个item:添加
                        doAddMember();
                    } else if (position == count - 1) {
                        // 倒数第一个item：删除
                        mIsDelAudit = !mIsDelAudit;
                        mAdapterAudit.refreshAdapter(mIsDelAudit);
                    } else {
                        if (mIsDelAudit) {
                            mAdapterAudit.removeItem(position);
                        }
                    }
                } else {
                    doAddMember();
                }
            }
        });
    }

    public void doAdapterAudit(){
        if(mAuditZdy != null && mAdapterAudit != null){
            Integer isUpdateAudit = mAuditZdy.getIsUpdateAudit();
            if (isUpdateAudit != null && isUpdateAudit == 0) {
                mIsEditAudit = false;
            }
            mAdapterAudit.setEdit(mIsEditAudit);
        }
        if(mAdapterAudit != null){
            mAdapterAudit.notifyDataSetChanged();
        }
    }

    /**
     * 最终审批人
     */
    @BindView(R.id.gv_approver)
    ComputeHeightGridView mGridViewApprover;
    @BindView(R.id.view_approver)
    View mViewApprover;
    private List<MemberBean> mApproverList = new ArrayList<>();
    private AuditPersonCommonAdapter mAdapterApprover;
    boolean mIsEditApprover = true;
    boolean mIsDelApprover;
    public void initAdapterApprover() {
        mViewApprover.setVisibility(View.VISIBLE);
        mAdapterApprover = new AuditPersonCommonAdapter(context, mApproverList, mIsEditApprover);
        mGridViewApprover.setAdapter(mAdapterApprover);
        mGridViewApprover.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!mIsEditApprover) {
                    return;
                }
                mAddMemberType = 2;
                int count = parent.getAdapter().getCount();
                if (count >= 3) {
                    if (position == count - 2) {
                        // 倒数第二个item:添加
                        doAddMember();
                    } else if (position == count - 1) {
                        // 倒数第一个item：删除
                        mIsDelApprover = !mIsDelApprover;
                        mAdapterApprover.refreshAdapter(mIsDelApprover);
                    } else {
                        if (mIsDelApprover) {
                            mAdapterApprover.removeItem(position);
                        }
                    }
                } else {
                    doAddMember();
                }
            }
        });
    }

    public void doAdapterApprover(){
        if(mAuditZdy != null){
            Integer isUpdateApprover = mAuditZdy.getIsUpdateApprover();
            if (isUpdateApprover != null && isUpdateApprover == 0) {
                mIsEditApprover = false;
            }
            mAdapterApprover.setEdit(mIsEditApprover);
        }
        if(mAdapterApprover != null){
            mAdapterApprover.notifyDataSetChanged();
        }
    }

    /**
     * 执行人
     */
    @BindView(R.id.view_exec)
    View mViewExec;
    @BindView(R.id.gv_exec_person)
    ComputeHeightGridView mGridViewExec;
    private List<MemberBean> mExecList = new ArrayList<>();
    private boolean mIsDelExec;
    AuditPersonCommonAdapter mAdapterExec;
    private void initAdapterExec() {
        mViewExec.setVisibility(View.VISIBLE);
        mAdapterExec = new AuditPersonCommonAdapter(context, mExecList, true);
        mGridViewExec.setAdapter(mAdapterExec);
        mGridViewExec.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                mAddMemberType = 3;
                int count = arg0.getAdapter().getCount();
                if (count >= 3) {
                    if (position == count - 2) {
                        doAddMember();
                    } else if (position == count - 1) {
                        // 倒数第一个item：删除
                        mIsDelExec = !mIsDelExec;
                        mAdapterExec.refreshAdapter(mIsDelExec);
                    } else {
                        if (mIsDelExec) {
                            mAdapterExec.removeItem(position);
                        }
                    }
                } else {
                    doAddMember();
                }
            }
        });
    }

    public void doAdapterExec(){
        if(mAdapterExec != null){
            mAdapterExec.notifyDataSetChanged();
        }
    }


    /**
     * 提交数据:这里审批人是上面传的（固定的），不是手动再添加
     */
    private void addData() {
        try {
            final String title = mEtTitle.getText().toString().trim();// 标题
            final String type = mEtType.getText().toString();// 类型
            final String startTime = mTvStartTime.getText().toString();// 时间
            final String endTime = mTvEndTime.getText().toString();
            final String detail = mEtDetail.getText().toString().trim();// 详情
            final String remo = mEtRemarks.getText().toString().trim();// 备注
            final String amount = mEtAmount.getText().toString().trim();// 金额

            if (MyStringUtil.isEmpty(title)) {
                ToastUtils.showCustomToast("请输入标题");
                return;
            }
            // 先判断是否还有该类型（有每个都是必填：除"备注"）
            //TODO 1:类型；2时间；3：详情；4：备注；5：金额
            String tp = mAuditModel.getTp();
            if (tp.indexOf("6") != -1) {
                if (null == mCurrentObjectEvent) {
                    ToastUtils.showCustomToast("请选择对象");
                    return;
                }
            }
            if (tp.indexOf("2") != -1) {
                if (startTime.contains("必填")) {
                    ToastUtils.showCustomToast("请输入开始时间");
                    return;
                }
                if (endTime.contains("必填")) {
                    ToastUtils.showCustomToast("请输入结束时间");
                    return;
                }
            }
            if ((mAuditList == null || mAuditList.isEmpty())  && (mApproverList == null || mApproverList.isEmpty())) {
                ToastUtils.showCustomToast("审核人和最终审批人至少有一个");
                return;
            }
            String zdyId = "", zdyNm = "", isSy = "";
            if (mAuditZdy != null) {
                zdyId = "" + mAuditZdy.getId();
                zdyNm = mAuditZdy.getZdyNm();
                isSy = mAuditZdy.getIsSy();
            }
            Integer approverId = null;
            if (mApproverList != null && mApproverList.size() > 0) {
                approverId = mApproverList.get(0).getMemberId();
            }
            getP().addData(context, title, type, startTime, endTime, detail, remo, amount, getIds(mAuditList, false), zdyNm, mAuditModel.getId(), isSy,
                    mFileList, getIds(mExecList, true), approverId, mCurrentObjectEvent, mCurrentAccountBean, mQueryToken, zdyId);
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 获取审批人ids(执行人前后拼接逗号)
     */
    private String getIds(List<MemberBean> list, boolean b) {
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            sb.append(list.get(0).getMemberId());
            for (int i = 1; i < list.size(); i++) {
                sb.append("," + list.get(i).getMemberId());
            }
        }
        return sb.toString();
    }

    // 显示开始日期
    private int startYear, startMonth, startDay;
    private int endYear, endMonth, endDay;
    private void showDialogDate(final int type) {
        if (0 == startDay) {
            startYear = MyTimeUtils.getYear();
            startMonth = MyTimeUtils.getMonth();
            startDay = MyTimeUtils.getDay();
        }
        if (0 == endYear) {
            endYear = MyTimeUtils.getYear();
            endMonth = MyTimeUtils.getMonth();
            endDay = MyTimeUtils.getDay();
        }
        String title;
        int year, month, day;
        if (1 == type) {
            year = startYear;
            month = startMonth;
            day = startDay;
            title = "开始时间";
        } else {
            year = endYear;
            month = endMonth;
            day = endDay;
            title = "结束时间";
        }
        new MyDatePickerDialog(context, title, year, month, day, new MyDatePickerDialog.DateTimeListener() {
            @Override
            public void onSetTime(int year, int month, int day, String timeStr) {
                try {
                    if (1 == type) {
                        startYear = year;
                        startMonth = month;
                        startDay = day;
                        mTvStartTime.setText(timeStr);
                    } else {
                        endYear = year;
                        endMonth = month;
                        endDay = day;
                        mTvEndTime.setText(timeStr);
                    }
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }

            @Override
            public void onCancel() {
            }
        }).show();
    }

    /**
     * 初始化 添加照片
     */
    @BindView(R.id.gv_pic)
    ComputeHeightGridView mGridViewPic;
    private ShenPi_PicAdatper mAdapterPic;
    MyOnImageBack onImageBack;
    private boolean mIsDelPic;
    private void initAdapterPic() {
        if (onImageBack == null) {
            onImageBack = new MyOnImageBack(context);
        }

        mGridViewPic = findViewById(R.id.gv_pic);
        refreshAdapterPic(false);
        mGridViewPic.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                int size = Constans.publish_pics.size();
                if (size > 0) {
                    //加
                    if (position == size) {
                        if (size >= Constans.maxImgCount) {
                            ToastUtils.showCustomToast("最多只能添加6张图片");
                            return;
                        } else {
                            onImageBack.setPicNum(Constans.publish_pics.size());
                            MyPopWindowManager.getI().show(context, onImageBack, "", "");
                        }
                        //减
                    } else if (position == size + 1) {
                        mIsDelPic = !mIsDelPic;
                        refreshAdapterPic(mIsDelPic);
                    } else {
                        //图片
                        if (mIsDelPic) {
                            Constans.publish_pics.remove(position);
                            refreshAdapterPic(mIsDelPic);
                        } else {
                            String[] urls = new String[size];
                            for (int i = 0; i < size; i++) {
                                urls[i] = "file://" + Constans.publish_pics.get(i);
                            }
                            ActivityManager.getInstance().zoomPic(context, urls, position);
                        }
                    }
                } else {
                    //加
                    onImageBack.setPicNum(Constans.publish_pics.size());
                    MyPopWindowManager.getI().show(context, onImageBack, "", "");
                }
            }
        });

    }

    /**
     * 刷新图片适配器
     */
    private void refreshAdapterPic(boolean isDelModel) {
        if (mAdapterPic == null) {
            mAdapterPic = new ShenPi_PicAdatper(this);
            mGridViewPic.setAdapter(mAdapterPic);
        } else {
            mAdapterPic.refreshAdapter(isDelModel);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (Constans.TAKE_PIC_YUNPAN == requestCode) {
            // 附件
            ArrayList<FileBean> fileBeans = data.getParcelableArrayListExtra(ConstantUtils.Intent.FILE_BEAN);
            mFileList.addAll(fileBeans);
            mAdapterFile.notifyDataSetChanged();
        } else if (requestCode == 222) {
            // 修改成功返回
//            ShenpiModel shenpiModel = (ShenpiModel) data.getSerializableExtra(ConstantUtils.Intent.SHENPI_MODEL_CHUAN);
//            if (shenpiModel != null) {
//                mShenpiModel = shenpiModel;
//                doUI(mShenpiModel);
//            }
        } else if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //图片选择器
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (images != null) {
                for (int i = 0; i < images.size(); i++) {
                    Constans.publish_pics.add(images.get(i).path);
                }
                refreshAdapterPic(mIsDelPic);
            }
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        OtherUtils.resetStepStatus(context);//重置默认状态,删除图片
    }

    @BindView(R.id.tv_type_name)
    TextView mTvTypeName;
    @BindView(R.id.tv_amount_name)
    TextView mTvAmountName;
    @BindView(R.id.tv_detail_name)
    TextView mTvDetailName;
    @BindView(R.id.tv_object_name)
    TextView mTvObjectName;
    @BindView(R.id.tv_account_name)
    TextView mTvAccountName;
    @BindView(R.id.tv_object)
    TextView mTvObject;
    @BindView(R.id.tv_account)
    TextView mTvAccount;
    public void doAuditModel(AuditModelBean data) {
        if (data == null) {
            return;
        }
        if (!MyStringUtil.isEmpty(data.getTypeName())) {
            mTvTypeName.setText(data.getTypeName());
        }
        if (!MyStringUtil.isEmpty(data.getAmountName())) {
            mTvAmountName.setText(data.getAmountName());
        }
        if (!MyStringUtil.isEmpty(data.getDetailName())) {
            mTvDetailName.setText(data.getDetailName());
        }
        if (!MyStringUtil.isEmpty(data.getObjectName())) {
            mTvObjectName.setText(data.getObjectName());
        }
        if (!MyStringUtil.isEmpty(data.getAccountName())) {
            mTvAccountName.setText(data.getAccountName());
        }
    }

    /**
     * 账户列表
     */
    private List<AccountListBean.AccountBean> mAccountList = new ArrayList<>();
    private AccountListBean.AccountBean mCurrentAccountBean;

    public void showDialogAccount(List<AccountListBean.AccountBean> list) {
        if(mAccountList != null && mAccountList.isEmpty()){
            mAccountList.addAll(list);
        }
        if (mAccountList != null && mAccountList.size() > 0) {
            final ArrayList<DialogMenuItem> items = new ArrayList<>();
            for (AccountListBean.AccountBean bean : mAccountList) {
                DialogMenuItem item = new DialogMenuItem(bean.getAccName(), bean.getId());
                items.add(item);
            }
            NormalListDialog dialog = new NormalListDialog(context, items);
            dialog.show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DialogMenuItem item = items.get(position);
                    mTvAccount.setText(item.mOperName);
                    for (AccountListBean.AccountBean bean : mAccountList) {
                        if (bean.getId() == item.mResId) {
                            mCurrentAccountBean = bean;
                            return;
                        }
                    }
                }
            });
        } else {
            ToastUtils.showCustomToast("暂无数据");
        }
    }

    //避免重复的token
    private String mQueryToken;

    public void doToken(String data) {
        mQueryToken = data;
    }


    private List<TreeBean> mTreeDatas = new ArrayList<>();

    private void showDialogMember() {
        try {
            boolean multiple = true;
            Map<Integer, Integer> checkMap = new LinkedHashMap<>();
            if (mAddMemberType == 1) {
                for (MemberBean bean : mAuditList) {
                    checkMap.put(bean.getMemberId(), 1);
                }
                multiple = true;
            } else if (mAddMemberType == 2) {
                for (MemberBean bean : mApproverList) {
                    checkMap.put(bean.getMemberId(), 1);
                }
                multiple = false;
            } else if (mAddMemberType == 3) {
                for (MemberBean bean : mExecList) {
                    checkMap.put(bean.getMemberId(), 1);
                }
                multiple = true;
            }
            MyTreeDialog treeDialog = new MyTreeDialog(context, mTreeDatas, checkMap, multiple);
            treeDialog.title("选择部门,员工").show();
            treeDialog.setOnClickListener(new MyTreeDialog.OnClickListener() {
                @Override
                public void onOkListener(String checkIds, String clientTypeIds, Map<Integer, Integer> checkMap) {
                    if (mAddMemberType == 1) {
                        mAuditList.clear();
                        for (Map.Entry<Integer, Integer> entry : checkMap.entrySet()){
                            for (MemberListBean.MemberBean memberBean : mSumMemberList) {
                                if( entry.getValue() == 1 && (String.valueOf(entry.getKey()).equals("" + memberBean.getMemberId()))){
                                    MemberBean bean = new MemberBean();
                                    bean.setMemberId(memberBean.getMemberId());
                                    bean.setMemberNm(memberBean.getMemberNm());
                                    bean.setMemberHead(memberBean.getMemberHead());
                                    mAuditList.add(bean);
                                    break;
                                }
                            }
                        }
                        mAdapterAudit.refreshAdapter(mIsDelAudit, mAuditList);

                    } else if (mAddMemberType == 2) {
                        mApproverList.clear();
                        for (MemberListBean.MemberBean memberBean : mSumMemberList) {
                            if (clientTypeIds.contains("" + memberBean.getMemberId())) {
                                MemberBean bean = new MemberBean();
                                bean.setMemberId(memberBean.getMemberId());
                                bean.setMemberNm(memberBean.getMemberNm());
                                bean.setMemberHead(memberBean.getMemberHead());
                                mApproverList.add(bean);
                            }
                        }
                        mAdapterApprover.refreshAdapter(mIsDelApprover, mApproverList);

                    } else if (mAddMemberType == 3) {
                        mExecList.clear();
                        for (MemberListBean.MemberBean memberBean : mSumMemberList) {
                            if (clientTypeIds.contains("" + memberBean.getMemberId())) {
                                MemberBean bean = new MemberBean();
                                bean.setMemberId(memberBean.getMemberId());
                                bean.setMemberNm(memberBean.getMemberNm());
                                bean.setMemberHead(memberBean.getMemberHead());
                                mExecList.add(bean);
                            }
                        }
                        mAdapterExec.refreshAdapter(mIsDelExec, mExecList);
                    }

                }
            });
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


    /**
     * 选择审批流
     */
    private boolean mShowNormalAudit = true;//是否显示预设默认审批流
    private List<AuditZdyBean> mAuditZdyList = new ArrayList<>();
    public void showDialogAuditZdy(List<AuditZdyBean> list) {
        if(mAuditZdyList != null && mAuditZdyList.isEmpty()){
            mAuditZdyList.addAll(list);
        }
        if (mAuditZdyList != null && mAuditZdyList.size() > 0) {
            final ArrayList<DialogMenuItem> items = new ArrayList<>();
            for (AuditZdyBean bean : mAuditZdyList) {
                DialogMenuItem item = new DialogMenuItem(bean.getZdyNm(), bean.getId());
                items.add(item);
                if(bean.getIsNormal() != null && bean.getIsNormal().equals(1)){
                    if(mShowNormalAudit){
                        mTvAuditZdyName.setText(bean.getZdyNm());
                        doAuditZdy(bean);
                        return;
                    }
                }
            }
            if(!mShowNormalAudit){
                NormalListDialog dialog = new NormalListDialog(context, items);
                dialog.show();
                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        DialogMenuItem item = items.get(position);
                        for (AuditZdyBean bean : mAuditZdyList) {
                            if (String.valueOf(bean.getId()).equals("" + item.mResId)) {
                                mTvAuditZdyName.setText(item.mOperName);
                                doAuditZdy(bean);
                                break;
                            }
                        }

                    }
                });
            }
        } else {
            ToastUtils.showCustomToast("暂无数据");
        }
    }

    List<MemberListBean.MemberBean> mSumMemberList;//总的员工列表

    public void refreshAdapterMemberTree(List<TreeBean> mDatas, List<MemberListBean.MemberBean> dataList) {
        this.mTreeDatas = mDatas;
        this.mSumMemberList = dataList;
        showDialogMember();
    }

    public void doAddMember() {
        if (null == mTreeDatas || mTreeDatas.isEmpty()) {
            getP().queryMemberList(context);
        } else {
            showDialogMember();
        }
    }


}
