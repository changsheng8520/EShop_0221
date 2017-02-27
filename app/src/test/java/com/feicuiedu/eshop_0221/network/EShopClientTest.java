package com.feicuiedu.eshop_0221.network;

import com.feicuiedu.eshop_0221.network.entity.CategoryRsp;
import com.feicuiedu.eshop_0221.network.entity.HomeBannerRsp;
import com.feicuiedu.eshop_0221.network.entity.HomeCategoryRsp;
import com.google.gson.Gson;

import org.junit.Test;

import okhttp3.Call;
import okhttp3.Response;

import static org.junit.Assert.*;

/**
 * Created by gqq on 2017/2/23.
 * 单元测试：测试接口
 */
public class EShopClientTest {

    @Test
    public void getCategory() throws Exception {
        Call call = EShopClient.getInstance().getCategory();
        Response response = call.execute();
        String string = response.body().string();
        CategoryRsp categoryRsp = new Gson().fromJson(string, CategoryRsp.class);
        // 断言方法：为我们做一个判断
        assertTrue(categoryRsp.getStatus().isSucceed());
    }

    @Test
    public void getHomeBanner() throws Exception{
        Call call = EShopClient.getInstance().getHomeBanner();
        Response response = call.execute();
        String string = response.body().string();
        HomeBannerRsp bannerRsp = new Gson().fromJson(string, HomeBannerRsp.class);
        assertTrue(bannerRsp.getStatus().isSucceed());
    }

    @Test
    public void getHomeCategory() throws Exception{
        Call call = EShopClient.getInstance().getHomeCategory();
        Response response = call.execute();
        String string = response.body().string();
        HomeCategoryRsp categoryRsp = new Gson().fromJson(string, HomeCategoryRsp.class);
        assertTrue(categoryRsp.getStatus().isSucceed());
    }
}