package com.bzh.dytt.ui.view.film;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bzh.dytt.presenter.impl.film.NewestFilmFImpl;

/**
 * ==========================================================<br>
 * <b>版权</b>：　　　别志华 版权所有(c)2016<br>
 * <b>作者</b>：　　  biezhihua@163.com<br>
 * <b>创建日期</b>：　16-3-20<br>
 * <b>描述</b>：　　　<br>
 * <b>版本</b>：　    V1.0<br>
 * <b>修订历史</b>：　<br>
 * ==========================================================<br>
 */
public interface INewestFilmView {

    void showException();

    void hideRecyclerView();

    void initRecyclerView(LinearLayoutManager linearLayoutManager, RecyclerView.Adapter adapter);

    void showSwipeRefreshing();

    void hideSwipeRefreshing();

    void setOnRefreshListener(NewestFilmFImpl newestFilmF);
}
