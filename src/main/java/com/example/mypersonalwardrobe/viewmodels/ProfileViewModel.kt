package com.example.mypersonalwardrobe.viewmodels

import GenericAdapter
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.net.Uri
import android.text.Editable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypersonalwardrobe.constants.FirebaseConst
import com.example.mypersonalwardrobe.source.FirebaseGenericRepo
import com.example.mypersonalwardrobe.domain.FirebaseUserRepo
import com.example.mypersonalwardrobe.models.Post
import com.example.mypersonalwardrobe.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.launch
import java.util.*

class ProfileViewModel: ViewModel() {

    val userRepo: FirebaseUserRepo = FirebaseUserRepo()
    val genericRepo: FirebaseGenericRepo = FirebaseGenericRepo()

    private var _usernameMutableLiveData = MutableLiveData<String>()
    private  var  _profileImageMutableLiveData = MutableLiveData<Uri>()
    private val  _hashtagsMutableLiveData = MutableLiveData<String>()
    private val  _bioMutableLiveData = MutableLiveData<String>()
    var _isUserObservedMutableLiveData = MutableLiveData<Boolean>()
    val _chatIdMutableLiveData = MutableLiveData<String>()
    var _isUsernameUniqueMutableLiveData = MutableLiveData<Boolean>()



    fun getProfileImageMutableLiveData(): MutableLiveData<Uri> {
        userRepo.getProfileImageMutableLiveData(_profileImageMutableLiveData)
        return _profileImageMutableLiveData
    }


    fun checkIfUserNameIsUnique(userName: String){

        val query = FirebaseFirestore
            .getInstance()
            .collection("users")
            .whereEqualTo("userName", userName)

        viewModelScope.launch {
            query.get().addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    _isUsernameUniqueMutableLiveData.postValue(false)
                    Log.w(ContentValues.TAG, "users Found: " + documents.toString())
                } else {
                    _isUsernameUniqueMutableLiveData.postValue(true)
                    Log.w(ContentValues.TAG, "users Found: 0")
                }
            }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting username documents: ", exception)
                }
        }
    }


    fun getIsUsernameUniqueMutableLiveData(): MutableLiveData<Boolean> {
        return _isUsernameUniqueMutableLiveData
    }

    fun getHashtagsMutableLiveData(): MutableLiveData<String> {
        userRepo.getHashtagsMutableLiveData(_hashtagsMutableLiveData)
        return _hashtagsMutableLiveData
    }

    fun getBioMutableLiveData(user: User): MutableLiveData<String> {
        userRepo.getBioMutableLiveData(user, _bioMutableLiveData)
        return _bioMutableLiveData
    }

    fun updateUsername(username: Editable){
        _usernameMutableLiveData.value = username.toString()
        userRepo.updateUsername(_usernameMutableLiveData.value!!)
    }

    fun updateBio(bio: Editable){
        _bioMutableLiveData.value = bio.toString()
        userRepo.updateBio(_bioMutableLiveData.value!!)
    }

    fun updateHashtags(hashtags: Editable){
        _hashtagsMutableLiveData.value = hashtags.toString()
        userRepo.updateHashtags(_hashtagsMutableLiveData.value!!)
    }





    fun checkIfUserIsObserved(user: User): MutableLiveData<Boolean> {

        val df: DocumentReference = FirebaseFirestore.getInstance().collection(FirebaseConst.OBSERVED).document(user.uid)
        df.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                _isUserObservedMutableLiveData.postValue(true)
            } else {
                _isUserObservedMutableLiveData.postValue(false)
            }
        }
        return _isUserObservedMutableLiveData

    }


    fun addToObserved(user: User) {
        userRepo.addToObserved(user)
    }

    fun removeFromObserved(user: User) {
        userRepo.removeFromObserved(user)
    }


    fun getChatId(user: User){

        val id = UUID.randomUUID().toString()

        val postMap = hashMapOf<String, Any>(
            "uids" to FirebaseConst.CURRENT_USER + "/" + user.uid,
        )

        val query: Query = FirebaseFirestore.getInstance().collection(FirebaseConst.CHAT)
            .whereIn("uids", listOf(FirebaseConst.CURRENT_USER + "/" + user.uid,
                user.uid + "/" + FirebaseConst.CURRENT_USER))

        query.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                if (snapshot.isEmpty) {
                    genericRepo.set(FirebaseConst.CHAT, id, postMap)
                    Log.d(TAG, "chatidmld created: " + id)
                    _chatIdMutableLiveData.postValue(id)
                } else {
                    for (document in snapshot) {
                        _chatIdMutableLiveData.postValue(document.id)
                    }
                    Log.d(TAG, "chatidmld snapshot: " + _chatIdMutableLiveData.value)
                }
            }
        })
    }


    fun getHashtagsDataFromFirestoreToRecyclerView(uid: String, adapter: GenericAdapter<String>) { userRepo.getHashtagsDataFromFirestoreToRecyclerView(uid, adapter) }

    fun getUserPostsFromFirestoreToRecyclerView(adapter: GenericAdapter<Post>, uid: String
    ) {
        genericRepo.getDataToRecyclerViewWithCondition(
            adapter,
            "authorUid",
            uid,
            FirebaseConst.POSTS)
    }


}