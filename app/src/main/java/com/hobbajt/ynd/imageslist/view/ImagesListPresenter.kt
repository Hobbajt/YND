package com.hobbajt.ynd.imageslist.view

import com.hobbajt.ynd.base.domain.Image
import com.hobbajt.ynd.base.mvp.BasePresenter
import com.hobbajt.ynd.base.network.InternetConnectionChecker
import com.hobbajt.ynd.base.network.NoInternetConnectionException
import com.hobbajt.ynd.imageslist.model.local.ImagesDatabaseDAO
import com.hobbajt.ynd.imageslist.model.network.ImagesNetworkLoader
import javax.inject.Inject

class ImagesListPresenter @Inject constructor(private val imagesNetworkLoader: ImagesNetworkLoader,
                                              private val imageDatabaseDao: ImagesDatabaseDAO,
                                              private val internetConnectionChecker: InternetConnectionChecker) :
        BasePresenter<ImagesListContract.View>(), ThumbnailLoadListener, ItemClickListener
{
    private var images = emptyList<Image>()
    private var listPosition: Int = 0
    private var imagesSourceType = ImagesSourceType.NONE

    enum class ImagesSourceType
    {
        NONE,
        LOCAL,
        NETWORK
    }

    fun onLoadStateComplete(imagesListState: ImagesListState?)
    {
        view?.displayLoader()
        view?.createNoInternetConnectionSnackbar()
        view?.createImagesList()

        setState(imagesListState)

        loadImages()
    }

    private fun setState(imagesListState: ImagesListState?)
    {
        if (imagesListState != null && imagesListState.images.isNotEmpty())
        {
            this.images = imagesListState.images
            this.listPosition = imagesListState.currentPosition
            this.imagesSourceType = imagesListState.imagesSourceType
        }
    }

    private fun loadImages()
    {
        if (imagesSourceType == ImagesSourceType.NETWORK || images.isNotEmpty())
        {
            displayImages(images)
        }

        if (imagesSourceType != ImagesSourceType.NETWORK)
        {
            tryConnect()
        }
    }

    // region Load Network Images
    private fun loadNetworkImages()
    {
        compositeDisposable.add(imagesNetworkLoader.load().subscribe(
                { onLoadNetworkImagesSuccess(it) },
                { onLoadNetworkImagesError(it) }))
    }

    private fun onLoadNetworkImagesSuccess(images: MutableList<Image>)
    {
        this.images = images.toList()
        imagesSourceType = ImagesSourceType.NETWORK

        view?.hideNoInternetConnectionError()
        displayImages(images)
    }

    private fun onLoadNetworkImagesError(throwable: Throwable)
    {
        loadLocalImages()
        if (throwable is NoInternetConnectionException)
        {
            view?.displayNoInternetConnectionError()
        }
    }
    // endregion Load Network Images

    // region Load Local Images
    private fun loadLocalImages()
    {
        compositeDisposable.add(imageDatabaseDao.load().subscribe(
                { onLoadLocalImagesSuccess(it) },
                { onLoadLocalImagesError() }))
    }

    private fun onLoadLocalImagesSuccess(images: List<Image>)
    {
        if (imagesSourceType == ImagesSourceType.NONE)
        {
            this.images = images.toList()
            displayImages(images)
        }
        imagesSourceType = ImagesSourceType.LOCAL
    }

    private fun onLoadLocalImagesError()
    {
        if (images.isNotEmpty())
        {
            displayImages(images)
        } else
        {
            imagesSourceType = ImagesSourceType.NONE
        }
    }
    // endregion Load Images

    private fun displayImages(images: List<Image>)
    {
        view?.hideLoader()
        view?.displayImages(images, listPosition)
    }

    fun onSaveState(): ImagesListState
    {
        return ImagesListState(images, listPosition, imagesSourceType)
    }

    private fun saveImage(image: Image)
    {
        compositeDisposable.add(imageDatabaseDao.checkIfExists(image)
                .subscribe { isExists ->
                    if (!isExists)
                    {
                        imageDatabaseDao.add(image).subscribe()
                    }
                })
    }

    private fun tryConnect()
    {
        if (!internetConnectionChecker.isConnected())
        {
            loadLocalImages()
            view?.displayNoInternetConnectionError()
        } else
        {
            loadNetworkImages()
            view?.hideNoInternetConnectionError()
        }
    }

    // region Load Thumbnail
    override fun onThumbnailLoadComplete(position: Int)
    {
        val image = images[position]
        saveImage(image)
    }

    override fun onThumbnailLoadFailed()
    {
        if (!internetConnectionChecker.isConnected())
        {
            view?.displayNoInternetConnectionError()
        } else
        {
            view?.hideNoInternetConnectionError()
        }
    }
    // endregion Load Thumbnail

    // region Clicks
    override fun onItemClick(position: Int)
    {
        this.listPosition = view?.getFirstVisibleItemPosition() ?: position
        view?.displayGallery(images, position)
    }

    fun onInternetConnectionRetryClicked()
    {
        tryConnect()
    }
    // endregion Clicks
}
