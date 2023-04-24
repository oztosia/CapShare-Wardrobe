package com.example.mypersonalwardrobe.utils

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.provider.ContactsContract
import android.util.Log
import com.example.mypersonalwardrobe.constants.ImgSourceConst
import com.example.mypersonalwardrobe.interfaces.ImageProcessor
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.core.Core.bitwise_and
import org.opencv.imgproc.Imgproc

class BackgroundRemover(val resultCode: Int): ImageProcessor {


    override fun process(mat: Mat): Mat {

        when (resultCode) {
            ImgSourceConst.CAMERA_RESULT_CODE -> {
                val cameraBackgroundProcessor = CameraBackgroundProcessor()
                cameraBackgroundProcessor.process(mat)
            }
            ImgSourceConst.GALLERY_RESULT_CODE -> {
                val galleryBackgroundProcessor = GalleryBackgroundProcessor()
                galleryBackgroundProcessor.process(mat)
            }
        }

        return mat
    }

    fun getMask (mat: Mat): Mat
    {
        process(mat)
        val contours = ArrayList<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(
            mat,
            contours,
            hierarchy,
            Imgproc.RETR_EXTERNAL,
            Imgproc.CHAIN_APPROX_SIMPLE
        )
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2BGRA)
        mat.setTo(Scalar(0.0,0.0,0.0))

        var maxVal = 0.0
        var maxValIdx = 0
        for (contourIdx in contours.indices) {
            val contourArea = Imgproc.contourArea(contours[contourIdx])
            if (maxVal < contourArea) {
                maxVal = contourArea
                maxValIdx = contourIdx
            }
        }
        Imgproc.drawContours(mat, contours, maxValIdx, Scalar(255.0, 255.0, 255.0, 255.0), -1)
        return mat
    }


    fun joinBitmaps(bitmap: Bitmap): Bitmap{
        val mat = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        val output = Mat(bitmap.height, bitmap.width, CvType.CV_8UC3)
        val outputCopy = Mat(bitmap.height, bitmap.width, CvType.CV_8UC3)
        val input = Mat(bitmap.height, bitmap.width, CvType.CV_8UC3)
        Utils.bitmapToMat(bitmap, input)
        Utils.bitmapToMat(bitmap, mat)
        getMask(mat)
        bitwise_and(input, mat, output)
        output.copyTo(outputCopy)
        val imageCropper = ImageCropper()
        val myROI : Rect = imageCropper.getRect(outputCopy)
        val croppedImage = output.submat(myROI.y, myROI.height, myROI.x, myROI.width)
        val newBitmap = Bitmap.createBitmap(croppedImage.cols(), croppedImage.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(croppedImage, newBitmap)
        return newBitmap
    }

    fun replaceBgColor(bitmap: Bitmap, fromColor:Int, targetColor: Int): Bitmap{
        val width: Int = bitmap.width
        val height: Int = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        for (x in pixels.indices) {
            pixels[x] = if (pixels[x] == fromColor) targetColor else pixels[x]
        }

        val result = Bitmap.createBitmap(width, height, bitmap.config)
        result.setPixels(pixels, 0, width, 0, 0, width, height)
        return result
    }
}