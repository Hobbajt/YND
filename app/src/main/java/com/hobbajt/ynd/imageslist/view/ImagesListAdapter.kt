package com.hobbajt.ynd.imageslist.view

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hobbajt.ynd.R
import com.hobbajt.ynd.base.domain.Image
import com.hobbajt.ynd.imageslist.model.ImageUrlCreator
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener
import kotlinx.android.synthetic.main.item_image.view.*

class ImagesListAdapter(private val thumbnailLoadListener: ThumbnailLoadListener,
                        private val itemClickListener: ItemClickListener,
                        private val thumbnailSize: Int) : RecyclerView.Adapter<ImagesListAdapter.ImageItemHolder>()
{
    private var images: List<Image> = listOf()
    private val imageLoader = ImageLoader.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageItemHolder
    {
        return ImageItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false))
    }

    override fun onBindViewHolder(holder: ImageItemHolder, position: Int)
    {
        val image = images[position]
        holder.bind(image)
    }

    override fun getItemCount(): Int = images.size

    fun setImages(images: List<Image>)
    {
        this.images = images
        notifyDataSetChanged()
    }

    inner class ImageItemHolder(val view: View) : RecyclerView.ViewHolder(view)
    {
        private val txtAuthor: TextView = view.txtAuthor
        private val imgImage: ImageView = view.imgImage

        fun bind(image: Image)
        {
            view.setOnClickListener { itemClickListener.onItemClick(adapterPosition) }
            displayThumbnail(image)
            txtAuthor.text = "${image.author} ${image.index}"
        }

        private fun displayThumbnail(image: Image)
        {
            val position = adapterPosition

            val thumbnailUrl = ImageUrlCreator.createThumbnailUrl(image.id, thumbnailSize)

            imageLoader.displayImage(thumbnailUrl, imgImage, object : ImageLoadingListener
            {
                override fun onLoadingComplete(imageUri: String?, view: View?, loadedImage: Bitmap?)
                {
                    loadedImage?.let {
                        thumbnailLoadListener.onThumbnailLoadComplete(position)
                    }
                }

                override fun onLoadingStarted(imageUri: String?, view: View?) {}
                override fun onLoadingCancelled(imageUri: String?, view: View?) {}
                override fun onLoadingFailed(imageUri: String?, view: View?, failReason: FailReason?)
                {
                    thumbnailLoadListener.onThumbnailLoadFailed()
                }

            })
        }

    }
}
