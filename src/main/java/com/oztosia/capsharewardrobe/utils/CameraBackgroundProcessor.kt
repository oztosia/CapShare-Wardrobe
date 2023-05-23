package com.oztosia.capsharewardrobe.utils

import com.oztosia.capsharewardrobe.interfaces.ImageProcessor
import org.opencv.core.*
import org.opencv.core.CvType.CV_8UC1
import org.opencv.imgproc.Imgproc.*

class CameraBackgroundProcessor: ImageProcessor {

   override fun process(mat: Mat):Mat {
       cvtColor(mat, mat, COLOR_BGR2GRAY)
       Canny(mat, mat, 80.0, 10.0)
       Sobel(mat, mat, CV_8UC1, 1, 0)
       GaussianBlur(mat, mat, Size(0.0, 0.0), 3.0)
       //Canny(mat, mat, 10.0, 10.0)
       val m = Mat.ones(5, 5, CV_8UC1)
       dilate(mat, mat, m)
       erode(mat, mat, m)
       return mat
   }
}