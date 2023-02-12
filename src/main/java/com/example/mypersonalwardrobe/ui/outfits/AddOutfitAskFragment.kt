package com.example.mypersonalwardrobe.ui.outfits

import GenericAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.adapters.viewholders.ItemToPostOrAskViewHolder
import com.example.mypersonalwardrobe.constants.FirebasePathsConstants
import com.example.mypersonalwardrobe.databinding.FragmentAddPostBinding
import com.example.mypersonalwardrobe.helpers.ItemsListHolder.ItemsListHolder.list
import com.example.mypersonalwardrobe.viewmodels.OutfitAskViewModel
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class AddOutfitAskFragment : Fragment() {

    private var _binding: FragmentAddPostBinding? = null
    private val binding get() = _binding!!
    private lateinit var outfitAskViewModel: OutfitAskViewModel
    lateinit var adapter: GenericAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        outfitAskViewModel =
            ViewModelProvider(requireActivity())[OutfitAskViewModel::class.java]

        _binding = FragmentAddPostBinding.inflate(inflater, container, false)
        binding.imageProgressBar.visibility = View.GONE

        val layoutManager = GridLayoutManager(MyPersonalWardrobe.getAppContext(), 3)
        binding.recyclerView.layoutManager = layoutManager
        adapter = GenericAdapter({ ItemToPostOrAskViewHolder(it) },
            R.layout.photo_item)
        binding.recyclerView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        outfitAskViewModel.getDataFromPhotoListToRecyclerView(adapter)

        outfitAskViewModel.getProfileImageMutableLiveData().observe(viewLifecycleOwner) { uri ->
            Glide.with(this).load(uri)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.miniProfilePhotoImageView)
        }

        binding.addImageIcon.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("userName", FirebasePathsConstants.CURRENT_USER)
            bundle.putString("action", "create")
           findNavController().navigate(R.id.action_AddOutfitAskFragment_to_GalleryFragment, bundle)
        }


        binding.addPostButton.setOnClickListener {

            val hashtags: String = binding.addHashtags.text.toString()
            val text: String = binding.text.text.toString()
            val title: String = binding.addTitle.text.toString()

            outfitAskViewModel.addOutfitAsk(title, text, hashtags)

            CoroutineScope(Dispatchers.IO).launch {
                delay(TimeUnit.SECONDS.toMillis(2.5.toLong()))
                withContext(Dispatchers.Main) {
                    activity?.onBackPressed()
                    list.clear()
                }


            }


            }

    }

    fun removeItem(uri: String){
        outfitAskViewModel.deleteImageUriFromPhotoList(uri)
        adapter.dataSet.remove(uri)
        adapter.notifyDataSetChanged()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}