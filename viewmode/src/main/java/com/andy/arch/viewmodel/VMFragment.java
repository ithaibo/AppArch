package com.andy.arch.viewmodel;

import android.app.Activity;
import android.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import android.os.Bundle;
import androidx.annotation.NonNull;

public class VMFragment extends Fragment implements LifecycleOwner, ViewModelStoreOwner {
    private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
    private ViewModelStore mViewModelStore;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Activity activity = this.getActivity();
        boolean isChangingConfigurations = activity != null && activity.isChangingConfigurations();
        if (this.mViewModelStore != null && !isChangingConfigurations) {
            this.mViewModelStore.clear();
        }
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return this.mLifecycleRegistry;
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        if (this.getActivity() == null) {
            throw new IllegalStateException("Can't access ViewModels from detached fragment");
        } else {
            if (this.mViewModelStore == null) {
                this.mViewModelStore = new ViewModelStore();
            }
            return this.mViewModelStore;
        }
    }
}
