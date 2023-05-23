package com.oztosia.capsharewardrobe.ui.profile

import GenericAdapter
import android.content.ContentValues
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
import com.oztosia.capsharewardrobe.R
import com.oztosia.capsharewardrobe.adapters.viewholders.HashtagListViewHolder
import com.oztosia.capsharewardrobe.adapters.viewholders.PostViewHolder
import com.oztosia.capsharewardrobe.constants.FirebaseConst
import com.oztosia.capsharewardrobe.databinding.FragmentProfileBinding
import com.oztosia.capsharewardrobe.models.Post
import com.oztosia.capsharewardrobe.models.User
import com.oztosia.capsharewardrobe.utils.SnackbarCreator
import com.oztosia.capsharewardrobe.viewmodels.ProfileViewModel
import com.google.android.flexbox.*
import org.parceler.Parcels


class ProfileFragment: Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    lateinit var hashtagsAdapter: GenericAdapter<String>
    lateinit var postsAdapter: GenericAdapter<Post>

    private lateinit var user: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        profileViewModel =
           ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        user = Parcels.unwrap(arguments?.getParcelable("user"))


        setFragmentResult("requestKey", bundleOf("bundleKey" to "result"))

        if (user.uid == FirebaseConst.CURRENT_USER){
            binding.editModeButton.visibility = View.VISIBLE
            binding.observeIcon.visibility = View.GONE
            binding.chatIcon.visibility = View.GONE
            binding.removeFromObservedIcon.visibility = View.GONE
        }
        else{
            observingDisable()

            profileViewModel.checkIfUserIsObserved(user).observe(viewLifecycleOwner,
                Observer<Boolean?> { isUserObserved ->
                    if (isUserObserved == false)
                        observingEnable()
                }
            )
        }


        val layoutManager = FlexboxLayoutManager(activity)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.flexWrap = FlexWrap.WRAP
        layoutManager.justifyContent = JustifyContent.CENTER
        layoutManager.alignItems = AlignItems.STRETCH
        binding.hashtagsRecyclerView.layoutManager = layoutManager
        hashtagsAdapter = GenericAdapter({ HashtagListViewHolder(it) }, R.layout.item_hashtag)
        binding.hashtagsRecyclerView.adapter = hashtagsAdapter


        val postLayoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL,false)
        binding.postsRecyclerView.layoutManager = postLayoutManager
        postsAdapter = GenericAdapter({ PostViewHolder(it) }, R.layout.item_post)
        binding.postsRecyclerView.adapter = postsAdapter
        binding.postsRecyclerView.recycledViewPool.setMaxRecycledViews(0, 0)


        profileViewModel.getHashtagsDataFromFirestoreToRecyclerView(user.uid, hashtagsAdapter)

        profileViewModel.getUserPostsFromFirestoreToRecyclerView(postsAdapter, user.uid)


        profileViewModel.getProfileImageMutableLiveData(user).observe(viewLifecycleOwner) {profileImage ->
            Glide.with(this).load(profileImage)
                .centerCrop()
                .placeholder(R.drawable.icon)
                .into(binding.profilePhotoImageView)
        }

        profileViewModel.getUserNameMutableLiveData(user).observe(viewLifecycleOwner) {username ->
            binding.userName.text = username
        }

        profileViewModel.getBioMutableLiveData(user).observe(viewLifecycleOwner) { bio ->
            binding.bioTextview.text = bio
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.observeIcon.setOnClickListener {

            profileViewModel.addToObserved(user)

            observingDisable()

            SnackbarCreator.show(binding.mainConstraintLayout, "Dodano do obserwowanych: " + user.userName)
        }

        binding.removeFromObservedIcon.setOnClickListener {

            profileViewModel.removeFromObserved(user)

            observingEnable()

            SnackbarCreator.show(binding.mainConstraintLayout, "Usunięto z obserwowanych: " + user.userName)
        }

        binding.chatIcon.setOnClickListener {

            profileViewModel.getChatId(user)

            profileViewModel._chatIdMutableLiveData.observe(viewLifecycleOwner,
                Observer<String> { chatId ->
                    val bundle = Bundle()
                    bundle.putParcelable("user", Parcels.wrap(user))
                    bundle.putString("chat", chatId)
                    findNavController().navigate(R.id.action_ViewProfileFragment_to_ChatFragment, bundle)
                }
            )
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

                }
                R.id.nav_bio -> {
                    binding.postsRecyclerView.visibility = View.GONE
                    binding.bioTextview.visibility = View.VISIBLE


                }
            }
            return@setNavigationItemSelectedListener true
        }

        binding.editModeButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("user", Parcels.wrap(user))
            findNavController().navigate(R.id.action_ProfileFragment_to_EditProfileFragment, bundle)
        }


    }

    fun observingEnable(){
        binding.observeIcon.visibility = View.VISIBLE
        binding.removeFromObservedIcon.visibility = View.GONE
    }

    fun observingDisable(){
        binding.observeIcon.visibility = View.GONE
        binding.removeFromObservedIcon.visibility = View.VISIBLE
    }

}