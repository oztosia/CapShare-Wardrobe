package com.oztosia.capsharewardrobe.ui.singleImage

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.oztosia.capsharewardrobe.CapShareWardrobe
import com.oztosia.capsharewardrobe.databinding.PopupPhotoBinding


class SingleImageFragment : DialogFragment() {

    private var _binding: PopupPhotoBinding? = null
    private val binding get() = _binding!!
    private lateinit var uri: String

    val application: Context = CapShareWardrobe.getAppContext()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = PopupPhotoBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        uri = arguments?.getString("uri").toString()

        Glide.with(application).load(uri)
            .into(binding.image)

        return binding.root
    }

}