package com.hobbajt.ynd.imageslist.view

import com.hobbajt.ynd.base.domain.Image

interface ImagesListContract
{
    interface View
    {
        fun displayImages(images: List<Image>, currentItemIndex: Int)
        fun displayGallery(images: List<Image>, position: Int)
        fun displayLoader()
        fun hideLoader()
        fun displayNoInternetConnectionError()
        fun hideNoInternetConnectionError()
        fun createNoInternetConnectionSnackbar()
        fun createImagesList()
        fun getFirstVisibleItemPosition(): Int
    }

    interface ImageHolderView
    {
        fun bind(image: Image)
    }
}