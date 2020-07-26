package com.qwb.view.checkstorage.model;

import com.qwb.view.base.model.BaseBean;

import java.util.List;

/**
 * 盘点仓库--商品
 */
public class StkWareListBean extends BaseBean {

    private List<StkWareBean> list;

    //------------------------------------
//	wareNm	:	青岛小优123
//	stkName	:	1号仓库
//	hsNum	:	123
//	unitName	:	件
//	wareId	:	8
//	minSumQty	:	1241107.7499999919
//	minUnitName	:	瓶
//	sumQty	:	10090.3069105686
//	sumAmt	:	238268.0638455325
//	stkId	:	7


    public List<StkWareBean> getList() {
        return list;
    }

    public void setList(List<StkWareBean> list) {
        this.list = list;
    }
}
