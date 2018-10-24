package com.hobbajt.ynd.gallery.view

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hobbajt.ynd.R
import com.hobbajt.ynd.base.domain.Image
import com.hobbajt.ynd.base.mvp.BaseMVPFragment
import kotlinx.android.synthetic.main.fragment_gallery.*
import javax.inject.Inject

class GalleryFragment : BaseMVPFragment<GalleryPresenter>(), GalleryContract.View, InternetConnectionMessageListener
{
    @Inject
    lateinit var presenter: GalleryPresenter

    private var snackbar: Snackbar? = null

    override fun attachView()
    {
        presenter.attachView(this)
    }

    override fun providePresenter() = presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun loadState(configurationChangeState: Bundle?, passedArguments: Bundle?)
    {
        var galleryState = configurationChangeState?.getParcelable<GalleryState>(GALLERY_STATE_TAG)
        if (galleryState == null)
        {
            galleryState = passedArguments?.getParcelable(GALLERY_STATE_TAG)
        }

        presenter.onLoadStateComplete(galleryState)
    }

    override fun createNoInternetConnectionMessage()
    {
        view?.let {
            snackbar = Snackbar.make(it, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG)
        }
    }

    override fun closeGallery()
    {
        goBack()
    }

    override fun displayImages(images: List<Image>, currentImageIndex: Int)
    {
        val galleryAdapter = GalleryAdapter(images, childFragmentManager)
        vpGallery.adapter = galleryAdapter
        vpGallery.currentItem = currentImageIndex
    }

    override fun displayNoInternetConnectionError()
    {
        snackbar?.let {
            if (!it.isShown)
            {
                it.show()
            }
        }
    }

    override fun hideNoInternetConnectionError()
    {
        snackbar?.dismiss()
    }

    override fun onPause()
    {
        super.onPause()
        hideNoInternetConnectionError()
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        val galleryState = presenter.onSaveState()
        outState.putParcelable(GALLERY_STATE_TAG, galleryState)
    }

    companion object
    {
        const val GALLERY_STATE_TAG = "galleryState"

        fun newInstance(images: List<Image>, currentImageIndex: Int): GalleryFragment
        {
            val fragment = GalleryFragment()
            val bundle = Bundle()
            val galleryState = GalleryState(images, currentImageIndex)
            bundle.putParcelable(GALLERY_STATE_TAG, galleryState)
            fragment.arguments = bundle
            return fragment
        }
    }
}
