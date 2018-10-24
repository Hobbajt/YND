package com.hobbajt.ynd.image.view

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hobbajt.ynd.R
import com.hobbajt.ynd.base.domain.Image
import com.hobbajt.ynd.base.mvp.BaseMVPFragment
import com.hobbajt.ynd.gallery.view.InternetConnectionMessageListener
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener
import kotlinx.android.synthetic.main.fragment_image.*
import javax.inject.Inject

class ImageFragment : BaseMVPFragment<ImagePresenter>(), ImageContract.View
{
    @Inject
    lateinit var presenter: ImagePresenter

    private var internetConnectionMessageListener: InternetConnectionMessageListener? = null

    override fun attachView()
    {
        presenter.attachView(this)
        internetConnectionMessageListener = parentFragment as? InternetConnectionMessageListener
    }

    override fun onDetach()
    {
        super.onDetach()
        internetConnectionMessageListener = null
    }

    override fun providePresenter() = presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun loadState(configurationChangeState: Bundle?, passedArguments: Bundle?)
    {
        var image = configurationChangeState?.getParcelable<Image>(IMAGE_TAG)
        if(image == null)
        {
            image = arguments?.getParcelable(IMAGE_TAG)
        }

        presenter.onLoadStateComplete(image)
    }

    // region Loader
    override fun displayLoader()
    {
        pbLoader?.visibility = View.VISIBLE
    }

    override fun hideLoader()
    {
        pbLoader?.visibility = View.GONE
    }
    // endregion Loader

    override fun displayInfo(author: String, size: String)
    {
        txtAuthor.text = author
        txtSize.text = size
    }

    override fun displayImage(imageUrl: String)
    {
        ImageLoader.getInstance().displayImage(imageUrl, imgImage, object: ImageLoadingListener
        {
            override fun onLoadingComplete(imageUri: String?, view: View?, loadedImage: Bitmap?)
            {
                presenter.onImageDisplayed()
            }

            override fun onLoadingStarted(imageUri: String?, view: View?)
            {
                presenter.onImageLoadStarted()
            }

            override fun onLoadingCancelled(imageUri: String?, view: View?)
            {

            }

            override fun onLoadingFailed(imageUri: String?, view: View?, failReason: FailReason?)
            {
                presenter.onImageLoadFailed()
            }

        })
    }

    override fun onDestroy()
    {
        super.onDestroy()
    }

    // region Internet Connection
    override fun displayNoInternetConnectionError()
    {
        internetConnectionMessageListener?.displayNoInternetConnectionError()
    }

    override fun hideNoInternetConnectionError()
    {
        internetConnectionMessageListener?.hideNoInternetConnectionError()
    }
    // endregion Internet Connection

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        val image = presenter.onSaveState()
        outState.putParcelable(IMAGE_TAG, image)
    }

    companion object
    {
        const val IMAGE_TAG = "image"

        fun newInstance(image: Image): ImageFragment
        {
            val fragment = ImageFragment()
            val bundle = Bundle()
            bundle.putParcelable(IMAGE_TAG, image)
            fragment.arguments = bundle
            return fragment
        }
    }
}
