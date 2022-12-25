package com.example.mypersonalwardrobe.viewmodels

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import android.widget.ProgressBar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mypersonalwardrobe.adapters.GalleryRecyclerAdapter
import com.example.mypersonalwardrobe.adapters.PostImageRecyclerAdapter
import com.example.mypersonalwardrobe.firebase.FirebasePhotosRepo
import com.example.mypersonalwardrobe.firebase.FirebasePostsRepo
import com.example.mypersonalwardrobe.firebase.FirebaseUserRepo
import com.example.mypersonalwardrobe.models.Photo


class SharedHomeAndGalleryViewModel: ViewModel() {

    private val photosRepo: FirebasePhotosRepo = FirebasePhotosRepo()
    private val userRepo: FirebaseUserRepo = FirebaseUserRepo()
    var galleryTypeMutableLiveData = MutableLiveData<String>()
    private var  profileImageMutableLiveData = MutableLiveData<Uri>()
    var imageToDeleteMutableLiveData = MutableLiveData<Photo>()
    private val postsRepo: FirebasePostsRepo = FirebasePostsRepo()


    fun uploadImageToDeleteMutableLiveData(photo: Photo): MutableLiveData<Photo> {
        imageToDeleteMutableLiveData.value = photo
        return imageToDeleteMutableLiveData
    }

    fun uploadGalleryTypeMutableLiveData(galleryTypePath: String): MutableLiveData<String> {
        galleryTypeMutableLiveData.value = galleryTypePath
        Log.d("TAG", "current gallery: " + galleryTypeMutableLiveData.value.toString() )
        return galleryTypeMutableLiveData
    }


    fun uploadImage(uri: Uri, galleryTypePath: String, progressBar: ProgressBar, hashtags: String) {
        photosRepo.uploadImage(uri, galleryTypePath, progressBar, hashtags)
        Log.e("GalleryViewModel", "Photo added" )
    }

    fun getDataFromFirestoreToRecyclerView(galleryTypePath: String, userImageFromFirebase: ArrayList<Photo>, adapter: GalleryRecyclerAdapter){
        photosRepo.getDataFromFirestoreToRecyclerView(galleryTypePath, userImageFromFirebase, adapter)
    }

    fun getProfileImageMutableLiveData(): MutableLiveData<Uri> {
        userRepo.getProfileImageMutableLiveData(profileImageMutableLiveData)
        Log.d(TAG, "profileImage: " + profileImageMutableLiveData.value.toString())
        return profileImageMutableLiveData
    }

    fun deleteImage(photo: Photo, adapter: GalleryRecyclerAdapter){
            photosRepo.deleteImage(photo, galleryTypeMutableLiveData.value.toString(), adapter)

    }

    fun addImageUriToPhotoList(uri: Uri){
        postsRepo.uriList.add(uri)
    }

    fun getDataFromPhotoListToRecyclerView(imageList: MutableList<Uri>, adapter: PostImageRecyclerAdapter){
        postsRepo.getDataFromPhotoListToRecyclerView(imageList, adapter)
    }



    fun clearList(){
        postsRepo.clearList()
    }

    fun deleteImage(uri: Uri){
        postsRepo.uriList.remove(uri)
    }




    fun addPost(text: String, hashtags: String, progressBar: ProgressBar){

        postsRepo.addPost(text, hashtags, progressBar)

    }

    fun editPost(){

    }

    fun deletePost(){

    }


    fun signOut(){
        userRepo.signOut()
        Log.e("TAG", "Signed out" )
    }
}