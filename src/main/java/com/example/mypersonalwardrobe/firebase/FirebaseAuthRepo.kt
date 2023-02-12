package com.example.mypersonalwardrobe.firebase

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseAuthRepo {

    val application = MyPersonalWardrobe.getAppContext()

    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    var _userMutableLiveData = MutableLiveData<FirebaseUser>()

    val _signedOutMutableLiveData = MutableLiveData<Boolean>()



    fun register(email: String, password: String, userName: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    if (!firebaseAuth.currentUser!!.isEmailVerified()) {
                        firebaseAuth.currentUser!!.sendEmailVerification()
                        addUserData(userName, email)
                        _userMutableLiveData.postValue(firebaseAuth.currentUser)
                    }
                } else {
                    Toast.makeText(
                        application.getApplicationContext(),
                        "Registration Failure: " + task.exception!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


    private fun addUserData(userName: String, email: String){
        FirebaseFirestore.getInstance().collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .set(mapOf(
                "userName" to userName,
                "email" to email,
                "bio" to "",
                "hashtags" to "",
                "profileImage" to "",
                "uid" to firebaseAuth.currentUser!!.uid

            ))
    }



    fun login(email: String, password: String) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    _userMutableLiveData.postValue(firebaseAuth.currentUser)
                } else {
                    Toast.makeText(
                        application.getApplicationContext(),
                        "Login Failure: " + task.exception!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }



    @JvmName("getUserMutableLiveData1")
    fun getUserMutableLiveData(): MutableLiveData<FirebaseUser> {
        if (firebaseAuth.currentUser != null) {
            _userMutableLiveData.postValue(firebaseAuth.currentUser)
            _signedOutMutableLiveData.postValue(false)
        }
        return _userMutableLiveData
    }
}