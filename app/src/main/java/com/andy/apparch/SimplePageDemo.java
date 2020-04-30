package com.andy.apparch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.andy.ui.page.PageContract;
import com.andy.ui.page.PresenterPage;

public class SimplePageDemo extends AtyPage {
    @Override
    protected PageContract.Presenter getPresenter() {
        return new Presenter(this, this);
    }

    @Override
    public void onPageDataReady(@NonNull Object data, int pageIndex) {

    }

    @Override
    public void onResultEmpty() {
    }

    @Override
    public void updateLoadMoreEnable(boolean loadMoreEnable) {

    }

    @Override
    public void onNoMoreData() {

    }

    @Override
    public void onRequestError(@NonNull Object error, int pageIndex) {

    }

    private static final class Presenter extends PresenterPage {
        public Presenter(PageContract.View view, LifecycleOwner lifecycleOwner) {
            super(view, lifecycleOwner);
        }

        @Override
        protected int parsePageSize(@NonNull Object result) {
            return 0;
        }

        @Override
        protected LiveData requestData(int page) {
            return null;
        }

        @Override
        protected boolean isRequestSuccess(Object data) {
            return false;
        }

        @Override
        protected Object getError(Object data) {
            return null;
        }

        @Override
        protected boolean isDataEmpty(@Nullable Object result) {
            return false;
        }
    }
}
