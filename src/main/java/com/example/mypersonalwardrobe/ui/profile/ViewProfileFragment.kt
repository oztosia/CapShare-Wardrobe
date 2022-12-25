package com.example.mypersonalwardrobe.ui.profile

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.databinding.FragmentProfileBinding
import com.example.mypersonalwardrobe.models.User
import com.example.mypersonalwardrobe.viewmodels.UsersListViewModel
import org.parceler.Parcels

class ViewProfileFragment: Fragment() {

    private lateinit var usersListViewModel: UsersListViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: User


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

        user = Parcels.unwrap(arguments?.getParcelable("user"))
        Log.d(ContentValues.TAG, "arguments receive: " + user.userName)

        observingDisable()

        usersListViewModel.checkIfUserIsObserved(user).observe(viewLifecycleOwner,
            Observer<Boolean?> { isUserObserved ->
                if (isUserObserved == false)
                    observingEnable()

            }
        )

            binding.navOption1.text = "UBRANIA"
        binding.navOption2.text = "OUTFITY"


        makeIconsInvisible()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            binding.userName.text = user.userName

            Glide.with(this).load(user.profileImage)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.profilePhotoImageView)

            binding.aboutTextView.text = user.bio

        binding.observeIcon.setOnClickListener {
            usersListViewModel.addToObserved(user)
            observingDisable()
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
        binding.editUserInfoIcon.visibility = View.GONE
        binding.editBioIcon.visibility = View.GONE
        binding.exitFromEditModeButton.visibility = View.GONE
        binding.editModeButton.visibility = View.GONE
    }
}