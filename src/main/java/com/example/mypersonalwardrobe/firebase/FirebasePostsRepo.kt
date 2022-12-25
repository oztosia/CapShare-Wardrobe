package com.example.mypersonalwardrobe.firebase

import GenericAdapter
import android.net.Uri
import android.widget.ProgressBar
import com.example.mypersonalwardrobe.adapters.PostImageRecyclerAdapter
import com.example.mypersonalwardrobe.constants.FirebasePathsConstants
import com.example.mypersonalwardrobe.models.Post
import kotlin.collections.ArrayList

class FirebasePostsRepo: FirebaseGenericRepo(){

    var uriList: ArrayList<Uri> = arrayListOf()

    fun addPost(text: String, hashtags: String, progressBar: ProgressBar){

        val postMap = hashMapOf<String, Any>(
            "date" to FirebasePathsConstants.postTimeStamp,
            "hashtags" to hashtags,
            "text" to text,
            "authorUid" to  FirebasePathsConstants.CURRENT_USER.toString(),
            "likes" to 0
        )
        set(FirebasePathsConstants.MY_POST_PATH, FirebasePathsConstants.postTimeStamp, postMap)
        uploadMultipleImages(uriList, progressBar)
    }


    fun uploadMultipleImages(urisList: ArrayList<Uri>,
                             progressBar: ProgressBar) {
        for (uri in urisList) {
            addImage(uri,
                FirebasePathsConstants.STORAGE_POSTS_IMAGES,
                FirebasePathsConstants.MY_FIRESTORE_POSTS_IMAGES,
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

//do profili użytkowników
    fun getUserPostsFromFirestoreToRecyclerView(userPostsListFromFirebase: ArrayList<Post>,
                                                uid: String,
                                                adapter: GenericAdapter<Post>
    ) {
        getDataToRecyclerView(userPostsListFromFirebase,
            adapter,
            FirebasePathsConstants.POSTS + uid)
    }


    fun getDataFromPhotoListToRecyclerView(photoList: MutableList<Uri>,
                                           adapter: PostImageRecyclerAdapter
    ){
        photoList.clear()
        photoList.addAll(uriList)
        adapter.notifyDataSetChanged()
    }


    fun clearList(){
        uriList.clear()
    }
}