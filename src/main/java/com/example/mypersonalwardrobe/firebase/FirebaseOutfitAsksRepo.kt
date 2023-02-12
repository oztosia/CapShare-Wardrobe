package com.example.mypersonalwardrobe.firebase

import android.widget.ProgressBar
import com.example.mypersonalwardrobe.constants.FirebasePathsConstants
import com.example.mypersonalwardrobe.helpers.ItemsListHolder.ItemsListHolder.list
import java.util.*

class FirebaseOutfitAsksRepo(): FirebaseGenericRepo() {

    fun addOutfitAsk(title: String, text: String, hashtags: String){

        val id = UUID.randomUUID().toString()

        val path = FirebasePathsConstants.OUTFIT_ASKS + id

        val postMap = hashMapOf<String, Any>(
            "id" to id,
            "date" to FirebasePathsConstants.postTimeStamp,
            "hashtags" to hashtags,
            "text" to text,
            "title" to title,
            "authorUid" to  FirebasePathsConstants.CURRENT_USER.toString(),
            "likes" to "0"
        )
        set(FirebasePathsConstants.OUTFIT_ASKS, id, postMap)
        uploadMultipleDocuments(list, path)
    }


    fun uploadMultipleDocuments(urisList: List<String>, path: String) {


        for (uri in urisList) {
            val postMap = hashMapOf<String, Any>(
                "downloadURL" to uri,
            )
            add(path + "/images", postMap)
        }
    }



}