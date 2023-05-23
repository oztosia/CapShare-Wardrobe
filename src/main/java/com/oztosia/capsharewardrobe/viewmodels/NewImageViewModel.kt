package com.oztosia.capsharewardrobe.viewmodels

import android.net.Uri
import android.widget.ProgressBar
import androidx.lifecycle.ViewModel
import com.oztosia.capsharewardrobe.source.FirebaseGenericRepo
import com.oztosia.capsharewardrobe.singletons.ItemsListHolder

class NewImageViewModel: ViewModel() {

    private val repo: FirebaseGenericRepo = FirebaseGenericRepo()

    fun addImageUriToMessagesPhotoList(uri:String){ ItemsListHolder.list.add(0, uri) }

    fun addImageUriToPhotoList(uri:String){ ItemsListHolder.list.add(uri) }

    fun addImage(uri: Uri, hashtags: String, storagePath: String, firestorePath: String, progressBar: ProgressBar) { repo.addImage(uri, hashtags, storagePath, firestorePath, progressBar) }

    fun replaceImage(uri: Uri, imageName: String, storagePath: String, firestorePath: String, documentPath: String, progressBar: ProgressBar){ repo.replaceImage(uri, imageName, storagePath, firestorePath, documentPath, progressBar) }

}