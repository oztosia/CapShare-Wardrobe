package com.example.mypersonalwardrobe.viewmodels

import GenericAdapter
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.example.mypersonalwardrobe.constants.FirebasePathsConstants
import com.example.mypersonalwardrobe.firebase.FirebaseGenericRepo
import com.example.mypersonalwardrobe.firebase.FirebaseOutfitAsksRepo
import com.example.mypersonalwardrobe.firebase.FirebaseUserRepo
import com.example.mypersonalwardrobe.helpers.ItemsListHolder
import com.example.mypersonalwardrobe.helpers.ItemsListHolder.ItemsListHolder.getItems
import com.example.mypersonalwardrobe.helpers.ItemsListHolder.ItemsListHolder.removeItem
import com.example.mypersonalwardrobe.models.OutfitAsk
import com.example.mypersonalwardrobe.models.PostImage
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class OutfitAskViewModel: ViewModel(){

    val firebaseRepo: FirebaseGenericRepo = FirebaseGenericRepo()
    val outfitAskRepo: FirebaseOutfitAsksRepo = FirebaseOutfitAsksRepo()




    private val userRepo: FirebaseUserRepo = FirebaseUserRepo()
    private var  _profileImageMutableLiveData = MutableLiveData<Uri>()

    fun getProfileImageMutableLiveData(): MutableLiveData<Uri> {
        userRepo.getProfileImageMutableLiveData(_profileImageMutableLiveData)
        Log.d(ContentValues.TAG, "profileImage: " + _profileImageMutableLiveData.value.toString())
        return _profileImageMutableLiveData
    }


    fun getOutfitAsksFromFirestoreToRecyclerView(adapter: GenericAdapter<OutfitAsk>
    ) {
        firebaseRepo.getDataToRecyclerView(adapter, FirebasePathsConstants.OUTFIT_ASKS)
    }


    fun getOutfitAskImagesFromFirestoreToRecyclerView(adapter: GenericAdapter<PostImage>,
                                                      id: String
    ) {
        firebaseRepo.getDataToRecyclerView(adapter,
            FirebasePathsConstants.OUTFIT_ASKS +
                    "$id/images")
    }

    fun getDataFromPhotoListToRecyclerView(adapter: GenericAdapter<String>){
        adapter.dataSet.addAll(ItemsListHolder.list)
        adapter.notifyDataSetChanged()
    }
/*
    fun addImageUriToPhotoList(uri: String){
        uriList.add(uri)
        Log.d(ContentValues.TAG, "data in urilist" + uriList.toString() + uriList.size)
    }
*/


    fun deleteImageUriFromPhotoList(uri: String){
        removeItem(uri)
        Log.d(ContentValues.TAG, "data in urilist" + getItems())
    }


    fun addOutfitAsk(title: String, text: String, hashtags: String){
        outfitAskRepo.addOutfitAsk(title, text, hashtags)
    }

    fun getHashtagsDataFromFirestoreToRecyclerView(adapter: GenericAdapter<String>,
                                                   outfitAskId: String) {
        firebaseRepo.getFromDocumentAndSplit(FirebasePathsConstants.OUTFIT_ASKS,
            outfitAskId,
            "hashtags",
            adapter)
    }

    fun getProfileImage(uid: String, profileImageView: ImageView, context: Context){
        val df: DocumentReference = FirebaseFirestore.getInstance().collection("users")
            .document(uid)
        df.get().addOnSuccessListener { documentSnapshot ->
            Glide
                .with(context)
                .load(documentSnapshot.getString("profileImage"))
                .centerCrop()
                .into(profileImageView)
        }
    }

    fun getUserName(uid: String, userNameTextView: TextView){
        val df: DocumentReference = FirebaseFirestore.getInstance().collection("users")
            .document(uid)
        df.get().addOnSuccessListener { documentSnapshot ->
            userNameTextView.text = documentSnapshot.getString("userName")
        }
    }


}