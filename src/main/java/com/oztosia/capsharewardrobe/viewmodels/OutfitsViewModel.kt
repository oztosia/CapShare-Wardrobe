package com.oztosia.capsharewardrobe.viewmodels

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.oztosia.capsharewardrobe.constants.FirebaseConst
import com.oztosia.capsharewardrobe.domain.FirebaseUserRepo
import com.oztosia.capsharewardrobe.domain.LikesRepo
import com.oztosia.capsharewardrobe.source.FirebaseGenericRepo

class OutfitsViewModel: ViewModel() {

    private val repo: FirebaseGenericRepo = FirebaseGenericRepo()
    private val userRepo: FirebaseUserRepo = FirebaseUserRepo()
    private val likesRepo: LikesRepo = LikesRepo()

    val  _profileImageMutableLiveData = MutableLiveData<String>()
    val  _usernameMutableLiveData = MutableLiveData<String>()
    val  _isOutfitLikedMutableLiveData = MutableLiveData<Boolean>()
    val  _likesMutableLiveData = MutableLiveData<String>()



    fun addOutfit(uri: Uri, uid: String, authorUid: String, id: String, progressBar: ProgressBar){
        val data = mutableMapOf<String, Any>("uid" to uid, "authorUid" to authorUid, "downloadURL" to uri, "askId" to id)
        repo.addImage(uri, data, FirebaseConst.STORAGE_OUTFITS, FirebaseConst.OUTFIT_ASKS + "$id/responses", progressBar)
    }

    fun getProfileImageMutableLiveData(uid: String): MutableLiveData<String> {
        userRepo.getProfileImageMutableLiveData(uid, _profileImageMutableLiveData)
        return _profileImageMutableLiveData
    }

    fun getUsernameMutableLiveData(uid: String): MutableLiveData<String> {
        userRepo.getUserNameMutableLiveData(uid, _usernameMutableLiveData)
        return _usernameMutableLiveData
    }

    suspend fun getLikesMutableLiveData(outfitAskId: String, outfitId: String): MutableLiveData<String> {
        likesRepo.getLikesFromFirestore("${FirebaseConst.OUTFIT_ASKS}$outfitAskId/responses/$outfitId/likes", _likesMutableLiveData)
        return _likesMutableLiveData
    }

    suspend fun getIsOutfitLikedMutableLiveData(outfitAskId: String, outfitId: String): MutableLiveData<Boolean> {
        likesRepo.checkifItemIsLiked("${FirebaseConst.OUTFIT_ASKS}$outfitAskId/responses/$outfitId/likes", _isOutfitLikedMutableLiveData)
        return _isOutfitLikedMutableLiveData
    }

    suspend fun like(outfitAskId: String, outfitId: String){
        likesRepo.like("${FirebaseConst.OUTFIT_ASKS}$outfitAskId/responses/$outfitId/likes")
    }

    suspend fun unlike(outfitAskId: String, outfitId: String){
        likesRepo.unlike("${FirebaseConst.OUTFIT_ASKS}$outfitAskId/responses/$outfitId/likes")
    }

}