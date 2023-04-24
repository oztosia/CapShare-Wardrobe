package com.example.mypersonalwardrobe.viewmodels

import GenericAdapter
import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import android.widget.ProgressBar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mypersonalwardrobe.constants.FirebaseConst
import com.example.mypersonalwardrobe.source.FirebaseGenericRepo
import com.example.mypersonalwardrobe.domain.FirebasePostsRepo
import com.example.mypersonalwardrobe.domain.FirebaseUserRepo
import com.example.mypersonalwardrobe.utils.ItemsListHolder.ItemsListHolder.list
import com.example.mypersonalwardrobe.models.Post
import com.example.mypersonalwardrobe.models.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class SharedHomeAndNewPhotoViewModel: ViewModel() {

    private val userRepo: FirebaseUserRepo = FirebaseUserRepo()
    private val postsRepo: FirebasePostsRepo = FirebasePostsRepo()
    private var  _profileImageMutableLiveData = MutableLiveData<Uri>()
    val  _currentUserMutableLiveData = MutableLiveData<User>()
    var  _lastDocumentSnapshotMutableLiveData = MutableLiveData<DocumentSnapshot>()

//////

    fun getProfileImageMutableLiveData(): MutableLiveData<Uri> {
        userRepo.getProfileImageMutableLiveData(_profileImageMutableLiveData)
        Log.d(TAG, "profileImage: " + _profileImageMutableLiveData.value.toString())
        return _profileImageMutableLiveData
    }

    fun getCurrentUser(): MutableLiveData<User> {

        val documentReference: DocumentReference = FirebaseFirestore.getInstance().collection(FirebaseConst.USERS_PATH)
            .document(FirebaseConst.CURRENT_USER)

        documentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                _currentUserMutableLiveData
                    .postValue(documentSnapshot.toObject<User>()!!)
                Log.d(TAG, "docSnapshot: " + documentSnapshot)
            }
            .addOnFailureListener {
                Log.d(TAG, "docSnapshot: " + it)
            }
        return _currentUserMutableLiveData
    }

 ////addpost vm

    fun addPost(text: String, hashtags: String, progressBar: ProgressBar){ postsRepo.addPost(text, hashtags, progressBar) }

    suspend fun createListOfPostsToDisplay(){ postsRepo.createListOfPostsToDisplay() }

    suspend fun getMorePostsFromFirestoreToRecyclerView(adapter: GenericAdapter<Post>) { postsRepo.loadData(adapter, _lastDocumentSnapshotMutableLiveData) }

    fun getDataFromPhotoListToRecyclerView(adapter: GenericAdapter<String>){ postsRepo.getDataFromPhotoListToRecyclerView(adapter) }

    fun deleteImage(uri: String){ list.remove(uri) }

    suspend fun getUserPostsFromFirestoreToRecyclerView(adapter: GenericAdapter<Post>
    ) {
        createListOfPostsToDisplay()
        postsRepo.loadData(adapter, _lastDocumentSnapshotMutableLiveData)
    }
}