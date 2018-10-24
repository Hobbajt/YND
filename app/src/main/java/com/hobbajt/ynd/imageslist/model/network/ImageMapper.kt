package com.hobbajt.ynd.imageslist.model.network

import com.hobbajt.ynd.base.domain.Image
import com.hobbajt.ynd.imageslist.model.dto.ImageDTO

class ImageMapper
{
    fun map(imageDto: ImageDTO): Image
    {
        return Image(imageDto.id,
                imageDto.width,
                imageDto.height,
                imageDto.author,
                imageDto.fileName,
                1)
    }

    fun addIndexes(images: MutableList<Image>): List<Image>
    {
        val authors = images.distinct()
                .map { it.author to 0 }
                .toMap()
                .toMutableMap()

        images.forEach {
            val index = (authors[it.author] ?: 0) + 1
            authors[it.author] = index
            it.index = index
        }

        return images
    }
}