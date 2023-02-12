package com.example.mypersonalwardrobe.firebase

import GenericAdapter
import com.example.mypersonalwardrobe.constants.FirebasePathsConstants
import com.example.mypersonalwardrobe.models.User
import java.util.ArrayList

class FirebaseUsersListRepo: FirebaseGenericRepo() {



    fun getUsersDataFromFirestoreToRecyclerView(adapter: GenericAdapter<User>) {
        getDataToRecyclerView(adapter, FirebasePathsConstants.USERS_PATH)
    }


    fun getObservedUsersDataFromFirestoreToRecyclerView(adapter: GenericAdapter<User>) {
        getDataToRecyclerViewWithQuery(adapter,
            FirebasePathsConstants.USERS_PATH, FirebasePathsConstants.OBSERVED)
    }

    fun addToObserved(user: User) {
        val observedUser = hashMapOf<String, String>(
            "uid" to user.uid,
        )
        set(FirebasePathsConstants.OBSERVED + "/", user.uid, observedUser)
    }

    fun getHashtagsDataFromFirestoreToRecyclerView(uid: String,
                                                   adapter: GenericAdapter<String>
    ) {
        getFromDocumentAndSplit(FirebasePathsConstants.USERS_PATH,
            uid,
            "hashtags",
            adapter)
    }
}