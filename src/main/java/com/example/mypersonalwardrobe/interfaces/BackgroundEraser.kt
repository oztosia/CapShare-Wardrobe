package com.example.mypersonalwardrobe.interfaces

import android.graphics.Bitmap
import android.graphics.Canvas

abstract class BackgroundEraser {

    fun replaceBackgroundColor(bitmap: Bitmap, fromColor:Int, targetColor: Int): Bitmap{
        return bitmap
    }

    fun eraseInPoint(bitmap: Bitmap){

    }


}