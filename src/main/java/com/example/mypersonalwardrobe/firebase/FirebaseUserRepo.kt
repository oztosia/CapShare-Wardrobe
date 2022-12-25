package com.example.mypersonalwardrobe.firebase


import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseUserRepo() {

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

    fun getUserBioMutableLiveData(mutableLiveDataType: MutableLiveData<String>) : MutableLiveData<String> {
        val df: DocumentReference = FirebaseFirestore.getInstance().collection("users")
            .document(firebaseAuth.currentUser!!.uid)
        df.get().addOnSuccessListener { documentSnapshot ->
            mutableLiveDataType.setValue(documentSnapshot.getString("bio"))
        }
        return mutableLiveDataType
    }

    fun addUserBio(bio: String){
        FirebaseFirestore.getInstance().collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .update(mapOf(
                "bio" to bio
            ))

    }

    fun signOut() {
        return firebaseAuth.signOut()
        _signedOutMutableLiveData.postValue(true)
    }

}
