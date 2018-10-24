package com.hobbajt.ynd.imageslist.di

import com.hobbajt.ynd.base.di.scopes.FragmentScope
import com.hobbajt.ynd.base.network.InternetConnectionChecker
import com.hobbajt.ynd.imageslist.model.local.ImagesDatabase
import com.hobbajt.ynd.imageslist.model.local.ImagesDatabaseDAO
import com.hobbajt.ynd.imageslist.model.network.ImageMapper
import com.hobbajt.ynd.imageslist.model.network.ImagesApi
import com.hobbajt.ynd.imageslist.model.network.ImagesNetworkLoader
import com.hobbajt.ynd.imageslist.view.ImagesListPresenter
import dagger.Module
import dagger.Provides

@Module
class ImagesListModule
{
    @Provides
    @FragmentScope
    fun providesImageMapper(): ImageMapper = ImageMapper()

    @Provides
    @FragmentScope
    fun providesImagesListApiLoader(api: ImagesApi, imageMapper: ImageMapper): ImagesNetworkLoader = ImagesNetworkLoader(api, imageMapper)

    @Provides
    @FragmentScope
    fun providesImageDatabaseDao(imagesDatabase: ImagesDatabase): ImagesDatabaseDAO = ImagesDatabaseDAO(imagesDatabase.imageDao())

    @Provides
    @FragmentScope
    fun providesImagesListPresenter(imagesNetworkLoader: ImagesNetworkLoader, imageDatabaseDao: ImagesDatabaseDAO, internetConnectionChecker: InternetConnectionChecker):
            ImagesListPresenter = ImagesListPresenter(imagesNetworkLoader, imageDatabaseDao, internetConnectionChecker)
}