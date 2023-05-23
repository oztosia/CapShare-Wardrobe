package com.oztosia.capsharewardrobe.viewmodels

import GenericAdapter
import androidx.lifecycle.ViewModel
import com.oztosia.capsharewardrobe.constants.FirebaseConst
import com.oztosia.capsharewardrobe.source.FirebaseGenericRepo
import com.oztosia.capsharewardrobe.models.Photo

class GalleryViewModel: ViewModel() {

    private val repo: FirebaseGenericRepo = FirebaseGenericRepo()

    fun getDataFromFirestoreToRecyclerView(galleryTypePath: String, user: String, adapter: GenericAdapter<Photo>){ repo.getDataToRecyclerView(adapter, FirebaseConst.USERS_PATH + "/$user/$galleryTypePath") }

    fun deleteImage(photo: Photo, adapter: GenericAdapter<Photo>){ repo.deleteImageFromFirestore(photo, FirebaseConst.CURRENT_USER + "/items", FirebaseConst.MY_ITEMS_PATH, adapter) }
}