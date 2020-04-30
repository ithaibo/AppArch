package com.andy.ui.page;

import androidx.annotation.NonNull;

/**
 * 分页逻辑
 */
public interface PageContract {
    interface View<T> {
        /**页数据请求成功回调*/
        void onPageDataReady(@NonNull T data, int pageIndex);
        /**数据为空*/
        void onResultEmpty();
        /**数据请求失败*/
        <E> void onRequestError(@NonNull E error, int pageIndex);
        /**更新加载更多的状态*/
        void updateLoadMoreEnable(boolean loadMoreEnable);
        /**数据加载完毕*/
        void onNoMoreData();
    }
    interface Presenter {
        void fetchPageData(int page);
        void fetchNextPage();
        void fetchFirstPage();
    }
}
