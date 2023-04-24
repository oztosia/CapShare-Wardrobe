package com.example.mypersonalwardrobe.interfaces

import org.opencv.core.Mat

interface ImageProcessor {

    fun process(mat: Mat): Mat

}