package com.hobbajt.ynd.application.di

import android.arch.persistence.room.Room
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.view.WindowManager
import com.hobbajt.ynd.R
import com.hobbajt.ynd.application.Application
import com.hobbajt.ynd.imageslist.model.local.ImagesDatabase
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule
{
    companion object
    {
        const val SCREEN_SIZE_TAG = "screenSize"
        private const val DATABASE_NAME_TAG = "images_db"
    }

    @Provides
    @Singleton
    @Named(SCREEN_SIZE_TAG)
    fun providesScreenSize(application: Application): Pair<Int, Int>
    {
        val windowManager = application.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return Pair(Math.max(size.x, size.y), Math.min(size.x, size.y))
    }

    @Provides
    @Singleton
    fun providesImageLoaderConfiguration(application: Application, @Named(SCREEN_SIZE_TAG) screenSize: Pair<Int, Int>): ImageLoaderConfiguration
    {
        val displayImageOptions = DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisk(true)
                .showImageOnFail(R.drawable.ic_error)
                .build()

        return ImageLoaderConfiguration.Builder(application.applicationContext)
                .threadPriority(Thread.NORM_PRIORITY)
                .defaultDisplayImageOptions(displayImageOptions)
                //Cache offline
                .diskCacheExtraOptions(screenSize.first / 2, screenSize.second / 2, null)
                .writeDebugLogs()
                .build()
    }

    @Provides
    @Singleton
    fun providesImagesDatabase(application: Application): ImagesDatabase = Room.databaseBuilder(application, ImagesDatabase::class.java, DATABASE_NAME_TAG).build()
}