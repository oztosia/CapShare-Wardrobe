package com.example.mypersonalwardrobe.ui.profile

import android.content.ContentValues
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.databinding.DialogEditProfileBinding
import com.example.mypersonalwardrobe.models.User
import com.example.mypersonalwardrobe.viewmodels.ProfileViewModel
import kotlinx.coroutines.*
import org.parceler.Parcels
import java.util.concurrent.TimeUnit

class EditProfileDialogFragment: DialogFragment() {

    private var _binding: DialogEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var user: User

    val application = MyPersonalWardrobe.getAppContext()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        _binding = DialogEditProfileBinding.inflate(inflater, container, false)

        user = Parcels.unwrap(arguments?.getParcelable("user"))
        Log.d(ContentValues.TAG, "arguments receive: " + user.userName)


        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //binding.viewModel = myProfileViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.getProfileImageMutableLiveData().observe(viewLifecycleOwner) { uri ->
            Glide.with(this).load(uri)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.profilePhotoImageView)
        }

        binding.addPhotoIcon.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("intent", "profileImage")
            findNavController().navigate(R.id.action_editProfileFragment_to_photoBottomSheet, bundle)
        }

        val username: String = binding.username.text.toString()

        binding.username.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                val userName: String = binding.username.text.toString()
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
        })


        profileViewModel.getBioMutableLiveData(user).observe(viewLifecycleOwner) { bio ->
            val editable: Editable = SpannableStringBuilder(bio)
            binding.editBioTextView.text = editable
        }

        profileViewModel.getHashtagsMutableLiveData().observe(viewLifecycleOwner) { myHashtags ->
            val editable: Editable = SpannableStringBuilder(myHashtags)
            binding.editHashtagsTextView.text = editable
        }


        binding.confirmButton.setOnClickListener {

            val username = binding.username.text
            profileViewModel.updateUsername(username)


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

