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
    private View xifuView;
    private View headView;
    private int[] xifuLocation = new int[2], headLocation = new int[2];

    public NavListViewScrollListener(View xifuView, View headView) {
        this.xifuView = xifuView;
        this.headView = headView;
    }

    public void setNav(NavBean nav) {
        this.nav = nav;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //处理了吸附view的显示与隐藏, 本质上只是控制吸附view的显示，而在listview的headerView中的导航栏不做处理
        if (NavBean.IS_XIFU && xifuView != null && nav != null) {
            xifuView.getLocationOnScreen(xifuLocation);
            headView.getLocationOnScreen(headLocation);
            if (xifuLocation[1] > headLocation[1]) {
                xifuView.setVisibility(View.VISIBLE);
            } else {
                xifuView.setVisibility(View.INVISIBLE);
            }
            nav.firstVisibleItem = firstVisibleItem;
            NavBean.firstVisibleItemUniversal = nav.firstVisibleItem;
            View v = view.getChildAt(0);
            nav.topDistance = (v == null) ? 0 : v.getTop();
            NavBean.topDistanceUniversal = nav.topDistance;
        }
    }
}
