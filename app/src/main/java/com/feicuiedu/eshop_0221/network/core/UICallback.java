package com.feicuiedu.eshop_0221.network.core;

import android.os.Handler;
import android.os.Looper;

import com.feicuiedu.eshop_0221.network.entity.Banner;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by gqq on 2017/2/23.
 */

// 为了统一处理OkHttp的Callback不能更新UI的问题
public abstract class UICallback<T> implements Callback{
    /**
     * 泛型
     * 如果T是String类型，List也必须是String的
     *
     * 自定义泛型：泛型类、泛型接口、泛型方法
     * 泛型通配符：?
     */
//    List<Integer> mStringList = new ArrayList<>();
//    List<T> mTList;
//    Class<T> t;// 代表的是泛型所代表的类，比如是一个Banner，他其实代表的Banner.class
//
//    List<? extends Banner> mList;
//    Class<? extends Banner> mClass;
//
//    public <T extends Banner>T setData(T t){
//
//        new Gson().fromJson(,t);
//        new Gson().fromJson(,mClass);
//
//        return t;
//    }

    public UICallback() {

    }

    // 创建一个运行在主线程的Handler
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onFailure(final Call call, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                // 添加到消息队列里，和handler运行在同一个线程
                onFailureInUI(call, e);
            }
        });
    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    onResponseInUI(call, response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 对外提供两个必须实现的方法：将onFailure、onResponse的数据传递出去。
    public abstract void onFailureInUI(Call call, IOException e);

    public abstract void onResponseInUI(Call call, Response response) throws IOException;

}
