package com.hobbajt.ynd.imageslist.model.local

import com.hobbajt.ynd.base.domain.Image
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ImagesDatabaseDAO @Inject constructor(private val imageDao: ImageDAO)
{
    fun load(): Single<List<Image>> = imageDao.load().retry(3)
            .doOnSuccess()
            {
                require(it.isNotEmpty())
                {
                    "API returned no images!"
                }
            }
            .flattenAsObservable { it }
            .toList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun add(image: Image): Completable = Completable.fromAction { imageDao.insert(image) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    fun checkIfExists(image: Image): Single<Boolean> = Single.fromCallable { imageDao.getById(image.id) }
            .map { it >= 1 }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}