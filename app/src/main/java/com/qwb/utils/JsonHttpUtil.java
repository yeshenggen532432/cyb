package com.qwb.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.zhy.http.okhttp.utils.MyUrlUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.droidlover.xdroidmvp.log.XLog;

public class JsonHttpUtil {

    private static JsonHttpUtil jsonHttpUtil;

    private JsonHttpUtil() {
    }

    public static JsonHttpUtil getInstance() {
        if (jsonHttpUtil == null) {
            jsonHttpUtil = new JsonHttpUtil();
        }
        return jsonHttpUtil;
    }

    /**
     * 判断网络
     *
     * @param context
     * @return
     */
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 进度条消失
     *
     * @param dialog
     */
    private void dissmissDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * POST方法--有加进度条提示的
     */
    public String getJsonData(final HttpPost httpPost, Context context, final Dialog creatDialog,
                              MultipartEntityBuilder muutil) {

        if (!isNetworkConnected(context)) {
            dissmissDialog(creatDialog);
            return "网络没连接";
        }
        int statusCode = -1;

        // 将请求体内容加入请求
        httpPost.setEntity(muutil.build());
        // 客户端对象来发请求
        HttpClient httpClient = new DefaultHttpClient();
        // 请求超时
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
        // 读取超时
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            if (creatDialog != null) {
                creatDialog.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog1) {
                        if (httpPost.isAborted()) {
                            httpPost.abort();
                        }
                        dissmissDialog(creatDialog);
                    }
                });
            }
        } catch (ClientProtocolException e) {
            return null;
        } catch (IOException e) {
            String message = e.getMessage();
            if (message.contains("refused")) {
                dissmissDialog(creatDialog);
                return "服务器异常";
            }
            if (message.contains("timed")) {
                dissmissDialog(creatDialog);
                return "请求超时";
            }
        }
        if (response != null) {
            statusCode = response.getStatusLine().getStatusCode();
        } else {
            dissmissDialog(creatDialog);
            return "请求异常";
        }
        if (statusCode != 200) {
            Log.e("statusCode != 200", "参数异常或url错误");
            dissmissDialog(creatDialog);
            return "参数异常";
        }
        InputStream is = null;
        try {
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String tmp = null;
            while ((tmp = br.readLine()) != null) {
                if (tmp != null) {
                    Log.e("resultjson-------进度条", tmp.toString());
                    dissmissDialog(creatDialog);
                    return tmp;
                }
            }
        } catch (IllegalStateException e) {
        } catch (IOException e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        dissmissDialog(creatDialog);
        return null;
    }

    /**
     * POST方法--没有加进度条提示
     */
    public String getJsonData(final HttpPost httpPost, Context context, MultipartEntityBuilder muutil) {
        if (!isNetworkConnected(context)) {
            return "网络没连接";
        }

        int statusCode = -1;
        // 将请求体内容加入请求
        httpPost.setEntity(muutil.build());
        // 客户端对象来发请求
        HttpClient httpClient = new DefaultHttpClient();
        // 请求超时
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
        // 读取超时
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
        } catch (ClientProtocolException e) {

            return null;
        } catch (IOException e) {
            String message = e.getMessage();
            if (message.contains("refused")) {
                return "服务器异常";
            }
            if (message.contains("timed")) {
                return "请求超时";
            }
        }
        if (response != null) {
            statusCode = response.getStatusLine().getStatusCode();
        } else {
            return "请求异常";
        }
        if (statusCode != 200) {
            Log.e("statusCode != 200", "参数异常或url错误");
            return "参数异常";
        }
        InputStream is = null;
        try {
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String tmp = null;
            while ((tmp = br.readLine()) != null) {
                if (tmp != null) {
                    Log.e("resultjson--------没有进度条", tmp.toString());
                    return tmp;
                }
            }
        } catch (IllegalStateException e) {
        } catch (IOException e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }

        return null;
    }

    /**
     * 未读接口用到
     *
     * @param httpPost
     * @param muutil
     * @return
     */
    public String getJsonDataForServiec(final HttpPost httpPost, MultipartEntityBuilder muutil) {
        int statusCode = -1;
        // 将请求体内容加入请求
        httpPost.setEntity(muutil.build());
        // 客户端对象来发请求
        HttpClient httpClient = new DefaultHttpClient();
        // 请求超时
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
        // 读取超时
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
        } catch (ClientProtocolException e) {

            return null;
        } catch (IOException e) {
            String message = e.getMessage();
            if (message.contains("refused")) {
                return "服务器异";
            }
            if (message.contains("timed")) {
                return "请求超时";
            }
        }
        if (response != null) {

            statusCode = response.getStatusLine().getStatusCode();
        } else {
            return "请求异常";
        }
        if (statusCode != 200) {
            Log.e("statusCode != 200", "参数异常或url错误");
            return "参数异常";
        }
        InputStream is = null;
        try {
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String tmp = null;
            while ((tmp = br.readLine()) != null) {
                if (tmp != null) {
                    Log.e("resultjson-------未读", tmp.toString());
                    return tmp;
                }
            }
        } catch (IllegalStateException e) {
        } catch (IOException e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }

        return null;
    }

    public String downFile(String fileUrl) {
        String fileDir = "";
        try {
            String filename = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            File fileexit = new File(Constans.DIR_VOICE + filename);
            if (fileexit.exists()) {
                return Constans.DIR_VOICE + filename;
            }
            URL url = new URL(MyUrlUtil.getUrl(Constans.IMGROOTHOST + fileUrl));
            // 创建连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            // 创建输入流
            InputStream is = conn.getInputStream();
            File file = new File(Constans.DIR_VOICE);
            // 判断文件目录是否存在
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(new File(Constans.DIR_VOICE, filename));
            // 缓存
            byte buf[] = new byte[1024];
            // 写入到文件中
            do {
                int numread = is.read(buf);
                if (numread <= 0) {
                    // 下载完成
                    fileDir = Constans.DIR_VOICE + filename;
                    break;
                }
                // 写入文件
                fos.write(buf, 0, numread);
            } while (true);// !mCancelUpdate);// 点击取消就停止下载.
            fos.close();
            is.close();
        } catch (Exception e) {
            XLog.e("文件下载地址错误", e.getMessage());
            return null;
        }
        return fileDir;
    }
}
