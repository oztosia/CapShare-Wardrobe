package com.oztosia.capsharewardrobe.ui.addImage

import android.Manifest
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.oztosia.capsharewardrobe.CapShareWardrobe
import com.oztosia.capsharewardrobe.R
import com.oztosia.capsharewardrobe.constants.ImgSourceConst.Companion.CAMERA_RESULT_CODE
import com.oztosia.capsharewardrobe.constants.ImgSourceConst.Companion.GALLERY_RESULT_CODE
import com.oztosia.capsharewardrobe.databinding.ItemBottomSheetBinding
import com.oztosia.capsharewardrobe.utils.BackgroundRemover
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.*


class PhotoBottomSheet: BottomSheetDialogFragment() {

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var _binding: ItemBottomSheetBinding? = null
    private val binding get() = _binding!!
    lateinit var imageUri: Uri
    lateinit var intentBundle: String
    val application = CapShareWardrobe.getAppContext()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
        _binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_bottom_sheet, container, false)

        intentBundle = arguments?.getString("intent").toString()

        binding.camera.setOnClickListener {

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                imageUri = takePhoto()
                val intent = Intent(ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(intent, CAMERA_RESULT_CODE)
            } else {

                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    CAMERA_RESULT_CODE
                )
            }

        }

        binding.files.setOnClickListener {
            val files = Intent()
            files.type = "image/*"
            files.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(files, GALLERY_RESULT_CODE)
        }

        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //val bitmap = getBitmapFromMediaStore(imageUri)
        lateinit var uriInApplicationCacheDir: Uri
        if (requestCode == CAMERA_RESULT_CODE && resultCode == AppCompatActivity.RESULT_OK) {

            val imagePath = imageUri.getRealPathFromUri()
            Log.d(TAG, "imageuri: " + imagePath.toString())
            val rotatedBitmap =  imagePath!!.rotateImageIfRequired(imageUri)

            uriInApplicationCacheDir = bitmapToUri(CAMERA_RESULT_CODE, rotatedBitmap, 10)

            application.contentResolver.delete(imageUri, null, null)

        } else if (requestCode == GALLERY_RESULT_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            imageUri = data!!.data!!
            uriInApplicationCacheDir = bitmapToUri(GALLERY_RESULT_CODE, getBitmapFromMediaStore(imageUri), 100)
        }

        val bundle = Bundle()
        bundle.putString("uri", uriInApplicationCacheDir.toString())
        bundle.putString("intent", intentBundle)
        findNavController().navigate(R.id.action_photoBottomSheet_to_newPhotoFragment, bundle)
    }

    fun takePhoto(): Uri {
        val fileName = "image.jpg"
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, fileName)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        imageUri = application.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
        return imageUri
    }

    fun getBitmapFromMediaStore(uri: Uri): Bitmap {
        val inputStream: InputStream? = application.contentResolver.openInputStream(uri)
        val imageBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()
        return imageBitmap
    }

    fun bitmapToUri(resultCode: Int, imageBitmap: Bitmap, quality: Int): Uri {
        val baos = ByteArrayOutputStream()

        when(intentBundle){
            "gallery" -> {
                val backgroundRemover = BackgroundRemover(resultCode)
                val croppedBitmap: Bitmap = backgroundRemover.joinBitmaps(imageBitmap)

                    backgroundRemover.replaceBgColor(croppedBitmap, Color.BLACK, Color.TRANSPARENT)
                        .compress(Bitmap.CompressFormat.WEBP, quality, baos)
            }
            else -> {
                imageBitmap.compress(Bitmap.CompressFormat.WEBP, quality, baos)}
        }
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

    fun Uri.getRealPathFromUri(): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = application.contentResolver.query(this, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                return it.getString(columnIndex)
            }
        }
        return null
    }

    fun String.rotateImageIfRequired(uri: Uri): Bitmap {

        var rotationAngle = 0
        var exif: ExifInterface? = null
        try {
            val inputStream: InputStream? = application.contentResolver.openInputStream(uri)
            inputStream?.run {
                exif = ExifInterface(this)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        exif?.run {

           rotationAngle = when (exif!!.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        }

        val bitmap = getBitmapFromMediaStore(uri)
        val matrix = Matrix()
        matrix.setRotate(rotationAngle.toFloat(), bitmap.width.toFloat() / 2, bitmap.height.toFloat() / 2)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

}