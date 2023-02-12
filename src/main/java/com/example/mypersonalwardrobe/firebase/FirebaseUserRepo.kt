package com.example.mypersonalwardrobe.firebase


import GenericAdapter
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.example.mypersonalwardrobe.constants.FirebasePathsConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class FirebaseUserRepo(): FirebaseGenericRepo() {

    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val _signedOutMutableLiveData = MutableLiveData<Boolean>()

    fun getProfileImageMutableLiveData(mutableLiveDataType: MutableLiveData<Uri>): MutableLiveData<Uri> {
        val df: DocumentReference = FirebaseFirestore.getInstance().collection("users")
            .document(firebaseAuth.currentUser!!.uid)
        df.get().addOnSuccessListener { documentSnapshot ->
            mutableLiveDataType.postValue(
                documentSnapshot.getString("profileImage")?.toUri()
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


    fun getHashtagsDataFromFirestoreToRecyclerView(adapter: GenericAdapter<String>
    ) {
        getFromDocumentAndSplit(FirebasePathsConstants.USERS_PATH,
            FirebasePathsConstants.CURRENT_USER.toString(),
            "hashtags",
            adapter)
    }

    fun getHashtagsMutableLiveData(mutableLiveDataType: MutableLiveData<String>): MutableLiveData<String> {
        val df: DocumentReference = FirebaseFirestore.getInstance().collection("users")
            .document(firebaseAuth.currentUser!!.uid)
        df.get().addOnSuccessListener { documentSnapshot ->
            mutableLiveDataType.postValue(documentSnapshot.getString("hashtags"))

        }
        return mutableLiveDataType
    }

    fun updateHashtags(hashtags: String){

        val postMap = hashMapOf<String, String>(
            "hashtags" to hashtags,
        )

        update(FirebasePathsConstants.USERS_PATH,
                FirebasePathsConstants.CURRENT_USER.toString(),
                postMap)

    }

    fun updateBio(bio: String){

        val postMap = hashMapOf<String, String>(
            "bio" to bio,
        )

        update(FirebasePathsConstants.USERS_PATH,
            FirebasePathsConstants.CURRENT_USER.toString() + "/bio/bio",
            postMap)

    }



    fun signOut() {
        return firebaseAuth.signOut()
        _signedOutMutableLiveData.postValue(true)
    }

}
