package com.example.mypersonalwardrobe.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.databinding.FragmentSignInBinding
import com.example.mypersonalwardrobe.viewmodels.AuthViewModel
import com.google.firebase.auth.*


class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private lateinit var authViewModel: AuthViewModel
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }


        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            checkCurrentUser()

            binding.buttonSignIn.setOnClickListener {
                val email: String = binding.authEmail.text.toString()
                val password: String = binding.authPassword.text.toString()
                if (email.length > 0 && password.length > 6) {
                    authViewModel.login(email, password)
                }
                else{
                    Toast.makeText(
                        context, "Proszę wpisać email i hasło", Toast.LENGTH_SHORT).show()
                                }
                    }
            binding.textViewSignIn.setOnClickListener {
                findNavController().navigate(R.id.action_SignInFragment_to_SignUpFragment)
            }
        }
        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

    fun checkCurrentUser(){
        activity?.let {
            authViewModel.getUserMutableLiveData().observe(
                it
            ) { firebaseUser ->
                if (firebaseUser != null && firebaseUser.isEmailVerified) {
                    findNavController().navigate(R.id.action_SignInFragment_to_HomeFragment)
                }
            }
        }
    }
}