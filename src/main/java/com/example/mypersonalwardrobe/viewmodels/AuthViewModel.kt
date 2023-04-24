package com.example.mypersonalwardrobe.viewmodels

import androidx.lifecycle.*
import com.example.mypersonalwardrobe.domain.FirebaseAuthRepo
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser

class AuthViewModel : ViewModel() {

    private val firebaseAuthRepo: FirebaseAuthRepo = FirebaseAuthRepo()
    var _userMutableLiveData: MutableLiveData<FirebaseUser?> = firebaseAuthRepo.getUserMutableLiveData()

    suspend fun signInWithEmailAndPassword(email: String, password: String) {
       firebaseAuthRepo.signInWithEmailAndPassword(email, password) }

    suspend fun signInWithGoogle(googleAccount: GoogleSignInAccount) { firebaseAuthRepo.signInWithGoogle(googleAccount) }

    suspend fun signInWithFacebook(token: AccessToken) { firebaseAuthRepo.signInWithFacebook(token) }

    suspend fun signUpWithEmailAndPassword(email: String, password: String){ firebaseAuthRepo.signUpWithEmailAndPassword(email, password) }

    fun signOut(){ firebaseAuthRepo.signOut() }
}