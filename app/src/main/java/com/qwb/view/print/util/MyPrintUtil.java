package com.qwb.view.print.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;
import com.qwb.common.OrderTypeEnum;
import com.qwb.utils.MyRequestUtil;
import com.qwb.utils.MyStringUtil;
import com.tools.command.CpclCommand;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.BaseBean;
import com.qwb.view.step.model.QueryBforderBean;
import com.qwb.view.step.model.XiaJi;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import java.util.List;
import java.util.Vector;
import okhttp3.Call;
import static dev.DevUtils.runOnUiThread;

/**
 * 蓝牙打印机：工具类
 */

public class MyPrintUtil {
    private static volatile MyPrintUtil instance = null;

    private MyPrintUtil() {

    }

    public static MyPrintUtil getInstance() {
        if (instance == null) {
            synchronized (MyPrintUtil.class) {
                if (instance == null) {
                    instance = new MyPrintUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 打印面单
     */
    public void sendCpcl(String orderNo, String khNm, QueryBforderBean currentData, int bluetoothId) {
        String orderStr = orderNo;//订单号;
        String clientStr = khNm;
        String phoneStr = currentData.getTel();
        String addressStr = currentData.getAddress();
        String remoStr = currentData.getRemo();
        String zdzkStr = currentData.getZdzk();
        String cjjeStr = currentData.getCjje();

        //备注每行的平均高度为30；商品列表为60
        List<XiaJi> list = currentData.getList();
        int sumHeight = list.size() * 60 + 620;//480为基本高度（16行*30）+商品列表的高度（数量*60）
        int height = 0;

        CpclCommand cpcl2 = new CpclCommand();
        cpcl2.addInitializePrinter(sumHeight, 1);

        String companyName = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_NAME);
        if (MyStringUtil.isNotEmpty(companyName)) {
            cpcl2.addJustification(CpclCommand.ALIGNMENT.LEFT);
//            cpcl2.addSetmag(1, 1);
            height += 35;
            cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 0, height, companyName);
        }

        cpcl2.addJustification(CpclCommand.ALIGNMENT.CENTER);
//        cpcl2.addSetmag(1, 1);//高宽放大倍数
        height += 35;
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 0, height, "供货凭证");

        height += 10;

        cpcl2.addJustification(CpclCommand.ALIGNMENT.LEFT);
        cpcl2.addSetmag(0, 0);
        height += 30;
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 0, height, "客户名称：");
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_3, 110, height, clientStr);
        height += 30;
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 0, height, "单　　号：");
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_3, 110, height, orderStr);
        height += 30;
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 0, height, "日　　期：");
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_3, 110, height, MyTimeUtils.getTodayStr());
        height += 30;
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 0, height, "电　　话：");
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_3, 110, height, phoneStr);
        height += 30;
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 0, height, "地　　址：");
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_3, 110, height, addressStr);
        height += 30;
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 0, height, "备　　注：");
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_3, 110, height, remoStr);

        cpcl2.addJustification(CpclCommand.ALIGNMENT.CENTER);
        height += 20;
//        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 0, height, "----------------------------\n");
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 0, height, "---------------------------------------------------------\n");

        cpcl2.addJustification(CpclCommand.ALIGNMENT.LEFT);
        height += 20;
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 0, height, "品项");
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 100, height, "规格");
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 200, height, "单位");
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 300, height, "数量");
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 400, height, "单价");
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 500, height, "总价");

        double sum = 0;
        double sumMoney = 0;
        for (int i = 0; i < list.size(); i++) {
            XiaJi xiaJi = list.get(i);
            String goodsName = xiaJi.getWareNm();
            String goodsGg = xiaJi.getWareGg();
            String goodsDw = xiaJi.getWareDw();
            String goodsDj = xiaJi.getWareDj();
            String goodsCount = xiaJi.getWareNum();
            String goodsZj = xiaJi.getWareZj();


            sum += Double.valueOf(goodsCount);
            sumMoney += Double.valueOf(goodsZj);
            height += 30;
            cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 0, height, goodsName);
            //-----------------------------条形码-------------------------------------
//            String barCode = getBarCode(xiaJi);
//            if(MyStringUtil.isNotEmpty(barCode)){
//                height += 30;
//                cpcl2.addText(CpclCommand.TEXT_FONT.FONT_3, 0, height, barCode);
//            }
            //-----------------------------条形码-------------------------------------
            height += 30;
            cpcl2.addText(CpclCommand.TEXT_FONT.FONT_3, 100, height, goodsGg);
            cpcl2.addText(CpclCommand.TEXT_FONT.FONT_3, 200, height, goodsDw);
            cpcl2.addText(CpclCommand.TEXT_FONT.FONT_3, 300, height, goodsCount);
            cpcl2.addText(CpclCommand.TEXT_FONT.FONT_3, 400, height, goodsDj);
            cpcl2.addText(CpclCommand.TEXT_FONT.FONT_3, 500, height, goodsZj);
        }
        height += 30;
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 0, height, "总计：");
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_3, 300, height, String.valueOf(sum));
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_3, 500, height, String.valueOf(sumMoney));

        cpcl2.addJustification(CpclCommand.ALIGNMENT.CENTER);
        height += 20;
//        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 0, height, "----------------------------\n");
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 0, height, "----------------------------------------------------------\n");

        cpcl2.addJustification(CpclCommand.ALIGNMENT.LEFT);
        //商城：有运费
        if (mOrderType == OrderTypeEnum.ORDER_SC.getType()){
            height += 30;
            cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 0, height, "商品总价：");
            cpcl2.addText(CpclCommand.TEXT_FONT.FONT_3, 110, height, cjjeStr);
            height += 30;
            cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 0, height, "运　　费：");
            cpcl2.addText(CpclCommand.TEXT_FONT.FONT_3, 110, height, ""+currentData.getFreight());
            height += 30;
            cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 0, height, "实付金额：");
            cpcl2.addText(CpclCommand.TEXT_FONT.FONT_3, 110, height, ""+currentData.getOrderAmount());
        }else {
            height += 30;
            cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 0, height, "整单折扣：");
            cpcl2.addText(CpclCommand.TEXT_FONT.FONT_3, 110, height, zdzkStr);
            height += 30;
            cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 0, height, "应收金额：");
            cpcl2.addText(CpclCommand.TEXT_FONT.FONT_3, 110, height, cjjeStr);
        }

        height += 30;
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 0, height, "业务　员：");
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_3, 110, height, SPUtils.getSValues(ConstantUtils.Sp.USER_NAME));
        height += 30;
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 0, height, "电　　话：");
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_3, 110, height, SPUtils.getSValues(ConstantUtils.Sp.USER_MOBILE));
        height += 30;
        cpcl2.addText(CpclCommand.TEXT_FONT.FONT_2, 0, height, "客户签字：");

        cpcl2.addJustification(CpclCommand.ALIGNMENT.LEFT);
        cpcl2.addPrint();
        Vector<Byte> datas2 = cpcl2.getCommand();
        // 发送数据
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[bluetoothId].sendDataImmediately(datas2);
    }

    /**
     * 打印票据
     */
    private static final String NEWLINE = "\n";

    public void sendReceipt(String orderNo, String clientNm, QueryBforderBean mCurrentData, int bluetoothId) {
        String orderStr = orderNo;//订单号;
        String clientStr = clientNm;
        String phoneStr = mCurrentData.getTel();
        String addressStr = mCurrentData.getAddress();
        String remoStr = mCurrentData.getRemo();
        String zdzkStr = mCurrentData.getZdzk();
        String cjjeStr = mCurrentData.getCjje();

        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();

        String companyName = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_NAME);
        if (MyStringUtil.isNotEmpty(companyName)) {
            esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印居中
            esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
            esc.addText(companyName); // 打印文字
            esc.addPrintAndLineFeed();
        }

        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText("供货凭证"); // 打印文字
        esc.addPrintAndLineFeed();

        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐
        esc.addSelectPrintModes(EscCommand.FONT.FONTB, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
        esc.addText("客户名称：" + clientStr + NEWLINE);
        esc.addText("单　　号：" + orderStr);
        esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
        esc.addSetAbsolutePrintPosition((short) 11);
        esc.addText("日　　期：" + MyTimeUtils.getTodayStr());
        esc.addPrintAndLineFeed();
        esc.addText("电　　话：" + phoneStr + NEWLINE);
        esc.addText("地　　址：" + addressStr + NEWLINE);
        esc.addText("备　　注：" + remoStr + NEWLINE);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印左对齐
        esc.addText("--------------------------------------------------------\n");

        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐
        esc.addText("品项");
        esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
        esc.addSetAbsolutePrintPosition((short) 4);
        esc.addText("规格");
        esc.addSetAbsolutePrintPosition((short) 8);
        esc.addText("单位");
        esc.addSetAbsolutePrintPosition((short) 11);
        esc.addText("数量");
        esc.addSetAbsolutePrintPosition((short) 14);
        esc.addText("单价");
        esc.addSetAbsolutePrintPosition((short) 17);
        esc.addText("总价");
        esc.addPrintAndLineFeed();

        double sum = 0;
        double sumMoney = 0;
        List<XiaJi> list = mCurrentData.getList();
        for (int i = 0; i < list.size(); i++) {
            XiaJi xiaJi = list.get(i);
            String goodsName = xiaJi.getWareNm();
            String goodsGg = xiaJi.getWareGg();
            String goodsDw = xiaJi.getWareDw();
            String goodsDj = xiaJi.getWareDj();
            String goodsCount = xiaJi.getWareNum();
            String goodsZj = xiaJi.getWareZj();

            esc.addText(goodsName + NEWLINE);
            //-----------------------------条形码-------------------------------------
//            String barCode = getBarCode(xiaJi);
//            if(MyStringUtil.isNotEmpty(barCode)){
//                esc.addText(barCode + NEWLINE);
//            }
            //-----------------------------条形码-------------------------------------
            esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
            esc.addSetAbsolutePrintPosition((short) 4);
            esc.addText(goodsGg);
            esc.addSetAbsolutePrintPosition((short) 8);
            esc.addText(goodsDw);
            esc.addSetAbsolutePrintPosition((short) 11);
            esc.addText(goodsCount);
            esc.addSetAbsolutePrintPosition((short) 14);
            esc.addText(goodsDj);
            esc.addSetAbsolutePrintPosition((short) 17);
            esc.addText(goodsZj);
            esc.addPrintAndLineFeed();
            try {
                sum += Double.valueOf(goodsCount);
                sumMoney += Double.valueOf(goodsZj);
            } catch (Exception e) {
            }
        }
        esc.addText("总计：");
        esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
        esc.addSetAbsolutePrintPosition((short) 11);
        esc.addText(String.valueOf(sum));
        esc.addSetAbsolutePrintPosition((short) 17);
        esc.addText(String.valueOf(sumMoney));
        esc.addPrintAndLineFeed();

        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印左对齐
        esc.addText("--------------------------------------------------------\n");
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐
        //商城：有运费
        if (mOrderType == OrderTypeEnum.ORDER_SC.getType()){
            esc.addText("整单折扣：" + zdzkStr);
            esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
            esc.addSetAbsolutePrintPosition((short) 11);
            esc.addText("商品总价：" + cjjeStr);
            esc.addPrintAndLineFeed();
            esc.addText("运　　费：" + mCurrentData.getFreight());
            esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
            esc.addSetAbsolutePrintPosition((short) 11);
            esc.addText("实付金额：" + mCurrentData.getOrderAmount());
            esc.addPrintAndLineFeed();
        }else{
            esc.addText("整单折扣：" + zdzkStr);
            esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
            esc.addSetAbsolutePrintPosition((short) 11);
            esc.addText("应收金额：" + cjjeStr);
            esc.addPrintAndLineFeed();
        }

        esc.addText("业务　员：" + SPUtils.getSValues(ConstantUtils.Sp.USER_NAME));
        esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
        esc.addSetAbsolutePrintPosition((short) 11);
        esc.addText("电　　话：" + SPUtils.getSValues(ConstantUtils.Sp.USER_MOBILE));
        esc.addPrintAndLineFeed();
        esc.addText("客户签字：\n");
        esc.addPrintAndLineFeed();

		/* 打印文字 */
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印左对齐
        esc.addText("打印结束\r\n"); // 打印结束
        esc.addText("--------------------------------------------------------\n");
        // 开钱箱
        esc.addGeneratePlus(LabelCommand.FOOT.F5, (byte) 255, (byte) 255);
        esc.addPrintAndFeedLines((byte) 3);

        Vector<Byte> datas = esc.getCommand(); // 发送数据
        // 发送数据
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[bluetoothId].sendDataImmediately(datas);
    }

    /**
     * 获取条形码
     */
    private String getBarCode(XiaJi xiaJi) {
        String minBarCode = xiaJi.getBeBarCode();
        String maxBarCode = xiaJi.getPackBarCode();
        String barCode = "";
        if(MyStringUtil.isNotEmpty(maxBarCode)){
            barCode += "条形码：" + maxBarCode +"(大)";
        }
        if(MyStringUtil.isNotEmpty(minBarCode)){
            if(MyStringUtil.isEmpty(barCode)){
                barCode += "条形码：";
            }else{
                barCode += "/";
            }
            barCode += "" + minBarCode +"(小)";
        }
        return barCode;
    }

    /**
     * 更换打印模式
     */
    private byte[] tscmode = {0x1f, 0x1b, 0x1f, (byte) 0xfc, 0x01, 0x02, 0x03, 0x33};
    private byte[] cpclmode = {0x1f, 0x1b, 0x1f, (byte) 0xfc, 0x01, 0x02, 0x03, 0x44};
    private byte[] escmode = {0x1f, 0x1b, 0x1f, (byte) 0xfc, 0x01, 0x02, 0x03, 0x55};

    public boolean changeMode(int type, int bluetoothId) {
        if (type == 0) {
            //面单模式
            Vector<Byte> data = new Vector<>(cpclmode.length);
            for (int i = 0; i < cpclmode.length; i++) {
                data.add(cpclmode[i]);
            }
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[bluetoothId].sendDataImmediately(data);
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[bluetoothId].closePort(bluetoothId);
        } else if (type == 1) {
            //票据模式
            Vector<Byte> data = new Vector<>(escmode.length);
            for (int i = 0; i < escmode.length; i++) {
                data.add(escmode[i]);
            }
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[bluetoothId].sendDataImmediately(data);
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[bluetoothId].closePort(bluetoothId);
        } else if (type == 2) {
            //票据模式
            Vector<Byte> data = new Vector<>(tscmode.length);
            for (int i = 0; i < tscmode.length; i++) {
                data.add(tscmode[i]);
            }
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[bluetoothId].sendDataImmediately(data);
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[bluetoothId].closePort(bluetoothId);
        }

        return true;
    }

    /**
     * 判断蓝牙是否连接
     */
    public boolean isCollect(Activity activity) {
        if (activity != null) {
            this.mContext = activity;
        }
        //判断蓝牙是否连接
        if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[bluetoothId] == null || !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[bluetoothId].getConnState()) {
            //连接蓝牙
            showDialogBluetooth();
            return false;
        }
        return true;
    }

    private ThreadPool threadPool;
    private int bluetoothId = 0;

    /**
     * 蓝牙打印
     */
    private Activity mContext;
    private int mOrderType;
    public void print(Activity activity, final String orderNo, final String khNm, final QueryBforderBean bean, int orderType) {
        try {
            if (activity != null) {
                this.mContext = activity;
            }
            mOrderType = orderType;

            //判断蓝牙是否连接
            if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[bluetoothId] == null || !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[bluetoothId].getConnState()) {
                //连接蓝牙
                showDialogBluetooth();
                return;
            }

            threadPool = ThreadPool.getInstantiation();
            threadPool.addTask(new Runnable() {
                @Override
                public void run() {
                    PrinterCommand printerCommand = DeviceConnFactoryManager.getDeviceConnFactoryManagers()[bluetoothId].getCurrentPrinterCommand();
                    if (printerCommand != null) {
                        Log.e("PrinterCommand", printerCommand.name());
                    }
                    if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[bluetoothId].getCurrentPrinterCommand() == PrinterCommand.CPCL) {
                        sendCpcl(orderNo, khNm, bean, bluetoothId);
                        Log.e("面单打印", "面单打印");
                    } else if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[bluetoothId].getCurrentPrinterCommand() == PrinterCommand.ESC) {
                        sendReceipt(orderNo, khNm, bean, bluetoothId);
                        Log.e("票据打印", "票据打印");
                    } else if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[bluetoothId].getCurrentPrinterCommand() == PrinterCommand.TSC) {
                        sendCpcl(orderNo, khNm, bean, bluetoothId);
                        Log.e("标签打印", "标签打印");
                    } else {
                        Log.e("没有匹配到打印模式，请切换打印模式", "没有匹配到打印模式，请切换打印模式");
                    }
                }
            });
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


    /**
     * 蓝牙连接回调
     */
    public void connectBluetooth(String macAddress) {
        try {
            /*蓝牙连接*/
            //重新连接回收上次连接的对象，避免内存泄漏
            if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[bluetoothId] != null && DeviceConnFactoryManager.getDeviceConnFactoryManagers()[bluetoothId].mPort != null) {
                DeviceConnFactoryManager.getDeviceConnFactoryManagers()[bluetoothId].mPort.closePort();
                DeviceConnFactoryManager.getDeviceConnFactoryManagers()[bluetoothId].mPort = null;
            }
            //初始化话DeviceConnFactoryManager
            new DeviceConnFactoryManager.Build()
                    .setId(bluetoothId)
                    .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.BLUETOOTH)
                    .setMacAddress(macAddress)
                    .build();
            //打开端口
            threadPool = ThreadPool.getInstantiation();
            threadPool.addTask(new Runnable() {
                @Override
                public void run() {
                    final boolean isOpenPort = DeviceConnFactoryManager.getDeviceConnFactoryManagers()[bluetoothId].openPort();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissProgress();
                            if (isOpenPort) {
                                ToastUtils.showCustomToast("蓝牙连接成功");
                            } else {
                                ToastUtils.showCustomToast("蓝牙连接失败");
                            }
                        }
                    });

                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * 关闭蓝牙
     */
    public void closeBluetooth() {
        DeviceConnFactoryManager.closeAllPort();
        if (threadPool != null) {
            threadPool = null;
        }
    }


    /**
     * 蓝牙对话框
     */
    public void showDialogBluetooth() {
        if (mContext != null) {
            MyBluetoothDialog dialog = new MyBluetoothDialog(mContext);
            dialog.show();
            dialog.setOnSuccessConnectListener(new MyBluetoothDialog.OnSuccessConnectListener() {
                @Override
                public void setOnSuccessConnectListener(String mac) {
                    if (Constans.ISDEBUG){
                        querySysCarBluetooth(mContext, mac);
                    }else{
                        showDialogProgress();
                        connectBluetooth(mac);
                    }
                }
            });
        }
    }

    /**
     * 连接蓝牙进度条
     */
    ProgressDialog progressDialog;

    public void showDialogProgress() {
        if (progressDialog == null && mContext != null) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("蓝牙连接中");
        }
        progressDialog.show();
    }

    /**
     * 连接蓝牙进度条消失
     */
    public void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


    /**
     * 该蓝牙设备是否在平台上登记
     */
    public void querySysCarBluetooth(Activity activity, final String mac) {
        OkHttpUtils
                .post()
                .addParams("token", SPUtils.getTK())
                .url(Constans.querySysCarBluetooth)
                .id(4)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                        showDialogProgress();
                        connectBluetooth(mac);
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        try {
                            BaseBean bean = JSON.parseObject(response, BaseBean.class);
                            if (MyRequestUtil.isSuccess(bean)) {
                                showDialogProgress();
                                connectBluetooth(mac);
                            }else{
                                ToastUtils.showCustomToast(bean.getMsg());
                            }
                        }catch (Exception e){
                            ToastUtils.showError(e);
                        }
                    }
                });
    }


}
