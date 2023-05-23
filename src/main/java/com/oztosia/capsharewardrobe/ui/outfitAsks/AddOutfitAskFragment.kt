package com.oztosia.capsharewardrobe.ui.outfitAsks

import GenericAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.oztosia.capsharewardrobe.CapShareWardrobe
import com.oztosia.capsharewardrobe.R
import com.oztosia.capsharewardrobe.adapters.viewholders.AddOutfitAskItemViewHolder
import com.oztosia.capsharewardrobe.constants.FirebaseConst
import com.oztosia.capsharewardrobe.databinding.FragmentAddPostBinding
import com.oztosia.capsharewardrobe.singletons.ItemsListHolder
import com.oztosia.capsharewardrobe.utils.SnackbarCreator
import com.oztosia.capsharewardrobe.viewmodels.OutfitAskViewModel
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

        outfitAskViewModel = ViewModelProvider(requireActivity())[OutfitAskViewModel::class.java]
        _binding = FragmentAddPostBinding.inflate(inflater, container, false)

        val layoutManager = GridLayoutManager(CapShareWardrobe.getAppContext(), 3)
        binding.recyclerView.layoutManager = layoutManager
        adapter = GenericAdapter({ AddOutfitAskItemViewHolder(this@AddOutfitAskFragment, it) }, R.layout.item_image)
        binding.recyclerView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        outfitAskViewModel.getDataFromPhotoListToRecyclerView(adapter)

        outfitAskViewModel.getProfileImageMutableLiveData().observe(viewLifecycleOwner) { uri ->
            Glide.with(this).load(uri)
                .centerCrop()
                .into(binding.profileImage)
        }

        binding.addImageIcon.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("userName", FirebaseConst.CURRENT_USER)
            bundle.putString("action", "create")
           findNavController().navigate(R.id.action_AddOutfitAskFragment_to_GalleryFragment, bundle)
        }


        binding.addPostButton.setOnClickListener {

            val hashtags: String = binding.addHashtags.text.toString()
            val text: String = binding.text.text.toString()
            val title: String = binding.addTitle.text.toString()

            if (!hashtags.isEmpty() && !text.isEmpty() && !title.isEmpty()){
                outfitAskViewModel.addOutfitAsk(title, text, hashtags)

                CoroutineScope(Dispatchers.IO).launch {
                    delay(TimeUnit.SECONDS.toMillis(2.5.toLong()))
                    withContext(Dispatchers.Main) {
                        activity?.onBackPressed()
                        ItemsListHolder.list.clear()
                    }
                }
            }
            else{
                SnackbarCreator.show(binding.background, "Proszę uzupełnić treść, tytuł i hashtagi")
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun delete(uri: String){
        outfitAskViewModel.deleteImageUriFromPhotoList(uri)
        adapter.dataSet.remove(uri)
        adapter.notifyDataSetChanged()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}