package com.andy.ui.recycler;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 分组的RecyclerViewAdapter
 */
abstract class HeaderFooterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_FOOTER = 32;
    /**注意：data的类型只能取[8,32)**/
    public static final int TYPE_DATA = 8;

    protected int mItemCount;

    /**注意：这里的key需要注意大小与view的显示顺序保持一致*/
    protected SparseArrayCompat<View> headerViews = new SparseArrayCompat<>();
    protected SparseArrayCompat<View> footerViews = new SparseArrayCompat<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType >= TYPE_HEADER && viewType < TYPE_DATA) {
            View headerView = headerViews.get(viewType);
            if (null == headerView) {
                headerView = new View(parent.getContext());
            }
            return new RecyclerView.ViewHolder(headerView) {};
        } else if (viewType >= TYPE_FOOTER) {
            View footerView = footerViews.get(viewType);
            if (null == footerView) {
                footerView = new View(parent.getContext());
            }
            return new RecyclerView.ViewHolder(footerView) {};
        } else {
            return createDataHolder(parent, viewType);
        }
    }

    protected abstract RecyclerView.ViewHolder createDataHolder(@NonNull ViewGroup viewGroup, int viewType);

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int itemPosition) {
        if (isHeaderItem(itemPosition)) {
            // bind header
            int typeHeader = getHeaderType(itemPosition);
            if (-1 != typeHeader) {
                bindHeader(holder, typeHeader);
            }
            return;
        } else if (isFooterItem(itemPosition)) {
            // bind footer
            int typeFooter = getFooterType(itemPosition);
            if (-1 != typeFooter) {
                bindFooter(holder, typeFooter);
            }
            return;
        }

        bindDataHolder(holder, itemPosition, itemPosition - getHeaderCount());
    }

    protected void bindHeader(RecyclerView.ViewHolder holder, int typeHeader) {
    }

    protected void bindFooter(RecyclerView.ViewHolder holder, int itemPosition){}

    protected abstract void bindDataHolder(RecyclerView.ViewHolder holder, int itemPosition, int dataPosition);

    public boolean isHeaderItem(int position) {
        return position >=0 && position < getHeaderCount();
    }

    public boolean isFooterItem(int position) {
        return position >= getHeaderCount() + getDataItemCount() && position < getItemCount();
    }

    @Override
    public final int getItemCount() {
        return mItemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderItem(position)) {
            return headerViews.keyAt(position);
        } else if (isFooterItem(position)) {
            return footerViews.keyAt(position - getHeaderCount() - getDataItemCount());
        }

        int dataType = getDataItemType(position);
        if (dataType < TYPE_DATA || dataType >= TYPE_FOOTER) {
            throw new IllegalArgumentException("data type must be in [8, 32)");
        }
        return dataType;
    }

    protected int getDataItemType(int position) {
        return TYPE_DATA;
    }

    public void updateItemCount() {
        int count = 0;
        count += getDataItemCount();
        count += headerViews.size();
        count += footerViews.size();
        mItemCount = count;
    }

    public abstract int getDataItemCount();

    public void addHeaderView(@NonNull View headerView, boolean notify) {
        addHeaderView(TYPE_HEADER, headerView, notify);
    }

    public void addHeaderView(int type, @NonNull View headerView, boolean notify) {
        if (type < TYPE_HEADER || type >= TYPE_DATA) {
            throw new IllegalArgumentException("header type must be in [8,32)");
        }

        headerViews.put(type, headerView);
        updateItemCount();

        if (notify) {
            int position = headerViews.indexOfKey(type);
            notifyItemInserted(position);
        }
    }

    private int getHeaderType(int itemPosition) {
        if (isHeaderItem(itemPosition)) {
            return headerViews.keyAt(itemPosition);
        }
        return -1;
    }

    private int getFooterType(int itemPosition) {
        if (isFooterItem(itemPosition)) {
            return footerViews.keyAt(itemPosition - getHeaderCount() - getDataItemCount());
        }

        return -1;
    }

    public void removeHeaderView(int type, boolean notify) {
        if (!headerViews.containsKey(type)) {
            return;
        }
        int position = headerViews.indexOfKey(type);
        headerViews.remove(type);
        updateItemCount();
        if (notify) {
            notifyItemRemoved(position);
        }
    }

    public View getHeaderView(int type) {
        return headerViews.get(type);
    }

    public int getHeaderCount() {
        return headerViews.size();
    }

    public int getFooterCount() {
        return footerViews.size();
    }

    public void addFooterView(@NonNull View footerView, boolean notify) {
        addFooterView(TYPE_FOOTER, footerView, notify);
    }
    public void addFooterView(int type, @NonNull View footerView, boolean notify) {
        if (type < TYPE_FOOTER) {
            throw new IllegalArgumentException("footer type must be equals or big than 32");
        }

        footerViews.put(type, footerView);
        updateItemCount();
        if (notify) {
            int position = footerViews.indexOfKey(type) + getHeaderCount() + getDataItemCount();
            notifyItemInserted(position);
        }
    }

    public void removeFooterView(int type, boolean notify) {
        if (!footerViews.containsKey(type)) {
            return;
        }
        int position = footerViews.indexOfKey(type) + getHeaderCount() + getDataItemCount();
        footerViews.remove(type);
        updateItemCount();
        if (notify) {
            notifyItemRemoved(position);
        }
    }

    public View getFooterView(int type) {
        return footerViews.get(type);
    }

    public void clearHeader() {
        headerViews.clear();
        updateItemCount();
    }

    public void clearFooter() {
        footerViews.clear();
        updateItemCount();
    }
}
