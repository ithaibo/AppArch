package com.andy.apparch.demos;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.andy.apparch.R;
import com.andy.ui.recycler.HolderHelper;

import java.util.LinkedList;
import java.util.List;

public class AdapterList extends BaseAdapter {
    List<FundManager> dataList = new LinkedList<>();

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public FundManager getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = HolderHelper.inflateItemView(
                    R.layout.list_item_stack_img,
                    parent);
        }

        renderItem(convertView, getItem(position));

        return convertView;
    }

    void renderItem(View itemView, FundManager data) {
        StackImgDemo.renderItem(itemView, data);
    }
}
