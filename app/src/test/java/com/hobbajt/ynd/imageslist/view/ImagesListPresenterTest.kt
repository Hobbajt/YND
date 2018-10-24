package com.hobbajt.ynd.imageslist.view

import com.hobbajt.ynd.base.domain.Image
import com.hobbajt.ynd.base.network.InternetConnectionChecker
import com.hobbajt.ynd.imageslist.model.local.ImagesDatabaseDAO
import com.hobbajt.ynd.imageslist.model.network.ImagesNetworkLoader
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ImagesListPresenterTest
{
    @Mock
    lateinit var imagesNetworkLoader: ImagesNetworkLoader

    @Mock
    lateinit var imageDatabaseDao: ImagesDatabaseDAO

    @Mock
    lateinit var internetConnectionChecker: InternetConnectionChecker

    @Mock
    lateinit var view: ImagesListContract.View

    lateinit var presenter: ImagesListPresenter

    @Before
    fun setUp()
    {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

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
    fun onLoadStateCompleteTest()
    {
        val imageListState = ImagesListState(emptyList(), 0, ImagesListPresenter.ImagesSourceType.NONE)
        presenter.onLoadStateComplete(imageListState)
        Mockito.verify(view, times(1)).displayLoader()
        Mockito.verify(view, times(1)).createNoInternetConnectionSnackbar()
        Mockito.verify(view, times(1)).createImagesList()
    }

}