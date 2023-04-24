package com.example.mypersonalwardrobe.viewmodels

import android.net.Uri
import android.widget.ProgressBar
import androidx.lifecycle.ViewModel
import com.example.mypersonalwardrobe.source.FirebaseGenericRepo
import com.example.mypersonalwardrobe.utils.ItemsListHolder

class NewImageViewModel: ViewModel() {

    private val genericRepo: FirebaseGenericRepo = FirebaseGenericRepo()

    fun addImageUriToMessagesPhotoList(uri:String){ ItemsListHolder.list.add(0, uri) }

    fun addImageUriToPhotoList(uri:String){ ItemsListHolder.list.add(uri) }

    fun addImage(uri: Uri, hashtags: String, storagePath: String, firestorePath: String, progressBar: ProgressBar) {
        genericRepo.addImage(uri, hashtags, storagePath, firestorePath, progressBar) }

    fun replaceImage(uri: Uri, imageName: String, storagePath: String, firestorePath: String, documentPath: String, progressBar: ProgressBar){
        genericRepo.replaceImage(uri, imageName, storagePath, firestorePath, documentPath, progressBar)
    }

}