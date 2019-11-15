package com.andy.ui.recycler;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HolderHelper {

    public static View inflateItemView(@LayoutRes int layout,
                                        @NonNull ViewGroup parent) {
        return LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
    }
}
