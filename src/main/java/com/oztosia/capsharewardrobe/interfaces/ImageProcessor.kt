package com.oztosia.capsharewardrobe.interfaces

import org.opencv.core.Mat

interface ImageProcessor {

    fun process(mat: Mat): Mat

}