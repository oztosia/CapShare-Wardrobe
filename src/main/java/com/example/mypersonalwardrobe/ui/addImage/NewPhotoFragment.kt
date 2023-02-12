package com.example.mypersonalwardrobe.ui.addImage

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mypersonalwardrobe.constants.FirebasePathsConstants
import com.example.mypersonalwardrobe.databinding.FragmentNewPhotoBinding
import com.example.mypersonalwardrobe.viewmodels.SharedHomeAndNewPhotoViewModel
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit


class NewPhotoFragment : Fragment() {

    lateinit var sharedHomeAndNewPhotoViewModel: SharedHomeAndNewPhotoViewModel
    private var _binding: FragmentNewPhotoBinding? = null
    private val binding get() = _binding!!
    private lateinit var uri: Uri

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val bundle = arguments?.getString("uri")
        Log.d(TAG, "arguments receive: " + bundle.toString())
        if (bundle != null)
        uri = Uri.parse(bundle.toString())

        sharedHomeAndNewPhotoViewModel = ViewModelProvider(requireActivity()).get(
            SharedHomeAndNewPhotoViewModel::class.java)
        _binding = FragmentNewPhotoBinding.inflate(inflater, container, false)

        binding.backgroundHashtags.visibility = View.GONE

        sharedHomeAndNewPhotoViewModel._galleryTypeMutableLiveData
            .observe(viewLifecycleOwner,  Observer<String?> { galleryTypePath ->
                if (galleryTypePath == "items")
                    binding.backgroundHashtags.visibility = View.VISIBLE
        })

        binding.imageProgressBar.visibility = View.GONE
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backgroundLayout.setOnClickListener {
        }

        Glide.with(this).load(uri.toString())
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
                       mParams.leftMargin = (x)-200
                       Log.d(TAG, "rawx margin: " + mParams.leftMargin)
                       mParams.bottomMargin = (y)-200
                       binding.image.setLayoutParams(mParams)
                   }
                   else -> {}
               }
               true
           }

        sharedHomeAndNewPhotoViewModel._galleryTypeMutableLiveData.observe(viewLifecycleOwner,  Observer<String?> { galleryTypePath ->

            Log.d(TAG, "gallery type: " + galleryTypePath)
            binding.uploadButton.setOnClickListener {

                val progressBar: ProgressBar = binding.imageProgressBar
                val hashtags = binding.hashtagsEdittext.text.toString()

                if (galleryTypePath == "post") {
                    sharedHomeAndNewPhotoViewModel.addImageUriToPhotoList(uri.toString())
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(TimeUnit.SECONDS.toMillis(2.5.toLong()))
                        withContext(Dispatchers.Main) {
                            activity?.onBackPressed()
                        }
                    }
                }
                else if (galleryTypePath == "items") {
                    sharedHomeAndNewPhotoViewModel.addImage(
                        uri,
                        hashtags,
                        FirebasePathsConstants.STORAGE_PATH + galleryTypePath,
                        FirebasePathsConstants.MY_ITEMS_PATH,
                        progressBar
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        delay(TimeUnit.SECONDS.toMillis(2.5.toLong()))
                        withContext(Dispatchers.Main) {
                            activity?.onBackPressed()
                        }
                    }
                }

                else if (galleryTypePath == "profileImage") {
                    binding.backgroundHashtags.visibility = View.GONE
                    sharedHomeAndNewPhotoViewModel
                        .replaceImage(uri,
                        galleryTypePath,
                        FirebasePathsConstants.STORAGE_PATH + galleryTypePath,
                        FirebasePathsConstants.USERS_PATH,
                        FirebasePathsConstants.CURRENT_USER.toString(),
                        progressBar)

                    CoroutineScope(Dispatchers.IO).launch {
                        delay(TimeUnit.SECONDS.toMillis(2.5.toLong()))
                        withContext(Dispatchers.Main) {
                            activity?.onBackPressed()
                        }}

                }
            }
        })

    }

    fun showOnlyImage(){
        binding.uploadButton.visibility = View.GONE
        binding.backgroundHashtags.visibility = View.GONE
    }

    fun showAllLayoutItems(){
        binding.uploadButton.visibility = View.VISIBLE
        binding.backgroundHashtags.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
