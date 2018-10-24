package com.hobbajt.ynd.main.view

interface MainContractor
{
    interface View
    {
        fun closeApp()
        fun requestExternalStorageIfRequired()
        fun start()
    }
}