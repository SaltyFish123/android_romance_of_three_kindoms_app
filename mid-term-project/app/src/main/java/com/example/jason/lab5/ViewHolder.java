package com.example.jason.lab5;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jason on 2017/10/21.
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews; // 储存list_item的子view
    private View mConvertView; // 存储list_item
    public ViewHolder(Context context, View itemView, ViewGroup parent) {
        super(itemView);
        mConvertView = itemView;
        mViews = new SparseArray<View>();
    }

    public static ViewHolder get(Context context, ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
         return new ViewHolder(context, itemView, parent);
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId); // 创建子view
            mViews.put(viewId, view); // 将view存入mViews
        }
        return (T) view;
    }
}
