package com.hobbajt.ynd.gallery.view

import com.hobbajt.ynd.base.domain.Image

interface GalleryContract
{
    interface View
    {
        fun displayImages(images: List<Image>, currentImageIndex: Int)
        fun createNoInternetConnectionMessage()
        fun closeGallery()
    }
}
