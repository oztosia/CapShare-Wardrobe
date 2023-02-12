package com.example.mypersonalwardrobe.viewmodels

import GenericAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mypersonalwardrobe.constants.FirebasePathsConstants
import com.example.mypersonalwardrobe.firebase.FirebaseGenericRepo
import com.example.mypersonalwardrobe.firebase.FirebaseUsersListRepo
import com.example.mypersonalwardrobe.models.Post
import com.example.mypersonalwardrobe.models.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class UsersListViewModel: ViewModel(){
    val usersListRepo: FirebaseUsersListRepo = FirebaseUsersListRepo()
    val genericRepo: FirebaseGenericRepo = FirebaseGenericRepo()
    var _isUserObservedMutableLiveData = MutableLiveData<Boolean>()


    fun getUsersDataFromFirestoreToRecyclerView(adapter: GenericAdapter<User>
    ) {
        genericRepo.getDataToRecyclerView(adapter, FirebasePathsConstants.USERS_PATH)
    }


    fun getObservedUsersDataFromFirestoreToRecyclerView(adapter: GenericAdapter<User>) {
        genericRepo.getDataToRecyclerViewWithQuery(
            adapter,
            FirebasePathsConstants.USERS_PATH, FirebasePathsConstants.OBSERVED)
    }

    fun addToObserved(user: User) {
        usersListRepo.addToObserved(user)
    }

    fun checkIfUserIsObserved(user: User): MutableLiveData<Boolean> {

        val df: DocumentReference = FirebaseFirestore.getInstance().collection(FirebasePathsConstants.OBSERVED).document(user.uid)
        df.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                _isUserObservedMutableLiveData.postValue(true)
            } else {
                _isUserObservedMutableLiveData.postValue(false)
            }
        }
        return _isUserObservedMutableLiveData

    }

    fun getUserPostsFromFirestoreToRecyclerView(adapter: GenericAdapter<Post>, uid: String
    ) {
        genericRepo.getDataToRecyclerViewWithCondition(
            adapter,
            "authorUid",
            uid,
            FirebasePathsConstants.POSTS)
    }


    fun getHashtagsDataFromFirestoreToRecyclerView(uid: String,
                                                   adapter: GenericAdapter<String>
    ) {
        usersListRepo.getHashtagsDataFromFirestoreToRecyclerView(uid, adapter)
    }
}
