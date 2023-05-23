package com.oztosia.capsharewardrobe.ui.addPost

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
import com.oztosia.capsharewardrobe.CapShareWardrobe
import com.oztosia.capsharewardrobe.R
import com.oztosia.capsharewardrobe.adapters.viewholders.AddPostImageViewHolder
import com.oztosia.capsharewardrobe.databinding.FragmentAddPostBinding
import com.oztosia.capsharewardrobe.singletons.ItemsListHolder
import com.oztosia.capsharewardrobe.utils.SnackbarCreator
import com.oztosia.capsharewardrobe.viewmodels.AddPostViewModel
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class AddPostFragment: Fragment(){

    private var _binding: FragmentAddPostBinding? = null
    private val binding get() = _binding!!
    private lateinit var addPostViewModel: AddPostViewModel

    lateinit var adapter: GenericAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        addPostViewModel = ViewModelProvider(requireActivity())[AddPostViewModel::class.java]

        _binding = FragmentAddPostBinding.inflate(inflater, container, false)

        binding.addTitle.visibility = View.GONE

        val layoutManager = GridLayoutManager(CapShareWardrobe.getAppContext(), 3)
        binding.recyclerView.layoutManager = layoutManager
        adapter = GenericAdapter({ AddPostImageViewHolder(this@AddPostFragment, it) }, R.layout.item_image)
        binding.recyclerView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addPostViewModel.getDataFromPhotoListToRecyclerView(adapter)

        addPostViewModel.getProfileImageMutableLiveData().observe(viewLifecycleOwner) { uri ->
            Glide.with(this).load(uri)
                .centerCrop()
                .into(binding.profileImage)
        }

        binding.addImageIcon.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("intent", "post")
            findNavController().navigate(R.id.action_AddPostFragment_to_photoBottomSheet, bundle)
        }


        binding.addPostButton.setOnClickListener {

            val progressBar: ProgressBar = binding.progressBar
            val hashtags: String = binding.addHashtags.text.toString()
            val text: String = binding.text.text.toString()

            if (!hashtags.isEmpty() && !text.isEmpty()){
                addPostViewModel.addPost(text, hashtags, progressBar)

                CoroutineScope(Dispatchers.IO).launch {
                    delay(TimeUnit.SECONDS.toMillis(2.5.toLong()))
                    withContext(Dispatchers.Main) {
                        activity?.onBackPressed()
                        ItemsListHolder.list.clear()
                    }
                }
            }
            else{
                SnackbarCreator.show(binding.background, "Proszę uzupełnić treść i hashtagi")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun delete(uri: String) {
        addPostViewModel.deleteImage(uri)
        adapter.dataSet.remove(uri)
        adapter.notifyDataSetChanged()
    }
}