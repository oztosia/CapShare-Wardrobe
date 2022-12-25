package com.example.mypersonalwardrobe.ui.items

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.adapters.GalleryRecyclerAdapter
import com.example.mypersonalwardrobe.databinding.FragmentBaseRecyclerviewWithSearchBinding
import com.example.mypersonalwardrobe.models.Photo
import com.example.mypersonalwardrobe.viewmodels.SharedHomeAndGalleryViewModel
import org.parceler.Parcels
import java.text.FieldPosition

class GalleryFragment : Fragment(), SearchView.OnQueryTextListener {

    lateinit var adapter: GalleryRecyclerAdapter
    var userImageFromFirebase: ArrayList<Photo> = ArrayList()
    private lateinit var sharedViewModel: SharedHomeAndGalleryViewModel
    private var _binding: FragmentBaseRecyclerviewWithSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        sharedViewModel =
            ViewModelProvider(this).get(SharedHomeAndGalleryViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBaseRecyclerviewWithSearchBinding.inflate(inflater, container, false)


        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedHomeAndGalleryViewModel::class.java)

        val layoutManager = GridLayoutManager(MyPersonalWardrobe.getAppContext(), 3)


        binding.recyclerView.layoutManager = layoutManager
        adapter = GalleryRecyclerAdapter(this@GalleryFragment,
            userImageFromFirebase
        )
        binding.recyclerView.adapter = adapter

        setFragmentResultListener("requestKey") { key, bundle ->
            val result = bundle.getString("bundleKey")
            if (result != null)
            sharedViewModel.imageToDeleteMutableLiveData.observe(viewLifecycleOwner,  Observer<Photo?> { photo ->
                sharedViewModel.deleteImage(photo, adapter)
                adapter.notifyAboutRemove(photo)
            })
        }

        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.search.setQuery("", true)

        sharedViewModel.galleryTypeMutableLiveData.observe(viewLifecycleOwner,  Observer<String?> { galleryTypePath ->
            sharedViewModel.getDataFromFirestoreToRecyclerView(galleryTypePath,
                userImageFromFirebase, adapter)
        })

        binding.addItemButton.setOnClickListener {
            findNavController().navigate(R.id.action_galleryFragment_to_photoBottomSheet)
        }
        binding.search.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.search.setOnQueryTextListener(this)


    }

    fun viewSinglePhoto(photo: Photo, position: Int){
        val bundle = Bundle()
        bundle.putParcelable("photo", Parcels.wrap(photo))
        bundle.putInt("position", position)
        Log.d(ContentValues.TAG, "arguments: " + bundle.toString())
        findNavController().navigate(R.id.action_GalleryFragment_to_SinglePhotoFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        adapter.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter.filter(newText)
        return false
    }

}

