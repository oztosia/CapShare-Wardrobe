package com.example.mypersonalwardrobe.viewmodels

import GenericAdapter
import android.content.ContentValues
import android.text.Editable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mypersonalwardrobe.constants.FirebasePathsConstants
import com.example.mypersonalwardrobe.firebase.FirebaseGenericRepo

class PreferencesViewModel : ViewModel(){

    val firebaseRepo: FirebaseGenericRepo = FirebaseGenericRepo()
    private val  observedHashtagsMutableLiveData = MutableLiveData<String>()
    private val  blockedHashtagsMutableLiveData = MutableLiveData<String>()

    fun getObservedHashtagsMutableLiveData(): MutableLiveData<String> {
        firebaseRepo.get(FirebasePathsConstants.HASHTAGS_PREFERENCES,
            FirebasePathsConstants.OBSERVED_HASHTAGS,
        "observed",
        observedHashtagsMutableLiveData)
        Log.d(ContentValues.TAG, "hashtagsmld: " + observedHashtagsMutableLiveData.value.toString())
        return observedHashtagsMutableLiveData
    }

    fun getBlockedHashtagsMutableLiveData(): MutableLiveData<String> {
        firebaseRepo.get(FirebasePathsConstants.HASHTAGS_PREFERENCES,
            FirebasePathsConstants.BLOCKED_HASHTAGS,
            "blocked",
            blockedHashtagsMutableLiveData)
        Log.d(ContentValues.TAG, "hashtagsmld: " + blockedHashtagsMutableLiveData.value.toString())
        return blockedHashtagsMutableLiveData
    }



    fun updateObservedHashtags(hashtags: Editable){
        observedHashtagsMutableLiveData.value = hashtags.toString()

        val postMap = hashMapOf<String, String>(
            "observed" to observedHashtagsMutableLiveData.value.toString(),
        )

        firebaseRepo.update(FirebasePathsConstants.HASHTAGS_PREFERENCES,
            FirebasePathsConstants.OBSERVED_HASHTAGS,
            postMap)

    }

        fun updateBlockedHashtags(hashtags: Editable){
        blockedHashtagsMutableLiveData.value = hashtags.toString()

            val postMap = hashMapOf<String, String>(
                "blocked" to blockedHashtagsMutableLiveData.value.toString(),
            )

            firebaseRepo.update(FirebasePathsConstants.HASHTAGS_PREFERENCES,
                FirebasePathsConstants.BLOCKED_HASHTAGS,
                postMap)
    }



    fun getHashtagsDataFromFirestoreToRecyclerView(adapter: GenericAdapter<String>,
                                                   documentPath: String,
                                                   hashtagsType: String
    ) {
        firebaseRepo.getFromDocumentAndSplit(FirebasePathsConstants.HASHTAGS_PREFERENCES,
            documentPath,
            hashtagsType,
            adapter)
    }

}