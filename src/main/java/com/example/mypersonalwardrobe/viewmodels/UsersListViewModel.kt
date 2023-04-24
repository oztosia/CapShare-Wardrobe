package com.example.mypersonalwardrobe.viewmodels

import GenericAdapter
import androidx.lifecycle.ViewModel
import com.example.mypersonalwardrobe.constants.FirebaseConst
import com.example.mypersonalwardrobe.source.FirebaseGenericRepo
import com.example.mypersonalwardrobe.models.User

class UsersListViewModel: ViewModel(){

    val genericRepo: FirebaseGenericRepo = FirebaseGenericRepo()


    fun getUsersDataFromFirestoreToRecyclerView(adapter: GenericAdapter<User>
    ) {
        genericRepo.getDataToRecyclerView(adapter, FirebaseConst.USERS_PATH)
    }

    fun getObservedUsersDataFromFirestoreToRecyclerView(adapter: GenericAdapter<User>) {
        genericRepo.getDataToRecyclerViewWithQuery(
            adapter,
            FirebaseConst.USERS_PATH, FirebaseConst.OBSERVED)
    }

}
