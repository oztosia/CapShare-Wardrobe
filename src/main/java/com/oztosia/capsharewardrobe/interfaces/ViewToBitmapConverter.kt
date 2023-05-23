package com.oztosia.capsharewardrobe.interfaces

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

class ViewToBitmapConverter {
    companion object ViewToBitmapConverter{
        fun viewToBitmap(view: View): Bitmap {
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            return bitmap
        }
    }

}