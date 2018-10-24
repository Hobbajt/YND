package com.hobbajt.ynd.imageslist.model.network

import com.hobbajt.ynd.base.domain.Image
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class ImagesNetworkLoader(private val api: ImagesApi, private val imageMapper: ImageMapper)
{
    // There is no pagination, because API does not support it. There is no sense for creating pagination on
    // application side, because all items is loaded into memory anyway. RecyclerView displays only a few items at the same time.
    fun load(): Single<MutableList<Image>> = api.getImages().retry(3)
            .doOnSuccess()
            {
                require(it.isNotEmpty())
                {
                    "API returned no images!"
                }
            }
            .flattenAsObservable { it }
            .map { imageMapper.map(it) }
            .toList()
            .doOnSuccess { imageMapper.addIndexes(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}