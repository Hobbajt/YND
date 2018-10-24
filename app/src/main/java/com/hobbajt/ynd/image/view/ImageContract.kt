package com.hobbajt.ynd.image.view

interface ImageContract
{
    interface View
    {
        fun displayImage(imageUrl: String)
        fun displayLoader()
        fun hideLoader()
        fun displayNoInternetConnectionError()
        fun hideNoInternetConnectionError()
        fun displayInfo(author: String, size: String)
    }
}