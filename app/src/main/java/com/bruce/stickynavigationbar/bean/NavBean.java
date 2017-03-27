package com.bruce.stickynavigationbar.bean;

import android.support.annotation.IntDef;
import android.widget.BaseAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by qizhenghao on 16/11/7.
 *
 * 导航栏bean信息
 */
public class NavBean {

    public static int TYPE_CURRENT;
    public static boolean IS_NEED_ATTACH = true;        //是否需要吸附

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_REPOST, TYPE_COMMENT, TYPE_LIKE})
    public @interface TYPE {
    }
    public static final int TYPE_REPOST = 1;
    public static final int TYPE_COMMENT = 2;
    public static final int TYPE_LIKE = 3;


    public NavBean(@TYPE int type, BaseAdapter adapter) {
        this.type = type;
        this.adapter = adapter;
        switch (type) {
            case TYPE_REPOST:
                title = "转发";
                break;
            case TYPE_COMMENT:
                title = "评论";
                break;
            case TYPE_LIKE:
                title = "赞";
                break;
        }
    }

    public BaseAdapter adapter;
    public String title;
    public int count;                       //展示的数据量
    public int type;                        //参考TYPE

    public boolean hasMore = false;         // 是否可以加载更多
    public boolean isRefresh = true;        // 是否是刷新
    public int pageNo = 1;
    public int pageSize = 10;

    private int firstVisibleItem;           //第一条可见的位置
    private int topDistance;                //距离顶部的位置
    public static int topDistanceUniversal;       //存储上一次的
    public static int firstVisibleItemUniversal;  //存储上一次的


    public int getFirstVisibleItem() {
        return firstVisibleItem;
    }

    public int getTopDistance() {
        return topDistance;
    }

    public void setTopDistance(int topDistance) {
        this.topDistance = topDistance;
        topDistanceUniversal = topDistance;
    }

    public void setFirstVisibleItem(int firstVisibleItem) {
        this.firstVisibleItem = firstVisibleItem;
        firstVisibleItemUniversal = firstVisibleItem;
    }
}
