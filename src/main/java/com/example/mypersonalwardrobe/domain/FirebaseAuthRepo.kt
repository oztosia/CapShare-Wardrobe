package com.example.mypersonalwardrobe.domain

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await


@Suppress("BlockingMethodInNonBlockingContext")
class FirebaseAuthRepo {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var _userMutableLiveData = MutableLiveData<FirebaseUser?>()
    val application: Context = MyPersonalWardrobe.getAppContext()


    suspend fun signUpWithEmailAndPassword(email: String, password: String) {

        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await().let {
                addUserDataFromEmailAuth(email)
                _userMutableLiveData.postValue(firebaseAuth.currentUser)
                Toast.makeText(application, "Proszę zweryfikować adres email", Toast.LENGTH_SHORT).show()
                firebaseAuth.currentUser!!.sendEmailVerification()
            }
        }
        catch (e: Exception){
            Toast.makeText(application.applicationContext, "Login Failure: " + e, Toast.LENGTH_SHORT).show()
        }
    }



    suspend fun signInWithEmailAndPassword(email: String, password: String) {
        try {

            firebaseAuth.signInWithEmailAndPassword(email, password).await()
                .let { _userMutableLiveData.postValue(firebaseAuth.currentUser)
                    if (!firebaseAuth.currentUser!!.isEmailVerified) {
                        Toast.makeText(application, "Proszę zweryfikować adres email", Toast.LENGTH_SHORT).show()
                        firebaseAuth.currentUser!!.sendEmailVerification()
                    }
                }
        }
        catch (e: Exception){
            Toast.makeText(application, "Login Failure: " + e, Toast.LENGTH_SHORT).show()
        }
        }



    suspend fun signInWithGoogle(googleAccount: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(googleAccount.idToken, null)
        firebaseAuth.signInWithCredential(credential).await().let {
            _userMutableLiveData.postValue(firebaseAuth.currentUser)
            addUserDataFromGoogleAuth(googleAccount)
        }

    }

     suspend fun signInWithFacebook(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")
        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential).await().let {
            _userMutableLiveData.postValue(firebaseAuth.currentUser)
            addUserDataFromFacebookAuth()
        }

    }

    private suspend fun checkIfUserExists(documentReference: DocumentReference): Boolean {
        return documentReference.get().await().exists()
    }

    private suspend fun addUserData(docName: String, postMap: Map<String, String?>){
        val documentReference = FirebaseFirestore.getInstance().collection("users").document(docName)
        if (!checkIfUserExists(documentReference)) {
            runBlocking { documentReference.set(postMap).await() }
        }
        else
            return
    }

    private suspend fun addUserDataFromEmailAuth(email: String){
        val postMap = mapOf("userName" to "", "email" to email, "hashtags" to "", "profileImage" to "", "uid" to firebaseAuth.currentUser!!.uid)
        addUserData(firebaseAuth.currentUser!!.uid, postMap)
    }

    private suspend fun addUserDataFromGoogleAuth(googleAccount: GoogleSignInAccount){
        val postMap = mapOf("userName" to googleAccount.email, "email" to googleAccount.email, "hashtags" to "", "profileImage" to googleAccount.photoUrl.toString(), "uid" to firebaseAuth.currentUser!!.uid)

            addUserData(firebaseAuth.currentUser!!.uid, postMap)
    }


    private suspend fun addUserDataFromFacebookAuth(){
        val profileImage = firebaseAuth.currentUser!!.photoUrl.toString()
        val username = firebaseAuth.currentUser!!.displayName
        val email = firebaseAuth.currentUser!!.email
        val postMap = mapOf("userName" to username, "email" to email, "hashtags" to "", "profileImage" to profileImage, "uid" to firebaseAuth.currentUser!!.uid)
        addUserData(firebaseAuth.currentUser!!.uid, postMap)
    }



























    @JvmName("getUserMutableLiveData1")
    fun getUserMutableLiveData(): MutableLiveData<FirebaseUser?> {
        if (firebaseAuth.currentUser != null) { _userMutableLiveData.postValue(firebaseAuth.currentUser) }
        else { _userMutableLiveData.postValue(null) }
        return _userMutableLiveData
    }

    fun signOut(){
        firebaseAuth.signOut()
    }

}