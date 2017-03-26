package com.bruce.stickynavigationbar.listener;

import android.view.View;
import android.widget.AbsListView;

import com.bruce.stickynavigationbar.bean.NavBean;

/**
 * Created by qizhenghao on 16/11/9.
 *
 * 实现了自动加载更多、记录导航栏信息
 */
public class NavListViewScrollListener implements AbsListView.OnScrollListener {

    private NavBean nav;
    private View rootView;
    private View headView;
    private int[] rootLocation = new int[2], headLocation = new int[2];

    public NavListViewScrollListener(View rootView, View headView) {
        this.rootView = rootView;
        this.headView = headView;
    }

    /**
     * 设置当前选中的nav
     * @param nav
     */
    public void setNav(NavBean nav) {
        this.nav = nav;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //处理了root导航栏的显示与隐藏, 本质上只是控制root导航栏的显示
        //而在listView的headerView中的导航栏不做处理，因为它会随着listView的滑动自行滑出页面
        if (NavBean.IS_NEED_ATTACH && rootView != null && nav != null) {
            rootView.getLocationOnScreen(rootLocation);
            headView.getLocationOnScreen(headLocation);
            //根据两者在屏幕中的location位置信息，决定root导航栏的显示与隐藏
            if (rootLocation[1] > headLocation[1]) {
                rootView.setVisibility(View.VISIBLE);
            } else {
                rootView.setVisibility(View.INVISIBLE);
            }

            //记录当前listView的滑动位置
            nav.setFirstVisibleItem(firstVisibleItem);
            nav.setTopDistance((view.getChildAt(0) == null) ? 0 : view.getChildAt(0).getTop());
        }
    }
}
