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
    public static boolean IS_XIFU = true;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_GIFT, TYPE_COMMENT, TYPE_LIKE})
    public @interface TYPE {
    }
    public static final int TYPE_GIFT = 1;
    public static final int TYPE_COMMENT = 2;
    public static final int TYPE_LIKE = 3;


    public NavBean(@TYPE int type, BaseAdapter adapter) {
        this.type = type;
        this.adapter = adapter;
        switch (type) {
            case TYPE_GIFT:
                title = "礼物";
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
    public int count;
    public int type;                    //参考TYPE

    public boolean hasMore = false;                          // 是否可以加载更多
    public boolean isRefresh = true;                  // 是否是刷新
    public int pageNo = 1;
    public int pageSize = 10;

    public int firstVisibleItem;
    public int topDistance;
    public static int topDistanceUniversal;
    public static int firstVisibleItemUniversal;

}
