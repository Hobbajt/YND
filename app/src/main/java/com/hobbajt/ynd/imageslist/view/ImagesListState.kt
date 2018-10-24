package com.hobbajt.ynd.imageslist.view

import android.os.Parcelable
import com.hobbajt.ynd.base.domain.Image
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImagesListState(val images: List<Image>,
                           val currentPosition: Int,
                           val imagesSourceType: ImagesListPresenter.ImagesSourceType): Parcelable