package com.hobbajt.ynd.imageslist.model.network

import com.hobbajt.ynd.base.domain.Image
import com.hobbajt.ynd.imageslist.model.dto.ImageDTO
import junit.framework.Assert
import org.junit.Test

class ImageMapperTest
{
    private val imageMapper = ImageMapper()

    @Test
    fun mapTest()
    {
        val imageDto = ImageDTO(123,
                635,
                842,
                "John Smith",
                "photo123.jpg")

        val imageTest = Image(123,
                635,
                842,
                "John Smith",
                "photo123.jpg",
                1)

        Assert.assertEquals(imageMapper.map(imageDto), imageTest)
    }

    @Test
    fun addIndexesTest()
    {
        val images = mutableListOf(Image(123, 635, 842, "John Smith", "photo123.jpg", 1),
                Image(123, 635, 842, "Tom Parker", "photo123.jpg", 1),
                Image(123, 635, 842, "John Smith", "photo123.jpg", 8),
                Image(123, 635, 842, "Paul", "photo123.jpg", 0),
                Image(123, 635, 842, "Tom", "photo123.jpg", 1),
                Image(123, 635, 842, "John Smith", "photo123.jpg", 1),
                Image(123, 635, 842, "John Smith", "photo123.jpg", 1),
                Image(123, 635, 842, "Paul", "photo123.jpg", 285))

        Assert.assertEquals(imageMapper.addIndexes(images).map { it.index }, listOf(1, 1, 2, 1, 1, 3, 4, 2))
    }
}