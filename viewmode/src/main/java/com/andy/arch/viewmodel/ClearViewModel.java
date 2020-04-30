package com.andy.arch.viewmodel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 处理RxJava2, LiveData等清理工作
 */
public class ClearViewModel extends ViewModel {
    protected LifecycleOwner lifecycleOwner;

    private CompositeDisposable compositeDisposable;
    private List<LiveData> liveDataList = new ArrayList<>(8);

    public <T> LiveData<T> accept(@NonNull LiveData<T> liveData) {
        if (null == lifecycleOwner) {
            throw new IllegalArgumentException("not set LifecycleOwner, cannot invoked this method");
        }

        manageLiveData(liveData);

        return liveData;
    }

    public <T> void accept(@NonNull LiveData<T> liveData, @NonNull Observer<T> observer) {
        manageLiveData(liveData);
        liveData.observe(this.getLifecycleOwner(), observer);
    }

    public void accept(@NonNull Disposable disposable) {
        if (disposable.isDisposed()) {
            return;
        }
        if (null == compositeDisposable) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    public LifecycleOwner getLifecycleOwner() {
        return lifecycleOwner;
    }

    public interface Observable<T> {
        void observe(@NonNull Observer<T> observer);
    }

    /**
     * 将LiveData转为{@link Observable}, 同时加入销毁管理队列
     *
     * @param liveData LiveData
     * @param <T>      type
     * @return Observable
     */
    public <T> Observable<T> map(final LiveData<T> liveData) {
        if (null == liveData) {
            return null;
        }

        return new Observable<T>() {
            @Override
            public void observe(@NonNull Observer<T> observer) {
                ClearViewModel.this.manageLiveData(liveData);
                liveData.observe(lifecycleOwner, observer);
            }
        };
    }

    public <T> Observable<T> map(final LiveData<T> liveData, boolean clearObserver) {
        if (clearObserver && null != liveData) {
            // 清除已有的Observer
            liveData.removeObservers(lifecycleOwner);
        }
        return map(liveData);
    }

    protected void manageLiveData(@NonNull LiveData liveData) {
        if (liveDataList.contains(liveData))
            return;
        liveDataList.add(liveData);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // clear Disposable in RxJava
        if (null != compositeDisposable) {
            compositeDisposable.clear();
            compositeDisposable = null;
        }
        // clear LiveData Observer
        Iterator<LiveData> liveDataIterator = liveDataList.iterator();
        while (liveDataIterator.hasNext()) {
            LiveData liveData = liveDataIterator.next();
            liveData.removeObservers(lifecycleOwner);
            liveDataIterator.remove();
        }
    }
}
