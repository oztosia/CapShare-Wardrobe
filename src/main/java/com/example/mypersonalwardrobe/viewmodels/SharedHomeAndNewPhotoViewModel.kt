package com.example.mypersonalwardrobe.viewmodels

import GenericAdapter
import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import android.widget.ProgressBar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mypersonalwardrobe.firebase.FirebaseGenericRepo
import com.example.mypersonalwardrobe.firebase.FirebasePostsRepo
import com.example.mypersonalwardrobe.firebase.FirebaseUserRepo
import com.example.mypersonalwardrobe.helpers.ItemsListHolder.ItemsListHolder.list

class SharedHomeAndNewPhotoViewModel: ViewModel() {

    private val userRepo: FirebaseUserRepo = FirebaseUserRepo()
    private val genericRepo: FirebaseGenericRepo = FirebaseGenericRepo()
    private val postsRepo: FirebasePostsRepo = FirebasePostsRepo()

    var _galleryTypeMutableLiveData = MutableLiveData<String>()
    private var  _profileImageMutableLiveData = MutableLiveData<Uri>()


    fun uploadGalleryTypeMutableLiveData(galleryTypePath: String): MutableLiveData<String> {
        _galleryTypeMutableLiveData.value = galleryTypePath
        Log.d("TAG", "current gallery: " + _galleryTypeMutableLiveData.value.toString() )
        return _galleryTypeMutableLiveData
    }

    ////

    fun addHashtags(path: String, data: Map<String, Any>){
        genericRepo.add(path, data)
    }
    ////

    fun addImage(uri: Uri,
                 hashtags: String,
                 storagePath: String,
                 firestorePath: String,
                 progressBar: ProgressBar) {
        genericRepo.addImage(uri,
            hashtags,
            storagePath,
            firestorePath,
            progressBar)
    }



    fun replaceImage(uri: Uri,
                     imageName: String,
                     storagePath: String,
                     firestorePath: String,
                     documentPath: String,
                     progressBar: ProgressBar){
        genericRepo.replaceImage(uri,
            imageName,
            storagePath,
            firestorePath,
            documentPath,
            progressBar)
    }

    fun getProfileImageMutableLiveData(): MutableLiveData<Uri> {
        userRepo.getProfileImageMutableLiveData(_profileImageMutableLiveData)
        Log.d(TAG, "profileImage: " + _profileImageMutableLiveData.value.toString())
        return _profileImageMutableLiveData
    }

    fun signOut(){
        userRepo.signOut()
        Log.e("TAG", "Signed out" )
    }

    //////////post vm

    fun addImageUriToPhotoList(uri:String){
        list.add(uri)
    }

    fun getDataFromPhotoListToRecyclerView(adapter: GenericAdapter<String>){
        postsRepo.getDataFromPhotoListToRecyclerView(adapter)
    }

    fun deleteImage(uri: String){
        list.remove(uri)
    }

    fun addPost(text: String, hashtags: String, progressBar: ProgressBar){
        postsRepo.addPost(text, hashtags, progressBar)
    }
    /////////////////post vm
}