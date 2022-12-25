package com.example.mypersonalwardrobe.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.databinding.FragmentProfileBinding
import com.example.mypersonalwardrobe.viewmodels.MyProfileViewModel

class MyProfileFragment: Fragment() {

    private lateinit var myProfileViewModel: MyProfileViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


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

        _binding = FragmentProfileBinding.inflate(inflater, container, false)



        binding.navOption1.text = "UBRANIA"
        binding.navOption2.text = "OUTFITY"


        makeIconsInvisible()
        binding.exitFromEditModeButton.visibility = View.GONE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        myProfileViewModel.getProfileImageMutableLiveData().observe(viewLifecycleOwner) { uri ->
            Glide.with(this).load(uri)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.profilePhotoImageView)
        }

        myProfileViewModel.getUserNameMutableLiveData().observe(viewLifecycleOwner) { userName ->
            binding.userName.text = userName
        }


        binding.editModeButton.setOnClickListener {
            binding.editModeButton.visibility = View.GONE
            binding.exitFromEditModeButton.visibility = View.VISIBLE
            makeIconsVisible()
        }

        binding.addPhotoIcon.setOnClickListener {
            findNavController().navigate(R.id.action_myProfileFragment_to_photoBottomSheet)
        }

        binding.editUserInfoIcon.setOnClickListener {
            //new Fragment
        }

        binding.editBioIcon.setOnClickListener {
            //editBioCard
        }

        binding.exitFromEditModeButton.setOnClickListener {
            binding.editModeButton.visibility = View.VISIBLE
            binding.exitFromEditModeButton.visibility = View.GONE
            makeIconsInvisible()
        }
    }

    fun makeIconsVisible() {
        binding.addPhotoIcon.visibility = View.VISIBLE
        binding.editUserInfoIcon.visibility = View.VISIBLE
        binding.editBioIcon.visibility = View.VISIBLE
    }

    fun makeIconsInvisible() {
        binding.observeIcon.visibility = View.GONE
        binding.chatIcon.visibility = View.GONE
        binding.addPhotoIcon.visibility = View.GONE
        binding.editUserInfoIcon.visibility = View.GONE
        binding.editBioIcon.visibility = View.GONE
    }

    /*

    fun editBio(){
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(activity)
        val view = layoutInflater.inflate(R.layout.bio_popup_window, null)
        val bioEditText: EditText = view.findViewById(R.id.bioEditText)
        val addBioIcon: ImageView = view.findViewById(R.id.add_bio_icon)

        addBioIcon.setOnClickListener(View.OnClickListener {
            val bio: String = bioEditText.getText().toString()
            myProfileViewModel.addUserBio(bio)
            dialog?.dismiss()
        })
        builder.setView(view)
        dialog = builder.create()
        dialog.show()
    }

     */
}