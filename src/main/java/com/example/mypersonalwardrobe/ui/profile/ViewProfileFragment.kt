package com.example.mypersonalwardrobe.ui.profile

import GenericAdapter
import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
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
import com.example.mypersonalwardrobe.models.User
import com.example.mypersonalwardrobe.viewmodels.UsersListViewModel
import com.google.android.flexbox.*
import com.google.android.material.snackbar.Snackbar
import org.parceler.Parcels


class ViewProfileFragment: Fragment() {

    private lateinit var usersListViewModel: UsersListViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: User

    lateinit var hashtagsAdapter: GenericAdapter<String>

    lateinit var postsAdapter: GenericAdapter<Post>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        usersListViewModel =
           ViewModelProvider(this).get(UsersListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.editHashtagsTextView.visibility = View.GONE

        user = Parcels.unwrap(arguments?.getParcelable("user"))
        Log.d(ContentValues.TAG, "arguments receive: " + user.userName)

        setFragmentResult("requestKey", bundleOf("bundleKey" to "result"))

        observingDisable()

        usersListViewModel.checkIfUserIsObserved(user).observe(viewLifecycleOwner,
            Observer<Boolean?> { isUserObserved ->
                if (isUserObserved == false)
                    observingEnable()

            }
        )

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

            usersListViewModel.getUserPostsFromFirestoreToRecyclerView(postsAdapter, user.uid)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usersListViewModel.getHashtagsDataFromFirestoreToRecyclerView(user.uid, hashtagsAdapter)


        binding.userName.text = user.userName

            Glide.with(this).load(user.profileImage)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.profilePhotoImageView)


        binding.observeIcon.setOnClickListener {
            usersListViewModel.addToObserved(user)
            observingDisable()

            val snackbar =
                Snackbar
                    .make(binding.mainConstraintLayout, "Dodano do obserwowanych: " + user.userName, Snackbar.LENGTH_LONG)
            snackbar.setBackgroundTint(Color.GRAY)
            snackbar.setTextColor(Color.WHITE)
            snackbar.show()
        }

        binding.chatIcon.setOnClickListener {
            findNavController().navigate(R.id.action_ViewProfileFragment_to_ChatFragment)
        }

        binding.itemsButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("userName", user.uid)
            findNavController().navigate(R.id.action_ViewProfileFragment_to_GalleryFragment, bundle)
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


    }

    fun observingEnable(){
        binding.observeIcon.visibility = View.VISIBLE
    }

    fun observingDisable(){
        binding.observeIcon.visibility = View.GONE
    }

    fun makeIconsInvisible() {
        binding.addPhotoIcon.visibility = View.GONE
        binding.editHashtagsButton.visibility = View.GONE
        binding.confirmButton.visibility = View.GONE
        binding.exitFromEditModeButton.visibility = View.GONE
        binding.editModeButton.visibility = View.GONE
    }
}