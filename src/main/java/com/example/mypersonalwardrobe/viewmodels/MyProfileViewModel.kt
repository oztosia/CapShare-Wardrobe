package com.example.mypersonalwardrobe.viewmodels

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mypersonalwardrobe.firebase.FirebaseUserRepo
import com.google.firebase.auth.FirebaseAuth

class MyProfileViewModel: ViewModel() {

    val userRepo: FirebaseUserRepo = FirebaseUserRepo()
    private var userNameMutableLiveData = MutableLiveData<String>()
    private var userBioMutableLiveData = MutableLiveData<String>()
    private  var  profileImageMutableLiveData = MutableLiveData<Uri>()



    fun addUserBio(bio: String){
        userRepo.addUserBio(bio)
    }

    fun getUserNameMutableLiveData(): MutableLiveData<String> {
        userRepo.getUserNameMutableLiveData(userNameMutableLiveData)
        Log.d(TAG, "username: " + userNameMutableLiveData.value.toString())
        return userNameMutableLiveData
    }

    fun getUserBioMutableLiveData(): MutableLiveData<String> {
        userRepo.getUserBioMutableLiveData(userBioMutableLiveData)
        return userBioMutableLiveData
    }

    fun getProfileImageMutableLiveData(): MutableLiveData<Uri> {
        userRepo.getProfileImageMutableLiveData(profileImageMutableLiveData)
        Log.d(TAG, "profileImage: " + profileImageMutableLiveData.value.toString())
        return profileImageMutableLiveData
    }
}