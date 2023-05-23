package com.oztosia.capsharewardrobe.viewmodels

import GenericAdapter
import androidx.lifecycle.ViewModel
import com.oztosia.capsharewardrobe.constants.FirebaseConst
import com.oztosia.capsharewardrobe.source.FirebaseGenericRepo
import com.oztosia.capsharewardrobe.models.User

class UsersListViewModel: ViewModel(){

    val genericRepo: FirebaseGenericRepo = FirebaseGenericRepo()

    fun getUsersDataFromFirestoreToRecyclerView(adapter: GenericAdapter<User>) { genericRepo.getDataToRecyclerView(adapter, FirebaseConst.USERS_PATH) }

    fun getObservedUsersDataFromFirestoreToRecyclerView(adapter: GenericAdapter<User>) { genericRepo.getDataToRecyclerViewWithQuery(adapter, FirebaseConst.USERS_PATH, FirebaseConst.OBSERVED) }

}
