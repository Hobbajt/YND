package com.hobbajt.ynd.application.di

import com.hobbajt.ynd.application.Application
import com.hobbajt.ynd.base.di.contributors.ActivityContributorModule
import com.hobbajt.ynd.base.network.di.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class, ActivityContributorModule::class, NetworkModule::class])
interface AppComponent
{
    @Component.Builder
    interface Builder
    {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(application: Application)
}