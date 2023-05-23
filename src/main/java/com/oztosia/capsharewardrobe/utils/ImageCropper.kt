package com.oztosia.capsharewardrobe.utils

import com.oztosia.capsharewardrobe.interfaces.ImageProcessor
import org.opencv.core.*
import org.opencv.imgproc.Imgproc


class ImageCropper: ImageProcessor {

    override fun process(mat: Mat): Mat {
        val galleryBackgroundProcessor = GalleryBackgroundProcessor()
        return galleryBackgroundProcessor.process(mat)
    }

    fun getRect(mat: Mat): Rect {
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
        mat.setTo(Scalar(0.0, 0.0, 0.0))

        var maxVal = 0.0
        var maxValIdx = 0
        for (contourIdx in contours.indices) {
            val contourArea = Imgproc.contourArea(contours[contourIdx])
            if (maxVal < contourArea) {
                maxVal = contourArea
                maxValIdx = contourIdx
            }
        }
        Imgproc.drawContours(mat, contours, maxValIdx, Scalar(255.0, 255.0, 255.0, 255.0), 1)


        var minX = 0
        var minY = 0
        var maxX = 0
        var maxY = 0

        for (contourIdx in contours.indices) {
            val rect = Imgproc.boundingRect(contours[contourIdx])
            minX = rect.x
            minY = rect.y
            maxX = rect.x + rect.width
            maxY = rect.y + rect.height
        }
        Imgproc.rectangle(
            mat,
            Point(minX.toDouble(), minY.toDouble()),
            Point(maxX.toDouble(), maxY.toDouble()),
            Scalar(255.0, 255.0, 0.0),
            -1
        )

        return Rect(minX, minY, maxX, maxY)
    }
}