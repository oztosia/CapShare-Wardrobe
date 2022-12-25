package com.example.mypersonalwardrobe.firebase

import GenericAdapter
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.mypersonalwardrobe.constants.FirebasePathsConstants
import com.example.mypersonalwardrobe.models.User
import com.google.firebase.firestore.DocumentReference
import java.util.ArrayList

class FirebaseUsersListRepo: FirebaseGenericRepo() {



    fun getUsersDataFromFirestoreToRecyclerView(usersListFromFirebase: ArrayList<User>,
                                                adapter: GenericAdapter<User>
    ) {
        getDataToRecyclerView(usersListFromFirebase, adapter, FirebasePathsConstants.USERS_PATH)
    }


    fun getObservedUsersDataFromFirestoreToRecyclerView(observedUsersListFromFirebase: ArrayList<User>,
                                                        adapter: GenericAdapter<User>
    ) {
        getDataToRecyclerViewWithQuery(observedUsersListFromFirebase,
            adapter,
            FirebasePathsConstants.USERS_PATH, FirebasePathsConstants.OBSERVED)
    }

    fun addToObserved(user: User) {
        val observedUser = hashMapOf<String, String>(
            "uid" to user.uid,
        )
        set(FirebasePathsConstants.OBSERVED + "/", user.uid, observedUser)
    }
}