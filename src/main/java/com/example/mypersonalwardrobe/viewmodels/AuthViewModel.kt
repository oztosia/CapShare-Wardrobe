package com.example.mypersonalwardrobe.viewmodels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.*
import com.example.mypersonalwardrobe.firebase.FirebaseAuthRepo
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


class AuthViewModel : ViewModel() {


    private val firebaseAuthRepo: FirebaseAuthRepo = FirebaseAuthRepo()
    private var userMutableLiveData: MutableLiveData<FirebaseUser> = firebaseAuthRepo.getUserMutableLiveData()
    val isUsernameUniqueMutableLiveData = MutableLiveData<Boolean>()

    fun login(email: String, password: String) {
        firebaseAuthRepo.login(email, password)
    }

   fun register(email: String, password: String, userName: String){
       firebaseAuthRepo.register(email, password, userName)
    }

    fun checkIfUserNameIsUnique(userName: String){

        val query = FirebaseFirestore
            .getInstance()
            .collection("users")
            .whereEqualTo("userName", userName)

        query.get().addOnSuccessListener { documents ->
            if (!documents.isEmpty) {
                isUsernameUniqueMutableLiveData.postValue(false)
                Log.w(ContentValues.TAG, "Found: " + documents.toString())
            }
            else{
                isUsernameUniqueMutableLiveData.postValue(true)
                Log.w(ContentValues.TAG, "Found: 0" )
            }
        }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    fun getIsUsernameUniqueMutableLiveData(): MutableLiveData<Boolean> {
        Log.w(ContentValues.TAG, "is unique get: " + isUsernameUniqueMutableLiveData.value.toString())
        return isUsernameUniqueMutableLiveData
    }

    fun getUserMutableLiveData(): LiveData<FirebaseUser> {
        return userMutableLiveData
    }
}