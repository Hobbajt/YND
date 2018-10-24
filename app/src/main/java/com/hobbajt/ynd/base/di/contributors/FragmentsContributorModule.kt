package com.hobbajt.ynd.base.di.contributors

import com.hobbajt.ynd.base.di.scopes.FragmentScope
import com.hobbajt.ynd.gallery.di.GalleryModule
import com.hobbajt.ynd.gallery.view.GalleryFragment
import com.hobbajt.ynd.image.di.ImageModule
import com.hobbajt.ynd.image.view.ImageFragment
import com.hobbajt.ynd.imageslist.di.ImagesListModule
import com.hobbajt.ynd.imageslist.view.ImagesListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
internal abstract class FragmentsContributorModule
{
    @ContributesAndroidInjector(modules = [ImagesListModule::class])
    @FragmentScope
    abstract fun bindImagesListFragment(): ImagesListFragment

    @ContributesAndroidInjector(modules = [GalleryModule::class])
    @FragmentScope
    abstract fun bindGalleryFragment(): GalleryFragment

    @ContributesAndroidInjector(modules = [ImageModule::class])
    @FragmentScope
    abstract fun bindImageFragment(): ImageFragment
}