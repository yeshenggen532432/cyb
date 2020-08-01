package com.qwb.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.scanlibrary.ScanActivity;
import com.qwb.application.MyApp;
import com.qwb.common.ChooseWareTypeEnum;
import com.qwb.common.OrderListEnum;
import com.qwb.common.OrderTypeEnum;
import com.qwb.common.ToStepEnum;
import com.qwb.common.TypeEnum;
import com.qwb.view.audit.ui.AddShenPiRenActivity;
import com.qwb.view.base.ui.XApplyActivity;
import com.qwb.view.base.ui.XLoginActivity;
import com.qwb.view.call.ui.CallPageActivity;
import com.qwb.view.call.ui.CallRecordActivity;
import com.qwb.view.car.ui.CarOrderActivity;
import com.qwb.view.car.ui.CarStkInOrderActivity;
import com.qwb.view.car.ui.CarStkOutOrderActivity;
import com.qwb.view.car.ui.CarStkMoveListActivity;
import com.qwb.view.customer.ui.CustomerVisitActivity;
import com.qwb.view.order.ui.OrderListActivity;
import com.qwb.view.plan.ui.PlanLineMapActivity;
import com.qwb.view.step.ui.Step5Activity;
import com.qwb.view.shop.ui.ShopStepActivity;
import com.qwb.view.tab.ui.message.XMessageActivity;
import com.qwb.view.table.ui.TableActivity4;
import com.qwb.view.web.ui.WebX5Activity;
import com.qwb.view.call.ui.CallQueryActivity;
import com.qwb.view.customer.ui.AddClientActivity;
import com.qwb.view.customer.ui.ChooseCustomerActivity;
import com.qwb.view.step.ui.ChooseWareActivity3;
import com.qwb.view.customer.ui.ClientManagerActivity;
import com.qwb.view.step.ui.StepActivity;
import com.qwb.view.location.ui.LocationMarkActivity;
import com.qwb.view.storehouse.ui.StorehouseArrangeActivity;
import com.qwb.view.storehouse.ui.StorehouseInActivity;
import com.qwb.view.storehouse.ui.StorehouseOutActivity;
import com.qwb.view.step.model.ShopInfoBean;
import com.qwb.view.ware.ui.ChooseWareActivity;
import com.qwb.view.location.ui.MapNavActivity;
import com.qwb.view.common.ui.XScanActivity;
import com.qwb.view.file.ui.ChooseFileActivity;
import com.qwb.view.ware.ui.WareEditActivity;
import com.xmsx.cnlife.widget.photo.ImagePagerActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cn.droidlover.xdroidmvp.router.Router;

/**
 * Activity管理类
 */

public class ActivityManager {

    /**
     * ActivityManager
     */
    private static ActivityManager MANAGER = null;

    /**
     * 维护Activity的list
     */
    private List<Activity> mActivitys = null;

    public List<Activity> getActivitys() {
        return mActivitys;
    }

    /**
     * Get Instance  *  * @return ActivityManager
     */
    public static ActivityManager getInstance() {
        if (MANAGER == null) {
            MANAGER = new ActivityManager();
        }
        return MANAGER;
    }

    private ActivityManager() {
        mActivitys = Collections.synchronizedList(new LinkedList<Activity>());
    }

    /**
     * @param activity 作用说明 ：添加一个activity到管理里
     */
    public void pushActivity(Activity activity) {
        mActivitys.add(activity);
    }

    /**
     * @param activity 作用说明 ：删除一个activity在管理里
     */
    public void popActivity(Activity activity) {
        mActivitys.remove(activity);
    }

    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return null;
        }
        Activity activity = mActivitys.get(mActivitys.size() - 1);
        return activity;
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public void finishCurrentActivity() {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        Activity activity = mActivitys.get(mActivitys.size() - 1);
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        if (activity != null) {
            mActivitys.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        List<Activity> activities = new ArrayList<>();
        for (Activity activity : mActivitys) {
            if (activity.getClass().equals(cls)) {
                activities.add(activity);
            }
        }
        for (Activity activity : activities) {
            finishActivity(activity);
        }
        // 结束所有类名相同activity
        mActivitys.removeAll(activities);
    }

    /**
     * 指定类名的Activity 是否存在
     */
    public boolean activityIsRunning(Class<?> cls) {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return false;
        }
        for (Activity activity : mActivitys) {
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 按照指定类名找到activity
     *
     * @param cls
     * @return
     */
    public Activity findActivity(Class<?> cls) {
        Activity targetActivity = null;
        if (mActivitys != null) {
            for (Activity activity : mActivitys) {
                if (activity.getClass().equals(cls)) {
                    targetActivity = activity;
                    break;
                }
            }
        }
        return targetActivity;
    }

    /**
     * @return 作用说明 ：获取当前最顶部activity的实例
     */
    public Activity getTopActivity() {
        Activity mBaseActivity;
        synchronized (mActivitys) {
            final int size = mActivitys.size() - 1;
            if (size < 0) {
                return null;
            }
            mBaseActivity = mActivitys.get(size);
        }
        return mBaseActivity;

    }

    /**
     * @return 作用说明 ：获取当前最顶部的acitivity 名字
     */
    public String getTopActivityName() {
        Activity mBaseActivity;
        synchronized (mActivitys) {
            final int size = mActivitys.size() - 1;
            if (size < 0) {
                return null;
            }
            mBaseActivity = mActivitys.get(size);
        }
        return mBaseActivity.getClass().getName();
    }

    /**
     * 移出除cls外的所有activity
     *
     * @param cls
     */
    public void popAllActivityExceptOne(Class<? extends Activity> cls) {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        List<Activity> activities = new ArrayList<>();
        for (Activity activity : mActivitys) {
            if (!activity.getClass().equals(cls)) {
                activities.add(activity);
            }
        }
        for (Activity activity : activities) {
            popActivity(activity);
        }
        // 结束所有类名相同activity
        mActivitys.removeAll(activities);

    }

    /**
     * 结束除cls之外的所有activity
     *
     * @param cls
     */
    public void finishAllActivityExceptOne(Class<? extends Activity> cls) {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        List<Activity> activities = new ArrayList<>();
        for (Activity activity : mActivitys) {
            if (!activity.getClass().equals(cls)) {
                activities.add(activity);
            }
        }
        for (Activity activity : activities) {
            finishActivity(activity);
        }
//        // 结束所有类名相同activity
//        mActivitys.removeAll(activities);
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (mActivitys == null) {
            return;
        }
        for (Activity activity : mActivitys) {
            activity.finish();
        }
        mActivitys.clear();
    }

    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAllActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 强行退出程序(异常)
     */
    public void appExceptionExit() {
        try {
            finishAllActivity();
            // 退出程序
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断某activity是否处于栈顶
     */
    public boolean isActivityTop(Class cls) {
        try {
            android.app.ActivityManager manager = (android.app.ActivityManager) MyApp.getI().getSystemService(Context.ACTIVITY_SERVICE);
            String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
            return name.equals(cls.getName());
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 简单的跳转页面
     */
    public void jumpActivity(Activity context, Class c) {
        try {
            Router.newIntent(context)
                    .to(c)
                    .launch();
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 编辑应用菜单
     */
    public void jumpToApplyActivity(Activity context){
        Router.newIntent(context)
                .to(XApplyActivity.class)
                .putInt(ConstantUtils.Intent.TYPE, ConstantUtils.Menu.INT_UPDATE)
                .launch();
    }

    /**
     * 登录页
     */
    public void jumpToLoginActivity(Context context, int type) {
        try {
            if (context != null){
                Intent intent = new Intent(context, XLoginActivity.class);
                intent.putExtra(ConstantUtils.Intent.TYPE, type);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 关闭界面
     */
    public void closeActivity(Activity context) {
        Router.pop(context);
    }

    /**
     * 跳转首页关闭其他页面
     */
    public void jumpMainActivity(Activity context, Class c) {
        try {
            finishAllActivity();
            Router.newIntent(context)
                    .to(c)
                    .launch();
//            finishAllActivityExceptOne(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 扫描页面（新的）
     */
    public void jumpScanActivity(Activity context, boolean multiple) {
        try {
            Router.newIntent(context)
                    .to(ScanActivity.class)
                    .putBoolean(ScanActivity.EXTRAS_MULTIPLE, multiple)
                    .requestCode(ConstantUtils.Intent.REQUEST_CODE_SCAN)
                    .launch();
        } catch (Exception e) {
        }
    }

    /**
     * 添加商品
     * ConstantUtils.Menu.INT_ADD
     * ConstantUtils.Menu.INT_UPDATE
     */
    public void jumpToAddWareActivity(Activity context, int type) {
        try {
            Router.newIntent(context)
                    .to(WareEditActivity.class)
                    .putInt(ConstantUtils.Intent.TYPE, type)
                    .launch();
        } catch (Exception e) {
        }
    }

    /**
     * 扫描页面（新的）
     */
    public void jumpXScanActivity(Activity context, boolean multiple, int stkId) {
        try {
            Router.newIntent(context)
                    .to(XScanActivity.class)
                    .putBoolean(XScanActivity.EXTRAS_MULTIPLE, multiple)
                    .putInt(XScanActivity.EXTRAS_STKID, stkId)
                    .requestCode(ConstantUtils.Intent.REQUEST_CODE_SCAN)
                    .launch();
        } catch (Exception e) {
        }
    }

    /**
     * 导航地图
     */
    public void jumpActivityNavMap(Activity activity, String latitude, String longitude, String address) {
        if (MyStringUtil.isEmpty(latitude) || MyStringUtil.isEmpty(longitude)) {
            ToastUtils.showCustomToast("没有经纬度不能导航");
            return;
        }
        Router.newIntent(activity)
                .putString(ConstantUtils.Intent.LATITUDE, latitude)
                .putString(ConstantUtils.Intent.LONGITUDE, longitude)
                .putString(ConstantUtils.Intent.LOCATION, address)
                .putBoolean(ConstantUtils.Intent.NEED_NAV, true)
                .to(MapNavActivity.class)
                .launch();
    }


    /**
     * 地址定位：1.查看；2.标注; 3.导航
     */
    public void jumpToLocationMarkActivity(Activity context, String latitude, String longitude, String address, String province, String city, String area) {
        try {
            Router.newIntent(context)
                    .to(LocationMarkActivity.class)
                    .putString(ConstantUtils.Intent.LATITUDE, latitude)
                    .putString(ConstantUtils.Intent.LONGITUDE, longitude)
                    .putString(ConstantUtils.Intent.ADDRESS, address)
                    .putString(ConstantUtils.Intent.PROVINCE, province)
                    .putString(ConstantUtils.Intent.CITY, city)
                    .putString(ConstantUtils.Intent.AREA, area)
                    .requestCode(ConstantUtils.Intent.REQUEST_CODE_LOCATION)
                    .launch();
        } catch (Exception e) {
        }
    }


    /**
     * 放大图片
     */
    public void zoomPic(Context context, String[] urls, int position) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        context.startActivity(intent);
    }

    /**
     * 添加审批人
     */
    public void addAuditPerson(Context context, int type, String title) {
        Intent intent = new Intent(context, AddShenPiRenActivity.class);
        intent.putExtra(ConstantUtils.Intent.TYPE, type);
        intent.putExtra(ConstantUtils.Intent.TITLE, title);
        context.startActivity(intent);
    }

    /**
     * 附件选择文件
     */
    public void jumpActivityFile(Activity activity) {
        Intent intent = new Intent(activity, ChooseFileActivity.class);
        intent.putExtra(ConstantUtils.Intent.IS_SIGNLE, false);// 是否单选，true:单选，false：多选
        activity.startActivityForResult(intent, Constans.TAKE_PIC_YUNPAN);
    }



    /**
     * 跳到：入仓单
     */
    public void jumpToStorehouseInActivity(Activity activity, int billId, TypeEnum typeEnum) {
        Router.newIntent(activity)
                .putInt(ConstantUtils.Intent.ID, billId)
                .putString(ConstantUtils.Intent.TYPE, typeEnum.getType())
                .to(StorehouseInActivity.class)
                .launch();
    }

    /**
     * 跳到：出仓单
     */
    public void jumpToStorehouseOutActivity(Activity activity, int billId, TypeEnum typeEnum) {
        Router.newIntent(activity)
                .putInt(ConstantUtils.Intent.ID, billId)
                .putString(ConstantUtils.Intent.TYPE, typeEnum.getType())
                .to(StorehouseOutActivity.class)
                .launch();
    }

    /**
     * 跳到：库位整理单
     */
    public void jumpToStorehouseArrangeActivity(Activity activity, int billId) {
        Router.newIntent(activity)
                .putInt(ConstantUtils.Intent.ID, billId)
                .to(StorehouseArrangeActivity.class)
                .launch();
    }

    /**
     * 跳到：选择商品
     */
    public void jumpToChooseWareActivity(Activity activity, int stkId, ArrayList<ShopInfoBean.Data> selectList) {
        Router.newIntent(activity)
                .putString(ConstantUtils.Intent.STK_ID, String.valueOf(stkId))
                .putParcelableArrayList(ConstantUtils.Intent.CHOOSE_WARE, selectList)
                .requestCode(ConstantUtils.Intent.REQUEST_CODE_CHOOSE_WARE)
                .to(ChooseWareActivity.class)
                .launch();
    }

    /**
     * 跳到：选择客户
     */
    public void jumpToChooseCustomerActivity(Activity activity) {
        Router.newIntent(activity)
                .requestCode(ConstantUtils.Intent.REQUEST_CODE_CHOOSE_CUSTOMER)
                .to(ChooseCustomerActivity.class)
                .launch();
    }

    /**
     * 跳到：编辑客户:1.修改 2.添加 3.查看
     */
    public void jumpToAddClientActivity(Activity activity, int type, String cId) {
        Router.newIntent(activity)
                .putInt(ConstantUtils.Intent.TYPE, type)
                .putString(ConstantUtils.Intent.CLIENT_ID, cId)
                .to(AddClientActivity.class)
                .launch();
    }


    /**
     * 线路地图：1.可以在地图上添加或移除客户（编辑线路）
     */
    public void jumpToPlanLineMapActivity(Activity activity, int type) {
        Router.newIntent(activity)
                .putInt(ConstantUtils.Intent.TYPE, type)
                .to(PlanLineMapActivity.class)
                .launch();
    }

    /**
     * 我的拜访(客户拜访)
     */
    public void jumpToCustomerVisitActivity(Activity activity, String type, String cId) {
        Router.newIntent(activity)
                .to(CustomerVisitActivity.class)
                .putString(ConstantUtils.Intent.CLIENT_ID, cId)
                .putString(ConstantUtils.Intent.TYPE, type)
                .launch();
    }

    /**
     * 下单
     */
    public void jumpToStep5Activity(Activity activity, OrderTypeEnum orderType, Integer orderId, String khNm, String orderZt, Integer isMe, String orderNo, int redMark) {
        if (orderType == null){
            orderType = OrderTypeEnum.ORDER_DHXD_ADD;
        }
        if (orderId == null){
            orderId = 0;
        }
        if (isMe == null){
            isMe = 0;
        }
        Router.newIntent(activity)
                .putInt(ConstantUtils.Intent.ORDER_TYPE, orderType.getType())// 1：拜访客户下单 2:单独下单(电话下单) 3：订货下单模块（列表）4：退货;5：退货下单（列表）
                .putInt(ConstantUtils.Intent.ORDER_ID, orderId)// 订单id
                .putString(ConstantUtils.Intent.CLIENT_NAME, khNm)//客户名称
                .putString(ConstantUtils.Intent.ORDER_STATE, orderZt)// 订单状态：审核，未审核（可修改）
                .putInt(ConstantUtils.Intent.IS_ME, isMe)// 是否自己：1:我的，2：别人
                .putString(ConstantUtils.Intent.ORDER_NO, orderNo)
                .putInt(ConstantUtils.Intent.ORDER_RED_MARK, redMark)
                .to(Step5Activity.class)
                .launch();
    }

    /**
     * 订货下单
     */
    public void jumpToOrderListActivity(Activity activity, OrderListEnum orderListEnum, String cid) {
        try {
            if (orderListEnum == null){
                orderListEnum = OrderListEnum.ORDER;
            }
            Router.newIntent(activity)
                    .putInt(ConstantUtils.Intent.TYPE, orderListEnum.getType())
                    .putString(ConstantUtils.Intent.CLIENT_ID, cid)
                    .to(OrderListActivity.class)
                    .launch();
        }catch (Exception e){}
    }

    /**
     * 选择商品
     */
    public void jumpToChooseWare3Activity(Activity activity, ChooseWareTypeEnum typeEnum, String customerId, String stkId, String autoPrice, boolean editPrice, List<ShopInfoBean.Data> dataList){
        try {
            if (typeEnum == null){
                typeEnum = ChooseWareTypeEnum.W_ORDER;
            }
            ArrayList<Integer> wareIdList = new ArrayList<>();
            if (MyCollectionUtil.isNotEmpty(dataList)) {
                for (ShopInfoBean.Data data : dataList){
                    wareIdList.add(data.getWareId());
                }
            }
            Router.newIntent(activity)
                    .putString(ConstantUtils.Intent.TYPE,typeEnum.getType())
                    .putString(ConstantUtils.Intent.CLIENT_ID, customerId)
                    .putString(ConstantUtils.Intent.STK_ID, stkId)
                    .putString(ConstantUtils.Intent.AUTO_PRICE, autoPrice)
                    .putBoolean(ConstantUtils.Intent.EDIT_PRICE, editPrice)
                    .putIntegerArrayList(ConstantUtils.Intent.CHOOSE_WARE_ID, wareIdList)
                    .requestCode(ConstantUtils.Intent.REQUEST_STEP_5_CHOOSE_GOODS)
                    .to(ChooseWareActivity3.class)
                    .launch();
        }catch (Exception e){
        }
    }

    /**
     * 下单：选择客户
     */
    public void jumpToClientManagerActivity(Activity activity, OrderTypeEnum typeEnum){
        try {
            if (typeEnum == null){
                typeEnum = OrderTypeEnum.ORDER_CAR_ADD;
            }
            Router.newIntent(activity)
                    .putInt(ConstantUtils.Intent.TYPE, typeEnum.getType())
                    .to(ClientManagerActivity.class)
                    .launch();
        }catch (Exception e){
        }
    }

    /**
     * 下单：选择客户
     */
    public void jumpToStepActivity(Activity activity, ToStepEnum typeEnum, int customerId, String customerName, String address, String tel, String mobile,
                                   String linkman,String longitude,String latitude, Integer locationTag, String date){
        try {
            if (typeEnum == null){
                typeEnum = ToStepEnum.STEP_MINE_CUSTOMER;
            }
            if (locationTag == null){
                locationTag = 1;
            }
            Router.newIntent(activity)
                    .putInt(ConstantUtils.Intent.TYPE, typeEnum.getType())
                    .putInt(ConstantUtils.Intent.CLIENT_ID, customerId)
                    .putInt(ConstantUtils.Intent.TAG, locationTag)
                    .putString(ConstantUtils.Intent.CLIENT_NAME, customerName)
                    .putString(ConstantUtils.Intent.ADDRESS, address)
                    .putString(ConstantUtils.Intent.TEL, tel)
                    .putString(ConstantUtils.Intent.MOBILE, mobile)
                    .putString(ConstantUtils.Intent.LINKMAN, linkman)
                    .putString(ConstantUtils.Intent.LONGITUDE, longitude)
                    .putString(ConstantUtils.Intent.LATITUDE, latitude)
                    .putString(ConstantUtils.Intent.DATE, date)
                    .to(StepActivity.class)
                    .launch();
        }catch (Exception e){
        }
    }
    /**
     * 拜访查询:1.六步骤
     */
    public void jumpToCallQueryActivity(Activity context, String type, String cid){
        try {
            Router.newIntent(context)
                    .putString(ConstantUtils.Intent.TYPE, type)
                    .putString(ConstantUtils.Intent.CLIENT_ID, String.valueOf(cid))
                    .to(CallQueryActivity.class)
                    .launch();
        }catch (Exception e){
        }
    }
    /**
     * 拜访报表4: 1:统计报表 2：拜访步骤
     */
    public void jumpToCallQueryActivity(Activity context, int type, String clientName, String cid, String startTime, String endTime){
        try {
            Router.newIntent(context)
                    .putInt(ConstantUtils.Intent.TYPE, type)
                    .putString(ConstantUtils.Intent.CLIENT_NAME, String.valueOf(clientName))
                    .putString(ConstantUtils.Intent.CLIENT_ID, String.valueOf(cid))
                    .putString(ConstantUtils.Intent.START_TIME, startTime)
                    .putString(ConstantUtils.Intent.END_TIME, endTime)
                    .to(TableActivity4.class)
                    .launch();
        }catch (Exception e){
        }
    }

    /**
     * 商城订单
     */
    public void jumpToShopOrderActivity(Activity context, int orderId, String companyId){
        try {
            Router.newIntent(context)
                    .putInt(ConstantUtils.Intent.ORDER_TYPE, ConstantUtils.Order.O_SC)
                    .putInt(ConstantUtils.Intent.ORDER_ID, orderId)
                    .putString(ConstantUtils.Intent.COMPANY_ID, companyId)
                    .to(ShopStepActivity.class)
                    .launch();
        }catch (Exception e){
        }
    }

    public void jumpToWebX5Activity(Activity context, String url, String title){
        Router.newIntent(context)
                .putString(ConstantUtils.Intent.URL, url)
                .putString(ConstantUtils.Intent.TITLE, title)
                .to(WebX5Activity.class)
                .launch();
    }

    public void jumpToShopLoginActivity(Activity context){
        Router.newIntent(context)
                .putString(ConstantUtils.Intent.URL, "http://192.168.31.217:8081/#login")
                .to(WebX5Activity.class)
                .launch();
    }

    /**
     * 拜访记录
     */
    public void jumpToCallRecordActivity(Activity context, int bfId,  String khNm, String memberName){
        Router.newIntent(context)
                .to(CallRecordActivity.class)
                .putInt(ConstantUtils.Intent.ID, bfId)
                .putString(ConstantUtils.Intent.CLIENT_NAME, khNm)
                .putString(ConstantUtils.Intent.MEMBER_NAME, memberName)
                .launch();
    }
    /**
     * 拜访记录
     */
    public void jumpToCallPageActivity(Activity context, String cid, String khNm, String startDate, String endDate, String timeTitle, boolean isShow,String type){
        Router.newIntent(context)
                .to(CallPageActivity.class)
                .putString(ConstantUtils.Intent.CLIENT_ID, cid)
                .putString(ConstantUtils.Intent.CLIENT_NAME, khNm)
                .putString(ConstantUtils.Intent.START_TIME, startDate)
                .putString(ConstantUtils.Intent.END_TIME, endDate)
                .putBoolean(ConstantUtils.Intent.IS_SHOW, isShow)
                .putString("bfTime", timeTitle)
                .putString(ConstantUtils.Intent.TYPE, type)
                .launch();
    }

    /**
     * 我的拜访
     */
    public void jumpToCallPageActivity(Activity context, String customerId, String type){
        Router.newIntent(context)
                .to(CallPageActivity.class)
                .putString(ConstantUtils.Intent.CLIENT_ID, customerId)
                .putString(ConstantUtils.Intent.TYPE, type)
                .launch();
    }

    /**
     * type: 1.车销配货单列表；2.车销回库单列表
     */
    public void jumpToCarStkOutOrderListActivity(Activity context, int type){
        Router.newIntent(context)
                .to(CarStkMoveListActivity.class)
                .putInt(ConstantUtils.Intent.TYPE, type)
                .launch();
    }

    /**
     * 车销配货单
     * type:1.添加 2.查看或修改
     */
    public void jumpToCarStkOutOrderActivity(Activity context, int id, TypeEnum typeEnum){
        if (typeEnum == null){
            typeEnum = TypeEnum.ADD;
        }
        Router.newIntent(context)
                .to(CarStkOutOrderActivity.class)
                .putString(ConstantUtils.Intent.ORDER_ID, "" + id)
                .putString(ConstantUtils.Intent.ORDER_TYPE, typeEnum.getType())
                .launch();
    }
    /**
     * 车销回库单
     */
    public void jumpToCarStkInOrderActivity(Activity context, int id,  TypeEnum typeEnum){
        if (typeEnum == null){
            typeEnum = TypeEnum.ADD;
        }
        Router.newIntent(context)
                .to(CarStkInOrderActivity.class)
                .putString(ConstantUtils.Intent.ORDER_ID, "" + id)
                .putString(ConstantUtils.Intent.ORDER_TYPE, typeEnum.getType())
                .launch();
    }

    /**
     * 车销单
     */
    public void jumpToCarOrderActivity(Activity context, int orderId, String khNm, String orderZt, int isMe, String orderNo, OrderTypeEnum typeEnum){
        if (typeEnum == null){
            typeEnum = OrderTypeEnum.ORDER_CAR_ADD;
        }
        Router.newIntent(context)
                .putInt(ConstantUtils.Intent.ORDER_ID, orderId)// 订单id
                .putString(ConstantUtils.Intent.CLIENT_NAME, khNm)//客户名称
                .putString(ConstantUtils.Intent.ORDER_STATE, orderZt)// 订单状态：审核，未审核（可修改）
                .putInt(ConstantUtils.Intent.IS_ME, isMe)// 是否自己：1:我的，2：别人
                .putInt(ConstantUtils.Intent.ORDER_TYPE, typeEnum.getType())//7:添加；8：修改
                .putString(ConstantUtils.Intent.ORDER_NO, orderNo)
                .to(CarOrderActivity.class)
                .launch();
    }

    /**
     * 消息
     */
    public void jumpToMessageActivity(Activity context, int banKuai,String title){
        Router.newIntent(context)
                .to(XMessageActivity.class)
                .putString(ConstantUtils.Intent.BANKUAI,"" + banKuai)
                .putString(ConstantUtils.Intent.TITLE,title)
                .launch();
    }



}
