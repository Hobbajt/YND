package com.hobbajt.ynd.gallery.view

import com.hobbajt.ynd.base.domain.Image
import com.hobbajt.ynd.base.mvp.BasePresenter

class GalleryPresenter : BasePresenter<GalleryContract.View>()
{
    private var images: List<Image> = emptyList()
    private var currentItemIndex: Int = 0

    fun onLoadStateComplete(galleryState: GalleryState?)
    {
        view?.createNoInternetConnectionMessage()

        if (galleryState == null)
        {
            view?.closeGallery()
        }
        else
        {
            this.images = galleryState.images
            this.currentItemIndex = galleryState.currentPosition

            view?.displayImages(galleryState.images, galleryState.currentPosition)
        }
    }

    fun onSaveState(): GalleryState
    {
        return GalleryState(images, currentItemIndex)
    }
}
