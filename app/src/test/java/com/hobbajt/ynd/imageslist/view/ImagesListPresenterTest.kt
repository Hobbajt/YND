package com.hobbajt.ynd.imageslist.view

import com.hobbajt.ynd.base.domain.Image
import com.hobbajt.ynd.base.network.InternetConnectionChecker
import com.hobbajt.ynd.imageslist.model.local.ImagesDatabaseDAO
import com.hobbajt.ynd.imageslist.model.network.ImagesNetworkLoader
import com.nhaarman.mockitokotlin2.eq
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ImagesListPresenterTest
{
    @Mock
    lateinit var view: ImagesListContract.View

    @Mock
    lateinit var imagesNetworkLoader: ImagesNetworkLoader

    @Mock
    lateinit var imageDatabaseDao: ImagesDatabaseDAO

    @Mock
    lateinit var internetConnectionChecker: InternetConnectionChecker

    lateinit var presenter: ImagesListPresenter

    private val images = listOf(Image(123, 635, 842, "John Smith", "photo123.jpg", 8))

    @Before
    fun setUp()
    {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        internetConnectionChecker = Mockito.spy(internetConnectionChecker)
        imagesNetworkLoader = Mockito.spy(imagesNetworkLoader)

        Mockito.doReturn(Single.just(images)).`when`(imagesNetworkLoader).load()
        Mockito.doReturn(Single.just(arrayListOf<Image>())).`when`(imageDatabaseDao).load()

        presenter = Mockito.spy(ImagesListPresenter(imagesNetworkLoader, imageDatabaseDao, internetConnectionChecker))

        presenter.attachView(view)
    }

    @Test
    fun attachViewTest()
    {
        presenter.detachView()

        Assert.assertNull(presenter.view)
        presenter.attachView(view)
        Assert.assertNotNull(presenter.view)
    }

    @Test
    fun detachViewTest()
    {
        presenter.detachView()

        presenter.attachView(view)
        Assert.assertNotNull(presenter.view)

        presenter.detachView()
        Assert.assertNull(presenter.view)
    }

    @Test
    fun onLoadStateCompleteNetworkTest()
    {
        val imagesListState = ImagesListState(images, 15, ImagesListPresenter.ImagesSourceType.NETWORK)

        testInit(imagesListState)

        Mockito.verify(view).hideLoader()
        Mockito.verify(view).displayImages(eq(images), Mockito.eq(15))
        Assert.assertEquals(presenter.onSaveState(), imagesListState)
    }

    @Test
    fun onLoadStateCompleteLocalWithoutInternetTest()
    {
        Mockito.doReturn(false).`when`(internetConnectionChecker).isConnected()
        val imagesListState = ImagesListState(images, 15, ImagesListPresenter.ImagesSourceType.LOCAL)

        testInit(imagesListState)

        Mockito.verify(internetConnectionChecker).isConnected()
        Mockito.verify(view).displayNoInternetConnectionError()
        Assert.assertEquals(presenter.onSaveState(), imagesListState)
    }

    @Test
    fun onLoadStateCompleteLocalWithInternetTest()
    {
        Mockito.doReturn(true).`when`(internetConnectionChecker).isConnected()

        val imagesListState = ImagesListState(emptyList(), 15, ImagesListPresenter.ImagesSourceType.LOCAL)
        val networkImagesListState = ImagesListState(images, 0, ImagesListPresenter.ImagesSourceType.NETWORK)

        testInit(imagesListState)

        Mockito.verify(internetConnectionChecker).isConnected()
        Mockito.verify(imagesNetworkLoader).load()
        Assert.assertEquals(presenter.onSaveState(), networkImagesListState)

        Mockito.verify(view).hideNoInternetConnectionError()
        Mockito.verify(view).hideLoader()
        Mockito.verify(view).displayImages(eq(images), Mockito.eq(0))
    }

    private fun testInit(imagesListState: ImagesListState)
    {
        presenter.onLoadStateComplete(imagesListState)
        Mockito.verify(view).displayLoader()
        Mockito.verify(view).createNoInternetConnectionSnackbar()
        Mockito.verify(view).createImagesList()
    }
}