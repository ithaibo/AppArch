package com.andy.ui.page;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 分页——通用逻辑封装
 * <ol>
 *     <li>整个请求发起——数据解析——结果渲染流程控制封装</li>
 *     <li>预留数据请求的实现入口</li>
 *     <li>预留数据解析实现
 *     <ul>
 *         <li>页码解析</li>
 *         <li>页大小解析</li>
 *         <li>数据是否为空</li>
 *         <li>请求是否成功</li>
 *         <li>错误类型解析</li>
 *     </ul>
 *     </li>
 *     <li>预留起始页码配置（重写getFirstPageIndex方法）</li>
 *     <li>预留页面大小配置（重写parsePageSize方法）</li>
 * </ol>
 * @param <T> 页面数据类型
 */
public abstract class PresenterPage<T> implements PageContract.Presenter {
    protected int page;
    private int tempPage;

    protected PageContract.View<T> view;
    protected LifecycleOwner lifecycleOwner;

    public PresenterPage(PageContract.View<T> view, LifecycleOwner lifecycleOwner) {
        if (null == view)
            throw new RuntimeException("view cannot be null!");
        if (null == lifecycleOwner)
            throw new RuntimeException("lifecycleOwner cannot be null!");

        this.view = view;
        this.lifecycleOwner = lifecycleOwner;
    }

    private Observer<T> observer = new Observer<T>() {
        @Override
        public void onChanged(@Nullable T t) {
            if (isRequestSuccess(t)) {
                page = tempPage;
                tempPage = getFirstPageIndex();

                if (null == t || isDataEmpty(t)) {
                    if (isFirstPage()) {
                        view.onResultEmpty(getFirstPageIndex());
                    } else {
                        view.onNoMoreData();
                    }
                    return;
                }

                final boolean loadCompleted = isLoadCompleted(t);
                if (isFirstPage()) {
                    view.onFirstPageDataReady(t);
                } else {
                    view.onMoreDataReady(t);
                }
                if (loadCompleted) {
                    view.onNoMoreData();
                    view.updateLoadMoreEnable(false);
                } else {
                    view.updateLoadMoreEnable(true);
                }
                return;
            }

            if (getError(t)) {
                view.onRequestError(getError(t), page);
            }
        }
    };

    @Override
    public final void requestFirstPage() {
        tempPage = getFirstPageIndex();
        requestData(tempPage).observe(lifecycleOwner, observer);
    }

    @Override
    public final void requestNextPage() {
        tempPage = page + 1;
    }

    /**
     *
     * @return 分页-起始页索引(默认从1开始)
     */
    protected int getFirstPageIndex() {
        return 1;
    }

    /**
     * @return 分页逻辑-页面的啊小(默认大小为10)
     */
    protected int getPageSize() {
        return 10;
    }

    /**
     *
     * @param result 页面数据
     * @return 是否已加载完成（有没有更多页面）
     */
    protected boolean isLoadCompleted(@NonNull T result) {
        return getPageSize() > parsePageSize(result);
    }

    /**
     * 解析出页面大小
     * @param result 页面数据
     * @return 返回数据的页面大小
     */
    protected abstract int parsePageSize(@NonNull T result);

    /**
     * 实际请求实现逻辑
     * @param page 请求的页面
     * @return 页面数据
     */
    protected abstract LiveData<T> requestData(int page);

    /**
     * @param data 页数据
     * @return 是否当前接口请求成功
     */
    protected abstract boolean isRequestSuccess(T data);

    /**
     * @param data 页数据
     * @param <E> 错误类型
     * @return 请求失败的错误
     */
    protected abstract <E> E getError(T data);

    /**
     * @param result 页数据
     * @return 数据是否为空
     */
    protected abstract boolean isDataEmpty(@Nullable T result);

    /**
     * @return 是否为第一页数据
     */
    public final boolean isFirstPage() {
        return page == getFirstPageIndex();
    }
}
