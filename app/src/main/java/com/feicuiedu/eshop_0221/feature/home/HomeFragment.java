package com.feicuiedu.eshop_0221.feature.home;

import com.feicuiedu.eshop_0221.R;
import com.feicuiedu.eshop_0221.base.BaseFragment;
import com.feicuiedu.eshop_0221.base.widgets.banner.BannerAdapter;
import com.feicuiedu.eshop_0221.base.widgets.banner.BannerLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by gqq on 2017/2/27.
 */

public class HomeFragment extends BaseFragment{

    @BindView(R.id.layout_banner)
    BannerLayout mBannerLayout;

    public static HomeFragment newInstance(){
        return new HomeFragment();
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {

        // 给BannerLayout设置适配器
        BannerAdapter<Object> bannerAdapter= new BannerAdapter<Object>() {
            @Override
            protected void bind(ViewHolder holder, Object data) {
                holder.mImageView.setImageResource(R.drawable.image_holder_banner);
            }
        };
        mBannerLayout.setAdapter(bannerAdapter);

        // 添加模拟的数据
        List<Object> data = new ArrayList<>();
        data.add(new Object());
        data.add(new Object());
        data.add(new Object());
        data.add(new Object());
        data.add(new Object());
        bannerAdapter.reset(data);
    }
}
