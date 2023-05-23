package com.oztosia.capsharewardrobe.domain

import GenericAdapter
import androidx.lifecycle.MutableLiveData
import com.oztosia.capsharewardrobe.constants.FirebaseConst
import com.oztosia.capsharewardrobe.models.User
import com.oztosia.capsharewardrobe.source.FirebaseGenericRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseUserRepo : FirebaseGenericRepo() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getProfileImageMutableLiveData(mutableLiveDataType: MutableLiveData<String>): MutableLiveData<String> {
        val df: DocumentReference = FirebaseFirestore.getInstance().collection("users")
            .document(firebaseAuth.currentUser!!.uid)
        df.get().addOnSuccessListener { documentSnapshot ->
            mutableLiveDataType.postValue(
                documentSnapshot.getString("profileImage")
            )}
        return mutableLiveDataType
    }

    fun getUserNameMutableLiveData(mutableLiveDataType: MutableLiveData<String>): MutableLiveData<String> {
        val df: DocumentReference = FirebaseFirestore.getInstance().collection("users")
            .document(firebaseAuth.currentUser!!.uid)
        df.get().addOnSuccessListener { documentSnapshot ->
            mutableLiveDataType.postValue(documentSnapshot.getString("userName"))

        }
        return mutableLiveDataType
    }

    fun getProfileImageMutableLiveData(user: User, mutableLiveDataType: MutableLiveData<String>): MutableLiveData<String> {
        val df: DocumentReference = FirebaseFirestore.getInstance().collection("users")
            .document(user.uid)
        df.get().addOnSuccessListener { documentSnapshot ->
            mutableLiveDataType.postValue(
                documentSnapshot.getString("profileImage")
            )}
        return mutableLiveDataType
    }

    fun getUserNameMutableLiveData(user: User, mutableLiveDataType: MutableLiveData<String>): MutableLiveData<String> {
        val df: DocumentReference = FirebaseFirestore.getInstance().collection("users")
            .document(user.uid)
        df.get().addOnSuccessListener { documentSnapshot ->
            mutableLiveDataType.postValue(documentSnapshot.getString("userName"))

        }
        return mutableLiveDataType
    }

    fun getProfileImageMutableLiveData(uid: String, mutableLiveDataType: MutableLiveData<String>): MutableLiveData<String> {
        val df: DocumentReference = FirebaseFirestore.getInstance().collection("users")
            .document(uid)
        df.get().addOnSuccessListener { documentSnapshot ->
            mutableLiveDataType.postValue(
                documentSnapshot.getString("profileImage")
            )}
        return mutableLiveDataType
    }

    fun getUserNameMutableLiveData(uid: String, mutableLiveDataType: MutableLiveData<String>): MutableLiveData<String> {
        val df: DocumentReference = FirebaseFirestore.getInstance().collection("users")
            .document(uid)
        df.get().addOnSuccessListener { documentSnapshot ->
            mutableLiveDataType.postValue(documentSnapshot.getString("userName"))

        }
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
