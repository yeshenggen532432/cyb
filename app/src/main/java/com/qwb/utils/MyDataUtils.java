package com.qwb.utils;


import android.content.ContentValues;
import android.content.Intent;
import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.qwb.application.MyApp;
import com.qwb.db.CacheLocationBean;
import com.qwb.view.longconnection.CacheService;
import com.qwb.db.DDeliveryCustomerBean;
import com.qwb.db.PassWordBean;
import com.qwb.db.DMineCustomerBean;
import com.qwb.db.CacheStep1Bean;
import com.qwb.db.CacheStepPicBean;
import com.qwb.db.Step5GoodsListBean;
import com.qwb.db.SaveLogBean;
import com.qwb.db.DMapTrackBean;
import com.qwb.db.DRecordUpdateBean;
import com.qwb.db.DStep1Bean;
import com.qwb.db.DStep2Bean;
import com.qwb.db.DStep3Bean;
import com.qwb.db.DStep4Bean;
import com.qwb.db.DStep5Bean;
import com.qwb.db.DStep6Bean;
import com.qwb.db.DStkCheckWareBean;
import com.qwb.db.DWareBean;
import com.qwb.db.DWareTypeBean;
import com.qwb.db.DCategoryMessageBean;
import com.qwb.db.DMessageBean;
import com.qwb.view.customer.model.MineClientInfo;
import com.qwb.view.map.model.TrackListBean;

import org.litepal.LitePal;
import org.litepal.crud.callback.SaveCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static com.github.promeg.pinyinhelper.Pinyin.toPinyin;

/**
 * 数据库的工具类
 */

public class MyDataUtils {

    private static volatile MyDataUtils instance = null;

    private MyDataUtils() {

    }

    public static MyDataUtils getInstance() {
        if (instance == null) {
            synchronized (MyDataUtils.class) {
                if (instance == null) {
                    instance = new MyDataUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 保存上传定位数据（间隔1分钟）（长连接）
     */
    public void saveCacheLocationData(CacheLocationBean bean) {
        bean.saveAsync().listen(new SaveCallback() {
            @Override
            public void onFinish(boolean success) {

            }
        });
    }


    /**
     * 删除定位缓存数据（间隔1分钟）（长连接）
     */
    public void delCacheLocationData(String s) {
        LitePal.deleteAllAsync(CacheLocationBean.class, "location_time=?", s);
    }

    /**
     * 上传缓存数据：判断是否有缓存数据，有打开Service上传数据；没有关闭Service
     */
    public void loadCacheData() {
        List<CacheLocationBean> beans = LitePal.findAll(CacheLocationBean.class);
        Intent service = new Intent(MyApp.getI(), CacheService.class);
        if (beans != null && beans.size() > 0) {
            MyApp.getI().startService(service);
        } else {
            MyApp.getI().stopService(service);
        }
    }

    /**
     * 记住密码
     */
    public void savePassWord(PassWordBean bean) {
        String phone = bean.getPhone();
        String userId = String.valueOf(bean.getUserId());
        String companyId = String.valueOf(bean.getCompanyId());
        String pwd = String.valueOf(bean.getPwd());
        String check = String.valueOf(bean.isCheck());
        List<PassWordBean> passWordBeanList = LitePal.where("phone = ?", phone).find(PassWordBean.class);
        //先判断是否有保存过：修改
        if (passWordBeanList != null && passWordBeanList.size() > 0) {
//            bean.updateAll("phone = ? and userId = ? and companyId = ? and pwd = ? and check = ?",
//                    phone, userId,companyId,pwd,check);
//            bean.delete();
            LitePal.deleteAll(PassWordBean.class, "phone = ?", phone);
        } else {
        }
        //保存
        bean.save();
    }

    /**
     * 保存日报；周报；月报--草稿
     */
    public void saveLog(SaveLogBean bean, String userId, String companyId, String type) {
        SaveLogBean oldBean = queryLog(userId, companyId, type);
        if (oldBean != null) {
            oldBean.setGzNrcd(bean.getGzNrcd());
            oldBean.setGzZjcd(bean.getGzZjcd());
            oldBean.setGzJhcd(bean.getGzJhcd());
            oldBean.setGzBzcd(bean.getGzBzcd());
            oldBean.setRemocd(bean.getRemocd());
            oldBean.setUserId(userId);
            oldBean.setCompanyId(companyId);
            oldBean.setType(type);
            oldBean.save();
        } else {
            bean.save();
        }
    }

    /**
     * 查询日报；周报；月报--草稿
     */
    public SaveLogBean queryLog(String userId, String companyId, String type) {
        SaveLogBean bean = null;
        List<SaveLogBean> beans = LitePal.where("userId = ? and companyId = ? and type = ?", userId, companyId, type).find(SaveLogBean.class);
        //先判断是否有保存过：修改
        if (beans != null && beans.size() > 0) {
            bean = beans.get(0);
        }
        return bean;
    }

    /**
     * 删除日报；周报；月报--草稿
     */
    public void delLog(String userId, String companyId, String type) {
        LitePal.deleteAll(SaveLogBean.class, "userId = ? and companyId = ? and type = ?", userId, companyId, type);
    }

    /**
     * 保存拜访地图-轨迹列表
     * mid--账号id
     */
    public void saveTrackList(final List<TrackListBean.TrackList> list, final String mid, final String companyId) {
        new Thread() {
            public void run() {
                LitePal.deleteAll(DMapTrackBean.class, "mid = ? and companyId = ?", mid, companyId);
                for (int i = 0; i < list.size(); i++) {
                    TrackListBean.TrackList track = list.get(i);
                    DMapTrackBean bean = new DMapTrackBean();
                    bean.setMid(mid);
                    bean.setCompanyId(companyId);
                    bean.setUserId(track.getUserId());//业务员id
                    bean.setUserNm(track.getUserNm());
                    bean.setUserTel(track.getUserTel());
                    bean.setUserHead(track.getUserHead());
                    bean.setAddress(track.getAddress());
                    bean.setLocation(track.getLocation());//经纬度
                    bean.setMemberJob(track.getMemberJob());//职位
                    bean.setTimes(track.getTimes());
                    bean.setZt(track.getZt());//状态：在线，下班，下线
                    bean.save();
                }
            }
        }.start();//开启线程
    }

    /**
     * 查询拜访地图-轨迹列表
     */
    public List<DMapTrackBean> queryTrackList(String mid, String companyId) {
        List<DMapTrackBean> beans = LitePal.where("mid = ? and companyId = ?", mid, companyId).find(DMapTrackBean.class);
        //先判断是否有保存过：修改
        if (beans != null && beans.size() > 0) {
            return beans;
        }
        return null;
    }


    /**
     * 订货下单，退货：保存商品列表
     */
    public void saveGoodsList(final ArrayList<HashMap<String, String>> xsList, final String cid, final String userID, final String companyID, final String dhTp, final boolean isSave) {
        new Thread() {
            public void run() {
                LitePal.deleteAll(Step5GoodsListBean.class, "cid = ? and userID = ? and companyID = ? and dhTp = ?", cid, userID, companyID, dhTp);
                //默认删除
                if (isSave) {
                    for (int i = 0; i < xsList.size(); i++) {
                        HashMap<String, String> map = xsList.get(i);
                        Step5GoodsListBean bean = new Step5GoodsListBean();
                        bean.setCid(cid);
                        bean.setUserID(userID);
                        bean.setCompanyID(companyID);
                        bean.setDhTp(dhTp);
                        bean.setGoodsID(map.get(ConstantUtils.Map.STEP5_0_GOODS_ID));
                        bean.setGoodsName(map.get(ConstantUtils.Map.STEP5_1_GOODS_NAME));
                        bean.setGoodsXstp(map.get(ConstantUtils.Map.STEP5_2_GOODS_XSTP));
                        bean.setGoodsCount(map.get(ConstantUtils.Map.STEP5_3_GOODS_COUNT));
                        bean.setGoodsDw(map.get(ConstantUtils.Map.STEP5_4_GOODS_DW));
                        bean.setGoodsDj(map.get(ConstantUtils.Map.STEP5_5_GOODS_DJ));
                        bean.setGoodsDjTemp(map.get(ConstantUtils.Map.STEP5_5_GOODS_DJ_TEMP));
                        bean.setGoodsZj(map.get(ConstantUtils.Map.STEP5_6_GOODS_ZJ));
                        bean.setGoodsDel(map.get(ConstantUtils.Map.STEP5_7_GOODS_DEL));
                        bean.setGoodsBeunit(map.get(ConstantUtils.Map.STEP5_8_GOODS_BEUNIT));
                        bean.setGoodsMaxunitCode(map.get(ConstantUtils.Map.STEP5_9_GOODS_MAXUNIT_CODE));
                        bean.setGoodsMinunitCode(map.get(ConstantUtils.Map.STEP5_10_GOODS_MINUNIT_CODE));
                        bean.setGoodsMaxunit(map.get(ConstantUtils.Map.STEP5_11_GOODS_MAXUNIT));
                        bean.setGoodsMinunit(map.get(ConstantUtils.Map.STEP5_12_GOODS_MINUNIT));
                        bean.setGoodsHsNum(map.get(ConstantUtils.Map.STEP5_13_GOODS_HS_NUM));
                        bean.setGoodsZxj(map.get(ConstantUtils.Map.STEP5_14_GOODS_ZXJ));
                        bean.setGoodsYj(map.get(ConstantUtils.Map.STEP5_15_GOODS_YJ));
                        bean.setGoodsWg(map.get(ConstantUtils.Map.STEP5_16_GOODS_GG));
                        bean.setGoodsRemark(map.get(ConstantUtils.Map.STEP5_17_GOODS_REMARK));
                        bean.save();
                    }

                }
            }
        }.start();//开启线程
    }

    /**
     * 订货下单，退货：查询商品列表
     */
    public ArrayList<HashMap<String, String>> queryGoodsList(final String cid, final String userID, final String companyID, final String dhTp) {
        List<Step5GoodsListBean> beans = LitePal.where("cid = ? and userID = ? and companyID = ? and dhTp = ?", cid, userID, companyID, dhTp).find(Step5GoodsListBean.class);
        //先判断是否有保存过：修改
        if (beans != null && beans.size() > 0) {
            ArrayList<HashMap<String, String>> xsList = new ArrayList<>();
            for (Step5GoodsListBean bean : beans) {
                HashMap<String, String> map = new HashMap<>();
                map.put(ConstantUtils.Map.STEP5_0_GOODS_ID, bean.getGoodsID());
                map.put(ConstantUtils.Map.STEP5_1_GOODS_NAME, bean.getGoodsName());
                map.put(ConstantUtils.Map.STEP5_2_GOODS_XSTP, bean.getGoodsXstp());
                map.put(ConstantUtils.Map.STEP5_3_GOODS_COUNT, bean.getGoodsCount());
                map.put(ConstantUtils.Map.STEP5_4_GOODS_DW, bean.getGoodsDw());
                map.put(ConstantUtils.Map.STEP5_5_GOODS_DJ, bean.getGoodsDj());
                map.put(ConstantUtils.Map.STEP5_5_GOODS_DJ_TEMP, bean.getGoodsDjTemp());
                map.put(ConstantUtils.Map.STEP5_6_GOODS_ZJ, bean.getGoodsZj());
                map.put(ConstantUtils.Map.STEP5_7_GOODS_DEL, bean.getGoodsDel());
                map.put(ConstantUtils.Map.STEP5_8_GOODS_BEUNIT, bean.getGoodsBeunit());
                map.put(ConstantUtils.Map.STEP5_9_GOODS_MAXUNIT_CODE, bean.getGoodsMaxunitCode());
                map.put(ConstantUtils.Map.STEP5_10_GOODS_MINUNIT_CODE, bean.getGoodsMinunitCode());
                map.put(ConstantUtils.Map.STEP5_11_GOODS_MAXUNIT, bean.getGoodsMaxunit());
                map.put(ConstantUtils.Map.STEP5_12_GOODS_MINUNIT, bean.getGoodsMinunit());
                map.put(ConstantUtils.Map.STEP5_13_GOODS_HS_NUM, bean.getGoodsHsNum());
                map.put(ConstantUtils.Map.STEP5_14_GOODS_ZXJ, bean.getGoodsZxj());
                map.put(ConstantUtils.Map.STEP5_15_GOODS_YJ, bean.getGoodsYj());
                map.put(ConstantUtils.Map.STEP5_16_GOODS_GG, bean.getGoodsWg());
                map.put(ConstantUtils.Map.STEP5_17_GOODS_REMARK, bean.getGoodsRemark());
                xsList.add(map);
            }
            return xsList;
        }
        return null;
    }

    /**
     * 缓存所有我的客户
     */
    public void saveMineClient(final List<MineClientInfo> dataList) {
        try {
            new Thread() {
                public void run() {
                    String userId = SPUtils.getID();
                    String companyId = SPUtils.getCompanyId();
                    if (null == dataList || dataList.isEmpty()) {
                        return;
                    }
                    List<DMineCustomerBean> beanList = new ArrayList<>();
                    for (MineClientInfo client : dataList) {
                        DMineCustomerBean bean = new DMineCustomerBean();
                        bean.setUserId(userId);
                        bean.setCompanyId(companyId);
                        bean.setCid(client.getId() + "");
                        bean.setKhTp(client.getKhTp() + "");
                        bean.setKhNm(client.getKhNm());
                        bean.setMemId(client.getMemId() + "");
                        bean.setMemberNm(client.getMemberNm());
                        bean.setBranchName(client.getBranchName());
                        bean.setLongitude(client.getLongitude());
                        bean.setLatitude(client.getLatitude());
                        bean.setAddress(client.getAddress());
                        bean.setScbfDate(client.getScbfDate());
                        bean.setLinkman(client.getLinkman());
                        bean.setMobile(client.getMobile());
                        bean.setTel(client.getTel());
                        bean.setLinkman(client.getLinkman());
                        bean.setProvince(client.getProvince());
                        bean.setCity(client.getCity());
                        bean.setArea(client.getArea());
                        bean.setQdtpNm(client.getQdtpNm());
                        bean.setXsjdNm(client.getXsjdNm());
                        bean.setXxzt(client.getXxzt());
                        //处理汉字拼音
                        bean.setPinyin(toPinyin(client.getKhNm(), "/"));//♞分隔符使用特殊符号避免sql中的特殊符号
                        //处理简拼
                        String khNm = client.getKhNm();
                        if (!MyStringUtil.isEmpty(khNm)) {
                            String py = "";
                            for (int i = 0; i < khNm.length(); i++) {
                                py += toPinyin(khNm.charAt(i)).charAt(0);
                            }
                            bean.setPy(py);
                        }

                        beanList.add(bean);
                    }
                    //默认删除所有，在保存
                    LitePal.deleteAll(DMineCustomerBean.class, "userId = ? and companyId = ?", userId, companyId);
                    LitePal.saveAll(beanList);
                }
            }.start();//开启线程
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 查询我的客户
     */
    public List<MineClientInfo> queryMineClient() {
        try {
            String userId = SPUtils.getID();
            String companyId = SPUtils.getCompanyId();
            List<DMineCustomerBean> beans = LitePal.where("userID = ? and companyID = ?", userId, companyId).limit(10).find(DMineCustomerBean.class);
            List<MineClientInfo> mineClientList = new ArrayList<>();
            if (null != beans && !beans.isEmpty()) {
                for (DMineCustomerBean bean : beans) {
                    MineClientInfo client = new MineClientInfo();
                    client.setId(Integer.valueOf(bean.getCid()));
                    client.setKhTp(Integer.valueOf(bean.getKhTp()));
                    client.setKhNm(bean.getKhNm());
                    client.setMemId(Integer.valueOf(bean.getMemId()));
                    client.setMemberNm(bean.getMemberNm());
                    client.setBranchName(bean.getBranchName());
                    client.setAddress(bean.getAddress());
                    client.setLatitude(bean.getLatitude());
                    client.setLongitude(bean.getLongitude());
                    client.setProvince(bean.getProvince());
                    client.setCity(bean.getCity());
                    client.setArea(bean.getArea());
                    client.setLinkman(bean.getLinkman());
                    client.setMobile(bean.getMobile());
                    client.setTel(bean.getTel());
                    client.setQdtpNm(bean.getQdtpNm());
                    client.setXsjdNm(bean.getXsjdNm());
                    mineClientList.add(client);
                }
                return mineClientList;
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
        return null;
    }

    /**
     * 查询我的客户
     */
    public List<DMineCustomerBean> queryMineClient2(int pageNo, int pageSize, String search, double lat, double lon) {
        try {
            String khNm = "";
            if (!MyStringUtil.isEmpty(search)) {
                khNm += "%";
                for (int i = 0; i < search.length(); i++) {
                    khNm += search.charAt(i) + "%";
                }
            }
            String py = "%" + search + "%";

            String userId = SPUtils.getID();
            String companyId = SPUtils.getCompanyId();

            List<DMineCustomerBean> beans = null;
            double degrees = 0.09;//1度~~111.11km === 0.09 ~~ 10km(过滤10公里之外的数据)
            String juli = " and latitude > " + (lat - degrees) + " and latitude < " + (lat + degrees) + " and longitude > " + (lon - degrees) + " and longitude <" + (lon + degrees);
            if (MyStringUtil.isEmpty(search)) {
//                beans = LitePal.where("userId = ? and companyId = ? "+juli, userId, companyId).limit(pageSize).offset( (pageNo-1) * pageSize).find(DMineCustomerBean.class);
                beans = LitePal.where("userId = ? and companyId = ? " + juli, userId, companyId).find(DMineCustomerBean.class);
            } else {
                beans = LitePal.where("userId = ? and companyId = ? and (khNm like ? or py like ?)", userId, companyId, khNm, py).limit(pageSize).offset((pageNo - 1) * pageSize).find(DMineCustomerBean.class);
            }
            if (null != beans && !beans.isEmpty()) {
                for (DMineCustomerBean bean : beans) {
                    String cLat = bean.getLatitude();
                    String cLon = bean.getLongitude();
                    if (!MyStringUtil.isEmpty(cLat) && !MyStringUtil.isEmpty(cLon)) {
                        LatLng p1 = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
                        LatLng p2 = new LatLng(Double.valueOf(cLat), Double.valueOf(cLon));
                        bean.setDistance((Math.floor(DistanceUtil.getDistance(p1, p2))));
                    } else {
                        bean.setDistance(100000000);//1百万米
                    }
                }
                Collections.sort(beans, new Comparator<DMineCustomerBean>() {
                    public int compare(DMineCustomerBean b1, DMineCustomerBean b2) {
                        return new Double(b1.getDistance()).compareTo(new Double(b2.getDistance())); //升序
                    }
                });
                return beans;
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
        return null;
    }


    /**
     * 查询我的客户
     */
    public List<MineClientInfo> queryMineClient(int pageNo, int pageSize, String search) {
        try {
            String khNm = "";
            if (!MyStringUtil.isEmpty(search)) {
                khNm += "%";
                for (int i = 0; i < search.length(); i++) {
                    khNm += search.charAt(i) + "%";
                }
            }

            String userId = SPUtils.getID();
            String companyId = SPUtils.getCompanyId();

            List<DMineCustomerBean> beans = null;
            if (MyStringUtil.isEmpty(search)) {
                beans = LitePal.where("userId = ? and companyId = ?", userId, companyId).limit(pageSize).offset((pageNo - 1) * pageSize).find(DMineCustomerBean.class);
            } else {
                beans = LitePal.where("userId = ? and companyId = ? and khNm like ?", userId, companyId, khNm).limit(pageSize).offset((pageNo - 1) * pageSize).find(DMineCustomerBean.class);
            }

            List<MineClientInfo> mineClientList = new ArrayList<>();
            if (null != beans && !beans.isEmpty()) {
                for (DMineCustomerBean bean : beans) {
                    MineClientInfo client = new MineClientInfo();
                    client.setId(Integer.valueOf(bean.getCid()));
                    client.setKhTp(Integer.valueOf(bean.getKhTp()));
                    client.setKhNm(bean.getKhNm());
                    client.setMemId(Integer.valueOf(bean.getMemId()));
                    client.setMemberNm(bean.getMemberNm());
                    client.setBranchName(bean.getBranchName());
                    client.setAddress(bean.getAddress());
                    client.setLatitude(bean.getLatitude());
                    client.setLongitude(bean.getLongitude());
                    client.setProvince(bean.getProvince());
                    client.setCity(bean.getCity());
                    client.setArea(bean.getArea());
                    client.setLinkman(bean.getLinkman());
                    client.setMobile(bean.getMobile());
                    client.setTel(bean.getTel());
                    client.setQdtpNm(bean.getQdtpNm());
                    client.setXsjdNm(bean.getXsjdNm());
                    mineClientList.add(client);
                }
                return mineClientList;
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
        return null;
    }


    /**
     * 根据搜索（客户名称）模糊查询我的客户
     */
    public List<MineClientInfo> queryMineClientBySearch(String search, int pageNo, int pageSize) {
        String khNmStr = "";
        if (!MyStringUtil.isEmpty(search)) {
            khNmStr += "%";
            for (int i = 0; i < search.length(); i++) {
//                pinyinStr += toPinyin(search.charAt(i))  + "%" ;//♞:分隔符；%：sql模糊搜索
                khNmStr += search.charAt(i) + "%";//♞:分隔符；%：sql模糊搜索
            }
        }
        List<MineClientInfo> mineClientList = new ArrayList<>();
        try {
            String userId = SPUtils.getID();
            String companyId = SPUtils.getCompanyId();

            List<DMineCustomerBean> beans = null;
            if (MyStringUtil.isEmpty(search)) {
                beans = LitePal.where("userId = ? and companyId = ?", userId, companyId).limit(pageSize).offset((pageNo - 1) * pageSize).find(DMineCustomerBean.class, false);
            } else {
                beans = LitePal.where("userId = ? and companyId = ? and khNm like ?", userId, companyId, khNmStr).limit(pageSize).offset((pageNo - 1) * pageSize).find(DMineCustomerBean.class, false);
            }

            if (beans != null && beans.size() > 0) {
                for (DMineCustomerBean bean : beans) {
                    MineClientInfo mineClientInfo = new MineClientInfo();
                    mineClientInfo.setId(Integer.valueOf(bean.getCid()));
                    mineClientInfo.setKhTp(Integer.valueOf(bean.getKhTp()));
                    mineClientInfo.setKhNm(bean.getKhNm());
                    mineClientInfo.setMemId(Integer.valueOf(bean.getMemId()));
                    mineClientInfo.setMemberNm(bean.getMemberNm());
                    mineClientInfo.setBranchName(bean.getBranchName());
                    mineClientInfo.setAddress(bean.getAddress());
                    mineClientInfo.setLatitude(bean.getLatitude());
                    mineClientInfo.setLongitude(bean.getLongitude());
                    mineClientInfo.setProvince(bean.getProvince());
                    mineClientInfo.setCity(bean.getCity());
                    mineClientInfo.setArea(bean.getArea());
                    mineClientInfo.setLinkman(bean.getLinkman());
                    mineClientInfo.setMobile(bean.getMobile());
                    mineClientInfo.setTel(bean.getTel());
                    mineClientInfo.setQdtpNm(bean.getQdtpNm());
                    mineClientInfo.setXsjdNm(bean.getXsjdNm());

                    mineClientList.add(mineClientInfo);
                }
                return mineClientList;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showError(e);
        }
        return mineClientList;
    }


    /**
     * 根据搜索（客户名称）模糊查询我的客户
     */
    public CacheStep1Bean queryStep1(final String userID, final String companyID) {
        //示例：List<Song> songs = LitePal.where("name like ? and duration < ?", "song%", "200").order("duration").find(Song.class);
        CacheStep1Bean bean = LitePal.where("userID = ? and companyID = ?",
                userID, companyID).findFirst(CacheStep1Bean.class);
        return bean;
    }

    /**
     * 根据搜索（客户名称）模糊查询我的客户
     */
    public List<CacheStepPicBean> queryStep1Pic(final String userID, final String companyID, final String step) {
        //示例：List<Song> songs = LitePal.where("name like ? and duration < ?", "song%", "200").order("duration").find(Song.class);
        List<CacheStepPicBean> beans = LitePal.where("userID = ? and companyID = ? and step = ?", userID, companyID, step).find(CacheStepPicBean.class);
        return beans;
    }

    //TODO--------------------------------------消息：start----------------------------------------------------------------

    /**
     * 消息
     */
    public void saveMessage(final DMessageBean bean) {
        try {
            new Thread() {
                public void run() {
                    bean.save();
                }
            }.start();//开启线程
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询板块信息：order by isread, addtime desc（先按是否已读排序，再按时间排序）
     */
    public DMessageBean queryMessageById(String bankuai, String type, String mark, String belongId, String mainId) {
        DMessageBean beans = null;
        try {
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            beans = LitePal.where("userId = ? and companyId = ? and bankuai = ? and type = ? and mark = ? and belongId = ? and mainId = ?", userId, companyId, bankuai, type, mark, belongId, mainId).findFirst(DMessageBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beans;
    }

    /**
     * 查询信息
     */
    public List<DMessageBean> queryMessageList() {
        List<DMessageBean> beans = null;
        try {
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            beans = LitePal.where("userId = ? and companyId = ? order by addTime desc", userId, companyId).find(DMessageBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beans;
    }

    /**
     * 查询板块信息：order by isread, addtime desc（先按是否已读排序，再按时间排序）
     */
    public List<DMessageBean> queryMessageByBankuai(String bankuai) {
        List<DMessageBean> beans = null;
        try {
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            beans = LitePal.where("userId = ? and companyId = ? and bankuai = ? ORDER BY isRead ASC, addTime DESC", userId, companyId, bankuai).find(DMessageBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beans;
    }

    /**
     * 查询板块信息：order by isread, addtime desc（先按是否已读排序，再按时间排序）
     */
    public List<DMessageBean> queryMessageBySearch(String search, String bankuai) {
        List<DMessageBean> beans = null;
        try {
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            if (!TextUtils.isEmpty(bankuai)) {
                beans = LitePal.where("userId = ? and companyId = ? and bankuai = ? and msg like ? ORDER BY isRead ASC, addTime DESC", userId, companyId, bankuai, "%" + search + "%").find(DMessageBean.class);
            } else {
                beans = LitePal.where("userId = ? and companyId = ? and msg like ? ORDER BY isRead ASC, addTime DESC", userId, companyId, "%" + search + "%").find(DMessageBean.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return beans;
    }

    /**
     * 查询未读消息数量
     */
    public int queryUnReadMessageCount(String bankuai) {
        int count = 0;
        List<DMessageBean> beans = null;
        try {
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            if (TextUtils.isEmpty(bankuai)) {
                //所有的未读信息数
                beans = LitePal.where("userId = ? and companyId = ? and isRead != ?", userId, companyId, "1").find(DMessageBean.class);
            } else {
                //分类的未读信息数
                beans = LitePal.where("userId = ? and companyId = ? and bankuai = ? and isRead != ?", userId, companyId, bankuai, "1").find(DMessageBean.class);
            }
            if (null != beans && !beans.isEmpty()) {
                count = beans.size();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 标记已读
     */
    public int updateMessageUnReadBankuai(String bankuai) {
        int count = 0;
        try {
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            ContentValues values = new ContentValues();
            values.put("isRead", "1");
            count = LitePal.updateAll(DMessageBean.class, values, "bankuai=? and userId=? and companyId=?", bankuai, userId, companyId);
        } catch (Exception e) {
        }
        return count;
    }

    /**
     * 标记已读
     */
    public int delMessage(DMessageBean bean) {
        int count = 0;
        try {
            count = bean.delete();
        } catch (Exception e) {
        }
        return count;
    }

    /**
     * 是否同意
     */
    public int updateMessage(DMessageBean bean) {
        int count = 0;
        try {
            String type = bean.getType();
            String bankuai = "" + bean.getBankuai();
            String mainId = "" + bean.getMainId();
//            String msgId = "" + bean.getMsgId();
            String userId = "" + bean.getUserId();
            String companyId = bean.getCompanyId();
            count = bean.updateAll("type=? and bankuai=? and mainId=? and userId=? and companyId=?", type, bankuai, mainId, userId, companyId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 分类消息:注bankuai -1 为总分类
     * bankuai:(-1.总消息；1.系统通知；2.审批;3.易办事；4.拜访查询-评论；6.沟通；10.日志-工作汇报；11.商城）
     */
    public void saveCategroyMessage(final DCategoryMessageBean bean) {
        try {
            new Thread() {
                public void run() {
                    String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
                    String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
                    bean.setCount(queryCategroyMessageCount(String.valueOf(bean.getBankuai())) + 1);
                    bean.saveOrUpdate("userId = ? and companyId = ? and bankuai = ?", userId, companyId, String.valueOf(bean.getBankuai()));
                    DCategoryMessageBean sumBean = new DCategoryMessageBean();
                    sumBean.setUserId(bean.getUserId());
                    sumBean.setCompanyId(bean.getCompanyId());
                    sumBean.setBankuai(-1);
                    sumBean.setCount(queryCategroyMessageCount(String.valueOf(ConstantUtils.Messeage.M_SUM)) + 1);
                    sumBean.saveOrUpdate("userId = ? and companyId = ? and bankuai = ?", userId, companyId, String.valueOf(-1));
                }
            }.start();//开启线程
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    /**
     * 查询分类消息数量：注bankuai -1 为总分类
     * bankuai:(-1.总消息；1.系统通知；2.审批;3.易办事；4.拜访查询-评论；6.沟通；10.日志-工作汇报；11.商城）
     */
    public int queryCategroyMessageCount(String bankuai) {
        int count = 0;
        try {
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            List<DCategoryMessageBean> beans = LitePal.where("userId = ? and companyId = ? and bankuai = ?", userId, companyId, bankuai).find(DCategoryMessageBean.class);
            if (null != beans && !beans.isEmpty()) {
                count = beans.get(0).getCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 标记已读:数量为0
     * bankuai:(-1.总消息；1.系统通知；2.审批;3.易办事；4.拜访查询-评论；6.沟通；10.日志-工作汇报；11.商城,12.公告）
     */
    public int updateCategroyMessageCount(String bankuai, boolean isUpdateSumCount) {
        int count = 0;
        try {
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            if (isUpdateSumCount) {
                //修改板块的数量同时也修改总数量（如点击首页的公告）
                int currentCount = queryCategroyMessageCount(bankuai);
                int sumCount = queryCategroyMessageCount(String.valueOf(ConstantUtils.Messeage.M_SUM));
                if (sumCount > 0) {
                    DCategoryMessageBean sumBean = new DCategoryMessageBean();
                    sumBean.setUserId(userId);
                    sumBean.setCompanyId(companyId);
                    sumBean.setBankuai(-1);
                    sumBean.setCount(sumCount - currentCount);
                    sumBean.saveOrUpdate("userId = ? and companyId = ? and bankuai = ?", userId, companyId, String.valueOf(String.valueOf(ConstantUtils.Messeage.M_SUM)));
                }
            }
            count = LitePal.deleteAll(DCategoryMessageBean.class, "bankuai=? and userId=? and companyId=?", bankuai, userId, companyId);
        } catch (Exception e) {
        }
        return count;
    }
    //TODO--------------------------------------消息：end----------------------------------------------------------------

    //TODO--------------------------------------拜访步骤的：start----------------------------------------------------------------

    /**
     * 保存拜访步骤1：签到拍照
     */
    public void saveStep1(final DStep1Bean bean) {
        try {
            new Thread() {
                public void run() {
                    bean.save();
                }
            }.start();//开启线程
        } catch (Exception e) {
        }
    }

    /**
     * 查询拜访步骤1：签到拍照
     */
    public DStep1Bean queryStep1() {
        DStep1Bean bean = null;
        try {
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            bean = LitePal.where("userId = ? and companyId = ? and submitCount < 3", userId, companyId).findFirst(DStep1Bean.class);
        } catch (Exception e) {
        } finally {
            return bean;
        }
    }

    /**
     * 查询拜访步骤1：签到拍照
     */
    public List<DStep1Bean> queryAllStep1() {
        List<DStep1Bean> beans = null;
        try {
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            beans = LitePal.where("userId = ? and companyId = ?", userId, companyId).find(DStep1Bean.class);
        } catch (Exception e) {
        } finally {
            return beans;
        }
    }

    /**
     * 查询拜访步骤1：签到拍照
     */
    public List<DStep1Bean> queryStep1(int pageNo, int pageSize) {
        List<DStep1Bean> beans = null;
        try {
            int offset;
            if(pageNo == 1){
                offset = 0;
            }else{
                offset = (pageNo - 1) * pageSize;
            }
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            beans = LitePal.where("userId = ? and companyId = ?", userId, companyId)
                    .limit(pageSize)
                    .offset(offset)
                    .order("time desc")
                    .find(DStep1Bean.class);
        } catch (Exception e) {
        } finally {
            return beans;
        }
    }

    /**
     * 查询拜访步骤2：生动化
     */
    public List<DStep2Bean> queryStep2(int pageNo, int pageSize) {
        List<DStep2Bean> beans = null;
        try {
            int offset;
            if(pageNo == 1){
                offset = 0;
            }else{
                offset = (pageNo - 1) * pageSize;
            }
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            beans = LitePal.where("userId = ? and companyId = ?", userId, companyId)
                    .limit(pageSize)
                    .offset(offset)
                    .order("time desc")
                    .find(DStep2Bean.class);
        } catch (Exception e) {
        } finally {
            return beans;
        }
    }
    /**
     * 查询拜访步骤3
     */
    public List<DStep3Bean> queryStep3(int pageNo, int pageSize) {
        List<DStep3Bean> beans = null;
        try {
            int offset;
            if(pageNo == 1){
                offset = 0;
            }else{
                offset = (pageNo - 1) * pageSize;
            }
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            beans = LitePal.where("userId = ? and companyId = ?", userId, companyId)
                    .limit(pageSize)
                    .offset(offset)
                    .order("time desc")
                    .find(DStep3Bean.class);
        } catch (Exception e) {
        } finally {
            return beans;
        }
    }
    /**
     * 查询拜访步骤6：签退
     */
    public List<DStep6Bean> queryStep6(int pageNo, int pageSize) {
        List<DStep6Bean> beans = null;
        try {
            int offset;
            if(pageNo == 1){
                offset = 0;
            }else{
                offset = (pageNo - 1) * pageSize;
            }
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            beans = LitePal.where("userId = ? and companyId = ?", userId, companyId)
                    .limit(pageSize)
                    .offset(offset)
                    .order("time desc")
                    .find(DStep6Bean.class);
        } catch (Exception e) {
        } finally {
            return beans;
        }
    }

    /**
     * 删除拜访步骤1：签到拍照
     */
    public int delStep1(DStep1Bean bean) {
        int count = 0;
        try {
            count = bean.delete();
        } catch (Exception e) {
        }
        return count;
    }

    /**
     * 修改拜访步骤1：签到拍照
     */
    public void updateStep1(DStep1Bean bean) {
        try {
            bean.setSubmitCount(bean.getSubmitCount() + 1);
            bean.save();
        } catch (Exception e) {
        }
    }

    /**
     * 保存拜访步骤2：生动化
     */
    public void saveStep2(final DStep2Bean bean) {
        try {
            new Thread() {
                public void run() {
                    bean.save();
                }
            }.start();//开启线程
        } catch (Exception e) {
        }
    }

    /**
     * 查询拜访步骤2：生动化
     */
    public DStep2Bean queryStep2() {
        DStep2Bean bean = null;
        try {
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            bean = LitePal.where("userId = ? and companyId = ? and submitCount < 3", userId, companyId).findFirst(DStep2Bean.class);
        } catch (Exception e) {
        } finally {
            return bean;
        }
    }

    /**
     * 查询拜访步骤2：生动化
     */
    public List<DStep2Bean> queryAllStep2() {
        List<DStep2Bean> beans = null;
        try {
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            beans = LitePal.where("userId = ? and companyId = ?", userId, companyId).find(DStep2Bean.class);
        } catch (Exception e) {
        } finally {
            return beans;
        }
    }

    /**
     * 查询拜访步骤2：生动化
     */
    public DStep2Bean queryStep2(String cid, String date) {
        DStep2Bean bean = null;
        try {
            String startTime = date + " 00:00:00";
            String endTime = date + " 23:59:59";
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            bean = LitePal.where("userId = ? and companyId = ? and cid = ? and time >=? and time <= ? ", userId, companyId, cid, startTime, endTime).findFirst(DStep2Bean.class);
        } catch (Exception e) {
        } finally {
            return bean;
        }
    }

    /**
     * 删除拜访步骤2：生动化
     */
    public int delStep2(DStep2Bean bean) {
        int count = 0;
        try {
            count = bean.delete();
        } catch (Exception e) {
        }
        return count;
    }

    /**
     * 修改拜访步骤2：生动化
     */
    public void updateStep2(DStep2Bean bean) {
        try {
            bean.setSubmitCount(bean.getSubmitCount() + 1);
            bean.save();
        } catch (Exception e) {
        }
    }

    /**
     * 保存拜访步骤3：库存检查
     */
    public void saveStep3(final DStep3Bean bean) {
        try {
            new Thread() {
                public void run() {
                    bean.save();
                }
            }.start();//开启线程
        } catch (Exception e) {
        }
    }

    /**
     * 查询拜访步骤3：库存检查
     */
    public DStep3Bean queryStep3() {
        DStep3Bean bean = null;
        try {
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            bean = LitePal.where("userId = ? and companyId = ? and submitCount < 3", userId, companyId).findFirst(DStep3Bean.class);
        } catch (Exception e) {
        } finally {
            return bean;
        }
    }

    /**
     * 查询拜访步骤3
     */
    public DStep3Bean queryStep3(String cid, String date) {
        DStep3Bean bean = null;
        try {
            String startTime = date + " 00:00:00";
            String endTime = date + " 23:59:59";
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            bean = LitePal.where("userId = ? and companyId = ? and cid = ? and time >=? and time <= ? ", userId, companyId, cid, startTime, endTime).findFirst(DStep3Bean.class);
        } catch (Exception e) {
        } finally {
            return bean;
        }
    }

    /**
     * 查询拜访步骤3：库存检查
     */
    public List<DStep3Bean> queryAllStep3() {
        List<DStep3Bean> beans = null;
        try {
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            beans = LitePal.where("userId = ? and companyId = ?", userId, companyId).find(DStep3Bean.class);
        } catch (Exception e) {
        } finally {
            return beans;
        }
    }

    /**
     * 删除拜访步骤3：库存检查
     */
    public int delStep3(DStep3Bean bean) {
        int count = 0;
        try {
            count = bean.delete();
        } catch (Exception e) {
        }
        return count;
    }

    /**
     * 修改拜访步骤3：库存检查
     */
    public void updateStep3(DStep3Bean bean) {
        try {
            bean.setSubmitCount(bean.getSubmitCount() + 1);
            bean.save();
        } catch (Exception e) {
        }
    }

    /**
     * 保存拜访步骤4：销售小结
     */
    public void saveStep4(final DStep4Bean bean) {
        try {
            new Thread() {
                public void run() {
                    bean.save();
                }
            }.start();//开启线程
        } catch (Exception e) {
        }
    }

    /**
     * 查询拜访步骤4：销售小结
     */
    public DStep4Bean queryStep4() {
        DStep4Bean bean = null;
        try {
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            bean = LitePal.where("userId = ? and companyId = ? and submitCount < 3", userId, companyId).findFirst(DStep4Bean.class);
        } catch (Exception e) {
        } finally {
            return bean;
        }
    }

    /**
     * 查询拜访步骤4：销售小结
     */
    public List<DStep4Bean> queryAllStep4() {
        List<DStep4Bean> beans = null;
        try {
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            beans = LitePal.where("userId = ? and companyId = ?", userId, companyId).find(DStep4Bean.class);
        } catch (Exception e) {
        } finally {
            return beans;
        }
    }

    /**
     * 删除拜访步骤4：销售小结
     */
    public int delStep4(DStep4Bean bean) {
        int count = 0;
        try {
            count = bean.delete();
        } catch (Exception e) {
        }
        return count;
    }

    /**
     * 修改拜访步骤4：销售小结
     */
    public void updateStep4(DStep4Bean bean) {
        try {
            bean.setSubmitCount(bean.getSubmitCount() + 1);
            bean.save();
        } catch (Exception e) {
        }
    }

    /**
     * 保存拜访步骤5：下单
     */
    public void saveStep5(final DStep5Bean bean) {
        try {
            new Thread() {
                public void run() {
                    bean.save();
                }
            }.start();//开启线程
        } catch (Exception e) {
        }
    }

    /**
     * 查询拜访步骤5：下单
     */
    public DStep5Bean queryStep5() {
        DStep5Bean bean = null;
        try {
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            bean = LitePal.where("userId = ? and companyId = ? and submitCount < 3 and autoType = 1", userId, companyId).findFirst(DStep5Bean.class);
        } catch (Exception e) {
        } finally {
            return bean;
        }
    }

    /**
     * 查询订单
     * type: 1.拜访下单(添加或修改) 2:电话下单(添加) 3：订货下单列表（查看或修改）4：退货(添加或修改) 5：退货下单列表（查看或修改）
     * cid:客户id
     */
    public DStep5Bean queryOrder(String type, String cid) {
        DStep5Bean bean = null;
        try {
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            if (MyStringUtil.isEmpty(cid)) {
                bean = LitePal.where("userId = ? and companyId = ? and submitCount < 3 and type = ?", userId, companyId, type).findFirst(DStep5Bean.class);
            } else {
                bean = LitePal.where("userId = ? and companyId = ? and submitCount < 3 and type = ? and cid = ?", userId, companyId, type, cid).findFirst(DStep5Bean.class);
            }
        } catch (Exception e) {
        } finally {
            return bean;
        }
    }

    /**
     * 查询拜访步骤5：下单
     */
    public List<DStep5Bean> queryAllStep5() {
        List<DStep5Bean> beans = null;
        try {
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            beans = LitePal.where("userId = ? and companyId = ?", userId, companyId).find(DStep5Bean.class);
        } catch (Exception e) {
        } finally {
            return beans;
        }
    }

    /**
     * 查询拜访步骤5：下单//type 1：拜访下单(添加或修改) 2:电话下单(添加) 3：订货下单列表（查看或修改）4：退货(添加或修改) 5：退货下单列表（查看或修改）
     */
    public List<DStep5Bean> queryAllStep5(String type, boolean equal) {
        List<DStep5Bean> beans = null;
        try {
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            if (equal) {
                beans = LitePal.where("userId = ? and companyId = ? and type = ?", userId, companyId, type).find(DStep5Bean.class);
            } else {
                beans = LitePal.where("userId = ? and companyId = ? and type != ?", userId, companyId, type).find(DStep5Bean.class);
            }

        } catch (Exception e) {
        } finally {
            return beans;
        }
    }

    /**
     * 删除拜访步骤5：下单
     */
    public int delStep5(DStep5Bean bean) {
        int count = 0;
        try {
            count = bean.delete();
        } catch (Exception e) {
        }
        return count;
    }

    /**
     * 修改拜访步骤5：下单
     */
    public void updateStep5(DStep5Bean bean) {
        try {
            bean.setSubmitCount(bean.getSubmitCount() + 1);
            bean.save();
        } catch (Exception e) {
        }
    }

    /**
     * 保存拜访步骤6：拍照签退
     */
    public void saveStep6(final DStep6Bean bean) {
        try {
            new Thread() {
                public void run() {
                    bean.save();
                }
            }.start();//开启线程
        } catch (Exception e) {
        }
    }

    /**
     * 查询拜访步骤6：拍照签退
     */
    public DStep6Bean queryStep6() {
        DStep6Bean bean = null;
        try {
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            bean = LitePal.where("userId = ? and companyId = ? and submitCount < 3", userId, companyId).findFirst(DStep6Bean.class);
        } catch (Exception e) {
        } finally {
            return bean;
        }
    }

    /**
     * 查询拜访步骤6:签退
     */
    public DStep6Bean queryStep6(String cid, String date) {
        DStep6Bean bean = null;
        try {
            String startTime = date + " 00:00:00";
            String endTime = date + " 23:59:59";
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            bean = LitePal.where("userId = ? and companyId = ? and cid = ? and time >=? and time <= ? ", userId, companyId, cid, startTime, endTime).findFirst(DStep6Bean.class);
        } catch (Exception e) {
        } finally {
            return bean;
        }
    }

    /**
     * 查询拜访步骤6：拍照签退
     */
    public List<DStep6Bean> queryAllStep6() {
        List<DStep6Bean> beans = null;
        try {
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            beans = LitePal.where("userId = ? and companyId = ?", userId, companyId).find(DStep6Bean.class);
        } catch (Exception e) {
        } finally {
            return beans;
        }
    }

    /**
     * 删除拜访步骤3：拍照签退
     */
    public int delStep6(DStep6Bean bean) {
        int count = 0;
        try {
            count = bean.delete();
        } catch (Exception e) {
        }
        return count;
    }

    /**
     * 修改拜访步骤6：拍照签退
     */
    public void updateStep6(DStep6Bean bean) {
        try {
            bean.setSubmitCount(bean.getSubmitCount() + 1);
            bean.save();
        } catch (Exception e) {
        }
    }


    /**
     * 保存所有商品
     */
    public void saveWare(final List<DWareBean> dataList) {
        try {
            new Thread() {
                public void run() {
                    String userId = SPUtils.getID();
                    String companyId = SPUtils.getCompanyId();
                    if (null == dataList || dataList.isEmpty()) {
                        return;
                    }
                    //默认删除所有，在保存
                    LitePal.deleteAll(DWareBean.class, "userId = ? and companyId = ?", userId, companyId);
                    LitePal.saveAll(dataList);
                }
            }.start();//开启线程
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


    /**
     * 查询所有商品
     */
    public List<DWareBean> queryWare(String search, String wareType, int pageNo, int PageSize) {
        List<DWareBean> beans = null;
        try {
            String wareNm = "";
            if (!MyStringUtil.isEmpty(search)) {
                wareNm += "%";
                for (int i = 0; i < search.length(); i++) {
                    wareNm += search.charAt(i) + "%";
                }
            }
            String py = "%" + search + "%";

            String userId = SPUtils.getID();
            String companyId = SPUtils.getCompanyId();

            if (MyStringUtil.isEmpty(wareType)) {
                if (MyStringUtil.isEmpty(search)) {
                    beans = LitePal.where("userId = ? and companyId = ? ", userId, companyId).limit(PageSize).offset((pageNo - 1) * PageSize).find(DWareBean.class);
                } else {
                    beans = LitePal.where("userId = ? and companyId = ? and (wareNm like ? or py like ?)", userId, companyId, wareNm, py).limit(PageSize).offset((pageNo - 1) * PageSize).find(DWareBean.class);
                }
            } else {
//                beans = LitePal.where("userId = ? and companyId = ? and wareType = ?", userId, companyId, wareType).limit(PageSize).offset((pageNo-1) * PageSize).find(DWareBean.class);
                beans = LitePal.where("userId = ? and companyId = ? and wareType in (" + wareType + ") order by wareId ASC", userId, companyId).limit(PageSize).offset((pageNo - 1) * PageSize).find(DWareBean.class);
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        } finally {
            return beans;
        }
    }

    /**
     * 缓存所有商品
     */
    public void saveWareType(final List<DWareTypeBean> dataList) {
        try {
            new Thread() {
                public void run() {
                    String userId = SPUtils.getID();
                    String companyId = SPUtils.getCompanyId();
                    if (null == dataList || dataList.isEmpty()) {
                        return;
                    }
                    //默认删除所有，在保存
                    LitePal.deleteAll(DWareTypeBean.class, "userId = ? and companyId = ?", userId, companyId);
                    LitePal.saveAll(dataList);
                }
            }.start();//开启线程
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


    /**
     * 查询商品分类
     */
    public List<DWareTypeBean> queryWareType() {
        List<DWareTypeBean> beans = null;
        try {
            String userId = SPUtils.getID();
            String companyId = SPUtils.getCompanyId();
            beans = LitePal.where("userId = ? and companyId = ?", userId, companyId).find(DWareTypeBean.class);
        } catch (Exception e) {
            ToastUtils.showError(e);
        } finally {
            return beans;
        }
    }


    /**
     * 记录今天是否已缓存(1:我的客户；2：商品分类及商品)
     */
    public DRecordUpdateBean queryRecodeUpdate(String model) {
        DRecordUpdateBean bean = null;
        try {
            String userId = SPUtils.getSValues(ConstantUtils.Sp.USER_ID);
            String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
            bean = LitePal.where("userId = ? and companyId = ? and model = ?", userId, companyId, model).findFirst(DRecordUpdateBean.class);
        } catch (Exception e) {
        } finally {
            return bean;
        }
    }

    /**
     * 记录今天是否已缓存(1:我的客户；2：商品分类及商品)
     */
    public void updateRecodeUpdate(DRecordUpdateBean bean) {
        try {
            bean.setTime(MyTimeUtils.getTodayStr());
            bean.setUpdate(1);
            bean.save();
        } catch (Exception e) {
        }
    }
    //TODO--------------------------------------拜访步骤的：end----------------------------------------------------------------


    /**
     * 保存库存商品
     */
    public void saveStkWare(final List<DStkCheckWareBean> dataList, final int type) {
        try {
            new Thread() {
                public void run() {
                    String userId = SPUtils.getID();
                    String companyId = SPUtils.getCompanyId();
                    if (null == dataList || dataList.isEmpty()) {
                        return;
                    }
                    //默认删除所有，在保存
                    LitePal.deleteAll(DStkCheckWareBean.class, "userId = ? and companyId = ? and type = ?", userId, companyId, ""+type);
                    LitePal.saveAll(dataList);
                }
            }.start();//开启线程
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 查询库存商品
     */
    public List<DStkCheckWareBean> queryStkWare(int type) {
        List<DStkCheckWareBean> beans = null;
        try {
            String userId = SPUtils.getID();
            String companyId = SPUtils.getCompanyId();
            beans = LitePal.where("userId = ? and companyId = ? and type = ?",userId, companyId, ""+type).find(DStkCheckWareBean.class);
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
        return beans;
    }

    /**
     * 删除库存商品
     */
    public int delStkWare(int type) {
        int count = 0;
        try {
            String userId = SPUtils.getID();
            String companyId = SPUtils.getCompanyId();
            LitePal.deleteAll(DStkCheckWareBean.class, "userId = ? and companyId = ? and type=?  ", userId, companyId, ""+type);
        } catch (Exception e) {
        }
        return count;
    }

    //*******************************************配送单需要导航的客户：start****************************************************
    /**
     * 保存：配送单导航客户
     */
    public void saveDeliveryCustomer(final List<DDeliveryCustomerBean> dataList) {
        try {
            new Thread() {
                public void run() {
                    String userId = SPUtils.getID();
                    String companyId = SPUtils.getCompanyId();
                    if (null == dataList || dataList.isEmpty()) {
                        return;
                    }
                    //默认删除所有，在保存
                    LitePal.deleteAll(DStkCheckWareBean.class, "userId = ? and companyId = ?", userId, companyId);
                    LitePal.saveAll(dataList);
                }
            }.start();//开启线程
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 查询：配送单导航客户
     */
    public List<DDeliveryCustomerBean> queryDeliveryCustomer() {
        List<DDeliveryCustomerBean> beans = null;
        try {
            String userId = SPUtils.getID();
            String companyId = SPUtils.getCompanyId();
            beans = LitePal.where("userId = ? and companyId = ?",userId, companyId).find(DDeliveryCustomerBean.class);
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
        return beans;
    }

    /**
     * 删除:配送单导航客户
     */
    public int delDeliveryCustomer(List<DDeliveryCustomerBean> list) {
        int count = 0;
        try {
            if(list != null){
                for (DDeliveryCustomerBean bean:list) {
                    bean.delete();
                }
            }
        } catch (Exception e) {
        }
        return count;
    }
    /**
     * 删除:配送单导航客户
     */
    public int updateDeliveryCustomer(DDeliveryCustomerBean bean) {
        int count = 0;
        try {
            if(bean!=null){
                bean.save();
            }
        } catch (Exception e) {
        }
        return count;
    }
    //*******************************************配送单需要导航的客户：start****************************************************


}
