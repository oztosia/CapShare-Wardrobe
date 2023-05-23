package com.oztosia.capsharewardrobe.ui.addImage

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
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.oztosia.capsharewardrobe.constants.FirebaseConst
import com.oztosia.capsharewardrobe.databinding.FragmentNewImageBinding
import com.oztosia.capsharewardrobe.utils.SnackbarCreator
import com.oztosia.capsharewardrobe.viewmodels.NewImageViewModel
import com.oztosia.capsharewardrobe.views.ResizeView
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
    lateinit var resizeView: ResizeView
    lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val bundle = arguments?.getString("uri")
        Log.d(TAG, "arguments receive: " + bundle.toString())
        if (bundle != null) uri = Uri.parse(bundle.toString())

        intentBundle = arguments?.getString("intent").toString()

        newImageViewModel = ViewModelProvider(requireActivity()).get(NewImageViewModel::class.java)
        _binding = FragmentNewImageBinding.inflate(inflater, container, false)

        progressBar = binding.progressBar
        binding.editLayout.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(requireActivity()).asBitmap().load(uri).fitCenter().into(binding.image)


        when (intentBundle) {"gallery" -> {
            binding.progressBar.visibility = View.VISIBLE
            binding.editLayout.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                delay(TimeUnit.SECONDS.toMillis(2.5.toLong()))
                withContext(Dispatchers.Main) {
                    val resultBitmap: Bitmap = binding.image.viewToBitmap()
                    val tempBitmap = Bitmap.createScaledBitmap(resultBitmap, resultBitmap.width, resultBitmap.height, false)
                    resizeView = ResizeView(requireContext(), tempBitmap)
                    binding.image.visibility = View.GONE
                    rescaleView(resizeView, binding.canvaLayout)
                }
                binding.progressBar.visibility = View.GONE
                }
            }
        }


        binding.uploadButton.setOnClickListener {

            when (intentBundle) {
                "gallery" -> {
                    val hashtags = binding.hashtagsEdittext.text.toString()
                    newImageViewModel.addImage(uri, hashtags, FirebaseConst.STORAGE_PATH + "items", FirebaseConst.MY_ITEMS_PATH, progressBar)
                }
                "post" -> { newImageViewModel.addImageUriToPhotoList(uri.toString()) }
                "profileImage" -> { newImageViewModel.replaceImage(uri, "profileImage", FirebaseConst.STORAGE_PATH + "profileImage", FirebaseConst.USERS_PATH, FirebaseConst.CURRENT_USER, progressBar) }
                "chat" -> { newImageViewModel.addImageUriToMessagesPhotoList(uri.toString()) }
            }
            CoroutineScope(Dispatchers.IO).launch {
                delay(TimeUnit.SECONDS.toMillis(2.5.toLong()))
                withContext(Dispatchers.Main) { SnackbarCreator.show(binding.backgroundLayout, "Dodano pomyślnie") }
            }
        }

        binding.eraseButton.setOnClickListener { erase() }

        binding.exitFromEraseButton.setOnClickListener { exitFromEraseMode() }

        binding.zoomIn.setOnClickListener { zoomIn() }

        binding.zoomOut.setOnClickListener { zoomOut() }
    }

    fun zoomIn(){
        val resultBitmap = resizeView.bitmap
        val tempBitmap = resultBitmap.resize((resultBitmap.width*1.3).toInt(), (resultBitmap.height*1.3).toInt())
        resizeView.bitmap = tempBitmap
        resizeView.invalidate()
        uri = tempBitmap.bitmapToUri()
    }

    fun zoomOut(){
        val resultBitmap = resizeView.bitmap
        val tempBitmap = resultBitmap.resize((resultBitmap.width/1.3).toInt(), (resultBitmap.height/1.3).toInt())
        resizeView.bitmap = tempBitmap
        resizeView.invalidate()
        uri = tempBitmap.bitmapToUri()
    }


    fun erase(){
        val resultBitmap = resizeView.bitmap
        val tempBitmap = Bitmap.createScaledBitmap(resultBitmap, resultBitmap.width, resultBitmap.height, false)
        myCanvasView = MyCanvasView(requireContext(),tempBitmap)
        binding.image.visibility = View.GONE
        binding.eraseButton.visibility = View.GONE
        binding.exitFromEraseButton.visibility = View.VISIBLE
        binding.canvaLayout.removeView(resizeView)
        binding.canvaLayout.visibility = View.GONE
        binding.human.visibility = View.GONE
        binding.eraseLayout.visibility = View.VISIBLE
        binding.eraseLayout.addView(myCanvasView)

    }

    fun exitFromEraseMode(){
        binding.eraseButton.visibility = View.VISIBLE
        binding.human.visibility = View.VISIBLE
        binding.exitFromEraseButton.visibility = View.GONE
        val resultBitmap = myCanvasView.extraBitmap
        uri = resultBitmap.bitmapToUri()
        resizeView = ResizeView(requireContext(), resultBitmap)
        rescaleView(resizeView, binding.canvaLayout)
        binding.canvaLayout.visibility = View.VISIBLE
        binding.eraseLayout.removeView(myCanvasView)
        binding.eraseLayout.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun Bitmap.bitmapToUri(): Uri {

        val baos = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.WEBP, 100, baos)
        val baosToByteArray = baos.toByteArray()
        val file = File(context?.cacheDir, toString())
        file.apply {
            delete()
            createNewFile()
        }
        val fos = FileOutputStream(file)
        fos.apply {
            write(baosToByteArray)
            flush()
            close() }

        baos.close()
        return file.toUri()
    }

    fun Bitmap.resize(newWidth: Int, newHeight: Int): Bitmap {
        val scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)
        val ratioX = newWidth / width.toFloat()
        val ratioY = newHeight / height.toFloat()
        val middleX = newWidth / 2.0f
        val middleY = newHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas = Canvas(scaledBitmap)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            this,
            middleX - width / 2,
            middleY - height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )
        return scaledBitmap
    }


    fun View.viewToBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        return bitmap
    }

    fun rescaleView(view: View, layout: FrameLayout){
        val targetWidthPx = 720
        val targetHeightPx = 1449

        view.id = View.generateViewId()

        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        view.layoutParams = layoutParams

        layout.addView(view)

        view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val screenWidthPx = resources.displayMetrics.widthPixels
                val screenHeightPx = resources.displayMetrics.heightPixels

                val scaleX = screenWidthPx.toFloat() / targetWidthPx
                val scaleY = screenHeightPx.toFloat() / targetHeightPx

                view.scaleX = scaleX
                view.scaleY = scaleY
            }
        })

    }

}
