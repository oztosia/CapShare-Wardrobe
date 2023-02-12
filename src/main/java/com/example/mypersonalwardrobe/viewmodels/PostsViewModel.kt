package com.example.mypersonalwardrobe.viewmodels

import GenericAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.example.mypersonalwardrobe.constants.FirebasePathsConstants
import com.example.mypersonalwardrobe.firebase.FirebaseGenericRepo
import com.example.mypersonalwardrobe.models.PostImage
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class PostsViewModel : ViewModel(){

    val firebaseRepo: FirebaseGenericRepo = FirebaseGenericRepo()
    val _userNameMutableLiveData = MutableLiveData<String>()
    val application = MyPersonalWardrobe.getAppContext()

    fun getProfileImage(uid: String, profileImageView: ImageView){
        val df: DocumentReference = FirebaseFirestore.getInstance().collection("users")
            .document(uid)
        df.get().addOnSuccessListener { documentSnapshot ->
            Glide
                .with(application)
                .load(documentSnapshot.getString("profileImage"))
                .centerCrop()
                .into(profileImageView)
        }
    }

    fun getUserName(uid: String, userNameTextView: TextView){
        val df: DocumentReference = FirebaseFirestore.getInstance().collection("users")
            .document(uid)
        df.get().addOnSuccessListener { documentSnapshot ->
            userNameTextView.text = documentSnapshot.getString("userName")
        }
    }

    fun getHashtagsDataFromFirestoreToRecyclerView(adapter: GenericAdapter<String>,
                                                   postId: String) {
        firebaseRepo.getFromDocumentAndSplit(FirebasePathsConstants.POSTS,
            postId,
            "hashtags",
            adapter)
    }


    fun testPostsImagesFromFirestoreToRecyclerView(adapter: GenericAdapter<PostImage>,
                                                   uid: String,
                                                   postId: String
    ) {
        firebaseRepo.getDataToRecyclerView(adapter,
            FirebasePathsConstants.POSTS + "$postId/images")
    }


}
