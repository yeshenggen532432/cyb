package com.qwb.view.call.model;

import com.qwb.view.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 拜访查询--拜访纪录
 *
 */
public class CallonRecordBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5245250141200166272L;
	private String Dqddate;//日期
	private String Dqdtime;//签到时间
	private String Daddress;//签到地址
	private String Dddtime;//离开时间
	private String Dbcbfzj;//拜访总结
	private String Ddbsx;//代办事项
	private String setime;//时长
	private String khaddress;//客户地址
	private String xxzt;//新鲜度（临期，正常）
	private String jd1;//签到地址--经度
	private String wd1;//签到地址--纬度
	private String jd2;//客户地址--经度
	private String wd2;//客户地址--纬度
	private String qtaddress;//签退地址--纬度
	private String jd3;//签退地址--经度
	private String wd3;//签退地址--纬度
	
	//1拜访签到拍照
	private String count1;//1拜访签到拍照（1有信息；0没信息）
	private String hbzt;//及时更换外观破损，肮脏的海报招贴
	private String ggyy;//拆除过时的附有旧广告用语的宣传品
	private String isXy;//是否显眼（1有，2无）
	private String remo;//摘要
	private List<BfqdpzBean> bfqdpzPic=new ArrayList<>();//图片组
	
	//2
	private String count2;//2生动化检查（1有信息；0没信息）
	private String pophb;//POP海报
	private String cq;//串旗
	private String wq;//围裙
//	private String isXy;//是否显眼（1有；2无）
	private String remo1;//生动化摘要
	private String remo2;//堆头摘要
	private List<BfqdpzBean> bfsdhjcPic1=new ArrayList<>();//图片组1
	private List<BfqdpzBean> bfsdhjcPic2=new ArrayList<>();//图片组2
	
	private String count3;//3:陈列采集（1有信息；0没信息）
	private List<BfClcjBean> list1=new ArrayList<>();//陈列采集
	private String count4;///4销售小结（1有信息；0没信息）
	private List<BfxsxjBean> list2=new ArrayList<>();//销售小结
	
	private String count5;//5供货下单（1有信息；0没信息）
	private String shr;//收货人
	private String tel;//电话
	private String address;//地址
//	private String remo;//备注
//	private String zje;//总金额
//	private String zdzk;//整单折扣
//	private String cjje;//成交金额
	private List<BfdhxdBean> orderDetail=new ArrayList<>();//签到日期
	
	private String count6;//6道谢并告知下次拜访（1有信息；0没信息）
	private String bcbfzj;//拜访总结
	private String dbsx;//代办事项
	private String xcdate;//下次拜访日期
	private List<BfqdpzBean> bfgzxcPic=new ArrayList<>();//图片
	
	private String voiceUrl;//语音路径
	private String voiceTime;//语音时间
	
	public String getVoiceUrl() {
		return voiceUrl;
	}
	public void setVoiceUrl(String voiceUrl) {
		this.voiceUrl = voiceUrl;
	}
	public String getVoiceTime() {
		return voiceTime;
	}
	public void setVoiceTime(String voiceTime) {
		this.voiceTime = voiceTime;
	}
	public String getJd1() {
		return jd1;
	}
	public void setJd1(String jd1) {
		this.jd1 = jd1;
	}
	public String getWd1() {
		return wd1;
	}
	public void setWd1(String wd1) {
		this.wd1 = wd1;
	}
	public String getJd2() {
		return jd2;
	}
	public void setJd2(String jd2) {
		this.jd2 = jd2;
	}
	public String getWd2() {
		return wd2;
	}
	public void setWd2(String wd2) {
		this.wd2 = wd2;
	}
	public String getDqddate() {
		return Dqddate;
	}
	public void setDqddate(String dqddate) {
		Dqddate = dqddate;
	}
	public String getDqdtime() {
		return Dqdtime;
	}
	public void setDqdtime(String dqdtime) {
		Dqdtime = dqdtime;
	}
	public String getDaddress() {
		return Daddress;
	}
	public void setDaddress(String daddress) {
		Daddress = daddress;
	}
	public String getDddtime() {
		return Dddtime;
	}
	public void setDddtime(String dddtime) {
		Dddtime = dddtime;
	}
	public String getDbcbfzj() {
		return Dbcbfzj;
	}
	public void setDbcbfzj(String dbcbfzj) {
		Dbcbfzj = dbcbfzj;
	}
	public String getDdbsx() {
		return Ddbsx;
	}
	public void setDdbsx(String ddbsx) {
		Ddbsx = ddbsx;
	}
	public String getSetime() {
		return setime;
	}
	public void setSetime(String setime) {
		this.setime = setime;
	}
	public String getKhaddress() {
		return khaddress;
	}
	public void setKhaddress(String khaddress) {
		this.khaddress = khaddress;
	}
	public String getCount1() {
		return count1;
	}
	public void setCount1(String count1) {
		this.count1 = count1;
	}
	public String getHbzt() {
		return hbzt;
	}
	public void setHbzt(String hbzt) {
		this.hbzt = hbzt;
	}
	public String getGgyy() {
		return ggyy;
	}
	public void setGgyy(String ggyy) {
		this.ggyy = ggyy;
	}
	public String getIsXy() {
		return isXy;
	}
	public void setIsXy(String isXy) {
		this.isXy = isXy;
	}
	public String getRemo() {
		return remo;
	}
	public void setRemo(String remo) {
		this.remo = remo;
	}
	public List<BfqdpzBean> getBfqdpzPic() {
		return bfqdpzPic;
	}
	public void setBfqdpzPic(List<BfqdpzBean> bfqdpzPic) {
		this.bfqdpzPic = bfqdpzPic;
	}
	public String getCount2() {
		return count2;
	}
	public void setCount2(String count2) {
		this.count2 = count2;
	}
	public String getPophb() {
		return pophb;
	}
	public void setPophb(String pophb) {
		this.pophb = pophb;
	}
	public String getCq() {
		return cq;
	}
	public void setCq(String cq) {
		this.cq = cq;
	}
	public String getWq() {
		return wq;
	}
	public void setWq(String wq) {
		this.wq = wq;
	}
	public String getRemo1() {
		return remo1;
	}
	public void setRemo1(String remo1) {
		this.remo1 = remo1;
	}
	public String getRemo2() {
		return remo2;
	}
	public void setRemo2(String remo2) {
		this.remo2 = remo2;
	}
	public List<BfqdpzBean> getBfsdhjcPic1() {
		return bfsdhjcPic1;
	}
	public void setBfsdhjcPic1(List<BfqdpzBean> bfsdhjcPic1) {
		this.bfsdhjcPic1 = bfsdhjcPic1;
	}
	public List<BfqdpzBean> getBfsdhjcPic2() {
		return bfsdhjcPic2;
	}
	public void setBfsdhjcPic2(List<BfqdpzBean> bfsdhjcPic2) {
		this.bfsdhjcPic2 = bfsdhjcPic2;
	}
	public String getCount3() {
		return count3;
	}
	public void setCount3(String count3) {
		this.count3 = count3;
	}
	public List<BfClcjBean> getList1() {
		return list1;
	}
	public void setList1(List<BfClcjBean> list1) {
		this.list1 = list1;
	}
	public String getCount4() {
		return count4;
	}
	public void setCount4(String count4) {
		this.count4 = count4;
	}
	public List<BfxsxjBean> getList2() {
		return list2;
	}
	public void setList2(List<BfxsxjBean> list2) {
		this.list2 = list2;
	}
	public String getCount5() {
		return count5;
	}
	public void setCount5(String count5) {
		this.count5 = count5;
	}
	public String getShr() {
		return shr;
	}
	public void setShr(String shr) {
		this.shr = shr;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<BfdhxdBean> getOrderDetail() {
		return orderDetail;
	}
	public void setOrderDetail(List<BfdhxdBean> orderDetail) {
		this.orderDetail = orderDetail;
	}
	public String getCount6() {
		return count6;
	}
	public void setCount6(String count6) {
		this.count6 = count6;
	}
	public String getBcbfzj() {
		return bcbfzj;
	}
	public void setBcbfzj(String bcbfzj) {
		this.bcbfzj = bcbfzj;
	}
	public String getDbsx() {
		return dbsx;
	}
	public void setDbsx(String dbsx) {
		this.dbsx = dbsx;
	}
	public String getXcdate() {
		return xcdate;
	}
	public void setXcdate(String xcdate) {
		this.xcdate = xcdate;
	}
	public List<BfqdpzBean> getBfgzxcPic() {
		return bfgzxcPic;
	}
	public void setBfgzxcPic(List<BfqdpzBean> bfgzxcPic) {
		this.bfgzxcPic = bfgzxcPic;
	}
	public String getXxzt() {
		return xxzt;
	}
	public void setXxzt(String xxzt) {
		this.xxzt = xxzt;
	}
	public String getQtaddress() {
		return qtaddress;
	}
	public void setQtaddress(String qtaddress) {
		this.qtaddress = qtaddress;
	}
	public String getJd3() {
		return jd3;
	}
	public void setJd3(String jd3) {
		this.jd3 = jd3;
	}
	public String getWd3() {
		return wd3;
	}
	public void setWd3(String wd3) {
		this.wd3 = wd3;
	}
	

}
