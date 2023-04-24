package com.example.mypersonalwardrobe.domain

import GenericAdapter
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.example.mypersonalwardrobe.constants.FirebaseConst
import com.example.mypersonalwardrobe.models.User
import com.example.mypersonalwardrobe.source.FirebaseGenericRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseUserRepo : FirebaseGenericRepo() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getProfileImageMutableLiveData(mutableLiveDataType: MutableLiveData<Uri>): MutableLiveData<Uri> {
        val df: DocumentReference = FirebaseFirestore.getInstance().collection("users")
            .document(firebaseAuth.currentUser!!.uid)
        df.get().addOnSuccessListener { documentSnapshot ->
            mutableLiveDataType.postValue(
                documentSnapshot.getString("profileImage")?.toUri()
            )}
        return mutableLiveDataType
    }

    fun getHashtagsMutableLiveData(mutableLiveDataType: MutableLiveData<String>): MutableLiveData<String> {
        val df: DocumentReference = FirebaseFirestore.getInstance().collection("users")
            .document(firebaseAuth.currentUser!!.uid)
        df.get().addOnSuccessListener { documentSnapshot ->
            mutableLiveDataType.postValue(documentSnapshot.getString("hashtags"))

        }
        return mutableLiveDataType
    }


    fun getBioMutableLiveData(user: User, mutableLiveDataType: MutableLiveData<String>): MutableLiveData<String> {

        val df: DocumentReference = FirebaseFirestore.getInstance().collection(FirebaseConst.USERS_PATH + "/"
                + user.uid + "/bio")
            .document("bio")
        df.get().addOnSuccessListener { documentSnapshot ->
            mutableLiveDataType.postValue(documentSnapshot.getString("bio"))
            if (!documentSnapshot.exists())
                mutableLiveDataType.postValue("")
        }
        return mutableLiveDataType
    }

    fun updateHashtags(hashtags: String){
        val postMap = hashMapOf("hashtags" to hashtags)
        update(FirebaseConst.USERS_PATH, FirebaseConst.CURRENT_USER, postMap)
    }

    fun updateBio(bio: String){
        val postMap = hashMapOf("bio" to bio)
        set(FirebaseConst.BIO, "bio", postMap)
    }

    fun updateUsername(username: String){
        val postMap = hashMapOf("userName" to username)
        update(FirebaseConst.USERS_PATH, FirebaseConst.CURRENT_USER, postMap)
    }

    fun addToObserved(user: User) {
        val observedUser = hashMapOf("uid" to user.uid,)
        set(FirebaseConst.OBSERVED + "/", user.uid, observedUser)
    }

    fun removeFromObserved(user: User) { delete(FirebaseConst.OBSERVED + "/", user.uid) }

    fun getHashtagsDataFromFirestoreToRecyclerView(uid: String, adapter: GenericAdapter<String>) { getFromDocumentAndSplit(FirebaseConst.USERS_PATH, uid, "hashtags", adapter) }
}
