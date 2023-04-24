package com.example.mypersonalwardrobe.viewmodels

import GenericAdapter
import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mypersonalwardrobe.constants.FirebaseConst
import com.example.mypersonalwardrobe.source.FirebaseGenericRepo
import com.example.mypersonalwardrobe.utils.ObservedPostsSetHolder.ObservedPostsSetHolder.postSet

class PreferencesViewModel : ViewModel(){

    private val firebaseRepo: FirebaseGenericRepo = FirebaseGenericRepo()
    private val  observedHashtagsMutableLiveData = MutableLiveData<String>()
    private val  blockedHashtagsMutableLiveData = MutableLiveData<String>()

    fun getObservedHashtagsMutableLiveData(): MutableLiveData<String> {
        firebaseRepo.get(FirebaseConst.HASHTAGS_PREFERENCES, FirebaseConst.OBSERVED_HASHTAGS, "observed", observedHashtagsMutableLiveData)
        return observedHashtagsMutableLiveData
    }

    fun getBlockedHashtagsMutableLiveData(): MutableLiveData<String> {
        firebaseRepo.get(FirebaseConst.HASHTAGS_PREFERENCES, FirebaseConst.BLOCKED_HASHTAGS, "blocked", blockedHashtagsMutableLiveData)
        return blockedHashtagsMutableLiveData
    }

    fun updateObservedHashtags(hashtags: Editable){
        observedHashtagsMutableLiveData.value = hashtags.toString()
        val postMap = hashMapOf("observed" to observedHashtagsMutableLiveData.value.toString(),)
        firebaseRepo.set(FirebaseConst.HASHTAGS_PREFERENCES, FirebaseConst.OBSERVED_HASHTAGS, postMap)
        postSet.clear()
    }

    fun updateBlockedHashtags(hashtags: Editable){
        blockedHashtagsMutableLiveData.value = hashtags.toString()
        val postMap = hashMapOf("blocked" to blockedHashtagsMutableLiveData.value.toString())
        firebaseRepo.set(FirebaseConst.HASHTAGS_PREFERENCES, FirebaseConst.BLOCKED_HASHTAGS, postMap)
    }

    fun getHashtagsDataFromFirestoreToRecyclerView(adapter: GenericAdapter<String>,
                                                   documentPath: String,
                                                   hashtagsType: String
    ) {
        firebaseRepo.getFromDocumentAndSplit(FirebaseConst.HASHTAGS_PREFERENCES,
            documentPath,
            hashtagsType,
            adapter)
    }

}