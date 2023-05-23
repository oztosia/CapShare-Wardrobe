package com.oztosia.capsharewardrobe.views

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.coroutines.*
import kotlin.math.abs


class CreateOutfitView(context: Context, val itemsList: MutableList<String>) : View(context) {


    lateinit var bitmaps: ArrayList<Bitmap>
    private var coroutineFinished = false
    private var currentBitmapIndex = -1
    var topBitmapIndex = -1
    var bitmapsPositions = mutableListOf<Pair<Float, Float>>()



    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)

        if (::bitmaps.isInitialized) { bitmaps.forEach { it.recycle() } }

        CoroutineScope(Dispatchers.IO).launch { val deferredBitmaps = itemsList.map { async { Glide.with(context).asBitmap().load(it).submit().get() } }

            bitmaps = deferredBitmaps.awaitAll() as ArrayList<Bitmap>

            bitmapsPositions.clear()


            bitmaps.forEach { bitmapsPositions.add(Pair(200f, 500f)) }

            withContext(Dispatchers.Main) {
                coroutineFinished = true
                invalidate()
            }
        }
    }


    override fun onDraw(canvas: Canvas) {
        if (::bitmaps.isInitialized) {
            for (i in 0 until bitmaps.size) {
                if (i != topBitmapIndex) {
                    canvas.drawBitmap(
                        bitmaps[i],
                        bitmapsPositions[i].first,
                        bitmapsPositions[i].second,
                        null
                    )
                }
            }
            if (topBitmapIndex >= 0) {
                canvas.drawBitmap(
                    bitmaps[topBitmapIndex],
                    bitmapsPositions[topBitmapIndex].first,
                    bitmapsPositions[topBitmapIndex].second,
                    null
                )
            }
        } else {
            if (!coroutineFinished) {
                super.onDraw(canvas)
                return
            }
        }
    }

    private fun getTouchedBitmapIndex(x: Float, y: Float): Int {
        for (i in 0 until bitmapsPositions.size) {
            val rect = Rect(
                bitmapsPositions[i].first.toInt(),
                bitmapsPositions[i].second.toInt(),
                bitmapsPositions[i].first.toInt() + bitmaps[i].width,
                bitmapsPositions[i].second.toInt() + bitmaps[i].height
            )
            if (rect.contains(x.toInt(), y.toInt())) { return i }

        }
        return 0
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
        currentBitmapIndex = getTouchedBitmapIndex(motionTouchEventX, motionTouchEventY)
        if (currentBitmapIndex >= 0) {
            bringBitmapToFront(currentBitmapIndex)
            topBitmapIndex = bitmaps.lastIndex
        }
        currentBitmapIndex = topBitmapIndex
    }

    private fun touchMove() {

        val bitmap = bitmaps[currentBitmapIndex]
        val dx = abs(motionTouchEventX)
        val dy = abs(motionTouchEventY)
            bitmapsPositions[currentBitmapIndex] = Pair(
                dx-bitmap.width/2,
                dy-bitmap.height/2)
        invalidate()
    }

    private fun touchUp() {
    }

    private fun bringBitmapToFront(index: Int) {
        if (index == topBitmapIndex || index >= bitmaps.size) {
            return
        }
        val bitmap = bitmaps[index]
        val bitmapPositions = bitmapsPositions[index]
        bitmaps.remove(bitmap)
        bitmapsPositions.removeAt(index)
        bitmaps.add(bitmap)
        bitmapsPositions.add(bitmapPositions)
        topBitmapIndex = bitmaps.lastIndex
    }



}