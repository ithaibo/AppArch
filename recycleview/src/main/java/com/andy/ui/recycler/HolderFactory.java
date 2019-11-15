package com.andy.ui.recycler;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public interface HolderFactory {
    RecyclerView.ViewHolder create(ViewGroup parent, int type);
}
