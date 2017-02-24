package com.feicuiedu.eshop_0221.feature.category;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.feicuiedu.eshop_0221.R;
import com.feicuiedu.eshop_0221.network.EShopClient;
import com.feicuiedu.eshop_0221.network.core.UICallback;
import com.feicuiedu.eshop_0221.network.entity.CategoryPrimary;
import com.feicuiedu.eshop_0221.network.entity.CategoryRsp;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by gqq on 2017/2/23.
 */

public class CategoryFragment extends Fragment {


    @BindView(R.id.standard_toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.standard_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.list_category)
    ListView mListCategory;
    @BindView(R.id.list_children)
    ListView mListChildren;

    private List<CategoryPrimary> mData;
    private CategoryAdapter mCategoryAdapter;
    private ChildrenAdapter mChildrenAdapter;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 初始化视图的操作
        initView();
    }

    private void initView() {

        initToolbar();

        // ListView的展示
        mCategoryAdapter = new CategoryAdapter();
        mListCategory.setAdapter(mCategoryAdapter);

        mChildrenAdapter = new ChildrenAdapter();
        mListChildren.setAdapter(mChildrenAdapter);

        // 拿到数据
        if (mData != null) {
            // 可以直接更新UI
            updateCategory();
        } else {
            // 去进行网络请求拿到数据
            Call call = EShopClient.getInstance().getCategory();
            call.enqueue(new UICallback() {
                @Override
                public void onFailureInUI(Call call, IOException e) {
                    Toast.makeText(getContext(), "请求失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponseInUI(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        CategoryRsp categoryRsp = new Gson().fromJson(response.body().string(), CategoryRsp.class);
                        if (categoryRsp.getStatus().isSucceed()) {
                            mData = categoryRsp.getData();
                            // 数据有了之后，数据给一级分类，默认选择第一条，二级分类才能展示
                            updateCategory();

                        }
                    }
                }
            });
        }
    }

    // 更新分类数据
    private void updateCategory() {
        mCategoryAdapter.reset(mData);
        // 切换展示二级分类
        chooseCategory(0);
    }

    // 用于根据一级分类的选项展示二级分类的内容
    private void chooseCategory(int position) {
        mListCategory.setItemChecked(position,true);
        mChildrenAdapter.reset(mCategoryAdapter.getItem(position).getChildren());
    }

    // 点击一级分类：展示相应二级分类
    @OnItemClick(R.id.list_category)
    public void onItemClick(int postion){
        chooseCategory(postion);
    }

    // 点击二级分类
    @OnItemClick(R.id.list_children)
    public void onChildrenClick(int position){

        // TODO: 2017/2/24 会完善到跳转页面的
        String name = mChildrenAdapter.getItem(position).getName();
        Toast.makeText(getContext(),name , Toast.LENGTH_SHORT).show();
    }

    private void initToolbar() {
        // Fragment显示选项菜单
        setHasOptionsMenu(true);
        // 处理toolbar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        // 处理actionbar不展示默认的标题
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        // 设置左上方的返回箭头
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarTitle.setText(R.string.category_title);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_category, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        // 返回箭头
        if (itemId == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }

        if (itemId == R.id.menu_search) {

            // TODO: 2017/2/24 后期会跳转到搜素页面上
            Toast.makeText(getContext(), "点击了搜索", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
