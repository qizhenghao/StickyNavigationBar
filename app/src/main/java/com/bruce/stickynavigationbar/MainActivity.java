package com.bruce.stickynavigationbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bruce.stickynavigationbar.adapter.TestAdapter;
import com.bruce.stickynavigationbar.bean.NavBean;
import com.bruce.stickynavigationbar.listener.NavListViewScrollListener;
import com.bruce.stickynavigationbar.view.StickNavHostSubject;
import com.bruce.stickynavigationbar.view.StickyNavHost;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements StickyNavHost.TabItemClickListener {

    private static final int NAV_LENGTH = 3;
    private int STICKY_POSITION;
    private ListView mListView;
    private StickyNavHost stickyNavHostRoot;
    private StickyNavHost stickyNavHostHead;

    private StickNavHostSubject stickNavHostSubject;
    private SparseArray<NavBean> mNavs;
    private NavListViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initNavsView();//初始化导航栏view

        initDefaultSelectedNav();//设置默认选择的导航tab
    }

    private void initDefaultSelectedNav() {
        onTabItemSelected(NavBean.TYPE_COMMENT);//通过此方法可进行切换tab
    }

    private void initNavsView() {
        initNavsData();
        stickyNavHostRoot.setTabItemClickListener(this);//设置点击回调
        stickyNavHostHead.setTabItemClickListener(this);//设置点击回调
        stickyNavHostRoot.setShowTopLine(false);

        stickNavHostSubject = new StickNavHostSubject();
        stickNavHostSubject.attachObserver(stickyNavHostRoot);//观察者模式
        stickNavHostSubject.attachObserver(stickyNavHostHead);

        NavBean[] sortedNavs = new NavBean[mNavs.size()];//指定导航栏的排列顺序
        sortedNavs[0] = mNavs.get(NavBean.TYPE_GIFT);
        sortedNavs[1] = mNavs.get(NavBean.TYPE_COMMENT);
        sortedNavs[2] = mNavs.get(NavBean.TYPE_LIKE);
        stickNavHostSubject.initTabData(sortedNavs);

        scrollListener = new NavListViewScrollListener(stickyNavHostRoot, stickyNavHostHead);
        mListView.setOnScrollListener(scrollListener);//为listView设置滑动监听，内部处理了吸附view的显示与隐藏
    }

    protected void initNavsData() {
        mNavs = new SparseArray<>(NAV_LENGTH);
        mNavs.put(NavBean.TYPE_GIFT, new NavBean(NavBean.TYPE_GIFT, new TestAdapter(20, "我是礼物", this)));
        mNavs.put(NavBean.TYPE_COMMENT, new NavBean(NavBean.TYPE_COMMENT, new TestAdapter(20, "我是评论", this)));
        mNavs.put(NavBean.TYPE_LIKE, new NavBean(NavBean.TYPE_LIKE, new TestAdapter(20, "我是赞", this)));
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.list_view);
        stickyNavHostRoot = (StickyNavHost) findViewById(R.id.sticky_nav_layout);
        stickyNavHostRoot.setVisibility(View.INVISIBLE);

        View testHeaderView = LayoutInflater.from(this).inflate(R.layout.listview_head_view_test_layout, null);
        mListView.addHeaderView(testHeaderView);

        View inflateView = LayoutInflater.from(this).inflate(R.layout.sticky_nav_host_layout, null);
        stickyNavHostHead = (StickyNavHost) inflateView.findViewById(R.id.sticky_nav_layout);
        stickyNavHostHead.setVisibility(View.VISIBLE);
        mListView.addHeaderView(stickyNavHostHead);
        STICKY_POSITION = mListView.getHeaderViewsCount();
    }

    @Override
    public void onTabItemSelected(@NavBean.TYPE int type) {
        NavBean currNav = mNavs.get(type);
        stickNavHostSubject.setSelectedType(type);
        if (currNav.type == NavBean.TYPE_CURRENT)
            return;
        NavBean.TYPE_CURRENT = currNav.type;
        scrollListener.setNav(currNav);
        mListView.setAdapter(currNav.adapter);
        if (stickyNavHostRoot.getVisibility() == View.VISIBLE) {//吸附在顶部的view正在展示
            if (currNav.firstVisibleItem < STICKY_POSITION)
                mListView.setSelectionFromTop(STICKY_POSITION, stickyNavHostRoot.getHeight() - 2);
            else
                mListView.setSelectionFromTop(currNav.firstVisibleItem, currNav.topDistance);
        } else {
            mListView.setSelectionFromTop(NavBean.firstVisibleItemUniversal, NavBean.topDistanceUniversal);
        }
    }
}
