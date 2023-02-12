package com.example.mypersonalwardrobe.ui.gallery

import GenericAdapter
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.adapters.viewholders.ItemsListViewHolder
import com.example.mypersonalwardrobe.adapters.viewholders.SingleImageViewHolder
import com.example.mypersonalwardrobe.constants.FirebasePathsConstants
import com.example.mypersonalwardrobe.databinding.FragmentBaseRecyclerviewWithSearchBinding
import com.example.mypersonalwardrobe.models.Photo
import com.example.mypersonalwardrobe.viewmodels.GalleryViewModel

class GalleryFragment : Fragment(), SearchView.OnQueryTextListener{


    private var _binding: FragmentBaseRecyclerviewWithSearchBinding? = null
    private val binding get() = _binding!!
    lateinit var itemsAdapter: GenericAdapter<Photo>
    lateinit var singleImageAdapter: GenericAdapter<Photo>
    private lateinit var galleryViewModel: GalleryViewModel
    var dialog: AlertDialog? = null





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel = ViewModelProvider(requireActivity()).get(GalleryViewModel::class.java)
        _binding = FragmentBaseRecyclerviewWithSearchBinding.inflate(inflater, container, false)
        val layoutManager = GridLayoutManager(MyPersonalWardrobe.getAppContext(), 3)
        binding.recyclerView.layoutManager = layoutManager
        itemsAdapter = GenericAdapter({ ItemsListViewHolder(this@GalleryFragment,it) },
            R.layout.photo_item)
        binding.recyclerView.adapter = itemsAdapter



        val bundle = arguments?.getString("userName")
        val actionBundle = arguments?.getString("action")
        galleryViewModel.getDataFromFirestoreToRecyclerView("items", bundle.toString(),
            itemsAdapter)
        if (bundle != FirebasePathsConstants.CURRENT_USER){
            binding.addItemButton.visibility = View.GONE
        }

        if (actionBundle == "create"){
            binding.pickItemButton.visibility = View.VISIBLE
            binding.addItemButton.visibility = View.GONE
        }

        binding.search.isIconified

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.search.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.search.setOnQueryTextListener(this)

        binding.addItemButton.setOnClickListener {
            findNavController().navigate(R.id.action_galleryFragment_to_photoBottomSheet)
            itemsAdapter.dataSet.clear()
        }


        binding.pickItemButton.setOnClickListener {
            activity?.onBackPressed()
        }



    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        itemsAdapter.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        itemsAdapter.filter.filter(newText)
        return false
    }



    fun showImage(position: Int){
        val builder = AlertDialog.Builder(activity, R.style.CustomAlertDialog)
        val view = layoutInflater.inflate(R.layout.popup_image, null)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.HORIZONTAL,false)
        recyclerView.layoutManager = layoutManager
        singleImageAdapter = GenericAdapter({ SingleImageViewHolder(this@GalleryFragment,it) },R.layout.single_photo_item)
        recyclerView.adapter = singleImageAdapter
        singleImageAdapter.dataSet.addAll(itemsAdapter.sortedArray)
        builder.setView(view)
        dialog = builder.create()
        dialog?.show()
        recyclerView.scrollToPosition(position)
    }

    fun delete(photo: Photo){
        galleryViewModel.deleteImage(photo, itemsAdapter)
        dialog?.hide()
    }

    override fun onResume() {
        super.onResume()
        _binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

