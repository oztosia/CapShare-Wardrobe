package com.example.mypersonalwardrobe.viewmodels

import GenericAdapter
import androidx.lifecycle.ViewModel
import com.example.mypersonalwardrobe.constants.FirebaseConst
import com.example.mypersonalwardrobe.source.FirebaseGenericRepo
import com.example.mypersonalwardrobe.models.Photo

class GalleryViewModel: ViewModel() {

    private val genericRepo: FirebaseGenericRepo = FirebaseGenericRepo()

    fun getDataFromFirestoreToRecyclerView(galleryTypePath: String, user: String, adapter: GenericAdapter<Photo>){
        genericRepo.getDataToRecyclerView(adapter,
            FirebaseConst.USERS_PATH +
                    "/$user/$galleryTypePath")
    }

    fun deleteImage(photo: Photo, adapter: GenericAdapter<Photo>){
        genericRepo.deleteImageFromFirestore(photo, FirebaseConst.CURRENT_USER + "/items",
            FirebaseConst.MY_ITEMS_PATH, adapter)

    }
}