package com.andy.apparch;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andy.ui.recycler.TypedAdapter;

import java.util.List;

public abstract class AtySimplePage<T> extends AtyPage<List<T>> {

    TypedAdapter<T> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(getLayoutManager());
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(AtySimplePage.this);
    }

    protected abstract @LayoutRes int getItemLayout();

}
