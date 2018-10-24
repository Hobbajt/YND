package com.hobbajt.ynd.gallery.view

import com.hobbajt.ynd.base.domain.Image
import com.hobbajt.ynd.base.domain.ImagesState
import com.hobbajt.ynd.base.mvp.BasePresenter

class GalleryPresenter : BasePresenter<GalleryContract.View>()
{
    private var images: List<Image> = emptyList()
    private var currentItemIndex: Int = 0

    fun onLoadStateComplete(imagesState: ImagesState?)
    {
        view?.createNoInternetConnectionMessage()

        if (imagesState == null)
        {
            view?.closeGallery()
        }
        else
        {
            this.images = imagesState.images
            this.currentItemIndex = imagesState.currentPosition

            view?.displayImages(imagesState.images, imagesState.currentPosition)
        }
    }

    fun onSaveState(): ImagesState
    {
        return ImagesState(images, currentItemIndex)
    }
}
