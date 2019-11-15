package com.andy.arch.rxjava;

import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.andy.arch.viewmodel.ClearViewModel;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public abstract class AutoReleaseSingleObserver<T> implements SingleObserver<T> {
    private ClearViewModel viewModel;

    public AutoReleaseSingleObserver(ClearViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public AutoReleaseSingleObserver(Fragment fragment, @NonNull Class<? extends ClearViewModel> clearVM) {
        viewModel = ViewModelProviders.of(fragment).get(clearVM);
    }

    public AutoReleaseSingleObserver(FragmentActivity fragmentActivity, @NonNull Class<? extends ClearViewModel> clearVM) {
        viewModel = ViewModelProviders.of(fragmentActivity).get(clearVM);
    }

    @Override
    public void onSubscribe(Disposable d) {
        viewModel.accept(d);
    }
}
