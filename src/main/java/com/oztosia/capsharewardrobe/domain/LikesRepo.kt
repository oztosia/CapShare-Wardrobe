package com.oztosia.capsharewardrobe.domain

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.oztosia.capsharewardrobe.constants.FirebaseConst

import kotlinx.coroutines.tasks.await

class LikesRepo {

    val firestore = FirebaseFirestore.getInstance()

    suspend fun like(path: String): Void? {
        val postMap = hashMapOf<String, Any>("uid" to FirebaseConst.CURRENT_USER)
        return firestore.collection(path).document(FirebaseConst.CURRENT_USER).set(postMap).await()
    }

    suspend fun unlike(path: String): Void? { return firestore.collection(path).document(FirebaseConst.CURRENT_USER).delete().await()}

    suspend fun checkifItemIsLiked(path: String, mutableLiveData: MutableLiveData<Boolean>) { return mutableLiveData.postValue(firestore.collection(path).document(FirebaseConst.CURRENT_USER).get().await().exists()) }

    suspend fun getLikesFromFirestore(path: String, mutableLiveData: MutableLiveData<String>) { return mutableLiveData.postValue(firestore.collection(path).get().await().count().toString()) }
}


