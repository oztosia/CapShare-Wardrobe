package com.example.mypersonalwardrobe.ui.addPost


import GenericAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.adapters.viewholders.ItemToPostOrAskViewHolder
import com.example.mypersonalwardrobe.databinding.FragmentAddPostBinding
import com.example.mypersonalwardrobe.helpers.ItemsListHolder
import com.example.mypersonalwardrobe.viewmodels.SharedHomeAndNewPhotoViewModel
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class AddPostFragment: Fragment() {

    private var _binding: FragmentAddPostBinding? = null
    private val binding get() = _binding!!
    private lateinit var postViewModel: SharedHomeAndNewPhotoViewModel


    lateinit var adapter: GenericAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        postViewModel = ViewModelProvider(requireActivity())[SharedHomeAndNewPhotoViewModel::class.java]

        _binding = FragmentAddPostBinding.inflate(inflater, container, false)


        binding.addTitle.visibility = View.GONE
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


        postViewModel.getDataFromPhotoListToRecyclerView(adapter)

        postViewModel.getProfileImageMutableLiveData().observe(viewLifecycleOwner) { uri ->
            Glide.with(this).load(uri)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.miniProfilePhotoImageView)
        }

        binding.addImageIcon.setOnClickListener {
            findNavController().navigate(R.id.action_AddPostFragment_to_photoBottomSheet)
        }


        binding.addPostButton.setOnClickListener {

            val progressBar: ProgressBar = binding.imageProgressBar
            val hashtags: String = binding.addHashtags.text.toString()
            val text: String = binding.text.text.toString()

            postViewModel.addPost(text, hashtags, progressBar)

            CoroutineScope(Dispatchers.IO).launch {
                delay(TimeUnit.SECONDS.toMillis(2.5.toLong()))
                withContext(Dispatchers.Main) {
                    activity?.onBackPressed()
                    ItemsListHolder.ItemsListHolder.list.clear()
                }


            }

        }
    }

    fun removeItemFromOriginalArrayList(uri: String){
        postViewModel.deleteImage(uri)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}