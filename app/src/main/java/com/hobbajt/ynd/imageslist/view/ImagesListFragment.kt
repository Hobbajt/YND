package com.hobbajt.ynd.imageslist.view

import android.os.Bundle
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hobbajt.ynd.R
import com.hobbajt.ynd.base.domain.Image
import com.hobbajt.ynd.base.mvp.BaseMVPFragment
import com.hobbajt.ynd.gallery.view.GalleryFragment
import kotlinx.android.synthetic.main.fragment_list_images.*
import javax.inject.Inject

class ImagesListFragment : BaseMVPFragment<ImagesListPresenter>(), ImagesListContract.View
{
    @Inject
    lateinit var presenter: ImagesListPresenter

    private var snackbar: Snackbar? = null

    override fun attachView()
    {
        presenter.attachView(this)
    }

    override fun providePresenter() = presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_list_images, container, false)
    }

    // region Images List
    override fun createImagesList()
    {
        rvImages.layoutManager = LinearLayoutManager(activity)
        val thumbnailSize = resources.getDimensionPixelSize(R.dimen.thumbnail_size)
        rvImages.adapter = ImagesListAdapter(presenter, presenter, thumbnailSize)
    }

    override fun onDestroy()
    {
        super.onDestroy()
        //Prevent memory leaks
        rvImages?.adapter = null
    }

    override fun getFirstVisibleItemPosition() = (rvImages.layoutManager as? LinearLayoutManager)?.findFirstCompletelyVisibleItemPosition() ?: 0

    override fun displayImages(images: List<Image>, currentItemIndex: Int)
    {
        if(rvImages.adapter is ImagesListAdapter)
        {
            (rvImages.adapter as ImagesListAdapter).setImages(images)
            rvImages.scrollToPosition(currentItemIndex)
        }
    }
    // endregion Images List

    // region Internet Connection
    override fun createNoInternetConnectionSnackbar()
    {
        view?.let {
            snackbar = Snackbar.make(it, getString(R.string.no_internet_connection), Snackbar.LENGTH_INDEFINITE)
            snackbar?.setAction(R.string.retry) {}

            // Prevents dismissing snackbar on action click
            snackbar?.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>()
            {
                override fun onShown(transientBottomBar: Snackbar?)
                {
                    super.onShown(transientBottomBar)
                    transientBottomBar?.view
                            ?.findViewById<View>(R.id.snackbar_action)
                            ?.setOnClickListener { onNetworkConnectionRetryClicked() }
                }
            })
        }
    }

    override fun displayNoInternetConnectionError()
    {
        snackbar?.let {
            if(!it.isShown)
            {
                it.show()
            }
        }
    }

    override fun hideNoInternetConnectionError()
    {
        snackbar?.dismiss()
    }

    fun onNetworkConnectionRetryClicked()
    {
        presenter.onInternetConnectionRetryClicked()
    }
    // endregion Internet Connection

    override fun loadState(configurationChangeState: Bundle?, passedArguments: Bundle?)
    {
        var imagesListState = configurationChangeState?.getParcelable<ImagesListState>(IMAGES_LIST_STATE_TAG)
        if(imagesListState == null)
        {
            imagesListState = passedArguments?.getParcelable(IMAGES_LIST_STATE_TAG)
        }

        presenter.onLoadStateComplete(imagesListState)
    }

    override fun onPause()
    {
        super.onPause()
        hideNoInternetConnectionError()
    }

    override fun displayGallery(images: List<Image>, position: Int)
    {
        val galleryFragment = GalleryFragment.newInstance(images, position)
        fragmentsManager.changeFragment(galleryFragment, true)
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        val imagesState = presenter.onSaveState()
        outState.putParcelable(IMAGES_LIST_STATE_TAG, imagesState)
    }

    // region Loader
    override fun displayLoader()
    {
        pbLoader.visibility = View.VISIBLE
    }

    override fun hideLoader()
    {
        pbLoader.visibility = View.GONE
    }
    // endregion Loader

    companion object
    {
        const val IMAGES_LIST_STATE_TAG = "imagesListStateTag"
        fun newInstance() = ImagesListFragment()
    }
}