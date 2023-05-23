package com.oztosia.capsharewardrobe.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.net.Uri
import android.view.View
import androidx.core.net.toUri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class BitmapConvertingUtils {

    companion object BitmapConvertingUtils {

        fun bitmapToUri(imageBitmap: Bitmap, context: Context): Uri {
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.WEBP, 100, baos)
            val baosToByteArray = baos.toByteArray()
            val file = File(context?.cacheDir, imageBitmap.toString())
            file.delete()
            file.createNewFile()
            val fos = FileOutputStream(file)
            fos.write(baosToByteArray)
            fos.flush()
            fos.close()
            baos.close()
            return file.toUri()
        }

        fun resize(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
            val scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)
            val ratioX = newWidth / bitmap.width.toFloat()
            val ratioY = newHeight / bitmap.height.toFloat()
            val middleX = newWidth / 2.0f
            val middleY = newHeight / 2.0f
            val scaleMatrix = Matrix()
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
            val canvas = Canvas(scaledBitmap)
            canvas.setMatrix(scaleMatrix)
            canvas.drawBitmap(
                bitmap,
                middleX - bitmap.width / 2,
                middleY - bitmap.height / 2,
                Paint(Paint.FILTER_BITMAP_FLAG)
            )
            return scaledBitmap
        }


        fun viewToBitmap(view: View): Bitmap {
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            return bitmap
        }
    }
}