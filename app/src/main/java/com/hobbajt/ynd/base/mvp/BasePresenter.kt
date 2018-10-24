package com.hobbajt.ynd.base.mvp

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<V>
{
    protected val compositeDisposable = CompositeDisposable()

    @Volatile
    var view: V? = null

    open fun attachView(view: V)
    {
        this.view = view
    }

    open fun detachView()
    {
        view = null
        compositeDisposable.dispose()
    }
}

