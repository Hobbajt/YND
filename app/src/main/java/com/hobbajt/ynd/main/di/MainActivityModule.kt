package com.hobbajt.ynd.main.di

import com.hobbajt.ynd.base.di.scopes.ActivityScope
import com.hobbajt.ynd.main.FragmentsManager
import com.hobbajt.ynd.main.view.MainActivity
import com.hobbajt.ynd.main.view.MainPresenter
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule
{
    @Provides
    @ActivityScope
    fun providesFragmentsManager(activity: MainActivity): FragmentsManager = FragmentsManager(activity.supportFragmentManager)

    @Provides
    @ActivityScope
    fun providesMainPresenter(): MainPresenter = MainPresenter()

}