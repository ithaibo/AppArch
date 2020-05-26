package com.andy.ui.recycler;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;


public class TypedAdapter<T> extends HeaderFooterAdapter {
    private final HolderFactory holderFactory;
    private final HolderBinder<T, RecyclerView.ViewHolder> holderBinder;
    private ViewTypeConverter<T> viewTypeConverter;
    private final List<T> dataList = new LinkedList<>();

    public TypedAdapter(HolderFactory holderFactory,
                        HolderBinder<T, RecyclerView.ViewHolder> holderBinder) {
        this.holderFactory = holderFactory;
        this.holderBinder = holderBinder;
    }

    public TypedAdapter(HolderFactory holderFactory,
                        HolderBinder<T, RecyclerView.ViewHolder> holderBinder,
                        ViewTypeConverter<T> viewTypeConverter) {
        this.holderFactory = holderFactory;
        this.holderBinder = holderBinder;
        this.viewTypeConverter = viewTypeConverter;
    }

    public static class Builder<V> {
        private HolderFactory holderFactory;
        private HolderBinder<V, RecyclerView.ViewHolder> holderBinder;
        private ViewTypeConverter viewTypeConverter;

        public Builder() {
        }

        public Builder<V> setHolderFactory(HolderFactory holderFactory) {
            this.holderFactory = holderFactory;
            return this;
        }

        public Builder<V> setHolderBinder(HolderBinder<V, RecyclerView.ViewHolder> holderBinder) {
            this.holderBinder = holderBinder;
            return this;
        }

        public Builder<V> setViewTypeConverter(ViewTypeConverter<V> viewTypeConverter) {
            this.viewTypeConverter = viewTypeConverter;
            return this;
        }

        public TypedAdapter<V> build() {
            return new TypedAdapter<>(holderFactory, holderBinder, viewTypeConverter);
        }
    }

    @Override
    protected RecyclerView.ViewHolder createDataHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (null == holderFactory) {
            return null;
        }
        return holderFactory.create(viewGroup, viewType);
    }

    @Override
    protected void bindDataHolder(RecyclerView.ViewHolder holder,
                                  int itemPosition,
                                  int dataPosition) {
        if (null != holderBinder) {
            holderBinder.bind(holder, dataPosition, getItem(dataPosition));
        }
    }

    @Override
    public int getDataItemCount() {
        return dataList.size();
    }

    @NonNull
    public List<T> getDataList() {
        return this.dataList;
    }

    public void updateDataList(@NonNull List<T> dataList) {
        if (dataList.isEmpty()) {
            this.dataList.clear();
            updateItemCount();
            notifyDataSetChanged();
            return;
        }

        int sizeBefore = this.dataList.size();
        int sizeNow = dataList.size();
        this.dataList.clear();
        updateItemCount();
        notifyDataSetChanged();

        this.dataList.addAll(dataList);
        updateItemCount();
        int headerCount = getHeaderCount();
        if (sizeBefore == sizeNow) {
            notifyItemRangeChanged(headerCount, sizeNow);
        } else if (sizeBefore > sizeNow) {
            notifyItemRangeChanged(headerCount, sizeNow);
            notifyItemRangeRemoved(headerCount + sizeNow, sizeBefore);
        } else {
            notifyItemRangeChanged(headerCount, sizeBefore);
            notifyItemRangeInserted(headerCount + sizeBefore, sizeNow - sizeBefore);
        }
    }

    public void appendList(@NonNull List<T> dataList) {
        if (dataList.isEmpty()) {
            return;
        }
        int start = this.dataList.size();
        this.dataList.addAll(dataList);
        updateItemCount();
        notifyItemRangeInserted(start, dataList.size());
    }

    public void append(@NonNull T dataItem) {
        dataList.add(dataItem);
        updateItemCount();
        int positionInsert = getHeaderCount() + this.dataList.size();
        notifyItemInserted(positionInsert);
    }

    @Nullable
    public T getItem(int position) {
        if (position < 0) {
            return null;
        }
        if (position >= dataList.size()) {
            return null;
        }
        return dataList.get(position);
    }

    public void clearData(boolean notify) {
        if (dataList.isEmpty()) return;
        dataList.clear();
        updateItemCount();
        if (notify) {
            notifyDataSetChanged();
        }
    }

    public void clear(boolean notify) {
        clearHeader();
        clearFooter();
        clearData(notify);
    }

    @Override
    protected int getDataItemType(int position) {
        if (null == this.viewTypeConverter) {
            return super.getDataItemType(position);
        }
        return this.viewTypeConverter
                .getTypeByPosition(getItem(position),
                        position - getHeaderCount());
    }
}