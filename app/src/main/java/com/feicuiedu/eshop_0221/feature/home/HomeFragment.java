package com.feicuiedu.eshop_0221.feature.home;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.feicuiedu.eshop_0221.R;
import com.feicuiedu.eshop_0221.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

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
}
