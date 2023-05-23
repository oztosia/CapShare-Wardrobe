package com.oztosia.capsharewardrobe.ui.outfits

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.oztosia.capsharewardrobe.CapShareWardrobe
import com.oztosia.capsharewardrobe.databinding.FragmentOutfitBinding
import com.oztosia.capsharewardrobe.models.Outfit
import com.oztosia.capsharewardrobe.viewmodels.OutfitAskViewModel
import com.oztosia.capsharewardrobe.viewmodels.OutfitsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.parceler.Parcels

class OutfitFragment: Fragment() {


    private var _binding: FragmentOutfitBinding? = null
    private val binding get() = _binding!!
    private lateinit var outfit: Outfit
    private lateinit var outfitsViewModel: OutfitsViewModel

    val application: Context = CapShareWardrobe.getAppContext()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentOutfitBinding.inflate(inflater, container, false)

        outfitsViewModel = ViewModelProvider(requireActivity())[OutfitsViewModel::class.java]

        outfit = Parcels.unwrap(arguments?.getParcelable("outfit"))

        Glide.with(application).load(outfit.downloadURL)
            .into(binding.image)

        outfitsViewModel.getProfileImageMutableLiveData(outfit.authorUid).observe(viewLifecycleOwner) { uri ->
            Glide.with(this).load(uri).centerCrop().into(binding.miniProfilePhotoImageView)
        }

        outfitsViewModel.getUsernameMutableLiveData(outfit.authorUid).observe(viewLifecycleOwner) { username ->
            binding.userName.text = username
        }

        checkIfOutfitIsLiked()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.likeIcon.setOnClickListener {
            lifecycleScope.launch { outfitsViewModel.like(outfit.askId, outfit.docId)
            checkIfOutfitIsLiked()}
        }

        binding.unlikeIcon.setOnClickListener {
            lifecycleScope.launch { outfitsViewModel.unlike(outfit.askId, outfit.docId)
            checkIfOutfitIsLiked()}
        }
    }

    fun showLikeIcon(){
        binding.likeIcon.visibility = View.VISIBLE
        binding.unlikeIcon.visibility = View.GONE
    }

    fun showUnlikeIcon(){
        binding.likeIcon.visibility = View.GONE
        binding.unlikeIcon.visibility = View.VISIBLE
    }

    fun checkIfOutfitIsLiked(){
        lifecycleScope.launch {

            outfitsViewModel.getLikesMutableLiveData(outfit.askId, outfit.docId)
                .observe(viewLifecycleOwner) { likes ->
                    binding.likes.text = likes
                }

            outfitsViewModel.getIsOutfitLikedMutableLiveData(outfit.askId, outfit.docId)
                .observe(viewLifecycleOwner) { isLiked ->
                    if (isLiked == true) {
                        showUnlikeIcon()
                    } else
                        showLikeIcon()
                }
        }
    }
}