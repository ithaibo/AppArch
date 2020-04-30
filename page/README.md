# 分页
该分页逻辑的封装是针对分页页面的基本逻辑进行高度抽象.

## 请求分页数据的方式有2种：

1. 按照页码请求数据
``` java
PageContract.Presenter presenter;
//请求页码为2的数据
presenter.fetchPageData(2);
```


2. 请求首页，请求下一页
``` java
PageContract.Presenter presenter;

//请求第一页数据
presenter.fetchNextPage();
//请求下一页数据
presenter.fetchFirstPage();
```

特别说明：默认情况下首页页码为1，可以复写方法com.andy.ui.page.PresenterPage.getFirstPageIndex方法来改变


## 数据处理
数据返回后可能出现3种情况
1. 数据请求失败
``` java
@Override
public void onRequestError(@NonNull Object error, int pageIndex) {
  //do something on error
}
```
2. 当前请求的接口没有数据
``` java
@Override
public void onResultEmpty() {
  //do something
}
```
3. 当前请求的页没有数据，即没有更多数据
``` java
@Override
public void onNoMoreData() {
  //do something
}
```
4. 当前页数据正常返回
``` java
@Override
public void onPageDataReady(@NonNull Object data, int pageIndex) {
  //do something on error
}
```