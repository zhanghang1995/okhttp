package com.example.sample;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by 74993 on 2018/3/15.
 */

public class OkHttpManager {
    /**
     * 静态实例
     */
    private static OkHttpManager sOkHttpManager;

    /**
     * okhttpclient实例
     */
    private OkHttpClient mClient;

    /**
     * 单例模式  获取OkHttpManager实例
     *
     * @return
     */
    public static OkHttpManager getInstance() {

        if (sOkHttpManager == null) {
            sOkHttpManager = new OkHttpManager();
        }
        return sOkHttpManager;
    }


    /**
     * 构造方法
     */
    private OkHttpManager() {

        mClient = new OkHttpClient();

        /**
         * 在这里直接设置连接超时.读取超时，写入超时
         */
        mClient.newBuilder().connectTimeout(10, TimeUnit.SECONDS);
        mClient.newBuilder().readTimeout(10, TimeUnit.SECONDS);
        mClient.newBuilder().writeTimeout(10, TimeUnit.SECONDS);
    }
}
