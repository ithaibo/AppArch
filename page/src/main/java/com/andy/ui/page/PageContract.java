package com.andy.ui.page;

import androidx.annotation.NonNull;

/**
 * 分页逻辑
 */
public interface PageContract {
    interface View<T> {
        /**首页数据*/
        void onFirstPageDataReady(@NonNull T firstPage);
        /**加载更多数据*/
        void onMoreDataReady(@NonNull T otherPage);
        /**数据为空*/
        void onResultEmpty(int pageIndex);
        /**数据请求失败*/
        <E> void onRequestError(@NonNull E error, int pageIndex);
        /**更新加载更多的状态*/
        void updateLoadMoreEnable(boolean loadMoreEnable);
        /**数据加载完毕*/
        void onNoMoreData();
    }
    interface Presenter {
        /**
         * 请求首页数据
         */
        void requestFirstPage();

        /**
         * 请求下一页数据
         */
        void requestNextPage();

        /**
         * @return 当前是否为第一页
         */
        boolean isFirstPage();
    }
}
