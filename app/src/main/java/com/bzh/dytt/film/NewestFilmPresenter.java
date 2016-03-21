package com.bzh.dytt.film;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.bzh.data.film.entity.FilmEntity;
import com.bzh.data.repository.Repository;
import com.bzh.dytt.R;
import com.bzh.dytt.base.IFragmentPresenter;
import com.bzh.dytt.base.BaseActivity;
import com.bzh.dytt.base.BaseFragment;
import com.bzh.recycler.ExCommonAdapter;
import com.bzh.recycler.ExRecyclerView;
import com.bzh.recycler.ExViewHolder;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
public class NewestFilmPresenter implements IFragmentPresenter, SwipeRefreshLayout.OnRefreshListener, ExCommonAdapter.OnItemClickListener, ExRecyclerView.OnLoadMoreListener {

    private int index = 1;

    private static final String TAG = "NewestFilmPresenter";

    private final BaseActivity baseActivity;
    private final BaseFragment baseFragment;
    private final NewestFilmIView newestFilmView;
    private ExCommonAdapter<FilmEntity> filmEntityExCommonAdapter;

    public NewestFilmPresenter(BaseActivity baseActivity, BaseFragment baseFragment, NewestFilmIView newestFilmView) {
        this.baseActivity = baseActivity;
        this.baseFragment = baseFragment;
        this.newestFilmView = newestFilmView;
    }

    @Override
    public void onFirstUserVisible() {
        filmEntityExCommonAdapter = new ExCommonAdapter<FilmEntity>(baseActivity, R.layout.item_newestfilm) {
            @Override
            protected void convert(ExViewHolder viewHolder, FilmEntity item) {
                viewHolder.setText(R.id.tv_film_name, item.getName());
                viewHolder.setText(R.id.tv_film_publish_time, item.getPublishTime());
            }
        };
        newestFilmView.getRecyclerView().setOnItemClickListener(this);
        newestFilmView.getRecyclerView().setOnLoadingMoreListener(this);
        newestFilmView.initRecyclerView(new LinearLayoutManager(baseActivity), filmEntityExCommonAdapter);
        newestFilmView.getSwipeRefreshLayout().setOnRefreshListener(this);

        Repository.getInstance().getNewest(index)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewestFilmSubscriber());
    }

    @Override
    public void onUserVisible() {
        Log.d(TAG, "onUserVisible() called with: " + "");
    }

    @Override
    public void onUserInvisible() {
        Log.d(TAG, "onUserInvisible() called with: " + "");
    }

    @Override
    public void onRefresh() {
        index = 1;
        Repository.getInstance().getNewest(index)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewestFilmSubscriber());
    }

    @Override
    public void onItemClick(ExViewHolder viewHolder) {
        Log.d(TAG, "onItemClick() called with: " + "viewHolder = [" + viewHolder + "]");
    }

    @Override
    public void onLoadingMore() {
        Repository.getInstance().getNewest(index)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<FilmEntity>>() {
                    @Override
                    public void onCompleted() {
                        index++;
                        newestFilmView.getRecyclerView().finishLoadingMore();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArrayList<FilmEntity> filmEntities) {
                        filmEntityExCommonAdapter.addData(filmEntities);
                    }
                });
    }

    private final class NewestFilmSubscriber extends Subscriber<ArrayList<FilmEntity>> {

        @Override
        public void onStart() {
            super.onStart();
            newestFilmView.showSwipeRefreshing();
        }

        @Override
        public void onCompleted() {
            newestFilmView.hideSwipeRefreshing();
            index++;
        }

        @Override
        public void onError(Throwable e) {
            newestFilmView.showException();
            newestFilmView.hideRecyclerView();
        }

        @Override
        public void onNext(ArrayList<FilmEntity> filmEntities) {
            filmEntityExCommonAdapter.setData(filmEntities);
        }
    }
}
