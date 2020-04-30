package com.andy.ui.recycler;

import androidx.annotation.NonNull;

public interface ViewTypeConverter<T> {
    int getTypeByPosition(@NonNull T item, int position);
}
