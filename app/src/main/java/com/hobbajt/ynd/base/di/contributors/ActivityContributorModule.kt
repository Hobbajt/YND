package com.hobbajt.ynd.base.di.contributors

import com.hobbajt.ynd.base.di.scopes.ActivityScope
import com.hobbajt.ynd.main.di.MainActivityModule
import com.hobbajt.ynd.main.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityContributorModule
{
    @ContributesAndroidInjector(modules = [MainActivityModule::class, FragmentsContributorModule::class])
    @ActivityScope
    abstract fun contributeMainActivity(): MainActivity
}