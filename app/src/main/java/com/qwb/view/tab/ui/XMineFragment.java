package com.qwb.view.tab.ui;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qwb.application.MyApp;
import com.qwb.view.tab.parsent.PXMine;
import com.qwb.utils.MyAppUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.MyGlideUtil;
import com.qwb.utils.MyFileUtil;
import com.xmsx.cnlife.widget.CircleImageView;
import com.xmsx.cnlife.widget.DownloadDialog;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.mine.ui.UserInfoActivity;
import com.qwb.view.mine.ui.FeedbackActivity;
import com.qwb.view.mine.ui.SetActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.QRCodeUtil;
import com.qwb.utils.SPUtils;
import com.xmsx.qiweibao.R;
import java.io.File;
import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XFragment;

/**
 * Tab：我的
 */
public class XMineFragment extends XFragment<PXMine> {

    public XMineFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.x_fragment_mine;
    }

    @Override
    public PXMine newP() {
        return new PXMine();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();

        // 正式（放开）：后台获取版本信息
        if (!Constans.ISDEBUG) {
            getP().queryDataUpdateVersion(context);
        }
    }

    @BindView(R.id.iv_user_head)
    CircleImageView mIvUserHead;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.tv_user_phone)
    TextView mTvUserPhone;
    @BindView(R.id.iv_version_new)
    ImageView mIvVersionNew;
    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(R.id.view_version)
    View mViewVersion;
    @BindView(R.id.iv_code)
    ImageView mIvCode;
    private void initUI() {
        setUserInfo();
        mTvVersion.setText("当前版本:" + MyAppUtil.getAppVersion());
        // 生成二维码
        createCode(mIvCode, (int) (100 * MyApp.getI().getBiLi()));
    }

    @OnClick({R.id.view_user, R.id.view_feedback,R.id.view_version, R.id.view_user_guide, R.id.view_setting, R.id.iv_code})
    public void OnClick(View view){
        switch (view.getId()){
            //用户信息
            case R.id.view_user:
                ActivityManager.getInstance().jumpActivity(context, UserInfoActivity.class);
                break;
            //意见反馈
            case R.id.view_feedback:
                ActivityManager.getInstance().jumpActivity(context, FeedbackActivity.class);
                break;
                //版本更新
            case R.id.view_version:
                showDialogVersionUpdate();
                break;
            //用户指南
            case R.id.view_user_guide:
                ActivityManager.getInstance().jumpToWebX5Activity(context, Constans.ZhiNanUrl, "操作指南");
                break;
            //设置
            case R.id.view_setting:
                ActivityManager.getInstance().jumpActivity(context, SetActivity.class);
                break;

            //二维码图片
            case R.id.iv_code:
                showDialogCode();
                break;
        }
    }


    /**
     * 生成二维码
     */
    private Bitmap codeBitmap;
    private void createCode(final ImageView code, final int size) {
        final String filePath = MyFileUtil.getFileRoot(context) + File.separator + "qr_" + System.currentTimeMillis() + ".jpg";
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = QRCodeUtil.createQRImage(Constans.AppDownUrl, size, size, null, filePath);
                if (success) {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            codeBitmap = BitmapFactory.decodeFile(filePath);
                            code.setImageBitmap(codeBitmap);
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 对话框：二维码
     */
    private Dialog codeDialog;
    private void showDialogCode() {
        if (codeDialog == null) {
            codeDialog = new Dialog(context, R.style.Translucent_NoTitle);
            codeDialog.setContentView(R.layout.x_dialog_code);
            ImageView iv_big_code = codeDialog.findViewById(R.id.iv_big_code);
            iv_big_code.setImageBitmap(codeBitmap);
            createCode(iv_big_code, (int) (200 * MyApp.getI().getBiLi()));
        }
        codeDialog.show();
    }


    @Override
    public void onResume() {
        super.onResume();
        setUserInfo();
    }

    /**
     * 设置用户信息：头像，用户名，账号
     */
    public void setUserInfo(){
        MyGlideUtil.getInstance().displayImageRound(Constans.IMGROOTHOST + SPUtils.getSValues(ConstantUtils.Sp.USER_HEAD), mIvUserHead);
        String userName = SPUtils.getSValues(ConstantUtils.Sp.USER_NAME);
        if (MyStringUtil.isEmpty(userName)) {
            mTvUserName.setText(SPUtils.getSValues(ConstantUtils.Sp.USER_MOBILE));
        } else {
            mTvUserName.setText(userName);
        }
        mTvUserPhone.setText("账号:" + SPUtils.getSValues(ConstantUtils.Sp.USER_MOBILE));
    }

    /**
     *  处理版本更新
     */
    private String versionUrl = "";//下载地址
    private String versionContent;//更新内容
    private String isQz = "2";//是否强制更新：1:是，2：否
    public void doVersionUpdate(boolean isUpdate, String isQz, String versionUrl, String versionContent){
        if (isUpdate) {
            mViewVersion.setClickable(true);
            mIvVersionNew.setVisibility(View.VISIBLE);
        } else {
            mViewVersion.setClickable(false);
            mIvVersionNew.setVisibility(View.GONE);
        }
        this.isQz = isQz;
        this.versionUrl = versionUrl;
        this.versionContent = versionContent;
    }

    /**
     * 对话框：版本更新
     */
    public void showDialogVersionUpdate(){
        DownloadDialog dialog = new DownloadDialog(context, versionUrl, versionContent, isQz);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog.setCancelable(false);
        dialog.show();
    }


}
