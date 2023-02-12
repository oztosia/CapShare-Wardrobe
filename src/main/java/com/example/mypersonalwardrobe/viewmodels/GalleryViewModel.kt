package com.example.mypersonalwardrobe.viewmodels

import GenericAdapter
import androidx.lifecycle.ViewModel
import com.example.mypersonalwardrobe.constants.FirebasePathsConstants
import com.example.mypersonalwardrobe.firebase.FirebaseGenericRepo
import com.example.mypersonalwardrobe.models.Photo

class GalleryViewModel: ViewModel() {

    private val genericRepo: FirebaseGenericRepo = FirebaseGenericRepo()

    fun getDataFromFirestoreToRecyclerView(galleryTypePath: String, user: String, adapter: GenericAdapter<Photo>){
        genericRepo.getDataToRecyclerView(adapter,
            FirebasePathsConstants.USERS_PATH +
                    "/$user/$galleryTypePath")
    }

    fun deleteImage(photo: Photo, adapter: GenericAdapter<Photo>){
        genericRepo.deleteImageFromFirestore(photo, FirebasePathsConstants.CURRENT_USER + "/items",
            FirebasePathsConstants.MY_ITEMS_PATH, adapter)

    }
}