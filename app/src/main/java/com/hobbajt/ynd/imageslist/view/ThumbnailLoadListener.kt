package com.hobbajt.ynd.imageslist.view

interface ThumbnailLoadListener
{
    fun onThumbnailLoadComplete(position: Int)

    fun onThumbnailLoadFailed()
}