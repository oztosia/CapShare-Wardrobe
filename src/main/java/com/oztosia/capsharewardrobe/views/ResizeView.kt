package com.oztosia.capsharewardrobe.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class ResizeView (context: Context, var bitmap: Bitmap) : View(context) {

    var top: Float = 0f
    var left: Float = 0f

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int){
        super.onSizeChanged(width, height, oldWidth, oldHeight)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, left, top, null)
    }


    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f


    override fun onTouchEvent(event: MotionEvent): Boolean {

        motionTouchEventX = event.x
        motionTouchEventY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }
        return true
    }

    private fun touchStart() {

    }

    private fun touchMove() {
        val dx = abs(motionTouchEventX)
        val dy = abs(motionTouchEventY)
         left = dx-bitmap.width/2
         top = dy-bitmap.height/2
        invalidate()
    }

    private fun touchUp() {
    }



}