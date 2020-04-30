package com.andy.apparch;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andy.ui.recycler.HolderBinder;
import com.andy.ui.recycler.HolderFactory;
import com.andy.ui.recycler.TypedAdapter;
import com.andy.ui.recycler.ViewTypeConverter;

import java.util.List;

public abstract class AtySimplePage<T> extends AtyPage<List<T>> {

    TypedAdapter<T> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(getLayoutManager());
        adapter = new TypedAdapter.Builder<T>()
                .setHolderFactory(new HolderFactory() {
                    @Override
                    public RecyclerView.ViewHolder create(ViewGroup parent, int type) {
                        //todo impl
                        return null;
                    }
                })
                .setHolderBinder(new HolderBinder<T, RecyclerView.ViewHolder>() {
                    @Override
                    public void bind(@NonNull RecyclerView.ViewHolder holder, int position, T data) {
                        //todo impl
                    }
                })
                .setViewTypeConverter(new ViewTypeConverter<T>() {
                    @Override
                    public int getTypeByPosition(@NonNull T item, int position) {
                        //todo impl
                        return 0;
                    }
                })
                .build();
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(AtySimplePage.this);
    }

    protected abstract @LayoutRes int getItemLayout();

}
