package com.oztosia.capsharewardrobe.ui.home

import GenericAdapter
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oztosia.capsharewardrobe.R
import com.oztosia.capsharewardrobe.adapters.viewholders.PostViewHolder
import com.oztosia.capsharewardrobe.constants.FirebaseConst
import com.oztosia.capsharewardrobe.databinding.FragmentHomeBinding
import com.oztosia.capsharewardrobe.models.Post
import com.oztosia.capsharewardrobe.viewmodels.HomeViewModel
import kotlinx.coroutines.launch
import org.parceler.Parcels


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var postsAdapter: GenericAdapter<Post>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeViewModel._lastDocumentSnapshotMutableLiveData.value = null

        binding.navigationDrawerCustomer.visibility = View.GONE
        binding.navigationDrawerProfile.visibility = View.GONE
        binding.closeNavigationIcon.visibility = View.GONE

        val postLayoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL,false)
        binding.recyclerView.layoutManager = postLayoutManager
        postsAdapter = GenericAdapter({ PostViewHolder(it) }, R.layout.item_post)
        binding.recyclerView.adapter = postsAdapter
        binding.recyclerView.recycledViewPool.setMaxRecycledViews(0, 0)

        homeViewModel.getCurrentUser()

        homeViewModel._currentUserMutableLiveData.observe(viewLifecycleOwner) { uid ->
            if (uid  == null){
                findNavController().navigate(R.id.action_HomeFragment_to_AuthFragment)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try { lifecycleScope.launch { homeViewModel.getUserPostsFromFirestoreToRecyclerView(postsAdapter) } }
        catch (e: Exception){Log.d(ContentValues.TAG, "error found: " + e)}



        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!binding.recyclerView.canScrollVertically(1)) {
                    lifecycleScope.launch { homeViewModel.getMorePostsFromFirestoreToRecyclerView(postsAdapter) }
                }
            }
        }

        binding.recyclerView.addOnScrollListener(scrollListener)


        binding.mainLayout.setOnClickListener { binding.navigationDrawerProfile.visibility = View.GONE }

       homeViewModel.getProfileImageMutableLiveData().observe(viewLifecycleOwner) { uri -> Glide.with(this).load(uri).centerCrop().placeholder(R.drawable.ic_launcher_foreground).into(binding.miniProfilePhotoImageView) }

        binding.miniProfilePhotoImageView.setOnClickListener{ binding.navigationDrawerProfile.visibility = View.VISIBLE }

        binding.navigationDrawerProfile.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_my_profile -> {
                    val bundle = Bundle()
                    bundle.putParcelable("user", Parcels.wrap(homeViewModel._currentUserMutableLiveData.value))
                    findNavController().navigate(R.id.action_HomeFragment_to_ProfileFragment, bundle)
                }
                R.id.nav_log_out -> {
                    val bundle = Bundle()
                    bundle.putString("signOut", "true")
                    findNavController().navigate(R.id.action_HomeFragment_to_AuthFragment, bundle)
                }
            }
            return@setNavigationItemSelectedListener true
        }


        binding.openNavigationIcon.setOnClickListener {
            binding.navigationDrawerCustomer.visibility = View.VISIBLE
            binding.openNavigationIcon.visibility = View.GONE
            binding.closeNavigationIcon.visibility = View.VISIBLE
        }

        binding.closeNavigationIcon.setOnClickListener {
            binding.navigationDrawerCustomer.visibility = View.GONE
            binding.openNavigationIcon.visibility = View.VISIBLE
            binding.closeNavigationIcon.visibility = View.GONE

        }


        binding.navigationDrawerCustomer.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.nav_items -> {
                    val bundle = Bundle()
                    bundle.putString("userName", FirebaseConst.CURRENT_USER)
                    findNavController().navigate(R.id.action_HomeFragment_to_GalleryFragment, bundle)
                }
                R.id.nav_outfits -> { findNavController().navigate(R.id.action_HomeFragment_to_OutfitsFragment) }
                //R.id.nav_planner -> { findNavController().navigate(R.id.action_HomeFragment_to_AskForOutfitFragment) }
                R.id.nav_contacts -> {
                    val bundle = Bundle()
                    bundle.putString("home", "home")
                    findNavController().navigate(R.id.action_HomeFragment_to_UsersListFragment, bundle)
                }
                R.id.nav_preferences -> { findNavController().navigate(R.id.action_HomeFragment_to_PreferencesFragment) }
            }
            return@setNavigationItemSelectedListener true
        }

        binding.addPostButton.setOnClickListener{ findNavController().navigate(R.id.action_HomeFragment_to_AddPostFragment) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}