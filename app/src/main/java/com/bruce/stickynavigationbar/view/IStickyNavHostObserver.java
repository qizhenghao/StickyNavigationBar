package com.bruce.stickynavigationbar.view;


import com.bruce.stickynavigationbar.bean.NavBean;

/**
 * Created by qizhenghao on 16/11/10.
 */
public interface IStickyNavHostObserver {

    void initTabData(NavBean[] navs);

    void refreshTabData(NavBean nav);

    void setSelectedType(@NavBean.TYPE int type);

    void setSelectedPosition(int position);
}
