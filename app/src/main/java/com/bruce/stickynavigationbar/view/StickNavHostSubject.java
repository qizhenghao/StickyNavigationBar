package com.bruce.stickynavigationbar.view;


import com.bruce.stickynavigationbar.bean.NavBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qizhenghao on 16/11/10.
 */
public class StickNavHostSubject extends AbstractSubject<IStickyNavHostObserver> {

    private List<IStickyNavHostObserver> observers;

    public StickNavHostSubject() {
        observers = new ArrayList<>();
    }

    public void attachObserver(IStickyNavHostObserver observer) {
        observers.add(observer);
    }

    public void detachObserver(IStickyNavHostObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void initTabData(NavBean[] navs) {
        for (IStickyNavHostObserver observer : observers)
            observer.initTabData(navs);
    }

    @Override
    public void refreshTabData(NavBean nav) {
        for (IStickyNavHostObserver observer : observers)
            observer.refreshTabData(nav);
    }

    @Override
    public void setSelectedType(@NavBean.TYPE int type) {
        for (IStickyNavHostObserver observer : observers)
            observer.setSelectedType(type);
    }

    @Override
    public void setSelectedPosition(int position) {
        for (IStickyNavHostObserver observer : observers)
            observer.setSelectedPosition(position);
    }
}
