package com.hobbajt.ynd.imageslist.model

import junit.framework.Assert
import org.junit.Test

class ImageUrlCreatorTest
{
    @Test
    fun createImageUrlTest()
    {
        Assert.assertEquals("https://picsum.photos/500/250?image=0", ImageUrlCreator.createImageUrl(0, 500, 250))
        Assert.assertEquals("https://picsum.photos/100/100?image=5", ImageUrlCreator.createImageUrl(5, 100, 100))
        Assert.assertEquals("https://picsum.photos/0/0?image=5", ImageUrlCreator.createImageUrl(5, 0, 0))
    }

    @Test
    fun createThumbnailUrlTest()
    {
        Assert.assertEquals("https://picsum.photos/0?image=7", ImageUrlCreator.createThumbnailUrl(7, 0))
        Assert.assertEquals("https://picsum.photos/262?image=7", ImageUrlCreator.createThumbnailUrl(7, 262))
    }
}