package com.example.mypersonalwardrobe.firebase

import GenericAdapter
import android.widget.ProgressBar
import androidx.core.net.toUri
import com.example.mypersonalwardrobe.constants.FirebasePathsConstants
import com.example.mypersonalwardrobe.helpers.ItemsListHolder.ItemsListHolder.list
import java.util.*

class FirebasePostsRepo: FirebaseGenericRepo(){

    fun addPost(text: String, hashtags: String, progressBar: ProgressBar){

        val id = UUID.randomUUID().toString()

        val path = FirebasePathsConstants.POSTS + id

        val postMap = hashMapOf<String, Any>(
            "id" to id,
            "date" to FirebasePathsConstants.postTimeStamp,
            "hashtags" to hashtags,
            "text" to text,
            "authorUid" to  FirebasePathsConstants.CURRENT_USER.toString(),
            "likes" to "0"
        )
        set(FirebasePathsConstants.POSTS, id, postMap)
        uploadMultipleImages(list, path, progressBar)
    }


    fun uploadMultipleImages(
        urisList: MutableList<String>,
        path: String,
        progressBar: ProgressBar) {
        for (uri in urisList) {
            addImage(uri.toUri(),
                "",
                FirebasePathsConstants.STORAGE_POSTS_IMAGES,
                path + "/images",
                progressBar)
        }
        urisList.clear()
    }

    fun editPost(){

    }

    fun deletePost(){

    }

    fun likePost(){

    }


    fun getDataFromPhotoListToRecyclerView(adapter: GenericAdapter<String>
    ){
        adapter.dataSet.addAll(list)
        adapter.notifyDataSetChanged()
    }
}