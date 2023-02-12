package com.example.mypersonalwardrobe.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.constants.FirebasePathsConstants
import com.example.mypersonalwardrobe.databinding.FragmentHomeBinding
import com.example.mypersonalwardrobe.viewmodels.SharedHomeAndNewPhotoViewModel

class HomeFragment : Fragment() {

    private lateinit var sharedViewModel: SharedHomeAndNewPhotoViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedViewModel = ViewModelProvider(requireActivity())[SharedHomeAndNewPhotoViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        requireActivity().window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

        binding.navigationDrawerCustomer.visibility = View.GONE
        binding.navigationDrawerProfile.visibility = View.GONE
        binding.closeNavigationIcon.visibility = View.GONE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainLayout.setOnClickListener {
            binding.navigationDrawerProfile.visibility = View.GONE
        }

        sharedViewModel.getProfileImageMutableLiveData().observe(viewLifecycleOwner) { uri ->
            Glide.with(this).load(uri)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.miniProfilePhotoImageView)
        }

        binding.miniProfilePhotoImageView.setOnClickListener{
            binding.navigationDrawerProfile.visibility = View.VISIBLE
        }

        binding.navigationDrawerProfile.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_my_profile -> {
                    sharedViewModel.uploadGalleryTypeMutableLiveData("profileImage")
                    findNavController().navigate(R.id.action_HomeFragment_to_MyProfileFragment)
                }
                R.id.nav_log_out -> {
                    sharedViewModel.signOut()
                    findNavController().navigate(R.id.action_HomeFragment_to_SignInFragment)
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
                    bundle.putString("userName", FirebasePathsConstants.CURRENT_USER)
                    sharedViewModel.uploadGalleryTypeMutableLiveData("items")
                    findNavController().navigate(R.id.action_HomeFragment_to_GalleryFragment, bundle)
                }
                R.id.nav_outfits -> {
                    sharedViewModel.uploadGalleryTypeMutableLiveData("outfits")
                    findNavController().navigate(R.id.action_HomeFragment_to_OutfitsFragment)
                }
                R.id.nav_help -> {
                    sharedViewModel.uploadGalleryTypeMutableLiveData("outfitsAsks")
                    findNavController().navigate(R.id.action_HomeFragment_to_AskForOutfitFragment)
                }
                R.id.nav_contacts -> {
                    val bundle = Bundle()
                    bundle.putString("home", "home")
                    findNavController().navigate(R.id.action_HomeFragment_to_UsersListFragment, bundle)
                }
                R.id.nav_preferences -> {
                    findNavController().navigate(R.id.action_HomeFragment_to_PreferencesFragment)
                }
            }
            return@setNavigationItemSelectedListener true
        }

        binding.addItemButton.setOnClickListener{
            sharedViewModel.uploadGalleryTypeMutableLiveData("post")
            findNavController().navigate(R.id.action_HomeFragment_to_AddPostFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}