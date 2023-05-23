package com.oztosia.capsharewardrobe.ui.auth

import android.content.ContentValues
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.oztosia.capsharewardrobe.CapShareWardrobe
import com.oztosia.capsharewardrobe.R
import com.oztosia.capsharewardrobe.databinding.DialogAuthWithEmailBinding
import com.oztosia.capsharewardrobe.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

class AuthWithEmailDialogFragment : DialogFragment() {

    private var _binding: DialogAuthWithEmailBinding? = null
    private lateinit var authViewModel: AuthViewModel
    private val binding get() = _binding!!

    val application = CapShareWardrobe.getAppContext()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = DialogAuthWithEmailBinding.inflate(inflater, container, false)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        val bundle = arguments?.getString("auth")

        if (bundle == "signIn") {
            binding.buttonSignUp.visibility = View.GONE
        } else if (bundle == "signUp") {
            binding.buttonSignIn.visibility = View.GONE
        }

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSignIn.setOnClickListener {
            val email: String = binding.authEmail.text.toString()
            val password: String = binding.authPassword.text.toString()
            if (email.length > 0 && password.length > 6) {
                lifecycleScope.launch { authViewModel.signInWithEmailAndPassword(email, password)
                    checkCurrentUser()
                }

            } else {
                Toast.makeText(
                    context, "Proszę wpisać email i hasło", Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.buttonSignUp.setOnClickListener {
            val email: String = binding.authEmail.text.toString()
            val password: String = binding.authPassword.text.toString()
            if (email.length > 0 && password.length > 6) {
                lifecycleScope.launch { authViewModel.signUpWithEmailAndPassword(email, password) }

            } else {
                Toast.makeText(
                    context, "Proszę wpisać email i hasło", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun checkCurrentUser() {
        activity?.let {
            authViewModel._userMutableLiveData.observe(it) { firebaseUser ->
                if (firebaseUser != null && firebaseUser.isEmailVerified) {
                    findNavController().navigate(R.id.action_AuthWithEmailDialogFragment_to_HomeFragment)
                }
                else { Log.d(ContentValues.TAG, "users Found check: " + firebaseUser?.uid + " " + findNavController().currentDestination) }
            }
        }
    }


}

