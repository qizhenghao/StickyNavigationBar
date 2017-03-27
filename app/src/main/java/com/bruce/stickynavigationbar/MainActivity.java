package com.bruce.stickynavigationbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.bruce.stickynavigationbar.adapter.TestAdapter;
import com.bruce.stickynavigationbar.bean.NavBean;
import com.bruce.stickynavigationbar.listener.NavListViewScrollListener;
import com.bruce.stickynavigationbar.view.StickNavHostSubject;
import com.bruce.stickynavigationbar.view.StickyNavHost;

public class MainActivity extends AppCompatActivity implements StickyNavHost.TabItemClickListener {

    private static final int NAV_LENGTH = 3;
    private int STICKY_POSITION_IN_HEADER;
    private ListView mListView;
    private StickyNavHost stickyNavHostRoot;//根布局中的导航栏，表现为ListView上滑吸附在顶部
    private StickyNavHost stickyNavHostHead;//添加在ListView的headerView中的导航栏

    private StickNavHostSubject stickNavHostSubject;//观察者，用于管理两个导航栏
    private SparseArray<NavBean> mNavs;             //导航栏的多个tab数据
    private NavListViewScrollListener scrollListener;//给ListView设置的滑动事件，里面处理了导航栏的显示与隐藏

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
        mNavs.put(NavBean.TYPE_GIFT, new NavBean(NavBean.TYPE_GIFT, new TestAdapter(20, "我是转发", this)));
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
        STICKY_POSITION_IN_HEADER = mListView.getHeaderViewsCount();
    }

    @Override
    public void onTabItemSelected(@NavBean.TYPE int type) {
        NavBean currNav = mNavs.get(type);
        stickNavHostSubject.setSelectedType(type);//事件分发给注册者，注册者进行相应的变化
        if (currNav.type == NavBean.TYPE_CURRENT)//等于当前选中的tab，可以屏蔽掉
            return;
        NavBean.TYPE_CURRENT = currNav.type;
        scrollListener.setNav(currNav);
        mListView.setAdapter(currNav.adapter);
        if (stickyNavHostRoot.getVisibility() == View.VISIBLE) {//吸附在顶部的rootView正在展示
            if (currNav.getFirstVisibleItem() < STICKY_POSITION_IN_HEADER)
                mListView.setSelectionFromTop(STICKY_POSITION_IN_HEADER, stickyNavHostRoot.getHeight() - 2);
            else
                mListView.setSelectionFromTop(currNav.getFirstVisibleItem(), currNav.getTopDistance());
        } else {//吸附在顶部的rootView没有展示，说明在切换导航栏的时候是不需要进行滑动的，保持上次的位置即可
            mListView.setSelectionFromTop(NavBean.firstVisibleItemUniversal, NavBean.topDistanceUniversal);
        }
    }
}
