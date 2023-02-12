package com.example.mypersonalwardrobe.ui.addImage

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.databinding.BottomSheetItemBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.opencv.android.Utils
import org.opencv.core.Core.bitwise_and
import org.opencv.core.CvType.CV_8UC1
import org.opencv.core.CvType.CV_8UC3
import org.opencv.core.Mat
import org.opencv.core.Mat.zeros
import org.opencv.core.MatOfPoint
import org.opencv.core.Range.all
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class PhotoBottomSheet: BottomSheetDialogFragment() {

    companion object {
        const val GALLERY_RESULT_CODE = 200
        const val CAMERA_RESULT_CODE = 300
    }

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var _binding: BottomSheetItemBinding? = null
    private val binding get() = _binding!!
    lateinit var imageUri: Uri

    val application = MyPersonalWardrobe.getAppContext()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            }

        _binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.bottom_sheet_item, container, false)

        binding.camera.setOnClickListener {

            val fileName = "testphoto.jpg"
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, fileName)
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")

            imageUri = application.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
            )!!


            Log.d(TAG, "Image path: " + imageUri.toString())


            val intent = Intent(ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, CAMERA_RESULT_CODE)
        }

        binding.files.setOnClickListener {
            val files = Intent()
            files.type = "image/*"
            files.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(files, GALLERY_RESULT_CODE);
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        lateinit var uriInApplicationCacheDir: Uri
        if (requestCode == CAMERA_RESULT_CODE && resultCode == AppCompatActivity.RESULT_OK) {

            uriInApplicationCacheDir = bitmapToUri(getBitmapFromMediaStore(imageUri), 10)
            application.contentResolver.delete(imageUri, null, null)

        } else if (requestCode == GALLERY_RESULT_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            imageUri = data!!.data!!

            uriInApplicationCacheDir = bitmapToUri(getBitmapFromMediaStore(imageUri), 100)
        }


        val bundle = Bundle()
        bundle.putString("uri", uriInApplicationCacheDir.toString())
        findNavController().navigate(R.id.action_photoBottomSheet_to_newPhotoFragment, bundle)
    }

    fun bitmapToUri(imageBitmap: Bitmap, quality: Int): Uri {
        val baos = ByteArrayOutputStream()
        removeBackground(imageBitmap)
        //cutimage
        replaceBgColor(imageBitmap, Color.BLACK, Color.TRANSPARENT)
            .compress(Bitmap.CompressFormat.PNG, quality, baos)
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

    fun getBitmapFromMediaStore(uri: Uri): Bitmap {
        val inputStream: InputStream? = application.contentResolver.openInputStream(uri)
        val imageBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()
        return imageBitmap
    }


    fun process(mat: Mat):Mat {
        cvtColor(mat, mat, COLOR_BGR2GRAY)
        //GaussianBlur(mat, mat, Size(5.0,5.0), 1.0)
        Canny(mat, mat, 150.0, 10.0)
        val m = Mat.ones(3, 3, CV_8UC1)
        dilate(mat, mat, m)
        erode(mat, mat, m)
        return mat
    }


    fun getMask (mat: Mat): Mat
    {
        process(mat)
        val contours = ArrayList<MatOfPoint>()
        val hierarchy = Mat()
        findContours(mat, contours, hierarchy, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE)
        cvtColor(mat, mat, COLOR_GRAY2BGRA)
        mat.setTo(Scalar(0.0,0.0,0.0))

        var maxVal = 0.0
        var maxValIdx = 0
        for (contourIdx in contours.indices) {
            val contourArea = contourArea(contours[contourIdx])
            if (maxVal < contourArea) {
                maxVal = contourArea
                maxValIdx = contourIdx
            }
        }
        drawContours(mat, contours, maxValIdx, Scalar(255.0, 255.0, 255.0, 255.0), -1)
        return mat
    }

    fun removeBackground(bitmap: Bitmap){
        val mat = Mat(bitmap.width, bitmap.height, CV_8UC1)
        val output = Mat(bitmap.width, bitmap.height, CV_8UC3)
        val input = Mat(bitmap.width, bitmap.height, CV_8UC3)
        Utils.bitmapToMat(bitmap, input)
        Utils.bitmapToMat(bitmap, mat)
        getMask(mat)
        bitwise_and(input, mat, output)
        Utils.matToBitmap(output, bitmap)
    }

    fun crop(bitmap: Bitmap){
        val mat = Mat(bitmap.width, bitmap.height, CV_8UC1)
        Utils.bitmapToMat(bitmap, mat)
        getMask(mat)
    }

    fun replaceBgColor(bitmap: Bitmap, fromColor:Int, targetColor: Int): Bitmap{
        removeBackground(bitmap)
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