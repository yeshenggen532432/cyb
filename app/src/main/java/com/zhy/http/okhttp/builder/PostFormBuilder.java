package com.zhy.http.okhttp.builder;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.SPUtils;
import com.zhy.http.okhttp.request.PostFormRequest;
import com.zhy.http.okhttp.request.RequestCall;
import com.zhy.http.okhttp.utils.MyUrlUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.log.XLog;

/**
 * Created by zhy on 15/12/14.
 */
public class PostFormBuilder extends OkHttpRequestBuilder<PostFormBuilder> implements HasParamsable {
    private List<FileInput> files = new ArrayList<>();

    @Override
    public RequestCall build() {
        url = MyUrlUtil.getUrl(url);
        addToken();
        XLog.e("RequestCall-url", url);
        if (params != null){
            XLog.e("RequestCall-params", JSON.toJSONString(params));
        }
        return new PostFormRequest(url, tag, params, headers, files, id).build();
    }

    public PostFormBuilder files(Map<String, File> files) {
        for (String key : files.keySet()) {
            File file = files.get(key);
            if (file != null) {
                this.files.add(new FileInput(key, file.getName(), file));
            }
        }
        return this;
    }

    public PostFormBuilder addFile(String key, File file) {
        if (file != null) {
            files.add(new FileInput(key, file.getName(), file));
        }
        return this;
    }

    public PostFormBuilder addFiles(String key, List<File> fileList) {
        if (fileList != null && fileList. size()> 0){
            for (File file: fileList){
                if (file != null) {
                    files.add(new FileInput(key, file.getName(), file));
                }
            }
        }

        return this;
    }

    public static class FileInput {
        public String key;
        public String filename;
        public File file;

        public FileInput(String name, String filename, File file) {
            this.key = name;
            this.filename = filename;
            this.file = file;
        }

        @Override
        public String toString() {
            return "FileInput{" +
                    "key='" + key + '\'' +
                    ", filename='" + filename + '\'' +
                    ", file=" + file +
                    '}';
        }
    }


    @Override
    public PostFormBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public PostFormBuilder addParams(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        if (MyStringUtil.isNotEmpty(val)) {
            params.put(key, val);
        }
        return this;
    }

    /**
     * 添加token
     */
    public void addToken(){
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        if (!params.containsKey("token") && MyStringUtil.isNotEmpty(SPUtils.getTK())){
            params.put("token", SPUtils.getTK());
        }
    }


}
