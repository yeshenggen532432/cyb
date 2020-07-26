package com.qwb.view.step.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.qwb.view.base.model.BaseBean;

import java.util.List;

/**
 * 商品信息列表
 * Parcelable
 * 在构造函数中读取数据，读取的顺序要和写入的顺序一样，必须按照成员变量声明的顺序来，不然会出问题，后续成员变量增加的话，都要按顺序来，
 * 不要随便在中间插入，按顺序，按顺序，按顺序，不按顺序会出现数据序列化传递过程中部分数据丢失的问题。
 */
public class ShopInfoBean extends BaseBean {

	private boolean editPrice=true;//价格是否可以编辑(默认true)

	private List<Data> list;

	public List<Data> getList() {
		return list;
	}

	public void setList(List<Data> list) {
		this.list = list;
	}

	public boolean isEditPrice() {
		return editPrice;
	}

	public void setEditPrice(boolean editPrice) {
		this.editPrice = editPrice;
	}

	public static class Data implements Parcelable {
		
//        "wareNm": "崂山听009",
//		  "wareId": 22,
//		  "sumQty": "2",
//		  "hsNum": 1,
//		  "wareGg": "500*9",
//		  "wareCode": "10014",
//		  "wareDj": 65,
//		  "stkQty": "198.00000",
//		  "wareDw": "箱",
//		  "maxUnitCode": "B",
//		  "minUnitCode": "S",
//		  "occQty": "5.0",//已占库存
//		  "minUnit": "S",
//		  "userId": 1


		private String id;
		private int userId;
		private int wareId;
		private int sunitFront;//开单辅单位排前；1:是
		private String wareType;
		private String wareNm;
		private String wareGg;
		private String wareDw;//包装单位
		private String wareDj;// 执行价
		private String hsNum;//换算数量
		private String minUnit;//计量单位
		private String wareCode;
		private String sumQty;
		private String stkQty;//库存
		private String minStkQty;//当前小单位库存
		private String occQty;//已占库存
		private String maxUnitCode;//包装单位代码
		private String minUnitCode;//计量单位代码
		private String productDate;//生成日期
		private String py;//简拼
//		包装单位=计量单位*换算数量
//		说明：选择商品时，默认都是包装单位，当选择计量单位时，销售价格=销售价格／换算数量


		//自己添加---拜访步骤5
		private String currentCount;
		private String currentDw;
		private String currentPrice;
		private String currentCode;
		private String currentXstp;//拜访步骤4,5共用
		private String currentBz;//拜访步骤4,5共用

		//自己添加---拜访步骤4
		private String currentDhl;
		private String currentSxl;
		private String currentKcl;
		private String currentDd;
		private String currentXxz;
		//自己添加---退货
		private String currentProductDate;
		private Double wareDjOriginal;//商品原价

		private String currentMaxPrice;
		private String currentMinPrice;

		//采购单
		private String rebatePrice;//采购单：付款返利单价
		private Integer inTypeCode;//采购类型编码
		private String inTypeName;//采购类型名称
		private String inPrice;//采购价
		private String minInPrice;//采购价(小)

		//批次盘点
		private String inPrice2;//
		private String sunitPrice;//小价格
		private String minHisPfPrice;//小单位历史价
		private String maxHisPfPrice;//大单位历史价
		private String autoPrice;//0执行价；1历史价
		private String lowestSalePrice;//最低销售价(大)：
		private String packBarCode;//大条形码
		private String beBarCode;//小条形码

		private List<WarePicBean> warePicList;


		public String getPackBarCode() {
			return packBarCode;
		}

		public void setPackBarCode(String packBarCode) {
			this.packBarCode = packBarCode;
		}

		public String getBeBarCode() {
			return beBarCode;
		}

		public void setBeBarCode(String beBarCode) {
			this.beBarCode = beBarCode;
		}

		public String getLowestSalePrice() {
			return lowestSalePrice;
		}

		public void setLowestSalePrice(String lowestSalePrice) {
			this.lowestSalePrice = lowestSalePrice;
		}

		public String getAutoPrice() {
			return autoPrice;
		}

		public void setAutoPrice(String autoPrice) {
			this.autoPrice = autoPrice;
		}

		public String getMinHisPfPrice() {
			return minHisPfPrice;
		}

		public void setMinHisPfPrice(String minHisPfPrice) {
			this.minHisPfPrice = minHisPfPrice;
		}

		public String getMaxHisPfPrice() {
			return maxHisPfPrice;
		}

		public void setMaxHisPfPrice(String maxHisPfPrice) {
			this.maxHisPfPrice = maxHisPfPrice;
		}

		public String getSunitPrice() {
			return sunitPrice;
		}

		public void setSunitPrice(String sunitPrice) {
			this.sunitPrice = sunitPrice;
		}

		public String getInPrice2() {
			return inPrice2;
		}

		public void setInPrice2(String inPrice2) {
			this.inPrice2 = inPrice2;
		}

		public String getInPrice() {
			return inPrice;
		}

		public void setInPrice(String inPrice) {
			this.inPrice = inPrice;
		}

		public String getMinInPrice() {
			return minInPrice;
		}

		public void setMinInPrice(String minInPrice) {
			this.minInPrice = minInPrice;
		}

		public Integer getInTypeCode() {
			return inTypeCode;
		}

		public void setInTypeCode(Integer inTypeCode) {
			this.inTypeCode = inTypeCode;
		}

		public String getInTypeName() {
			return inTypeName;
		}

		public void setInTypeName(String inTypeName) {
			this.inTypeName = inTypeName;
		}

		public String getRebatePrice() {
			return rebatePrice;
		}

		public void setRebatePrice(String rebatePrice) {
			this.rebatePrice = rebatePrice;
		}

		public String getCurrentMaxPrice() {
			return currentMaxPrice;
		}

		public void setCurrentMaxPrice(String currentMaxPrice) {
			this.currentMaxPrice = currentMaxPrice;
		}

		public String getCurrentMinPrice() {
			return currentMinPrice;
		}

		public void setCurrentMinPrice(String currentMinPrice) {
			this.currentMinPrice = currentMinPrice;
		}

		public Double getWareDjOriginal() {
			return wareDjOriginal;
		}

		public void setWareDjOriginal(Double wareDjOriginal) {
			this.wareDjOriginal = wareDjOriginal;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getCurrentBz() {
			return currentBz;
		}

		public void setCurrentBz(String currentBz) {
			this.currentBz = currentBz;
		}

		public String getCurrentXstp() {
			return currentXstp;
		}

		public void setCurrentXstp(String currentXstp) {
			this.currentXstp = currentXstp;
		}

		public String getCurrentCount() {
			return currentCount;
		}

		public void setCurrentCount(String currentCount) {
			this.currentCount = currentCount;
		}

		public String getCurrentDw() {
			return currentDw;
		}

		public void setCurrentDw(String currentDw) {
			this.currentDw = currentDw;
		}

		public String getCurrentPrice() {
			return currentPrice;
		}

		public void setCurrentPrice(String currentPrice) {
			this.currentPrice = currentPrice;
		}

		public String getCurrentCode() {
			return currentCode;
		}

		public void setCurrentCode(String currentCode) {
			this.currentCode = currentCode;
		}



		public int getUserId() {
			return userId;
		}
		public void setUserId(int userId) {
			this.userId = userId;
		}
		public int getWareId() {
			return wareId;
		}
		public void setWareId(int wareId) {
			this.wareId = wareId;
		}
		public String getWareNm() {
			return wareNm;
		}
		public void setWareNm(String wareNm) {
			this.wareNm = wareNm;
		}
		public String getWareDw() {
			return wareDw;
		}
		public void setWareDw(String wareDw) {
			this.wareDw = wareDw;
		}
		public String getSumQty() {
			return sumQty;
		}
		public void setSumQty(String sumQty) {
			this.sumQty = sumQty;
		}
		public String getWareCode() {
			return wareCode;
		}
		public void setWareCode(String wareCode) {
			this.wareCode = wareCode;
		}
		public String getWareDj() {
			return wareDj;
		}
		public void setWareDj(String wareDj) {
			this.wareDj = wareDj;
		}
		public String getStkQty() {
			return stkQty;
		}
		public void setStkQty(String stkQty) {
			this.stkQty = stkQty;
		}
		public String getWareGg() {
			return wareGg;
		}
		public void setWareGg(String wareGg) {
			this.wareGg = wareGg;
		}
		public String getMaxUnitCode() {
			return maxUnitCode;
		}

		public void setMaxUnitCode(String maxUnitCode) {
			this.maxUnitCode = maxUnitCode;
		}

		public String getMinUnitCode() {
			return minUnitCode;
		}

		public void setMinUnitCode(String minUnitCode) {
			this.minUnitCode = minUnitCode;
		}

		public String getMinUnit() {
			return minUnit;
		}

		public void setMinUnit(String minUnit) {
			this.minUnit = minUnit;
		}

		public String getHsNum() {
			return hsNum;
		}

		public void setHsNum(String hsNum) {
			this.hsNum = hsNum;
		}

		public String getOccQty() {
			return occQty;
		}

		public void setOccQty(String occQty) {
			this.occQty = occQty;
		}

		public List<WarePicBean> getWarePicList(){
			return warePicList;
		}

		public void setWarePicList(List<WarePicBean> warePicList) {
			this.warePicList = warePicList;
		}

		public int getSunitFront() {
			return sunitFront;
		}

		public void setSunitFront(int sunitFront) {
			this.sunitFront = sunitFront;
		}

		public String getProductDate() {
			return productDate;
		}

		public void setProductDate(String productDate) {
			this.productDate = productDate;
		}

		public String getCurrentDhl() {
			return currentDhl;
		}

		public void setCurrentDhl(String currentDhl) {
			this.currentDhl = currentDhl;
		}

		public String getCurrentSxl() {
			return currentSxl;
		}

		public void setCurrentSxl(String currentSxl) {
			this.currentSxl = currentSxl;
		}

		public String getCurrentKcl() {
			return currentKcl;
		}

		public void setCurrentKcl(String currentKcl) {
			this.currentKcl = currentKcl;
		}

		public String getCurrentDd() {
			return currentDd;
		}

		public void setCurrentDd(String currentDd) {
			this.currentDd = currentDd;
		}

		public String getCurrentXxz() {
			return currentXxz;
		}

		public void setCurrentXxz(String currentXxz) {
			this.currentXxz = currentXxz;
		}

		public String getWareType() {
			return wareType;
		}

		public void setWareType(String wareType) {
			this.wareType = wareType;
		}

		public String getPy() {
			return py;
		}

		public void setPy(String py) {
			this.py = py;
		}

		public String getCurrentProductDate() {
			return currentProductDate;
		}

		public void setCurrentProductDate(String currentProductDate) {
			this.currentProductDate = currentProductDate;
		}

		public String getMinStkQty() {
			return minStkQty;
		}

		public void setMinStkQty(String minStkQty) {
			this.minStkQty = minStkQty;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel parcel, int i) {
			//要与createFromParcel顺序一致
			parcel.writeInt(userId);
			parcel.writeInt(wareId);
			parcel.writeInt(sunitFront);

			parcel.writeString(wareNm);
			parcel.writeString(wareGg);
			parcel.writeString(wareDw);
			parcel.writeString(wareDj);
			parcel.writeString(hsNum);
			parcel.writeString(minUnit);
			parcel.writeString(wareCode);
			parcel.writeString(sumQty);
			parcel.writeString(stkQty);
			parcel.writeString(minStkQty);
			parcel.writeString(occQty);
			parcel.writeString(maxUnitCode);
			parcel.writeString(minUnitCode);
			parcel.writeString(productDate);


			parcel.writeString(currentCount);
			parcel.writeString(currentDw);
			parcel.writeString(currentPrice);
			parcel.writeString(currentCode);
			parcel.writeString(currentXstp);
			parcel.writeString(currentBz);

			parcel.writeString(currentDhl);
			parcel.writeString(currentSxl);
			parcel.writeString(currentKcl);
			parcel.writeString(currentDd);
			parcel.writeString(currentXxz);
			parcel.writeString(currentProductDate);
//			parcel.writeDouble(wareDjOriginal);

			parcel.writeString(currentMaxPrice);
			parcel.writeString(currentMinPrice);
			parcel.writeString(inPrice2);

			parcel.writeString(lowestSalePrice);
			parcel.writeString(packBarCode);
			parcel.writeString(beBarCode);


		}

		public static final Parcelable.Creator<Data> CREATOR = new Creator<Data>() {

			@Override
			public Data createFromParcel(Parcel source) {
				//要与writeToParcel顺序一致
				Data data = new Data();
				data.setUserId(source.readInt());
				data.setWareId(source.readInt());
				data.setSunitFront(source.readInt());

				data.setWareNm(source.readString());
				data.setWareGg(source.readString());
				data.setWareDw(source.readString());
				data.setWareDj(source.readString());
				data.setHsNum(source.readString());
				data.setMinUnit(source.readString());
				data.setWareCode(source.readString());
				data.setSumQty(source.readString());
				data.setStkQty(source.readString());
				data.setMinStkQty(source.readString());
				data.setOccQty(source.readString());
				data.setMaxUnitCode(source.readString());
				data.setMinUnitCode(source.readString());
				data.setProductDate(source.readString());

				data.setCurrentCount(source.readString());
				data.setCurrentDw(source.readString());
				data.setCurrentPrice(source.readString());
				data.setCurrentCode(source.readString());
				data.setCurrentXstp(source.readString());
				data.setCurrentBz(source.readString());

				data.setCurrentDhl(source.readString());
				data.setCurrentSxl(source.readString());
				data.setCurrentKcl(source.readString());
				data.setCurrentDd(source.readString());
				data.setCurrentXxz(source.readString());
				data.setCurrentProductDate(source.readString());
//				data.setWareDjOriginal(source.readDouble());

				data.setCurrentMaxPrice(source.readString());
				data.setCurrentMinPrice(source.readString());

				data.setInPrice2(source.readString());

				data.setLowestSalePrice(source.readString());
				data.setPackBarCode(source.readString());
				data.setBeBarCode(source.readString());

				return data;
			}

			@Override
			public Data[] newArray(int size) {
				return new Data[size];
			}
		};
	}


}
