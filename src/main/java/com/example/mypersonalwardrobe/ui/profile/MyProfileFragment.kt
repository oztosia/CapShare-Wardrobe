package com.example.mypersonalwardrobe.ui.profile

import GenericAdapter
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.adapters.viewholders.HashtagListViewHolder
import com.example.mypersonalwardrobe.adapters.viewholders.PostViewHolder
import com.example.mypersonalwardrobe.constants.FirebasePathsConstants
import com.example.mypersonalwardrobe.databinding.FragmentProfileBinding
import com.example.mypersonalwardrobe.models.Post
import com.example.mypersonalwardrobe.viewmodels.MyProfileViewModel
import com.google.android.flexbox.*


class MyProfileFragment: Fragment() {

    private lateinit var myProfileViewModel: MyProfileViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    lateinit var postsAdapter: GenericAdapter<Post>

    lateinit var hashtagsAdapter: GenericAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        myProfileViewModel =
            ViewModelProvider(this).get(MyProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        myProfileViewModel = ViewModelProvider(requireActivity())[MyProfileViewModel::class.java]

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)


        binding.editHashtagsTextView.visibility = View.GONE

        val layoutManager = FlexboxLayoutManager(activity)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.flexWrap = FlexWrap.WRAP
        layoutManager.justifyContent = JustifyContent.CENTER
        layoutManager.alignItems = AlignItems.STRETCH

        binding.hashtagsRecyclerView.layoutManager = layoutManager
        hashtagsAdapter = GenericAdapter({ HashtagListViewHolder(it) }, R.layout.hashtag_item)
        binding.hashtagsRecyclerView.adapter = hashtagsAdapter





        val postLayoutManager = LinearLayoutManager(activity,
        LinearLayoutManager.VERTICAL,false)

        binding.postsRecyclerView.layoutManager = postLayoutManager
        postsAdapter = GenericAdapter({ PostViewHolder(it) }, R.layout.post_item)
        binding.postsRecyclerView.adapter = postsAdapter


        makeIconsInvisible()
        binding.exitFromEditModeButton.visibility = View.GONE
        binding.editBioTextView.visibility = View.GONE


        //binding.lifecycleOwner = this
        binding.viewModel = myProfileViewModel

        myProfileViewModel.getUserPostsFromFirestoreToRecyclerView(postsAdapter)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(ContentValues.TAG, "item count: " + postsAdapter.itemCount)

        myProfileViewModel.getHashtagsDataFromFirestoreToRecyclerView(hashtagsAdapter)


        myProfileViewModel.getProfileImageMutableLiveData().observe(viewLifecycleOwner) { uri ->
            Glide.with(this).load(uri)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.profilePhotoImageView)
        }

        myProfileViewModel.getBioMutableLiveData().observe(viewLifecycleOwner) { bio ->
            binding.bioTextview.text = bio
        }

        myProfileViewModel.getUserNameMutableLiveData().observe(viewLifecycleOwner) { userName ->
            binding.userName.text = userName
        }

        binding.editModeButton.setOnClickListener {
            binding.postsRecyclerView.visibility = View.GONE
            binding.editModeButton.visibility = View.GONE
            binding.exitFromEditModeButton.visibility = View.VISIBLE
            editBioEnable()
            makeIconsVisible()
        }

        binding.addPhotoIcon.setOnClickListener {
            findNavController().navigate(R.id.action_myProfileFragment_to_photoBottomSheet)
        }

        binding.editHashtagsButton.setOnClickListener {

            binding.hashtagsRecyclerView.visibility = View.GONE
            binding.editHashtagsTextView.visibility = View.VISIBLE
            binding.confirmButton.visibility = View.VISIBLE
            binding.editHashtagsButton.visibility = View.GONE
        }

        binding.confirmButton.setOnClickListener{

            val hashtags = binding.editHashtagsTextView.text

            myProfileViewModel.updateHashtags(hashtags)
            myProfileViewModel.getHashtagsDataFromFirestoreToRecyclerView(hashtagsAdapter)

            binding.hashtagsRecyclerView.visibility = View.VISIBLE
            binding.editHashtagsTextView.visibility = View.GONE
            binding.confirmButton.visibility = View.GONE



        }


        binding.exitFromEditModeButton.setOnClickListener {

            val bio = binding.editBioTextView.text
            myProfileViewModel.updateBio(bio)
            binding.editModeButton.visibility = View.VISIBLE
            binding.editBioTextView.visibility = View.GONE
            binding.bioTextview.visibility = View.VISIBLE
            binding.exitFromEditModeButton.visibility = View.GONE
            makeIconsInvisible()
        }

        binding.bottomNavigationView.setOnNavigationItemSelectedListener setNavigationItemSelectedListener@{
            when (it.itemId) {
                R.id.nav_posts -> {
                    binding.postsRecyclerView.visibility = View.VISIBLE
                    binding.bioTextview.visibility = View.GONE
                    binding.editBioTextView.visibility = View.GONE

                }
                R.id.nav_bio -> {
                    binding.postsRecyclerView.visibility = View.GONE
                    binding.bioTextview.visibility = View.VISIBLE


                }
            }
            return@setNavigationItemSelectedListener true
        }

        binding.itemsButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("userName", FirebasePathsConstants.CURRENT_USER)
            findNavController().navigate(R.id.action_MyProfileFragment_to_GalleryFragment, bundle)
        }
    }

    fun makeIconsVisible() {
        binding.addPhotoIcon.visibility = View.VISIBLE
        binding.editHashtagsButton.visibility = View.VISIBLE
    }

    fun makeIconsInvisible() {
        binding.observeIcon.visibility = View.GONE
        binding.chatIcon.visibility = View.GONE
        binding.addPhotoIcon.visibility = View.GONE
        binding.editHashtagsButton.visibility = View.GONE
        binding.confirmButton.visibility = View.GONE
    }

    fun editBioEnable(){
        binding.bioTextview.visibility = View.GONE
        binding.editBioTextView.visibility = View.VISIBLE
    }
}