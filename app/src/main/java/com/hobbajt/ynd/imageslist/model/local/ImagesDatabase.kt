package com.hobbajt.ynd.imageslist.model.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.hobbajt.ynd.base.domain.Image

@Database(entities = [Image::class], version = 1, exportSchema = false)
abstract class ImagesDatabase: RoomDatabase()
{
    abstract fun imageDao(): ImageDAO
}