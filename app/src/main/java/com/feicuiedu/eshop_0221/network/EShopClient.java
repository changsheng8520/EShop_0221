package com.feicuiedu.eshop_0221.network;

import com.feicuiedu.eshop_0221.network.entity.CategoryRsp;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by gqq on 2017/2/23.
 */

// 网络请求的客户端
public class EShopClient {

    public static final String BASE_URL= "http://106.14.32.204/eshop/emobile/?url=";

    private static EShopClient shopClient;
    private final OkHttpClient mOkHttpClient;

    public static synchronized EShopClient getInstance(){
        if (shopClient==null){
            shopClient = new EShopClient();
        }
        return shopClient;
    }

    private EShopClient() {

        // 日志拦截器的创建
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // OkHttpClient 的初始化
        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    // 构建一下商品分类的接口请求
    public Call getCategory(){
        Request request = new Request.Builder()
                .get()
                .url(BASE_URL+"/category")
                .build();
        return mOkHttpClient.newCall(request);
    }
}
