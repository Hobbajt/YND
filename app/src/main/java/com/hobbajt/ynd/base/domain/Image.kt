package com.hobbajt.ynd.base.domain

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "images")
data class Image(@PrimaryKey
                 val id: Long,
                 val width: Int,
                 val height: Int,
                 val author: String,
                 val fileName: String,
                 var index: Int): Parcelable