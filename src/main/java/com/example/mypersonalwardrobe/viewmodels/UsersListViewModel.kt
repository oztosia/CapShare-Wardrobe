package com.example.mypersonalwardrobe.viewmodels

import GenericAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mypersonalwardrobe.constants.FirebasePathsConstants
import com.example.mypersonalwardrobe.firebase.FirebaseUsersListRepo
import com.example.mypersonalwardrobe.models.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class UsersListViewModel: ViewModel(){
    val usersListRepo: FirebaseUsersListRepo = FirebaseUsersListRepo()
    var _isUserObservedMutableLiveData = MutableLiveData<Boolean>()


    fun getUsersDataFromFirestoreToRecyclerView(usersListFromFirebase: ArrayList<User>,
                                                adapter: GenericAdapter<User>
    ) {
        usersListRepo.getUsersDataFromFirestoreToRecyclerView(usersListFromFirebase, adapter)
    }

    fun getObservedUsersDataFromFirestoreToRecyclerView(usersListFromFirebase: ArrayList<User>,
                       adapter: GenericAdapter<User>
    ){
        usersListRepo.getObservedUsersDataFromFirestoreToRecyclerView(usersListFromFirebase, adapter)
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
}
