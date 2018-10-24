package com.hobbajt.ynd.imageslist.model.network

import com.hobbajt.ynd.imageslist.model.dto.ImageDTO
import io.reactivex.Single
import retrofit2.http.GET

interface ImagesApi
{
    @GET("list")
    fun getImages(): Single<List<ImageDTO>>
}
