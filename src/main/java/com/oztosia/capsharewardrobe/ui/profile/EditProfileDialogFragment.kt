package com.oztosia.capsharewardrobe.ui.profile

import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.oztosia.capsharewardrobe.CapShareWardrobe
import com.oztosia.capsharewardrobe.R
import com.oztosia.capsharewardrobe.databinding.FragmentEditProfileBinding
import com.oztosia.capsharewardrobe.models.User
import com.oztosia.capsharewardrobe.utils.SnackbarCreator
import com.oztosia.capsharewardrobe.viewmodels.ProfileViewModel
import kotlinx.coroutines.*
import org.parceler.Parcels
import java.util.concurrent.TimeUnit

class EditProfileFragment: Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var user: User

    val application = CapShareWardrobe.getAppContext()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        user = Parcels.unwrap(arguments?.getParcelable("user"))
        Log.d(ContentValues.TAG, "arguments receive: " + user.userName)

        if (user.userName.isNotEmpty()){
            binding.editUsername.visibility = View.GONE
            binding.iconWrong.visibility = View.GONE
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.iconEdit.setOnClickListener {
            binding.iconEdit.visibility = View.GONE
            binding.username.visibility = View.GONE
            binding.editUsername.visibility = View.VISIBLE
        }

        profileViewModel.getProfileImageMutableLiveData(user).observe(viewLifecycleOwner) {profileImage ->
            Glide.with(this).load(profileImage)
                .centerCrop()
                .into(binding.profilePhotoImageView)
        }

        binding.addPhotoIcon.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("intent", "profileImage")
            findNavController().navigate(R.id.action_editProfileFragment_to_photoBottomSheet, bundle)
        }

        binding.editUsername.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                val userName: String = binding.editUsername.text.toString()
                if (binding.editUsername.visibility == View.VISIBLE){
                profileViewModel.checkIfUserNameIsUnique(userName)
                activity?.let {
                    profileViewModel.getIsUsernameUniqueMutableLiveData().observe(it) { response ->
                        if (response == true && userName.length > 4) {
                            binding.iconWrong.visibility = View.GONE
                            binding.iconCorrect.visibility = View.VISIBLE
                        } else {
                            binding.iconWrong.visibility = View.VISIBLE
                            binding.iconCorrect.visibility = View.GONE
                        }
                    }
                    }
                }
            }
        })

        profileViewModel.getUserNameMutableLiveData(user).observe(viewLifecycleOwner) {username ->
            val editable: Editable = SpannableStringBuilder(username)
            binding.editUsername.text = editable
            binding.username.text = username
        }


        profileViewModel.getBioMutableLiveData(user).observe(viewLifecycleOwner) { bio ->
            val editable: Editable = SpannableStringBuilder(bio)
            binding.editBioTextView.text = editable
        }

        profileViewModel.getHashtagsMutableLiveData().observe(viewLifecycleOwner) { myHashtags ->
            val editable: Editable = SpannableStringBuilder(myHashtags)
            binding.editHashtagsTextView.text = editable
        }


        binding.confirmButton.setOnClickListener {

            if (binding.editUsername.visibility == View.VISIBLE) {
                if (binding.iconCorrect.visibility == View.VISIBLE) {
                    val username = binding.editUsername.text
                    profileViewModel.updateUsername(username)
                } else {
                    SnackbarCreator.show(
                        binding.background,
                        "Nazwa zajęta lub krótsza niż 4 znaki."
                    )
                }
            }


            val hashtags = binding.editHashtagsTextView.text
            profileViewModel.updateHashtags(hashtags)


            val bio = binding.editBioTextView.text
            profileViewModel.updateBio(bio)

            CoroutineScope(Dispatchers.IO).launch {
                delay(TimeUnit.SECONDS.toMillis(2.5.toLong()))
                withContext(Dispatchers.Main) {
                    activity?.onBackPressed()
                }

            }
        }
    }
}

