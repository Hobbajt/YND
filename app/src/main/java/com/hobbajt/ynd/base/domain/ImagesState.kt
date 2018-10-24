package com.hobbajt.ynd.base.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImagesState(val images: List<Image>,
                       val currentPosition: Int): Parcelable