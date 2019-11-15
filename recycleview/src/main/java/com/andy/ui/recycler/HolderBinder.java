package com.andy.ui.recycler;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public interface HolderBinder<T, VH extends RecyclerView.ViewHolder> {
    void bind(@NonNull VH holder, int position, T data);
}
