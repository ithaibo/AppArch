package com.andy.apparch;

import androidx.appcompat.app.AppCompatActivity;

import com.andy.ui.page.PageContract;

public abstract class AtyPage<T> extends AppCompatActivity implements PageContract.View<T> {
    private boolean canTouch;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus && ! canTouch) {
            canTouch = true;
            onRefresh();
        }
    }

    protected abstract PageContract.Presenter getPresenter();

    protected void onRefresh() {
        getPresenter().requestFirstPage();
    }

    protected void onLoadMore() {
        getPresenter().requestNextPage();
    }
}
