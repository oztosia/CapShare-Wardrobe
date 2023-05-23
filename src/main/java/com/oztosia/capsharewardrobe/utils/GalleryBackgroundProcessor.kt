package com.oztosia.capsharewardrobe.utils

import com.oztosia.capsharewardrobe.interfaces.ImageProcessor
import org.opencv.core.CvType.CV_8UC1
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc.*


class GalleryBackgroundProcessor: ImageProcessor {

    override fun process(mat: Mat): Mat {
        cvtColor(mat, mat, COLOR_BGR2GRAY)
        Canny(mat, mat, 100.0, 10.0)
        val m = Mat.ones(5, 5, CV_8UC1)
        dilate(mat, mat, m)
        erode(mat, mat, m)
        return mat
    }
}