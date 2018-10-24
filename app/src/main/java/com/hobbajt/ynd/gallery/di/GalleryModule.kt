package com.hobbajt.ynd.gallery.di

import com.hobbajt.ynd.base.di.scopes.FragmentScope
import com.hobbajt.ynd.gallery.view.GalleryPresenter
import dagger.Module
import dagger.Provides

@Module
class GalleryModule
{
    @Provides
    @FragmentScope
    fun providesGalleryPresenter(): GalleryPresenter = GalleryPresenter()
}