package com.hobbajt.ynd.gallery.view

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.hobbajt.ynd.base.domain.Image
import com.hobbajt.ynd.image.view.ImageFragment


class GalleryAdapter(private val images: List<Image>, fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager)
{
    override fun getItem(position: Int): Fragment = ImageFragment.newInstance(images[position])

    override fun getCount(): Int = images.size
}