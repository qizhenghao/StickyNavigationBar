package com.bruce.stickynavigationbar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bruce.stickynavigationbar.R;


/**
 * Created by qizhenghao on 17/3/22.
 */
public class TestAdapter extends BaseAdapter {

    private String text;
    private Context mContext;
    private int count;

    public TestAdapter(int count, String text, Context context) {
        this.mContext = context;
        this.count = count;
        this.text = text;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int i) {
        return text;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.listview_item_test_layout, null);
            holder = new ViewHolder();
            holder.textView = (TextView) view.findViewById(R.id.textview);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.textView.setText(text);
        return view;
    }

    class ViewHolder {
        TextView textView;
    }
}
