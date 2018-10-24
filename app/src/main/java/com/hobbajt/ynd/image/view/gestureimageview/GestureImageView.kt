package com.hobbajt.ynd.image.view.gestureimageview

import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView

/**
 * Custom image, which allows to perform zoom, pinch and pan gestures.
 * https://stackoverflow.com/a/43647820/6697357
 */
class GestureImageView : AppCompatImageView, View.OnTouchListener
{
    private var currentMode = GestureType.NONE

    private var currentScale = 1f

    private var originalWidth: Float = 0f
    private var originalHeight: Float = 0f

    private val previousDragPosition = PointF()
    private val startDragPosition = PointF()

    private val currentMatrix = Matrix()
    private var matrixValues = FloatArray(9)


    private var oldMeasuredHeight: Int = 0
    private var oldMeasuredWidth: Int = 0
    private var viewWidth: Int = 0
    private var viewHeight: Int = 0

    private var scaleDetector: ScaleGestureDetector
    
    companion object
    {
        private const val MAX_SCALE = 5f
        private const val MIN_SCALE = 1f

        private const val MIN_DRAG_DISTANCE = 3f
    }
    
    enum class GestureType
    {
        NONE,
        DRAG,
        ZOOM
    }

    init
    {
        scaleDetector = ScaleGestureDetector(context, ScaleListener())
    }

    constructor(context: Context) : super(context)
    {
        bind()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {
        bind()
    }

    private fun bind()
    {
        super.setClickable(true)

        imageMatrix = currentMatrix
        scaleType = ImageView.ScaleType.MATRIX

        setOnTouchListener(this)
    }

    override fun onTouch(view: View?, event: MotionEvent): Boolean
    {
        scaleDetector.onTouchEvent(event)
        val currentPosition = PointF(event.x, event.y)

        when (event.action)
        {
            MotionEvent.ACTION_DOWN -> onDragBegin(currentPosition)
            MotionEvent.ACTION_MOVE -> onDragContinue(currentPosition)
            MotionEvent.ACTION_UP -> onDragEnd(currentPosition)
            MotionEvent.ACTION_POINTER_UP -> currentMode = GestureType.NONE
        }

        imageMatrix = currentMatrix
        invalidate()
        return true
    }

    private fun onDragBegin(currentPosition: PointF)
    {
        previousDragPosition.set(currentPosition)
        startDragPosition.set(currentPosition)
        currentMode = GestureType.DRAG
    }

    private fun onDragContinue(currentPosition: PointF)
    {
        if (currentMode == GestureType.DRAG)
        {
            // Calculates swipe distances
            val deltaX = currentPosition.x - previousDragPosition.x
            val deltaY = currentPosition.y - previousDragPosition.y

            // Calculates drag translation
            val dragTranslationX = getDragTranslation(deltaX, viewWidth.toFloat(), originalWidth * currentScale)
            val dragTranslationY = getDragTranslation(deltaY, viewHeight.toFloat(), originalHeight * currentScale)

            // Translates
            currentMatrix.postTranslate(dragTranslationX, dragTranslationY)
            fixTranslation()

            previousDragPosition.set(currentPosition.x, currentPosition.y)
        }
    }

    private fun onDragEnd(currentPosition: PointF)
    {
        currentMode = GestureType.NONE

        // Calculates drag distance
        val dragDistanceX = Math.abs(currentPosition.x - startDragPosition.x)
        val dragDistanceY = Math.abs(currentPosition.y - startDragPosition.y)

        // Treats drag as click when drag distance is too small
        if (dragDistanceX < MIN_DRAG_DISTANCE && dragDistanceY < MIN_DRAG_DISTANCE)
        {
            performClick()
        }
    }

    private fun fixTranslation()
    {
        currentMatrix.getValues(matrixValues)
        val translationX = matrixValues[Matrix.MTRANS_X]
        val translationY = matrixValues[Matrix.MTRANS_Y]
        val fixTranslationX = getFixTranslation(translationX, viewWidth.toFloat(), originalWidth * currentScale)
        val fixTranslationY = getFixTranslation(translationY, viewHeight.toFloat(), originalHeight * currentScale)

        if (fixTranslationX != 0f || fixTranslationY != 0f)
        {
            currentMatrix.postTranslate(fixTranslationX, fixTranslationY)
        }
    }

    private fun getFixTranslation(translation: Float, viewSize: Float, contentSize: Float): Float
    {
        val minTranslation: Float
        val maxTranslation: Float

        if (contentSize <= viewSize)
        {
            minTranslation = 0f
            maxTranslation = viewSize - contentSize
        }
        else
        {
            maxTranslation = 0f
            minTranslation = viewSize - contentSize
        }

        return when
        {
            translation < minTranslation -> -translation + minTranslation
            translation > maxTranslation -> -translation + maxTranslation
            else -> 0f
        }
    }

    /**
     * @return 0 (no translation) if whole image is visible or delta otherwise
     */
    private fun getDragTranslation(delta: Float, viewSize: Float, contentSize: Float): Float
    {
        return if (contentSize <= viewSize) 0f else delta
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        viewWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        viewHeight = View.MeasureSpec.getSize(heightMeasureSpec)

        if (oldMeasuredHeight == viewWidth && oldMeasuredWidth == viewHeight || viewWidth == 0 || viewHeight == 0)
            return

        oldMeasuredHeight = viewHeight
        oldMeasuredWidth = viewHeight

        if (currentScale == MIN_SCALE)
        {
            //Reset view.
            if (drawable == null || drawable.intrinsicWidth == 0 || drawable.intrinsicHeight == 0)
                return

            val imageWidth = drawable.intrinsicWidth
            val imageHeight = drawable.intrinsicHeight

            val scale = calculateWholeImageScale(imageWidth, imageHeight)

            currentMatrix.setScale(scale, scale)

            // Center the image
            val redundantSpaceX = viewWidth.toFloat() - scale * imageWidth.toFloat()
            val redundantSpaceY = viewHeight.toFloat() - scale * imageHeight.toFloat()

            currentMatrix.postTranslate(redundantSpaceX / 2f, redundantSpaceY / 2f)
            originalWidth = viewWidth - redundantSpaceX
            originalHeight = viewHeight - redundantSpaceY
            imageMatrix = currentMatrix
        }
        fixTranslation()
    }

    /**
     * @return maximum scale allowing display whole image
     */
    private fun calculateWholeImageScale(imageWidth: Int, imageHeight: Int): Float
    {
        val scaleX = viewWidth.toFloat() / imageWidth.toFloat()
        val scaleY = viewHeight.toFloat() / imageHeight.toFloat()
        return Math.min(scaleX, scaleY)
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener()
    {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean
        {
            currentMode = GestureType.ZOOM
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean
        {
            var scaleFactor = detector.scaleFactor
            val originalScale = currentScale
            currentScale *= scaleFactor

            // Too big scale
            if (currentScale > MAX_SCALE)
            {
                currentScale = MAX_SCALE
                scaleFactor = MAX_SCALE / originalScale
            }
            // Too small scale
            else if (currentScale < MIN_SCALE)
            {
                currentScale = MIN_SCALE
                scaleFactor = MIN_SCALE / originalScale
            }

            // If image is too small, scale to fit
            if (originalWidth * currentScale <= viewWidth || originalHeight * currentScale <= viewHeight)
            {
                currentMatrix.postScale(scaleFactor, scaleFactor, (viewWidth / 2).toFloat(), (viewHeight / 2).toFloat())
            }
            // Scale relative to a touch point
            else
            {
                currentMatrix.postScale(scaleFactor, scaleFactor, detector.focusX, detector.focusY)
            }

            fixTranslation()
            return true
        }
    }
}
