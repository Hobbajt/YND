package com.hobbajt.ynd.main.view

import com.hobbajt.ynd.base.mvp.BasePresenter

class MainPresenter: BasePresenter<MainContractor.View>()
{
    fun onExternalStoragePermissionDenied()
    {
        view?.closeApp()
    }

    fun onExternalStoragePermissionGranted()
    {
        view?.start()
    }

    fun onViewReady()
    {
        view?.requestExternalStorageIfRequired()
    }
}