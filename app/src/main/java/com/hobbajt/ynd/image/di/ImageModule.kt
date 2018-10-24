package com.hobbajt.ynd.image.di

import com.hobbajt.ynd.application.di.AppModule
import com.hobbajt.ynd.base.di.scopes.FragmentScope
import com.hobbajt.ynd.base.network.InternetConnectionChecker
import com.hobbajt.ynd.image.view.ImagePresenter
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ImageModule
{
    @Provides
    @FragmentScope
    fun providesImagePresenter(internetConnectionChecker: InternetConnectionChecker, @Named(AppModule.SCREEN_SIZE_TAG) screenSize: Pair<Int, Int>):
            ImagePresenter = ImagePresenter(internetConnectionChecker, screenSize)
}