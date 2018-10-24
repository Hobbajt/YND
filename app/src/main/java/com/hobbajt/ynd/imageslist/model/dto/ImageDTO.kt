package com.hobbajt.ynd.imageslist.model.dto

import com.google.gson.annotations.SerializedName

data class ImageDTO(val id: Long,
                    val width: Int,
                    val height: Int,
                    val author: String,
                    @SerializedName("filename")
                    val fileName: String)