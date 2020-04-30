package com.andy.arch.rxjava;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.andy.arch.viewmodel.ClearViewModel;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class AutoReleaseObserver<T> implements Observer<T> {
    private ClearViewModel viewModel;

    public AutoReleaseObserver(@NonNull Fragment fragment) {
        viewModel = new ViewModelProvider(fragment).get(ClearViewModel.class);
        viewModel.setLifecycleOwner(fragment);
    }

    public AutoReleaseObserver(@NonNull FragmentActivity fragmentActivity) {
        viewModel = new ViewModelProvider(fragmentActivity).get(ClearViewModel.class);
        viewModel.setLifecycleOwner(fragmentActivity);
    }

    public AutoReleaseObserver(ClearViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void onSubscribe(Disposable d) {
        viewModel.accept(d);
    }
}
