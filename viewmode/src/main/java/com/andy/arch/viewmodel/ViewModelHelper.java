package com.andy.arch.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.app.FragmentManager;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.atomic.AtomicBoolean;

public class ViewModelHelper {
    private static final String TAG_VM_FRAGMENT = "TAG_VM_FRAGMENT";
    private static ViewModelProvider.NewInstanceFactory factory;
    private static AtomicBoolean init = new AtomicBoolean(false);

    public static void init(Application application) {
        if (init.get()) {
            return;
        }
        if (null == application) {
            return;
        }
        application.registerActivityLifecycleCallbacks(new SimpleActivityLifecycle() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (!(activity instanceof FragmentActivity)) {
                    try {
                        activity.getFragmentManager().beginTransaction().add(new VMFragment(), TAG_VM_FRAGMENT).commit();
                        activity.getFragmentManager().executePendingTransactions();
                    } catch (Exception ignored) {}
                }
            }
        });
        init.compareAndSet(false, true);
    }

    /**
     * 基于FragmentActivity创建ViewModel
     * @param fragmentActivity 绑定于fragmentActivity的生命周期
     * @param type ViewModel类型
     * @param <T> 类型
     * @return ViewModel的实例
     */
    public static <T extends ViewModel> T createViewModel(FragmentActivity fragmentActivity, Class<T> type) {
        T vm = ViewModelProviders.of(fragmentActivity).get(type);
        if (vm instanceof ClearViewModel) {
            ((ClearViewModel) vm).setLifecycleOwner(fragmentActivity);
        }
        return vm;
    }

    public static <T extends ViewModel> T createViewModel(Fragment fragment, Class<T> type) {
        T vm = ViewModelProviders.of(fragment).get(type);
        if (vm instanceof ClearViewModel) {
            ((ClearViewModel) vm).setLifecycleOwner(fragment);
        }
        return vm;
    }

    /**
     * 用改方法创建的ViewModel的生命周期将于参数activity绑定
     * @param activity 绑定于activity的生命周期
     * @param type ViewModel类型
     * @param <T>类型
     * @return ViewModel的实例
     */
    public static <T extends ViewModel> T createViewModel(Activity activity, Class<T> type) {
        checkInit();

        if (null == activity || null == type) {
            return null;
        }
        android.app.Fragment fragment = activity.getFragmentManager().findFragmentByTag(TAG_VM_FRAGMENT);
        if (!(fragment instanceof VMFragment)) {
            fragment = new VMFragment();
            FragmentManager fm = activity.getFragmentManager();
            fm.beginTransaction().add(fragment, TAG_VM_FRAGMENT).commit();
            fm.executePendingTransactions();
        }

        return createViewModel((VMFragment)fragment, type);
    }

    public static <T extends ViewModel> T createViewModel(ViewModelStoreOwner owner, Class<T> type) {
        T viewModel = null;
        if (null != owner && null != type) {
            if (null == factory) {
                factory = new ViewModelProvider.NewInstanceFactory();
            }
            viewModel = createViewModel(new ViewModelProvider(owner, factory), type);
        }
        return viewModel;
    }

    public static <T extends ViewModel> T createViewModel(ViewModelProvider viewModelProvider, Class<T> type) {
        if (null == viewModelProvider || null == type) {
            return null;
        }
        return viewModelProvider.get(type);
    }

    private static void checkInit() {
        if (!init.get()) {
            throw new IllegalStateException("ViewModelHelper not initialized!");
        }
    }
}
