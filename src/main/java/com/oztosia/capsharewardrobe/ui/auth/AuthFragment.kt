package com.oztosia.capsharewardrobe.ui.auth

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.oztosia.capsharewardrobe.CapShareWardrobe
import com.oztosia.capsharewardrobe.R
import com.oztosia.capsharewardrobe.databinding.FragmentAuthBinding
import com.oztosia.capsharewardrobe.viewmodels.AuthViewModel
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch


class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private lateinit var authViewModel: AuthViewModel
    private val binding get() = _binding!!
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var googleSignInOptions: GoogleSignInOptions
    lateinit var callbackManager: CallbackManager
    lateinit var bundle: String

    val application: Context = CapShareWardrobe.getAppContext()

    companion object {
        const val GOOGLE_REQUEST_AUTH: Int = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        _binding = FragmentAuthBinding.inflate(inflater, container, false)

        bundle = arguments?.getString("signOut").toString()
        if (bundle == "true") {
            authViewModel.signOut()
        } else {
            checkCurrentUser()
        }

        configureGoogleSignIn()
        callbackManager = CallbackManager.Factory.create()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonEmail.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("auth", "signIn")
            findNavController().navigate(
                R.id.action_AuthFragment_to_AuthWithEmailDialogFragment,
                bundle
            )
        }
        binding.buttonGoogle.setOnClickListener {
            val signInIntent: Intent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, GOOGLE_REQUEST_AUTH)
        }

        binding.buttonFacebook.setOnClickListener {

            arrayOf<String?>("email", "public_profile", "user_birthday", "user_friends")

            binding.buttonFacebook.registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        Log.d(TAG, "facebook:onSuccess:$result")
                        lifecycleScope.launch {
                            authViewModel.signInWithFacebook(result.accessToken)
                            if (bundle == "true") {
                                checkCurrentUser()
                            }
                        }


                    }

                    override fun onCancel() {
                        Log.d(TAG, "facebook:onCancel")
                    }

                    override fun onError(error: FacebookException) {
                        Log.d(TAG, "facebook:onError", error)
                    }
                })
        }

        binding.textViewSignUp.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("auth", "signUp")
            findNavController().navigate(
                R.id.action_AuthFragment_to_AuthWithEmailDialogFragment,
                bundle
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_REQUEST_AUTH) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getEmail())
                lifecycleScope.launch {
                    authViewModel.signInWithGoogle(account)
                    if (bundle == "true") {
                        checkCurrentUser()
                    }
                }


            } catch (e: ApiException) {
                Log.d(TAG, "nie udalo sie" + e)
            }
        }
    }

    private fun configureGoogleSignIn() {
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun checkCurrentUser() {
        activity?.let {
            authViewModel._userMutableLiveData.observe(it) { firebaseUser ->

                val provider =
                    firebaseUser?.getProviderData()?.get(firebaseUser.getProviderData().size - 1)
                        ?.getProviderId()

                Log.d(
                    TAG, "users Found check: " + firebaseUser?.uid + " "
                            + firebaseUser?.getProviderData()
                        ?.get(firebaseUser.getProviderData().size - 1)?.getProviderId()
                )

                if (firebaseUser != null && firebaseUser.isEmailVerified && provider == "password") {
                    findNavController().navigate(R.id.action_AuthFragment_to_HomeFragment)
                } else if (firebaseUser != null && provider != "password") {
                    findNavController().navigate(R.id.action_AuthFragment_to_HomeFragment)
                } else if (firebaseUser != null && !firebaseUser.isEmailVerified && provider == "password") {
                    Toast.makeText(
                        application,
                        "Proszę zweryfikować adres email",
                        Toast.LENGTH_SHORT
                    ).show()
                    firebaseUser.sendEmailVerification()
                }

            }
        }
    }


}