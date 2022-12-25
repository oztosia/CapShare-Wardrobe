package com.example.mypersonalwardrobe.ui.addImage

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mypersonalwardrobe.databinding.FragmentNewPhotoBinding
import com.example.mypersonalwardrobe.viewmodels.SharedHomeAndGalleryViewModel
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class NewPhotoFragment : Fragment() {

    lateinit var sharedHomeAndGalleryViewModel: SharedHomeAndGalleryViewModel
    private var _binding: FragmentNewPhotoBinding? = null
    private val binding get() = _binding!!
    private lateinit var uri: Uri

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val bundle = arguments?.getString("uri")
        Log.d(ContentValues.TAG, "arguments receive: " + bundle.toString())
        if (bundle != null)
        uri = Uri.parse(bundle.toString())

        sharedHomeAndGalleryViewModel = ViewModelProvider(requireActivity()).get(
            SharedHomeAndGalleryViewModel::class.java)
        _binding = FragmentNewPhotoBinding.inflate(inflater, container, false)

        binding.imageProgressBar.visibility = View.GONE
        binding.aboutIcon.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backgroundLayout.setOnClickListener {
            binding.constraintLayoutAboutImage.visibility = View.GONE
            binding.aboutIcon.visibility = View.VISIBLE
        }

        binding.aboutIcon.setOnClickListener {
            binding.constraintLayoutAboutImage.visibility = View.VISIBLE
            binding.aboutIcon.visibility = View.GONE
        }


        Glide.with(this).load(uri.toString())
            .fitCenter()
            .into(binding.image)

        sharedHomeAndGalleryViewModel.galleryTypeMutableLiveData.observe(viewLifecycleOwner,  Observer<String?> { galleryTypePath ->

            Log.d(TAG, "gallery type: " + galleryTypePath)
            binding.uploadIcon.setOnClickListener {
                val progressBar: ProgressBar = binding.imageProgressBar

                val hashtags = binding.aboutPhotoEditText.text.toString()



                if (galleryTypePath == "post") {
                    sharedHomeAndGalleryViewModel.addImageUriToPhotoList(uri)
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(TimeUnit.SECONDS.toMillis(2.5.toLong()))
                        withContext(Dispatchers.Main) {
                            activity?.onBackPressed()
                        }

                    }
                } else {

                    sharedHomeAndGalleryViewModel.uploadImage(
                        uri,
                        galleryTypePath,
                        progressBar,
                        hashtags
                    )

                    Log.d(TAG, "uploading: " + galleryTypePath + uri.toString() + hashtags)

                    CoroutineScope(Dispatchers.IO).launch {
                        delay(TimeUnit.SECONDS.toMillis(2.5.toLong()))
                        withContext(Dispatchers.Main) {
                            activity?.onBackPressed()
                        }
                    }
                }
            }
        })

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
