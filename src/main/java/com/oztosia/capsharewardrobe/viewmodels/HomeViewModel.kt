package com.oztosia.capsharewardrobe.viewmodels

import GenericAdapter
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oztosia.capsharewardrobe.constants.FirebaseConst
import com.oztosia.capsharewardrobe.domain.FirebasePostsRepo
import com.oztosia.capsharewardrobe.domain.FirebaseUserRepo
import com.oztosia.capsharewardrobe.models.Post
import com.oztosia.capsharewardrobe.models.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class HomeViewModel: ViewModel() {

    private val userRepo: FirebaseUserRepo = FirebaseUserRepo()
    private val postsRepo: FirebasePostsRepo = FirebasePostsRepo()

    val  _profileImageMutableLiveData = MutableLiveData<String>()
    val _lastDocumentSnapshotMutableLiveData = MutableLiveData<DocumentSnapshot>()
    val  _currentUserMutableLiveData = MutableLiveData<User>()


    fun getProfileImageMutableLiveData(): MutableLiveData<String> {
        userRepo.getProfileImageMutableLiveData(_profileImageMutableLiveData)
        return _profileImageMutableLiveData
    }

    fun getCurrentUser(): MutableLiveData<User> {

        val documentReference: DocumentReference = FirebaseFirestore.getInstance().collection(
            FirebaseConst.USERS_PATH)
            .document(FirebaseConst.CURRENT_USER)

        documentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                _currentUserMutableLiveData
                    .postValue(documentSnapshot.toObject<User>()!!)
                Log.d(ContentValues.TAG, "docSnapshot: " + documentSnapshot)
            }
            .addOnFailureListener {
                Log.d(ContentValues.TAG, "docSnapshot: " + it)
            }
        return _currentUserMutableLiveData
    }

    suspend fun createListOfPostsToDisplay(){ postsRepo.createListOfPostsToDisplay() }

    suspend fun getMorePostsFromFirestoreToRecyclerView(adapter: GenericAdapter<Post>) { postsRepo.loadData(adapter, _lastDocumentSnapshotMutableLiveData) }

    suspend fun getUserPostsFromFirestoreToRecyclerView(adapter: GenericAdapter<Post>
    ) {
        createListOfPostsToDisplay()
        postsRepo.loadData(adapter, _lastDocumentSnapshotMutableLiveData)
    }
}