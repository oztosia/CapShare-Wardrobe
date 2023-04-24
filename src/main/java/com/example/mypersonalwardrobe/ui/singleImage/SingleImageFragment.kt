package com.example.mypersonalwardrobe.ui.singleImage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.databinding.PopupPhotoBinding


class SingleImageFragment : DialogFragment() {

    private var _binding: PopupPhotoBinding? = null
    private val binding get() = _binding!!
    private lateinit var uri: String

    val application: Context = MyPersonalWardrobe.getAppContext()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = PopupPhotoBinding.inflate(inflater, container, false)

        uri = arguments?.getString("uri").toString()

        Glide.with(application).load(uri)
            .into(binding.image)

        return binding.root
    }

}