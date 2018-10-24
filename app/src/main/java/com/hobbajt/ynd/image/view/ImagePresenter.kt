package com.hobbajt.ynd.image.view

import com.hobbajt.ynd.base.domain.Image
import com.hobbajt.ynd.base.mvp.BasePresenter
import com.hobbajt.ynd.base.network.InternetConnectionChecker
import com.hobbajt.ynd.imageslist.model.ImageUrlCreator

class ImagePresenter(private val internetConnectionChecker: InternetConnectionChecker, private val imageSize: Pair<Int, Int>) : BasePresenter<ImageContract.View>()
{
    private var image: Image? = null

    fun onLoadStateComplete(image: Image?)
    {
        image?.let {
            this.image = image
            val url = ImageUrlCreator.createImageUrl(image.id, imageSize.first, imageSize.second)
            view?.displayImage(url)
            displayInfo(it)
        }
    }

    private fun displayInfo(image: Image)
    {
        val author = "${image.author} ${image.index}"
        val size = "${image.width}x${image.height}"
        view?.displayInfo(author, size)

    }

    fun onImageLoadFailed()
    {
        tryConnect()
        view?.hideLoader()
    }

    private fun tryConnect()
    {
        if (!internetConnectionChecker.isConnected())
        {
            view?.displayNoInternetConnectionError()
        } else
        {
            view?.hideNoInternetConnectionError()
        }
    }

    // region Image Loading
    fun onImageDisplayed()
    {
        tryConnect()
        view?.hideLoader()
    }

    fun onImageLoadStarted()
    {
        view?.displayLoader()
    }
    // endregion Image Loading

    fun onSaveState() = image
}
