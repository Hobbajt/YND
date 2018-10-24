package com.hobbajt.ynd.gallery.view.edgeswipeviewpager

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Custom ViewPager, which allows to move between fragments and perform gesture on image.
 * ViewPager detects moves between fragments in specific areas on left and right side of view.
 * Remaining space in the middle of view is used to perform gestures on image
 */
class EdgeSwipeViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null): ViewPager(context, attrs)
{
    private var swipeableLeftRange: IntRange? = null
    private var swipeableRightRange: IntRange? = null

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int)
    {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        calculateSwipeAreas(width)
    }

    private fun calculateSwipeAreas(width: Int)
    {
        swipeableLeftRange = IntRange(0, (width * .15).toInt())
        swipeableRightRange = IntRange((width * .85).toInt(), width)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean
    {
        return if (isInSwipeableArea(event.x))
        {
            super.onInterceptTouchEvent(event)
        }
        else
        {
            false
        }
    }

    private fun isInSwipeableArea(x: Float): Boolean
    {
        val isInLeftArea = swipeableLeftRange?.contains(x) ?: false
        val isInRightArea = swipeableRightRange?.contains(x) ?: false

        return  isInLeftArea || isInRightArea
    }
}