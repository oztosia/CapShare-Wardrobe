package com.example.mypersonalwardrobe.ui.items

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.adapters.GalleryRecyclerAdapter
import com.example.mypersonalwardrobe.databinding.FragmentSinglePhotoBinding
import com.example.mypersonalwardrobe.models.Photo
import com.example.mypersonalwardrobe.viewmodels.SharedHomeAndGalleryViewModel
import org.parceler.Parcels
import kotlin.properties.Delegates

class SinglePhotoFragment: Fragment(){

    lateinit var sharedHomeAndGalleryViewModel: SharedHomeAndGalleryViewModel
    private var _binding: FragmentSinglePhotoBinding? = null
    private val binding get() = _binding!!
    private lateinit var photo: Photo
    private var position by Delegates.notNull<Int>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        sharedHomeAndGalleryViewModel = ViewModelProvider(requireActivity()).get(
            SharedHomeAndGalleryViewModel::class.java)

        photo = Parcels.unwrap(arguments?.getParcelable("photo"))
        position = requireArguments().getInt("position")
        _binding = FragmentSinglePhotoBinding.inflate(inflater, container, false)
        binding.aboutIcon.visibility = View.GONE
        binding.photoMenuDrawer.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backgroundLayout.setOnClickListener {
            binding.constraintLayoutAboutImage.visibility = View.GONE
            binding.aboutIcon.visibility = View.VISIBLE
            binding.photoMenuDrawer.visibility = View.GONE
        }

        binding.aboutIcon.setOnClickListener {
            binding.photoMenuDrawer.visibility = View.VISIBLE
            binding.aboutIcon.visibility = View.GONE
        }

        binding.photoMenuDrawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_info -> {
                    binding.constraintLayoutAboutImage.visibility = View.VISIBLE
                    binding.photoMenuDrawer.visibility = View.GONE
                }
                R.id.nav_delete -> {
                    sharedHomeAndGalleryViewModel.imageToDeleteMutableLiveData.value = photo
                    val result = position.toString()
                    // Use the Kotlin extension in the fragment-ktx artifact
                    setFragmentResult("requestKey", bundleOf("bundleKey" to result))
                    activity?.onBackPressed()
                }
            }
            return@setNavigationItemSelectedListener true
        }

        Glide.with(this).load(photo.uri)
            .fitCenter()
            .format(DecodeFormat.PREFER_ARGB_8888)
            .into(binding.image)

        binding.hashtagsTextView.text = photo.hashtags
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
