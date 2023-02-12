package com.example.mypersonalwardrobe.viewmodels

import GenericAdapter
import android.content.ContentValues.TAG
import android.net.Uri
import android.text.Editable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mypersonalwardrobe.constants.FirebasePathsConstants
import com.example.mypersonalwardrobe.firebase.FirebaseGenericRepo
import com.example.mypersonalwardrobe.firebase.FirebaseUserRepo
import com.example.mypersonalwardrobe.models.Post
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class MyProfileViewModel: ViewModel() {

    val userRepo: FirebaseUserRepo = FirebaseUserRepo()
    val firebaseRepo: FirebaseGenericRepo = FirebaseGenericRepo()
    private var userNameMutableLiveData = MutableLiveData<String>()
    private  var  profileImageMutableLiveData = MutableLiveData<Uri>()
    private val  hashtagsMutableLiveData = MutableLiveData<String>()
    private val  bioMutableLiveData = MutableLiveData<String>()


    fun getUserNameMutableLiveData(): MutableLiveData<String> {
        userRepo.getUserNameMutableLiveData(userNameMutableLiveData)
        Log.d(TAG, "username: " + userNameMutableLiveData.value.toString())
        return userNameMutableLiveData
    }

    fun getProfileImageMutableLiveData(): MutableLiveData<Uri> {
        userRepo.getProfileImageMutableLiveData(profileImageMutableLiveData)
        return profileImageMutableLiveData
    }

    fun getHashtagsMutableLiveData(): MutableLiveData<String> {
        userRepo.getHashtagsMutableLiveData(hashtagsMutableLiveData)
        Log.d(TAG, "hashtagsmld: " + hashtagsMutableLiveData.value.toString())
        return hashtagsMutableLiveData
    }


    fun updateHashtags(hashtags: Editable){
        hashtagsMutableLiveData.value = hashtags.toString()
        userRepo.updateHashtags(hashtagsMutableLiveData.value!!)
    }


    fun getHashtagsDataFromFirestoreToRecyclerView(adapter: GenericAdapter<String>
    ) {
        userRepo.getHashtagsDataFromFirestoreToRecyclerView(adapter)
    }


    fun getUserPostsFromFirestoreToRecyclerView(adapter: GenericAdapter<Post>
    ) {
        firebaseRepo.getDataToRecyclerViewWithCondition(
            adapter,
            "authorUid",
            FirebasePathsConstants.CURRENT_USER.toString(),
            FirebasePathsConstants.POSTS)
    }


    fun getBioMutableLiveData(): MutableLiveData<String> {
        val df: DocumentReference = FirebaseFirestore.getInstance().collection(FirebasePathsConstants.USERS_PATH + "/" + FirebasePathsConstants.CURRENT_USER.toString() + "/bio")
            .document("bio")
        df.get().addOnSuccessListener { documentSnapshot ->
            bioMutableLiveData.postValue(documentSnapshot.getString("bio"))

        }
        return bioMutableLiveData
    }


    fun updateBio(bio: Editable){
        bioMutableLiveData.value = bio.toString()
        userRepo.updateBio(bioMutableLiveData.value!!)
    }
}