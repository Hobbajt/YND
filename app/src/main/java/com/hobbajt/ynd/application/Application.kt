package com.hobbajt.ynd.application

import android.app.Activity
import android.app.Application
import com.hobbajt.ynd.application.di.DaggerAppComponent
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


class Application : Application(), HasActivityInjector
{
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var imageLoaderConfiguration: ImageLoaderConfiguration

    private lateinit var instance: com.hobbajt.ynd.application.Application

    override fun onCreate()
    {
        super.onCreate()

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)

        instance = this

        ImageLoader.getInstance().init(imageLoaderConfiguration)
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector
}