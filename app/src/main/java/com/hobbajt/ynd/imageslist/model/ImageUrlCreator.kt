package com.hobbajt.ynd.imageslist.model

import com.hobbajt.ynd.BuildConfig

object ImageUrlCreator
{
    fun createImageUrl(id: Long, width: Int, height: Int) = "${BuildConfig.SERVER_URL}$width/$height?image=$id"

    fun createThumbnailUrl(id: Long, size: Int): String = "${BuildConfig.SERVER_URL}$size?image=$id"
}