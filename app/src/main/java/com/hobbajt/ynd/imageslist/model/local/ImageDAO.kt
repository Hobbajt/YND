package com.hobbajt.ynd.imageslist.model.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.hobbajt.ynd.base.domain.Image
import io.reactivex.Single

@Dao
interface ImageDAO
{
    @Query("SELECT * FROM images")
    fun load(): Single<List<Image>>

    @Insert
    fun insert(image: Image)

    @Query("SELECT COUNT(*) FROM images WHERE images.id = :id")
    fun getById(id: Long): Int
}