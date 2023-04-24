package com.example.mypersonalwardrobe.ui.addImage

import MyCanvasView
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mypersonalwardrobe.constants.FirebaseConst
import com.example.mypersonalwardrobe.databinding.FragmentNewImageBinding
import com.example.mypersonalwardrobe.viewmodels.NewImageViewModel
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit


class NewImageFragment : Fragment() {

    lateinit var newImageViewModel: NewImageViewModel
    private var _binding: FragmentNewImageBinding? = null
    private val binding get() = _binding!!
    private lateinit var uri: Uri
    lateinit var intentBundle: String
    lateinit var myCanvasView: MyCanvasView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val bundle = arguments?.getString("uri")
        Log.d(TAG, "arguments receive: " + bundle.toString())
        if (bundle != null)
        uri = Uri.parse(bundle.toString())

        intentBundle = arguments?.getString("intent").toString()

        newImageViewModel = ViewModelProvider(requireActivity()).get(NewImageViewModel::class.java)
        _binding = FragmentNewImageBinding.inflate(inflater, container, false)

        binding.backgroundHashtags.visibility = View.GONE
        binding.imageProgressBar.visibility = View.GONE

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backgroundLayout.setOnClickListener {
        }
            Glide.with(this).asBitmap().load(uri)
            .fitCenter()
            .into(binding.image)


       binding.image.setOnTouchListener { view, event ->

               val eid = event.action
               when (eid) {
                   MotionEvent.ACTION_MOVE -> {

                       val mParams = binding.image.getLayoutParams() as ConstraintLayout.LayoutParams
                       val x = event.x.toInt()
                       Log.d(TAG, "rawx: " + x)
                       val y = event.y.toInt()
                       mParams.leftMargin = (x)
                       Log.d(TAG, "rawx margin: " + mParams.leftMargin)
                       mParams.topMargin = (y)-200
                       binding.image.setLayoutParams(mParams)
                   }
                   else -> {}
               }
               true
           }

        when (intentBundle) {"gallery" -> { binding.backgroundHashtags.visibility = View.VISIBLE }}


        binding.uploadButton.setOnClickListener {

            val progressBar: ProgressBar = binding.imageProgressBar


            when (intentBundle) {
                "gallery" -> {
                    val hashtags = binding.hashtagsEdittext.text.toString()
                    newImageViewModel.addImage(
                        uri,
                        hashtags,
                        FirebaseConst.STORAGE_PATH + "items",
                        FirebaseConst.MY_ITEMS_PATH,
                        progressBar
                    )
                }
                "post" -> {
                    newImageViewModel.addImageUriToPhotoList(uri.toString())
                }
                "profileImage" -> {
                    binding.backgroundHashtags.visibility = View.GONE
                    newImageViewModel
                        .replaceImage(uri,
                            "profileImage",
                            FirebaseConst.STORAGE_PATH + "profileImage",
                            FirebaseConst.USERS_PATH,
                            FirebaseConst.CURRENT_USER,
                            progressBar)
                }
                "chat" -> {
                    newImageViewModel.addImageUriToMessagesPhotoList(uri.toString())
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                delay(TimeUnit.SECONDS.toMillis(2.5.toLong()))
                withContext(Dispatchers.Main) {
                    activity?.onBackPressed()
                }
            }
        }

        binding.eraseButton.setOnClickListener { erase() }

        binding.exitFromEraseButton.setOnClickListener { exitFromEraseMode() }

        binding.zoomIn.setOnClickListener { zoomIn() }

        binding.zoomOut.setOnClickListener { zoomOut() }
    }

    fun zoomIn(){
        val resultBitmap = binding.image.drawable.toBitmap()
        val tempBitmap = resize(resultBitmap,
            (resultBitmap.width*1.3).toInt(), (resultBitmap.height*1.3).toInt())
        binding.image.setImageBitmap(tempBitmap)
    }

    fun zoomOut(){
        val resultBitmap = binding.image.drawable.toBitmap()
        val tempBitmap = resize(resultBitmap,
            (resultBitmap.width/1.3).toInt(), (resultBitmap.height/1.3).toInt())
        binding.image.setImageBitmap(tempBitmap)
    }


    fun erase(){
        val resultBitmap = binding.image.drawable.toBitmap()
        val tempBitmap = Bitmap.createScaledBitmap(resultBitmap, resultBitmap.width/2, resultBitmap.height/2, false)
        myCanvasView = MyCanvasView(requireContext(),tempBitmap)
        binding.image.visibility = View.GONE
        binding.eraseButton.visibility = View.GONE
        binding.exitFromEraseButton.visibility = View.VISIBLE
        binding.canvaLayout.addView(myCanvasView)

    }

    fun exitFromEraseMode(){
        binding.imageLayout.setImageBitmap(myCanvasView.extraBitmap)
        binding.eraseButton.visibility = View.VISIBLE
        binding.exitFromEraseButton.visibility = View.GONE
        binding.image.visibility = View.VISIBLE
        val resultBitmap = binding.imageLayout.drawable.toBitmap()
        binding.image.setImageDrawable(BitmapDrawable(resources, resultBitmap))
        uri = bitmapToUri(resultBitmap)
        binding.canvaLayout.removeView(myCanvasView)
        binding.imageLayout.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun bitmapToUri(imageBitmap: Bitmap): Uri {
        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.WEBP, 30, baos)
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
}
