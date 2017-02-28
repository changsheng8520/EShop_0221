package com.feicuiedu.eshop_0221.feature.home;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.feicuiedu.eshop_0221.R;
import com.feicuiedu.eshop_0221.base.BaseFragment;
import com.feicuiedu.eshop_0221.base.widgets.banner.BannerAdapter;
import com.feicuiedu.eshop_0221.base.widgets.banner.BannerLayout;
import com.feicuiedu.eshop_0221.network.EShopClient;
import com.feicuiedu.eshop_0221.network.core.UICallback;
import com.feicuiedu.eshop_0221.network.entity.Banner;
import com.feicuiedu.eshop_0221.network.entity.HomeBannerRsp;
import com.feicuiedu.eshop_0221.network.entity.HomeCategoryRsp;
import com.feicuiedu.eshop_0221.network.entity.Picture;
import com.feicuiedu.eshop_0221.network.entity.SimpleGoods;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.GrayscaleTransformation;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by gqq on 2017/2/27.
 */

public class HomeFragment extends BaseFragment {


    @BindView(R.id.standard_toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.standard_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.list_home_goods)
    ListView mListHomeGoods;
    @BindView(R.id.standard_refresh_layout)
    PtrFrameLayout mRefreshLayout;

    private ImageView[] mIvPromotes = new ImageView[4];
    private TextView mMTvPromoteGoods;
    private BannerAdapter<Banner> mBannerAdapter;
    private HomeGoodsAdapter mGoodsAdapter;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        initToolbar();
        initPtr();

        // 设置适配器
        mGoodsAdapter = new HomeGoodsAdapter();
        mListHomeGoods.setAdapter(mGoodsAdapter);

        // ListView的头布局
        View view = LayoutInflater.from(getContext()).inflate(R.layout.partial_home_header,mListHomeGoods,false);

        // 找到头布局里面的控件
        BannerLayout bannerLayout = ButterKnife.findById(view,R.id.layout_banner);
        // 数据和视图的绑定
// TODO: 2017/2/28 图片展示待实现
        mBannerAdapter = new BannerAdapter<Banner>() {
            @Override
            protected void bind(ViewHolder holder, Banner data) {
                // 数据和视图的绑定
//                holder.mImageView.setImageResource(R.drawable.image_holder_banner);
                Picasso.with(getContext()).load(data.getPicture().getLarge()).into(holder.mImageView);
            }
        };
        bannerLayout.setAdapter(mBannerAdapter);

        // 促销商品
        mIvPromotes[0] = ButterKnife.findById(view,R.id.image_promote_one);
        mIvPromotes[1] = ButterKnife.findById(view,R.id.image_promote_two);
        mIvPromotes[2] = ButterKnife.findById(view,R.id.image_promote_three);
        mIvPromotes[3] = ButterKnife.findById(view,R.id.image_promote_four);

        // 促销单品的TextView
        mMTvPromoteGoods = ButterKnife.findById(view, R.id.text_promote_goods);

        mListHomeGoods.addHeaderView(view);

    }

    // 刷新的初始化
    private void initPtr() {
        // 设置刷新显示的头布局、刷新事件等
        mRefreshLayout.disableWhenHorizontalMove(true);

        // 设置刷新显示的头布局：默认的
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(getContext());
        mRefreshLayout.setHeaderView(header);
        mRefreshLayout.addPtrUIHandler(header);

        // 设置刷新处理的Handler
        mRefreshLayout.setPtrHandler(mPtrHandler);

        // 一进来我们就去刷新：自动刷新的方法，延时任务然后自动刷新
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 自动刷新
                mRefreshLayout.autoRefresh();
            }
        },50);
    }

    private PtrDefaultHandler2 mPtrHandler = new PtrDefaultHandler2() {

        // 加载时触发
        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {

        }

        // 刷新的时候触发
        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            // 请求数据刷新页面
            getHomeData();
        }
    };

    // 初始化Toolbar的操作
    private void initToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        // 设置Toolbar作为ActionBar
        activity.setSupportActionBar(mToolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mToolbarTitle.setText(R.string.home_title);
    }

    // 去请求数据
    public void getHomeData() {

        // 轮播图和促销单品的数据
        Call bannerCall = EShopClient.getInstance().getHomeBanner();
        bannerCall.enqueue(new UICallback() {
            @Override
            public void onFailureInUI(Call call, IOException e) {
                Toast.makeText(getContext(), "请求错误"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseInUI(Call call, Response response) throws IOException {

                // 返回成功
                if (response.isSuccessful()){
                    String json = response.body().string();

                    // 解析拿到数据
                    HomeBannerRsp bannerRsp = new Gson().fromJson(json, HomeBannerRsp.class);
                    if (bannerRsp.getStatus().isSucceed()){
                        // 数据拿到了，首先给bannerAdapter,另外是给促销单品
                        mBannerAdapter.reset(bannerRsp.getData().getBanners());
                        setPromoteGoods(bannerRsp.getData().getGoodsList());
                    }else {
                        Toast.makeText(getContext(), bannerRsp.getStatus().getErrorDesc(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // 推荐的分类商品
        Call categoryCall = EShopClient.getInstance().getHomeCategory();
        categoryCall.enqueue(new UICallback() {
            @Override
            public void onFailureInUI(Call call, IOException e) {
                Toast.makeText(getContext(), "请求失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseInUI(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String json = response.body().string();
                    HomeCategoryRsp categoryRsp = new Gson().fromJson(json, HomeCategoryRsp.class);
                    if (categoryRsp.getStatus().isSucceed()){
                        // 拿到了推荐分类商品的数据
                        mGoodsAdapter.reset(categoryRsp.getData());
                    }else {
                        Toast.makeText(getContext(), categoryRsp.getStatus().getErrorDesc(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // 拿到数据之后，停止刷新
        mRefreshLayout.refreshComplete();
    }

    // 设置促销单品的展示
    private void setPromoteGoods(List<SimpleGoods> goodsList) {
        mMTvPromoteGoods.setVisibility(View.VISIBLE);
        for (int i = 0; i < mIvPromotes.length; i++) {
            mIvPromotes[i].setVisibility(View.VISIBLE);
            final SimpleGoods simpleGoods = goodsList.get(i);
            Picture picture = simpleGoods.getImg();
//            mIvPromotes[i].setImageResource(R.drawable.image_holder_goods);

            // 圆形、灰度
            Picasso.with(getContext()).load(picture.getLarge())
                    .transform(new CropCircleTransformation())// 圆形
                    .transform(new GrayscaleTransformation())// 灰度
                    .into(mIvPromotes[i]);

            mIvPromotes[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), simpleGoods.getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
