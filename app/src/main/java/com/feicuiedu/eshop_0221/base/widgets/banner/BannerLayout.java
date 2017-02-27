package com.feicuiedu.eshop_0221.base.widgets.banner;

/**
 * Created by gqq on 2017/2/27.
 */

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.feicuiedu.eshop_0221.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * 自定义的轮播图控件
 * 1. 自动轮播
 * 2. 数据可随意设置(适配器的问题)
 * 3. 自动和手动的冲突
 */
public class BannerLayout extends RelativeLayout{

    @BindView(R.id.pager_banner)
    ViewPager mPagerBanner;

    @BindView(R.id.indicator)
    CircleIndicator mIndicator;

    public BannerLayout(Context context) {
        super(context);
        init(context);
    }

    public BannerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BannerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    // 布局的填充和初始化相关
    private void init(Context context) {

        // Merge标签一定要设置ViewGroup和attachToRoot为true
        LayoutInflater.from(context).inflate(R.layout.widget_banner_layout,this,true);
        ButterKnife.bind(this);
    }
}
