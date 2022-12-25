package com.example.mypersonalwardrobe.ui.auth

import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.databinding.FragmentSignUpBinding
import com.example.mypersonalwardrobe.viewmodels.AuthViewModel
import kotlinx.coroutines.*


class SignUpFragment : Fragment() {

    lateinit var signUpViewModel: AuthViewModel
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentSignUpBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        signUpViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        checkCurrentUser()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_SignUpFragment_to_SignInFragment)
        }

        binding.userName.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun afterTextChanged(s: Editable) {
                val userName: String = binding.userName.text.toString()
                signUpViewModel.checkIfUserNameIsUnique(userName)
            }
        })


        binding.buttonSignUp.setOnClickListener {

            val email: String = binding.authEmail.text.toString()
            val userName: String = binding.userName.text.toString()
            val password: String = binding.authPassword.text.toString()


            if (signUpViewModel.getIsUsernameUniqueMutableLiveData().value == true) {
                if (email.length > 0 && password.length > 6 && userName.length > 1) {
                    signUpViewModel.register(email, password, userName)
                } else {
                    Toast.makeText(
                        context,
                        "Wszystkie pola muszą być wypełnione",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else
                Toast.makeText(
                    context,
                    "Nazwa użytkownika jest zajęta.",
                    Toast.LENGTH_SHORT
                ).show()

        }
    }

    override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

    fun checkCurrentUser(){
        activity?.let {
            signUpViewModel.getUserMutableLiveData().observe(
                it
            ) { firebaseUser ->
                if (firebaseUser != null && firebaseUser.isEmailVerified) {
                    findNavController().navigate(R.id.action_SignUpFragment_to_HomeFragment)
                }
                else if (!firebaseUser.isEmailVerified){
                    firebaseUser.sendEmailVerification()
                    Toast.makeText(
                        context,
                        "Proszę zweryfikować adres email",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
    }

    }
