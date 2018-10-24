package com.hobbajt.ynd.gallery.view

import android.os.Parcelable
import com.hobbajt.ynd.base.domain.Image
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GalleryState(val images: List<Image>,
                        val currentPosition: Int): Parcelable