package com.feicuiedu.eshop_0221.network;

import com.feicuiedu.eshop_0221.network.entity.CategoryRsp;
import com.feicuiedu.eshop_0221.network.entity.SearchReq;
import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.FormBody;
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

    // 分类页面：商品分类请求
    public Call getCategory(){
        Request request = new Request.Builder()
                .get()
                .url(BASE_URL+"/category")
                .build();
        return mOkHttpClient.newCall(request);
    }

    // 首页：banner请求接口
    public Call getHomeBanner(){
        Request request = new Request.Builder()
                .get()
                .url(BASE_URL+"/home/data")
                .build();
        return mOkHttpClient.newCall(request);
    }

    // 首页：分类和推荐的商品
    public Call getHomeCategory(){
        Request request = new Request.Builder()
                .get()
                .url(BASE_URL+"/home/category")
                .build();
        return mOkHttpClient.newCall(request);
    }

    // 搜索：搜索商品
    public Call getSearch(SearchReq searchReq){

        // 构建请求体
        String param = new Gson().toJson(searchReq);
        RequestBody body = new FormBody.Builder()
                .add("json",param)
                .build();

        Request request = new Request.Builder()
                .post(body)
                .url(BASE_URL+"/search")
                .build();
        return mOkHttpClient.newCall(request);
    }
}
